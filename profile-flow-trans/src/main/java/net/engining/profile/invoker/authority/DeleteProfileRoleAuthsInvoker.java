package net.engining.profile.invoker.authority;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.profile.sdk.key.RoleIdKey;
import net.engining.profile.sdk.service.db.ProfileRoleAuthService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/9 18:49
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "删除角色已有权限",
        requires = {
                RoleIdKey.class
        }
)
public class DeleteProfileRoleAuthsInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileRoleAuth表操作服务
     */
    @Autowired
    private ProfileRoleAuthService profileRoleAuthService;

    @Override
    public void invoke(FlowContext flowContext) {
        profileRoleAuthService.deleteProfileRoleAuthsByRoleId(flowContext.get(RoleIdKey.class));
    }

}
