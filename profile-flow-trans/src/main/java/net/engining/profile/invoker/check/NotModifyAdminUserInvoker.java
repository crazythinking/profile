package net.engining.profile.invoker.check;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.profile.sdk.key.UserIdKey;

import static net.engining.profile.sdk.service.constant.ParameterConstants.ADMIN_LIST;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/27 10:34
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "不能对admin用户进行操作的校验",
        requires = {
                UserIdKey.class
        }
)
public class NotModifyAdminUserInvoker extends AbstractSkippableInvoker {

    @Override
    public void invoke(FlowContext flowContext) {
        if (ADMIN_LIST.contains(flowContext.get(UserIdKey.class))) {
            throw new ErrorMessageException(ErrorCode.CheckError, "不能对超级管理员用户进行该操作");
        }
    }

}
