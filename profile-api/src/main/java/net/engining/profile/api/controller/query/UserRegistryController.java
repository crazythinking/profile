package net.engining.profile.api.controller.query;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.sdk.service.bean.param.UserRegistryDetailsReq;
import net.engining.profile.sdk.service.bean.param.UserRegistryDetailsRes;
import net.engining.profile.sdk.service.query.UserRegistryExcelService;
import net.engining.profile.sdk.service.query.UserRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户登记薄Controller
 * @author heqingxi
 */
@Api(value = "UserRegistryController")
@RequestMapping("/userRegistry")
@RestController
public class UserRegistryController {

  @Autowired
  UserRegistryService userRegistryService;

  @Autowired
  UserRegistryExcelService userRegistryExcelService;

  @PreAuthorize("hasAuthority('UserRegistryDetailSearch')")
  @ApiOperation(value = "用户登记薄详情查询", notes = "")
  @RequestMapping(value = "/userRegistryDetailSearch", method = RequestMethod.POST)
  @ResponseBody
  public CommonWithHeaderResponse userRegistryDetailSearch(@RequestBody @Validated UserRegistryDetailsReq uRegDetailsReq){
    FetchResponse<UserRegistryDetailsRes> fetchResponse= userRegistryService.getSecopeDetails(uRegDetailsReq);
    return new CommonWithHeaderResponseBuilder<Void, FetchResponse<UserRegistryDetailsRes>>()
            .build()
            .setResponseData(fetchResponse);
  }

  @PreAuthorize("hasAuthority('UserRegistryExport')")
  @ApiOperation(value="用户登记薄查询Excel", notes="")
  @RequestMapping(value="/userRegistryExport",method= RequestMethod.POST)
  public void userRegistryExportExcel(@RequestBody @Validated UserRegistryDetailsReq uRegDetailsReq, HttpServletResponse response){
    FetchResponse<UserRegistryDetailsRes> excelData= userRegistryService.excelData(uRegDetailsReq);
      List<UserRegistryDetailsRes> dataList= excelData.getData();
    userRegistryExcelService.excelFile(dataList, response);
  }







}
