package net.engining.profile.api.controller.profile;


import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.engining.control.sdk.FlowTransProcessorTemplate;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.web.WebCommonUtils;
import net.engining.pg.web.bean.CommonWithHeaderRequest;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.pg.web.bean.DefaultRequestHeader;
import net.engining.pg.web.bean.DefaultResponseHeader;
import net.engining.profile.api.bean.request.department.AddDepartmentRequest;
import net.engining.profile.api.bean.request.department.DeleteDepartmentRequest;
import net.engining.profile.api.bean.request.department.ListDepartmentRequest;
import net.engining.profile.api.bean.request.department.UpdateDepartmentRequest;
import net.engining.profile.api.bean.response.department.AddDepartmentResponse;
import net.engining.profile.api.bean.response.department.ListDepartmentResponse;
import net.engining.profile.api.bean.vo.DepartmentListVo;
import net.engining.profile.api.util.CheckRequestUtils;
import net.engining.profile.api.util.ControllerUtils;
import net.engining.profile.api.util.VoTransformationUtils;
import net.engining.profile.enums.OperationType;
import net.engining.profile.flow.sdk.department.AddDepartmentFlowRequest;
import net.engining.profile.flow.sdk.department.AddDepartmentFlowResponse;
import net.engining.profile.flow.sdk.department.DeleteDepartmentFlowRequest;
import net.engining.profile.flow.sdk.department.DeleteDepartmentFlowResponse;
import net.engining.profile.flow.sdk.department.UpdateDepartmentFlowRequest;
import net.engining.profile.flow.sdk.department.UpdateDepartmentFlowResponse;
import net.engining.profile.sdk.service.DepartmentManagementService;
import net.engining.profile.sdk.service.bean.dto.DepartmentListDto;
import net.engining.profile.sdk.service.bean.query.DepartmentPagingQuery;
import net.engining.profile.sdk.service.util.PagingQueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static net.engining.profile.api.constant.ParameterNameConstants.DEPARTMENT_ID;
import static net.engining.profile.api.constant.ParameterNameConstants.OPERATOR_ID;

/**
 * @author tuyi
 */
@Api(value = "ProfileBranchController", description = "机构服务模块")
@RequestMapping("/profile")
@RestController
public class ProfileBranchController {

    /**
     * 部门名称
     */
    private final String DEPARTMENT_NAME = "部门名称";
    /**
     * 机构服务
     */
    @Autowired
    private DepartmentManagementService departmentManagementService;
    /**
     * 交易流程模版
     */
    @Autowired
    private FlowTransProcessorTemplate flowTransProcessorTemplate;

    /**
     * 部门列表查询
     *
     * @param request 请求
     * @return 结果
     */
    @PreAuthorize("hasAuthority('listDepartment')")
    @ApiOperation(value = "部门列表查询", notes = "分页查询部门表")
    @RequestMapping(value = "/listDepartment", method = RequestMethod.GET)
    public CommonWithHeaderResponse<Void, ListDepartmentResponse<DepartmentListVo>>
    listDepartment(@Valid ListDepartmentRequest request) {
        Long pageSize = request.getPageSize();
        CheckRequestUtils.checkPageSizeIsWithinRange(pageSize);

        DepartmentPagingQuery query = PagingQueryUtils.initDepartmentPagingQuery(request.getPageNum(), pageSize);
        FetchResponse<DepartmentListDto> fetchResponse = departmentManagementService.listDepartmentByPaging(query);
        ListDepartmentResponse<DepartmentListVo> response = new ListDepartmentResponse<>();
        VoTransformationUtils.convertToPagingQueryResponse(fetchResponse, response, source -> {
            List<DepartmentListVo> data = new ArrayList<>(source.size());
            for (DepartmentListDto dto : source) {
                DepartmentListVo vo = new DepartmentListVo();
                vo.setDepartmentId(dto.getDepartmentId());
                vo.setDepartmentName(dto.getDepartmentName());
                data.add(vo);
            }
            return data;
        });

        return ControllerUtils.returnSuccessResponseWithoutHead(response);
    }

