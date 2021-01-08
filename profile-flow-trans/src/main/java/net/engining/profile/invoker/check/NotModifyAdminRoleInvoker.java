package net.engining.profile.invoker.check;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.profile.sdk.key.RoleIdKey;

import static net.engining.profile.sdk.service.constant.ParameterConstants.SUPERADMIN;

/**
 * 不能对admin角色进行修改的校验
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/27 10:00
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "不能对admin角色进行操作的校验",
        requires = {
                RoleIdKey.class
        }
)
public class NotModifyAdminRoleInvoker extends AbstractSkippableInvoker {

    @Override
    public void invoke(FlowContext flowContext) {
        if (SUPERADMIN.equals(flowContext.get(RoleIdKey.class))) {
            throw new ErrorMessageException(ErrorCode.CheckError, "不能对超级管理员角色进行该操作");
        }
    }

}
