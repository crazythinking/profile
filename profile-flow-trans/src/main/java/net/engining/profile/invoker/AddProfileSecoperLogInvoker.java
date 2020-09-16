package net.engining.profile.invoker;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.web.WebCommonUtils;
import net.engining.profile.enums.OperationType;
import net.engining.profile.sdk.key.OperationTypeKey;
import net.engining.profile.sdk.key.OperatorIdKey;
import net.engining.profile.sdk.key.PuIdKey;
import net.engining.profile.sdk.key.UserIdKey;
import net.engining.profile.security.service.ProfileSecurityLoggerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/14 16:29
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "操作安全日志记录",
        requires = {
                PuIdKey.class,
                OperationTypeKey.class,
                OperatorIdKey.class,
                UserIdKey.class
        }
)
public class AddProfileSecoperLogInvoker extends AbstractSkippableInvoker {

    /**
     * 安全日志服务
     */
    @Autowired
    ProfileSecurityLoggerService profileSecurityLoggerService;

    @Override
    public void invoke(FlowContext flowContext) {
        profileSecurityLoggerService.logSecuOperation(flowContext.get(PuIdKey.class),
                flowContext.get(OperationTypeKey.class),
                flowContext.get(OperatorIdKey.class),
                new Date(),
                flowContext.get(UserIdKey.class));
    }

}
