package net.engining.profile.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : lijiahui
 * @version : 版本
 * @Description :授权中心参数
 * @date : 2019/11/29 18:28
 */
@ConfigurationProperties(prefix = "profile.oauth")
public class ProfileOauthProperties {

    /**
     * 当前应用是oauth授权中心,则为true
     * 当前应用不是oauth授权中心,则为false
     * 默认为false
     */
    private boolean oauthed = false;

    public boolean isOauthed() {
        return oauthed;
    }

    public void setOauthed(boolean oauthed) {
        this.oauthed = oauthed;
    }
}
