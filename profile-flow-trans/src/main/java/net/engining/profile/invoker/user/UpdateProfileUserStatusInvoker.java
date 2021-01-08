package net.engining.profile.invoker.user;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.sdk.key.OperationDateKey;
import net.engining.profile.sdk.key.OperatorIdKey;
import net.engining.profile.sdk.key.ProfileUserDtoKey;
import net.engining.profile.sdk.key.UserIdKey;
import net.engining.profile.sdk.key.UserStatusKey;
import net.engining.profile.sdk.service.db.ProfileUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 修改用户状态
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 13:00
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "修改用户状态",
        requires = {
                UserIdKey.class,
                UserStatusKey.class,
                OperatorIdKey.class
        },
        optional = {
                OperationDateKey.class,
                ProfileUserDtoKey.class
        }
)
public class UpdateProfileUserStatusInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileUser表操作服务
     */
    @Autowired
    private ProfileUserService profileUserService;

    @Override
    public void invoke(FlowContext flowContext) {
        Date operateDate = flowContext.get(OperationDateKey.class);
        if (ValidateUtilExt.isNullOrEmpty(operateDate)) {
            operateDate = new Date();
        }

        profileUserService.updateProfileUserStatus(flowContext.get(UserIdKey.class),
                flowContext.get(UserStatusKey.class),
                flowContext.get(OperatorIdKey.class),
                flowContext.get(OperationDateKey.class));
    }

}
