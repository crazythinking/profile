package net.engining.profile.invoker.role;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.sdk.key.DepartmentIdKey;
import net.engining.profile.sdk.key.OperationDateKey;
import net.engining.profile.sdk.key.OperationObjectKey;
import net.engining.profile.sdk.key.OperatorIdKey;
import net.engining.profile.sdk.key.RoleIdKey;
import net.engining.profile.sdk.key.RoleNameKey;
import net.engining.profile.sdk.key.SystemKey;
import net.engining.profile.sdk.service.db.ProfileRoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 新增角色表记录
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/30 10:20
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "新增角色表记录",
        requires = {
                SystemKey.class,
                DepartmentIdKey.class,
                RoleNameKey.class,
                OperatorIdKey.class
        },
        optional = {
                OperationDateKey.class
        },
        results = {
                RoleIdKey.class
        }
)
public class AddProfileRoleInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileRole表操作服务
     */
    @Autowired
    private ProfileRoleService profileRoleService;

    @Override
    public void invoke(FlowContext flowContext) {
        Date operateDate = flowContext.get(OperationDateKey.class);
        if (ValidateUtilExt.isNullOrEmpty(operateDate)) {
            operateDate = new Date();
        }

        String roleId = profileRoleService.addProfileRole(flowContext.get(RoleNameKey.class),
                flowContext.get(DepartmentIdKey.class),
                flowContext.get(SystemKey.class),
                flowContext.get(OperatorIdKey.class),
                operateDate);

        flowContext.put(RoleIdKey.class, roleId);
        flowContext.put(OperationObjectKey.class, roleId);
    }

}
