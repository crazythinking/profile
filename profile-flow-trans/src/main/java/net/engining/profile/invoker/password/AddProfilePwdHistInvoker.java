package net.engining.profile.invoker.password;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.sdk.key.OperationDateKey;
import net.engining.profile.sdk.key.OperatorIdKey;
import net.engining.profile.sdk.key.PasswordKey;
import net.engining.profile.sdk.key.PuIdKey;
import net.engining.profile.sdk.service.db.ProfilePwdHistService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 11:02
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "增加密码历史记录",
        requires = {
                PuIdKey.class,
                PasswordKey.class,
                OperatorIdKey.class
        },
        optional = {
                OperationDateKey.class
        }
)
public class AddProfilePwdHistInvoker extends AbstractSkippableInvoker {

    /**
     * ProfilePwdHist表操作服务
     */
    @Autowired
    private ProfilePwdHistService profilePwdHistService;

    @Override
    public void invoke(FlowContext flowContext) {
        Date operateDate = flowContext.get(OperationDateKey.class);
        if (ValidateUtilExt.isNullOrEmpty(operateDate)) {
            operateDate = new Date();
        }

        profilePwdHistService.addProfilePwdHist(flowContext.get(PuIdKey.class),
                flowContext.get(PasswordKey.class),
                flowContext.get(OperatorIdKey.class),
                operateDate);
    }

}
