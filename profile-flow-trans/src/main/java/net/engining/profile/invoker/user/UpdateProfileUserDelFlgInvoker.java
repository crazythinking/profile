package net.engining.profile.invoker.user;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.sdk.key.OperationDateKey;
import net.engining.profile.sdk.key.OperatorIdKey;
import net.engining.profile.sdk.key.PuIdKey;
import net.engining.profile.sdk.key.UserIdKey;
import net.engining.profile.sdk.service.db.ProfileUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 逻辑删除用户表记录
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 12:49
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "逻辑删除用户表记录",
        requires = {
                UserIdKey.class,
                OperatorIdKey.class
        },
        optional = {
                OperationDateKey.class
        }
)
public class UpdateProfileUserDelFlgInvoker extends AbstractSkippableInvoker {

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

        profileUserService.updateProfileUserDelFlg(flowContext.get(UserIdKey.class),
                flowContext.get(OperatorIdKey.class),
                operateDate);
    }

}