    /**
     * 新增部门
     *
     * @param httpServletRequest http请求
     * @param request 请求
     * @return 结果
     */
    @PreAuthorize("hasAuthority('addDepartment')")
    @RequestMapping(value = "/addDepartment", method = RequestMethod.POST)
    @ApiOperation(value = "新增部门", notes = "新增部门表记录")
    public CommonWithHeaderResponse<DefaultResponseHeader, AddDepartmentResponse>
    addDepartment(HttpServletRequest httpServletRequest,
                  @Valid @RequestBody CommonWithHeaderRequest<DefaultRequestHeader, AddDepartmentRequest> request) {
        AddDepartmentRequest requestData = request.getRequestData();
        DefaultRequestHeader requestHead = request.getRequestHead();

        String txnSerialNo = requestHead.getTxnSerialNo();
        Date timestamp = requestHead.getTimestamp();

        String departmentName = requestData.getDepartmentName();
        CheckRequestUtils.checkIsChinese(departmentName, DEPARTMENT_NAME);
        String operatorId = requestData.getOperatorId();
        CheckRequestUtils.checkIsNumberOrLetter(operatorId, OPERATOR_ID);

        AddDepartmentFlowRequest flowRequest = new AddDepartmentFlowRequest();
        flowRequest.setChannelRequestSeq(txnSerialNo);
        flowRequest.setTxnDateTime(timestamp);
        flowRequest.setChannel(requestHead.getChannelId());
        flowRequest.setOnlineData(JSON.toJSONString(request));
        flowRequest.setDepartmentName(departmentName);
        flowRequest.setOperatorId(operatorId);
        flowRequest.setOperationIp(WebCommonUtils.getIpAddress(httpServletRequest));
        flowRequest.setOperationDate(new Date());
        flowRequest.setOperationType(OperationType.AB);

        AddDepartmentFlowResponse flowResponse = flowTransProcessorTemplate.process(flowRequest,
                AddDepartmentFlowResponse.class);
        ControllerUtils.checkFlowResponse(flowResponse);

        AddDepartmentResponse responseData = new AddDepartmentResponse();
        responseData.setDepartmentId(flowResponse.getDepartmentId());

        return ControllerUtils.returnSuccessResponseWithDefualtHead(flowResponse.getTransId(), txnSerialNo,
                timestamp, responseData);
    }

    /**
     * 修改部门
     *
     * @param httpServletRequest http请求
     * @param request 请求
     * @return 结果
     */
    @PreAuthorize("hasAuthority('updateDepartment')")
    @RequestMapping(value = "/updateDepartment", method = RequestMethod.POST)
    @ApiOperation(value = "修改部门", notes = "修改部门表记录")
    public CommonWithHeaderResponse<DefaultResponseHeader, Void>
    updateDepartment(HttpServletRequest httpServletRequest,
                     @Valid @RequestBody CommonWithHeaderRequest<DefaultRequestHeader, UpdateDepartmentRequest> request) {
        UpdateDepartmentRequest requestData = request.getRequestData();
        DefaultRequestHeader requestHead = request.getRequestHead();

        String txnSerialNo = requestHead.getTxnSerialNo();
        Date timestamp = requestHead.getTimestamp();

        String departmentId = requestData.getDepartmentId();
        CheckRequestUtils.checkIsNumber(departmentId, DEPARTMENT_ID);
        String departmentName = requestData.getDepartmentName();
        CheckRequestUtils.checkIsChinese(departmentName, DEPARTMENT_NAME);
        String operatorId = requestData.getOperatorId();
        CheckRequestUtils.checkIsNumberOrLetter(operatorId, OPERATOR_ID);

        UpdateDepartmentFlowRequest flowRequest = new UpdateDepartmentFlowRequest();
        flowRequest.setChannelRequestSeq(txnSerialNo);
        flowRequest.setTxnDateTime(timestamp);
        flowRequest.setChannel(requestHead.getChannelId());
        flowRequest.setOnlineData(JSON.toJSONString(request));
        flowRequest.setDepartmentId(departmentId);
        flowRequest.setDepartmentName(departmentName);
        flowRequest.setOperatorId(operatorId);
        flowRequest.setOperationIp(WebCommonUtils.getIpAddress(httpServletRequest));
        flowRequest.setOperationDate(new Date());
        flowRequest.setOperationType(OperationType.UB);
        flowRequest.setOperationObject(departmentId);

        UpdateDepartmentFlowResponse flowResponse = flowTransProcessorTemplate.process(flowRequest,
                UpdateDepartmentFlowResponse.class);
        ControllerUtils.checkFlowResponse(flowResponse);

        return ControllerUtils.returnSuccessResponseWithDefualtHead(flowResponse.getTransId(), txnSerialNo,
                timestamp);
    }

