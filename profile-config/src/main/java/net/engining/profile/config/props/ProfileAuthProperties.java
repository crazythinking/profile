package net.engining.profile.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : lijiahui
 * @version : 版本
 * @Description :权限参数
 * 菜单权限对应profile_role_auth表的AutuUri字段，和本地profile模式下的appcd，
 * 都统一为pg包下的DbConstants.NULL
 * @date : 2019/11/29 18:28
 */
@ConfigurationProperties(prefix = "profile.auth")
public class ProfileAuthProperties {

    /**
     * 客户端启用的是远程oauth授权中心,则为true
     * 客户端启用的是本地profile依赖，则为false
     */
    private boolean authEnabled = false;

    public boolean isAuthEnabled() {
        return authEnabled;
    }

    public void setAuthEnabled(boolean authEnabled) {
        this.authEnabled = authEnabled;
    }

}
