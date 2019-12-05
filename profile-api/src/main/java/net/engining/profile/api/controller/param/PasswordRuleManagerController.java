package net.engining.profile.api.controller.param;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.sdk.service.bean.param.PasswordRuleBean;
import net.engining.profile.sdk.service.bean.param.ResultMessageBean;
import net.engining.profile.sdk.service.param.PasswordRuleManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Description 密码规则管理
 * @Author yangli
 */
@Api(value = "PasswordRuleManagerController")
@RestController
@RequestMapping(value = "/passwordRule")
public class PasswordRuleManagerController {

    @Autowired
    private PasswordRuleManagerService passwordRuleManagerService;

    @PreAuthorize("hasAuthority('PasswordRuleManager')")
    @ApiOperation(value = "密码规则确认", notes = "")
    @RequestMapping(value = "/passwordRuleManager", method = RequestMethod.POST)
    @ResponseBody
    public CommonWithHeaderResponse passwordRuleManager(@RequestBody PasswordRuleBean passwordRuleBean) {
        ResultMessageBean resultMessageBean = passwordRuleManagerService.passwordRuleManager(passwordRuleBean);
        return new CommonWithHeaderResponseBuilder<Void, Void>()
                .build()
                .putAdditionalRepMap("result", resultMessageBean);
    }


    @ApiOperation(value = "密码规则查询", notes = "")
    @RequestMapping(value = "/passwordRuleSearch", method = RequestMethod.POST)
    @ResponseBody
    public CommonWithHeaderResponse passwordRuleSearch() {
        PasswordRuleBean passwordRuleBean = passwordRuleManagerService.passwordRuleSearch();
        return new CommonWithHeaderResponseBuilder<Void, PasswordRuleBean>()
                .build()
                .setResponseData(passwordRuleBean);
    }
}
