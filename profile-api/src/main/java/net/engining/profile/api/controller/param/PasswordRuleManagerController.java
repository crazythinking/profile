//package net.engining.profile.api.controller.param;
//
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import net.engining.pg.web.CommonWithHeaderResponseBuilder;
//import net.engining.pg.web.bean.CommonWithHeaderResponse;
//import net.engining.profile.sdk.service.bean.param.PasswordRuleBean;
//import net.engining.profile.sdk.service.bean.param.ResultMessageBean;
//import net.engining.profile.sdk.service.param.PasswordRuleManagerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
///**
// * @Description 密码规则管理
// * @Author yangli
// */
//@Api(value = "PasswordRuleManagerController",description = "密码规则管理")
//@RestController
//@RequestMapping(value = "/passwordRule")
//public class PasswordRuleManagerController {
//    /**
//     * 密码管理服务
//     */
//    @Autowired
//    private PasswordRuleManagerService passwordRuleManagerService;
//
//    /**
//     * 密码修改
//     * @param passwordRuleBean 修改请求
//     * @return 修改结果
//     */
//    @PreAuthorize("hasAuthority('PasswordRuleManager')")
//    @ApiOperation(value = "密码规则确认", notes = "密码规则修改")
//    @RequestMapping(value = "/passwordRuleManager", method = RequestMethod.POST)
//    @ResponseBody
//    public CommonWithHeaderResponse passwordRuleManager(@RequestBody @Validated PasswordRuleBean passwordRuleBean) {
//        ResultMessageBean resultMessageBean = passwordRuleManagerService.passwordRuleManager(passwordRuleBean);
//        return new CommonWithHeaderResponseBuilder<Void, Void>()
//                .build()
//                .putAdditionalRepMap("result", resultMessageBean);
//    }
//
//    /**
//     * 密码规则查询
//     * @return 密码规则
//     */
//    @ApiOperation(value = "密码规则查询", notes = "密码规则查询")
//    @RequestMapping(value = "/passwordRuleSearch", method = RequestMethod.POST)
//    @ResponseBody
//    public CommonWithHeaderResponse passwordRuleSearch() {
//        PasswordRuleBean passwordRuleBean = passwordRuleManagerService.passwordRuleSearch();
//        return new CommonWithHeaderResponseBuilder<Void, PasswordRuleBean>()
//                .build()
//                .setResponseData(passwordRuleBean);
//    }
//}
