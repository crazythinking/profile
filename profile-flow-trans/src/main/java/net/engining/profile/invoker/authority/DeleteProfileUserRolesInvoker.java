package net.engining.profile.invoker.authority;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.profile.sdk.key.PuIdKey;
import net.engining.profile.sdk.service.db.ProfileUserRoleService;
import net.engining.profile.sdk.service.db.ProfileUserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 15:34
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "删除用户拥有的角色",
        requires = {
                PuIdKey.class
        }
)
public class DeleteProfileUserRolesInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileUser表操作服务
     */
    @Autowired
    private ProfileUserService profileUserService;
    /**
     * ProfileUserRole表操作服务
     */
    @Autowired
    private ProfileUserRoleService profileUserRoleService;

    @Override
    public void invoke(FlowContext flowContext) {
        String puId = flowContext.get(PuIdKey.class);

        profileUserRoleService.deleteProfileUserRolesByPuId(flowContext.get(PuIdKey.class));
    }

}