    /**
     * 删除部门
     *
     * @param httpServletRequest http请求
     * @param request 请求
     * @return 结果
     */
//    @RequestMapping(value = "/deleteDepartment", method = RequestMethod.POST)
//    @ApiOperation(value = "删除部门", notes = "删除部门表记录")
//    public CommonWithHeaderResponse<DefaultResponseHeader, Void>
//    deleteDepartment(HttpServletRequest httpServletRequest,
//                     @Valid @RequestBody CommonWithHeaderRequest<DefaultRequestHeader, DeleteDepartmentRequest> request) {
//        DeleteDepartmentRequest requestData = request.getRequestData();
//        DefaultRequestHeader requestHead = request.getRequestHead();
//
//        String txnSerialNo = requestHead.getTxnSerialNo();
//        Date timestamp = requestHead.getTimestamp();
//
//        String departmentId = requestData.getDepartmentId();
//        CheckRequestUtils.checkIsNumber(departmentId, DEPARTMENT_ID);
//        String operatorId = requestData.getOperatorId();
//        CheckRequestUtils.checkIsNumberOrLetter(operatorId, OPERATOR_ID);
//
//        DeleteDepartmentFlowRequest flowRequest = new DeleteDepartmentFlowRequest();
//        flowRequest.setChannelRequestSeq(txnSerialNo);
//        flowRequest.setTxnDateTime(timestamp);
//        flowRequest.setChannel(requestHead.getChannelId());
//        flowRequest.setOnlineData(JSON.toJSONString(request));
//        flowRequest.setDepartmentId(departmentId);
//        flowRequest.setOperatorId(operatorId);
//        flowRequest.setOperationIp(WebCommonUtils.getIpAddress(httpServletRequest));
//        flowRequest.setOperationDate(new Date());
//        flowRequest.setOperationType(OperationType.DB);
//        flowRequest.setOperationObject(departmentId);
//
//        DeleteDepartmentFlowResponse flowResponse = flowTransProcessorTemplate.process(flowRequest,
//                DeleteDepartmentFlowResponse.class);
//        ControllerUtils.checkFlowResponse(flowResponse);
//
//        return ControllerUtils.returnSuccessResponseWithDefualtHead(flowResponse.getTransId(), txnSerialNo,
//                timestamp);
//    }

//	/**
//	 * 通过上级分支id查询机构分支信息
//	 * @param branchFilter
//	 * @return
//	 */
//	@RequestMapping(value="/branchesBySuperid",method= RequestMethod.POST)
//	@ApiOperation(value="通过上级分支id查询机构分支信息", notes="通过上级分支id查询机构分支信息")
////	@PreAuthorize("hasAuthority('ProfileBranch')")
//	public @ResponseBody
//	CommonWithHeaderResponse fetchBranchesBySuperid(@RequestBody @Validated BranchFilter branchFilter) {
//		FetchResponse<ProfileBranch> fetchBranch = profileBranchService.fetchBranch(
//				branchFilter.getRange(),
//				branchFilter.getSuperiorId(),
//				provider4Organization.getCurrentOrganizationId()
//		);
//		FetchResponse<ProfileBranchForm> profileBranchForm = branchService.profileBranchForm(fetchBranch);
//		return new CommonWithHeaderResponseBuilder<Void,FetchResponse<ProfileBranchForm>>().build()
//				.setResponseData(profileBranchForm);
//	}
//
//	@RequestMapping(value="/branches",method= RequestMethod.POST)
//	@ApiOperation(value="查询机构分支信息列表", notes="查询机构分支信息列表")
////	@PreAuthorize("hasAuthority('ProfileBranch')")
//	public @ResponseBody
//	CommonWithHeaderResponse fetchBranches(@RequestBody Range range) {
//		FetchResponse<ProfileBranch> fetchBranch = profileBranchService.fetchBranch(range);
//		FetchResponse<ProfileBranchForm> profileBranchForm = branchService.profileBranchForm(fetchBranch);
//		return new CommonWithHeaderResponseBuilder<Void,FetchResponse<ProfileBranchForm>>().build()
//				.setResponseData(profileBranchForm);
//	}
//
//	@RequestMapping(value="/branch/{branchId}",method= RequestMethod.GET)
//	@ApiOperation(value="通过分支id查询机构分支信息", notes="通过分支id查询机构分支信息")
////	@PreAuthorize("hasAuthority('ProfileBranch')")
//	public
//	CommonWithHeaderResponse getBranch(@PathVariable String branchId) {
//		ProfileBranch branch = profileBranchService.getBranch(
//				provider4Organization.getCurrentOrganizationId(),
//				branchId
//		);
//		if(ValidateUtilExt.isNullOrEmpty(branch)){
//			return null;
//		}
//		ProfileBranchForm profileBranchForm = branchService.profileBranchForm(branch);
//		return new CommonWithHeaderResponseBuilder<Void,ProfileBranchForm>().build()
//				.setResponseData(profileBranchForm);
//	}
//
//	@RequestMapping(value="/updateBranch",method= RequestMethod.POST)
//	@ApiOperation(value="更新机构分支信息", notes="更新机构分支信息")
////	@PreAuthorize("hasAuthority('ProfileBranch')")
//	public @ResponseBody
//	CommonWithHeaderResponse updateBranch(@RequestBody ProfileBranchForm branch) {
//		ProfileBranch profileBranch = branchService.profileBranch(branch);
//		return new CommonWithHeaderResponseBuilder<Void,Void>()
//				.build();
//	}
//
//	@RequestMapping(value="/getBranchNames",method= RequestMethod.GET)
//	@ApiOperation(value="获得当前机构id下分支名", notes="获得当前机构id下分支名")
////	@PreAuthorize("hasAuthority('ProfileBranch')")
//	public
//	CommonWithHeaderResponse getBranchNames() {
//		Map<String, String> fetchBranchNamesByOrg = profileBranchService.fetchBranchNamesByOrg(
//				provider4Organization.getCurrentOrganizationId()
//		);
//		return new CommonWithHeaderResponseBuilder<Void,Map<String, String>>().build()
//				.setResponseData(fetchBranchNamesByOrg);
//	}
//
//	@RequestMapping(value="/removeBranches",method= RequestMethod.POST)
//	@ApiOperation(value="删除机构分支信息", notes="删除机构分支信息")
////	@PreAuthorize("hasAuthority('ProfileBranch')")
//	public @ResponseBody
//	CommonWithHeaderResponse removeBranches(@RequestBody
//											@ApiParam(value = "分支id", required = true)
//													String[] branchIds) {
//		List<String> branches= Arrays.asList(branchIds);
//		profileBranchService.deleteProfileBranch(branches, provider4Organization.getCurrentOrganizationId());
//		return new CommonWithHeaderResponseBuilder<Void,Void>()
//				.build();
//	}

    //    /**
//     * 获取所有的分支下拉框
//     */
//    @RequestMapping(value = "/fetchAllProfileBranch", method = RequestMethod.POST)
//    @ApiOperation(value = "获取所有的分支下拉框", notes = "获取所有的分支下拉框")
//    public CommonWithHeaderResponse fetchAllProfileBranch() {
//		FetchResponse<Map<String, Object>> fetchResponse = profileMgmService.fetchAllProfileBranch();
//
//        return null;
//    }
}