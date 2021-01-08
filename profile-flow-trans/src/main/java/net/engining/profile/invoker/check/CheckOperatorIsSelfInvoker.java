package net.engining.profile.invoker.check;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.profile.sdk.key.OperatorIdKey;
import net.engining.profile.sdk.key.UserIdKey;

import static net.engining.profile.sdk.service.constant.ParameterConstants.SUPERADMIN;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/15 10:14
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "校验操作人是否是自己",
        requires = {
                UserIdKey.class,
                OperatorIdKey.class
        }
)
public class CheckOperatorIsSelfInvoker extends AbstractSkippableInvoker {

    @Override
    public void invoke(FlowContext flowContext) {
        if (!flowContext.get(UserIdKey.class).equals(flowContext.get(OperatorIdKey.class))) {
            throw new ErrorMessageException(ErrorCode.CheckError, "用户只能修改自己的密码");
        }
    }

}
