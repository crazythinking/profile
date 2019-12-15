package net.engining.profile.api.controller.query;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.sdk.service.bean.param.MenuQueryRequest;
import net.engining.profile.sdk.service.query.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 菜单
 *
 * @author heqingxi
 */

@Api(value = "ProfileMenuController")
@RequestMapping("/menu")
@RestController
public class ProfileMenuController {

    @Autowired
    private AuthService authService;

    //    @PreAuthorize("hasAuthority('menuQurey')")
    @RequestMapping(value = "/menuQuery", method = RequestMethod.GET)
    @ApiOperation(value = "主菜单信息查询", notes = "")
    public CommonWithHeaderResponse
    getMenu(MenuQueryRequest request) {
        String menuList = authService.getMenuData(request.getUserId(),request.getAppCd());
        return new CommonWithHeaderResponseBuilder<Void, String>()
                .build()
                .setResponseData(menuList);
    }
}
