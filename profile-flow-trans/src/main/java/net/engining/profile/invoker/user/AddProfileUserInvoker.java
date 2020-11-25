package net.engining.profile.invoker.user;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.sdk.key.DepartmentIdKey;
import net.engining.profile.sdk.key.OperationDateKey;
import net.engining.profile.sdk.key.OperationObjectKey;
import net.engining.profile.sdk.key.OperatorIdKey;
import net.engining.profile.sdk.key.PasswordKey;
import net.engining.profile.sdk.key.PuIdKey;
import net.engining.profile.sdk.key.UserIdKey;
import net.engining.profile.sdk.key.UserNameKey;
import net.engining.profile.sdk.service.db.ProfileUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static net.engining.profile.sdk.service.constant.ParameterConstants.DEFAULT_PASSWORD;

/**
 * 新增用户表记录
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 10:21
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "新增用户表记录",
        requires = {
                UserIdKey.class,
                UserNameKey.class,
                DepartmentIdKey.class,
                OperatorIdKey.class
        },
        optional = {
                OperationDateKey.class
        }
)
public class AddProfileUserInvoker extends AbstractSkippableInvoker {

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

        String puId = profileUserService.addProfileUser(flowContext.get(DepartmentIdKey.class),
                userId,
                flowContext.get(UserNameKey.class),
                flowContext.get(OperatorIdKey.class),
                operateDate);

        flowContext.put(PuIdKey.class, puId);
        flowContext.put(PasswordKey.class, DEFAULT_PASSWORD);
    }

}
