package net.engining.profile.invoker;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.sdk.key.OriginalPasswordKey;
import net.engining.profile.sdk.key.ProfileUserKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/14 15:24
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "校验旧密码是否正确",
        requires = {
                OriginalPasswordKey.class,
                ProfileUserKey.class
        }
)
public class CheckOldPasswordIsRightInvoker extends AbstractSkippableInvoker {

    /**
     * 密码加密服务
     */
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void invoke(FlowContext flowContext) {
        if (passwordEncoder.matches(flowContext.get(OriginalPasswordKey.class),
                flowContext.get(ProfileUserKey.class).getPassword())) {
            throw new ErrorMessageException(ErrorCode.CheckError, "原密码不正确");
        }
    }

}
