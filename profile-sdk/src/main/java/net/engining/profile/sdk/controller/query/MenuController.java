package net.engining.profile.sdk.controller.query;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.support.dstruct.TreeNode;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.sdk.service.bean.MenuBean;
import net.engining.profile.sdk.service.query.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping(value = "/menuQuery", method = RequestMethod.POST)
    @ApiOperation(value = "主菜单信息查询", notes = "")
    public CommonWithHeaderResponse
    getMenu(@RequestBody String userId) {
        String subjectList = menuService.getData(userId);
        return new CommonWithHeaderResponseBuilder<Void, String>()
                .build()
                .setResponseData(subjectList);
    }


}
