package net.engining.profile.invoker.security;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.sdk.key.OperationDateKey;
import net.engining.profile.sdk.key.OperationIpKey;
import net.engining.profile.sdk.key.OperationObjectKey;
import net.engining.profile.sdk.key.OperationTypeKey;
import net.engining.profile.sdk.key.OperatorIdKey;
import net.engining.profile.sdk.key.RemarksKey;
import net.engining.profile.sdk.key.RoleIdKey;
import net.engining.profile.sdk.service.db.ProfileUserService;
import net.engining.profile.security.service.ProfileSecurityLoggerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 记录操作日志
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/30 11:16
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "记录操作日志",
        requires = {
                OperatorIdKey.class,
                OperationObjectKey.class,
                OperationIpKey.class,
                OperationTypeKey.class
        },
        optional = {
                OperationDateKey.class,
                RemarksKey.class
        }
)
public class AddOperationLogInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileUser表操作服务
     */
    @Autowired
    private ProfileUserService profileUserService;
    /**
     * 安全日志操作服务
     */
    @Autowired
    private ProfileSecurityLoggerService profileSecurityLoggerService;

    @Override
    public void invoke(FlowContext flowContext) {
        Date operateDate = flowContext.get(OperationDateKey.class);
        if (ValidateUtilExt.isNullOrEmpty(operateDate)) {
            operateDate = new Date();
        }

        String operatorId = flowContext.get(OperatorIdKey.class);
        String puId = profileUserService.getPuIdByUserId(operatorId);

        profileSecurityLoggerService.logSecuOperation(puId,
                flowContext.get(OperationTypeKey.class),
                flowContext.get(OperationIpKey.class),
                operateDate,
                flowContext.get(OperationObjectKey.class),
                flowContext.get(RemarksKey.class));
    }

}
