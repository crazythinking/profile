package net.engining.profile.invoker;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.profile.entity.enums.StatusDef;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.sdk.key.OperatorIdKey;
import net.engining.profile.sdk.key.PasswordKey;
import net.engining.profile.sdk.key.ProfileUserKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/14 16:18
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "修改密码",
        requires = {
                PasswordKey.class,
                ProfileUserKey.class,
                OperatorIdKey.class
        }
)
public class ChangePasswordInvoker extends AbstractSkippableInvoker {

    /**
     * 密码加密服务
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void invoke(FlowContext flowContext) {
        ProfileUser profileUser = flowContext.get(ProfileUserKey.class);

        profileUser.setMtnTimestamp(new Date());
        profileUser.setMtnUser(flowContext.get(OperatorIdKey.class));
        profileUser.setPassword(passwordEncoder.encode(flowContext.get(PasswordKey.class)));
        profileUser.setPwdTries(0);
        profileUser.setStatus(StatusDef.A);
    }

}
