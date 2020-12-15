package net.engining.profile.invoker.authority;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.sdk.key.RoleIdKey;
import net.engining.profile.sdk.key.UserIdKey;
import net.engining.profile.sdk.service.query.AuthService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/9 19:09
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "更新本地缓存",
        optional = {
                RoleIdKey.class,
                UserIdKey.class
        }
)
public class RefreshCacheInvoker extends AbstractSkippableInvoker {

    /**
     * 授权服务
     */
    @Autowired
    private AuthService authService;

    @Override
    public void invoke(FlowContext flowContext) {
        String roleId = flowContext.get(RoleIdKey.class);
        if (ValidateUtilExt.isNotNullOrEmpty(roleId)) {
            authService.refreshRoleAuthCacheByRoleId(roleId);
            authService.refreshUserMenuCacheByRoleId(roleId);
        }

        String userId = flowContext.get(UserIdKey.class);
        if (ValidateUtilExt.isNotNullOrEmpty(userId)) {
            authService.refreshUserMenuCacheByUserId(userId);
        }
    }

}
