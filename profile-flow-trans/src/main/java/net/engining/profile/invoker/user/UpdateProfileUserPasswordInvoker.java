package net.engining.profile.invoker.user;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.sdk.key.OperationDateKey;
import net.engining.profile.sdk.key.OperationObjectKey;
import net.engining.profile.sdk.key.OperatorIdKey;
import net.engining.profile.sdk.key.PasswordKey;
import net.engining.profile.sdk.key.PuIdKey;
import net.engining.profile.sdk.key.UserIdKey;
import net.engining.profile.sdk.service.db.ProfileUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 15:11
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "修改密码",
        requires = {
                UserIdKey.class,
                PasswordKey.class,
                OperatorIdKey.class
        },
        optional = {
                OperationDateKey.class
        }
)
public class UpdateProfileUserPasswordInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileUser表操作服务
     */
    @Autowired
    private ProfileUserService profileUserService;

    @Override
    public void invoke(FlowContext flowContext) {
        String userId = flowContext.get(UserIdKey.class);
        Date operateDate = flowContext.get(OperationDateKey.class);
        if (ValidateUtilExt.isNullOrEmpty(operateDate)) {
            operateDate = new Date();
        }

        String puId = profileUserService.updateProfileUserPassword(flowContext.get(UserIdKey.class),
                flowContext.get(PasswordKey.class),
                flowContext.get(OperatorIdKey.class),
                operateDate);

        flowContext.put(PuIdKey.class, puId);
        // 修改密码传入的是puId，所以需要在这里单独赋值
        flowContext.put(OperationObjectKey.class, userId);
    }

}
