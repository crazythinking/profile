package net.engining.profile.sdk.controller.query;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.sdk.service.bean.param.MenuQueryRequest;
import net.engining.profile.sdk.service.query.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * 菜单
 *
 * @author heqingxi
 */

@Api(value = "MenuController")
@RequestMapping("/menu")
@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    //    @PreAuthorize("hasAuthority('menuQurey')")
    @RequestMapping(value = "/menuQuery", method = RequestMethod.GET)
    @ApiOperation(value = "主菜单信息查询", notes = "")
    public CommonWithHeaderResponse
    getMenu(MenuQueryRequest request) {
        String menuList = menuService.getMenuData(request.getUserId(),request.getAppCd());
        return new CommonWithHeaderResponseBuilder<Void, String>()
                .build()
                .setResponseData(menuList);
    }

    //    @PreAuthorize("hasAuthority('menuQurey')")
    @RequestMapping(value = "/menuAuthorityQuery", method = RequestMethod.GET)
    @ApiOperation(value = "权限菜单信息查询", notes = "")
    public CommonWithHeaderResponse
    getAuthorityMenu(@ApiParam("应用代码") String appCd) {
        //所有角色
        String menuList = menuService.getAuthorityData(appCd);
        return new CommonWithHeaderResponseBuilder<Void, String>()
                .build()
                .setResponseData(menuList);
    }


}
