package net.engining.template.config.props;

import net.engining.pg.props.CommonProperties;
import net.engining.pg.support.utils.ValidateUtilExt;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : lijiahui
 * @version : 版本
 * @Description :权限初始化参数
 * @date : 2019/11/29 18:28
 */
@ConfigurationProperties(prefix = "profile.param")
public class ProfileParamProperties {

    /**
     * 权限初始化时AppCd
     */
    private String appCd = "unknown";
    /**
     * 菜单权限的AutuUri统一为 "-"
     */
    private String menuAutuUri = "-";
    /**
     * 远程授权中心状态,默认为false本地profile
     */
    private boolean authEnabled = false;

    public boolean isAuthEnabled() {
        return authEnabled;
    }

    public void setAuthEnabled(boolean authEnabled) {
        this.authEnabled = authEnabled;
    }

    public String getAppCd() {
        return appCd;
    }

    public void setAppCd(String appCd) {
        this.appCd = appCd;
    }
    public String getMenuAutuUri() {
        return menuAutuUri;
    }

    public void setMenuAutuUri(String menuAutuUri) {
        this.menuAutuUri = menuAutuUri;
    }
}
