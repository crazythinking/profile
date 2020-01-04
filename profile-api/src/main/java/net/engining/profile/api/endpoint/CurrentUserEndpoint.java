package net.engining.profile.api.endpoint;

import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.sdk.service.ClientWebUser;
import net.engining.profile.sdk.service.ProfileRuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 获取当前登录用户的基本信息；
 * 注意此服务通过SecurityContextHolder获取用户，是线程级的；由使用方注入到容器；
 *
 * @author Eric Lu
 * @date 2019-11-29 13:01
 **/
public class CurrentUserEndpoint {

    @Autowired
    ProfileRuntimeService profileRuntimeService;

    @RequestMapping(value = "/oauth/user_info")
    @ResponseBody
    public CommonWithHeaderResponse<Void, ClientWebUser> loginedUserInfo(){

        ClientWebUser clientWebUser = profileRuntimeService.loadCurrentUser();
        return new CommonWithHeaderResponseBuilder<Void, ClientWebUser>().build().setResponseData(clientWebUser);
    }
}
