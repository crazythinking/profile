package net.engining.profile.invoker.check;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.profile.sdk.key.OperatorIdKey;
import net.engining.profile.sdk.key.UserIdKey;

/**
 * 不能对自己进行操作
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/12/11 16:40
 * @since 1.0.0
 */
@InvokerDefinition(
        requires = {
                UserIdKey.class,
                OperatorIdKey.class
        }
)
public class CheckOperatorIsNotSelfInvoker extends AbstractSkippableInvoker {

    @Override
    public void invoke(FlowContext flowContext) {
        if (flowContext.get(UserIdKey.class).equals(flowContext.get(OperatorIdKey.class))) {
            throw new ErrorMessageException(ErrorCode.CheckError, "不能对自己进行该操作");
        }
    }

}
