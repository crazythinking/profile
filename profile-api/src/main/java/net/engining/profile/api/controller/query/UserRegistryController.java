package net.engining.profile.api.controller.query;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.api.bean.request.serurity.ListOperationLogRequest;
import net.engining.profile.api.bean.response.serurity.ListOperationLogResponse;
import net.engining.profile.api.bean.vo.OperationLogListVo;
import net.engining.profile.api.util.CheckRequestUtils;
import net.engining.profile.api.util.ControllerUtils;
import net.engining.profile.api.util.VoTransformationUtils;
import net.engining.profile.enums.OperationType;
import net.engining.profile.enums.UpdateFieldEnum;
import net.engining.profile.sdk.service.DepartmentManagementService;
import net.engining.profile.sdk.service.bean.UpdateRecord;
import net.engining.profile.sdk.service.bean.dto.OperationLogListDto;
import net.engining.profile.sdk.service.bean.query.OperationLogPagingQuery;
import net.engining.profile.sdk.service.query.UserRegistryService;
import net.engining.profile.sdk.service.util.PagingQueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 操作日志管理
 *
 * @author heqingxi
 */
@Api(value = "UserRegistryController")
@RequestMapping("/profile")
@RestController
public class UserRegistryController {
    /**
     * 用户登记服务
     */
    @Autowired
    private UserRegistryService userRegistryService;
    /**
     * 其它服务
     */
    @Autowired
    private DepartmentManagementService departmentManagementService;

    /**
     * 用户操作日志列表查询
     *
     * @param request 请求
     * @return 结果
     */
    @PreAuthorize("hasAuthority('listOperationLog')")
    @ApiOperation(value = "用户操作日志列表查询", notes = "分页查询用户安全操作日志表")
    @RequestMapping(value = "/listOperationLog", method = RequestMethod.GET)
    public CommonWithHeaderResponse<Void, ListOperationLogResponse<OperationLogListVo>>
    listOperationLog(@Valid ListOperationLogRequest request) {
        String operatorId = request.getOperatorId();
        CheckRequestUtils.checkIsNumberOrLetter(operatorId, "操作用户ID");
        String operatorName = request.getOperatorName();
//        CheckRequestUtils.checkIsChinese(operatorName, "操作用户姓名");
        Date startDate = request.getStartDate();
        Date newEndDate = CheckRequestUtils.checkStartDateAndEndDate("操作日期起始", startDate,
                "操作日期结束", request.getEndDate());
        Long pageSize = request.getPageSize();
        CheckRequestUtils.checkPageSizeIsWithinRange(pageSize);

        OperationLogPagingQuery query = PagingQueryUtils.initOperationLogPagingQuery(operatorId, operatorName,
                null, startDate, newEndDate, request.getPageNum(), pageSize);
        FetchResponse<OperationLogListDto> fetchResponse = userRegistryService.listOperationLogByPaging(query);

        ListOperationLogResponse<OperationLogListVo> response = new ListOperationLogResponse<>();
        VoTransformationUtils.convertToPagingQueryResponse(fetchResponse, response, source -> {
            Map<String, String> departmentMap = departmentManagementService.mapAllDepartment();
            List<OperationLogListVo> data = new ArrayList<>(source.size());
            for (OperationLogListDto dto : source) {
                OperationLogListVo vo = new OperationLogListVo();
                vo.setOperatorId(dto.getOperatorId());
                vo.setOperatorName(dto.getOperatorName());
                vo.setOperationTimestamp(dto.getOperationTimestamp());
                OperationType operationType = dto.getOperationType();
                vo.setOperationType(operationType.getLabel());
                switch (operationType) {
                    case AR:
                    case UR:
                    case DA:
                        vo.setOperationTarget(String.format("角色ID【%s】", dto.getOperationTarget()));
                        vo.setOperationModle("角色管理");
                        break;
                    case AD:
                    case UP:
                    case DE:
                    case US:
                    case GN:
                    case RP:
                        vo.setOperationTarget(String.format("用户ID【%s】", dto.getOperationTarget()));
                        vo.setOperationModle("用户管理");
                        break;
                    case AB:
                    case UB:
                    case DB:
                        String departmentName = departmentMap.get(dto.getOperationTarget());
                        if (ValidateUtilExt.isNullOrEmpty(departmentName)) {
                            departmentName = "";
                        }
                        vo.setOperationTarget(String.format("部门名称【%s】", departmentName));
                        vo.setOperationModle("部门管理");
                        break;
                    case CP:
                        vo.setOperationTarget(String.format("用户ID【%s】", dto.getOperationTarget()));
                        vo.setOperationModle(vo.getOperationType());
                    default:
                        vo.setOperationTarget(dto.getOperationTarget());
                        vo.setOperationModle(vo.getOperationType());
                        break;
                }
                String remarks = dto.getRemarks();
                if (ValidateUtilExt.isNotNullOrEmpty(remarks)) {
                    vo.setRemarks(transformRemarks(remarks));
                }
                data.add(vo);
            }
            return data;
        });
        return ControllerUtils.returnSuccessResponseWithoutHead(response);
    }


