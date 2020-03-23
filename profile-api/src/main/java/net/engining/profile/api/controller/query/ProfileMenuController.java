package net.engining.profile.api.controller.query;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.sdk.service.bean.param.MenuQueryRequest;
import net.engining.profile.sdk.service.query.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static net.engining.profile.sdk.service.query.AuthService.MENU_RESOURCE_URL;

/**
 * 菜单
 *  "/menu" 本地profile模式
 *   /menu_resource" 资源服务模式
 * @author heqingxi
 */

@Api(value = "ProfileMenuController",description = "菜单服务模块")
@RequestMapping({
        "/menu",
        MENU_RESOURCE_URL
})
@RestController
public class ProfileMenuController {

    @Autowired
    private AuthService authService;

    //    @PreAuthorize("hasAuthority('menuQurey')")
    @RequestMapping(value = "/menuQuery", method = RequestMethod.GET)
    @ApiOperation(value = "主菜单信息查询", notes = "主菜单信息查询")
    public CommonWithHeaderResponse
    getMenu(@Validated MenuQueryRequest request) {
        String menuList = authService.getMenuData(
                request.getUserId(),
                request.getAppCd()
        );
        return new CommonWithHeaderResponseBuilder<Void, String>()
                .build()
                .setResponseData(menuList);
    }
}