    /**
     * 将数据库中的备注转换成页面展示需要的形式
     *
     * @param remarks 数据库中存储的备注信息
     * @return 转换后的信息
     */
    private String transformRemarks(String remarks) {
        List<UpdateRecord> updateRecordList = JSONObject.parseObject(remarks,
                new TypeReference<List<UpdateRecord>>() {
                });
        String message = null;
        for (UpdateRecord updateRecord : updateRecordList) {
            UpdateFieldEnum updateField = updateRecord.getUpdateField();
            String fieldValue;
            if (UpdateFieldEnum.ROLE_AUTH.equals(updateField)
                    || UpdateFieldEnum.USER_ROLE.equals(updateField)) {
                fieldValue = updateRecord.getNewValue().replaceFirst("\\[", "")
                        .replaceFirst("]", "")
                        .replaceAll(",", "】/【");
            } else {
                fieldValue = updateRecord.getNewValue();
            }
            String detail = String.format("%s修改为【%s】", updateField.getLabel(), fieldValue);
            if (ValidateUtilExt.isNullOrEmpty(message)) {
                message = detail;
            } else {
                message = message + "；" + detail;
            }
        }
        return message;
    }
//  @PreAuthorize("hasAuthority('UserRegistryDetailSearch')")
//  @ApiOperation(value = "用户登记薄详情查询", notes = "用户登记薄详情查询")
//  @RequestMapping(value = "/userRegistryDetailSearch", method = RequestMethod.POST)
//  @ResponseBody
//  public CommonWithHeaderResponse userRegistryDetailSearch(@RequestBody
//                                                             @Validated UserRegistryDetailsReq uRegDetailsReq){
//    FetchResponse<UserRegistryDetailsRes> fetchResponse= userRegistryService.getSecopeDetails(uRegDetailsReq);
//    return new CommonWithHeaderResponseBuilder<Void, FetchResponse<UserRegistryDetailsRes>>()
//            .build()
//            .setResponseData(fetchResponse);
//  }
//
//  @PreAuthorize("hasAuthority('UserRegistryExport')")
//  @ApiOperation(value="用户登记薄查询Excel", notes="用户登记薄查询Excel")
//  @RequestMapping(value="/userRegistryExport",method= RequestMethod.POST)
//  public void userRegistryExportExcel(@RequestBody
//                                        @Validated UserRegistryDetailsReq uRegDetailsReq,
//                                      HttpServletResponse response){
//    FetchResponse<UserRegistryDetailsRes> excelData= userRegistryService.excelData(uRegDetailsReq);
//      List<UserRegistryDetailsRes> dataList= excelData.getData();
//    userRegistryExcelService.excelFile(dataList, response);
//  }
}
