package net.engining.profile.invoker.department;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.sdk.key.DepartmentIdKey;
import net.engining.profile.sdk.key.DepartmentNameKey;
import net.engining.profile.sdk.key.OperationDateKey;
import net.engining.profile.sdk.key.OperatorIdKey;
import net.engining.profile.sdk.service.db.ProfileBranchService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 修改部门表记录
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 14:54
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "修改部门表记录",
        requires = {
                DepartmentIdKey.class,
                DepartmentNameKey.class,
                OperatorIdKey.class
        },
        optional = {
                OperationDateKey.class
        }
)
public class UpdateProfileBranchInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileBranch表操作服务
     */
    @Autowired
    private ProfileBranchService profileBranchService;

    @Override
    public void invoke(FlowContext flowContext) {
        Date operateDate = flowContext.get(OperationDateKey.class);
        if (ValidateUtilExt.isNullOrEmpty(operateDate)) {
            operateDate = new Date();
        }

        profileBranchService.updateProfileBranch(flowContext.get(DepartmentIdKey.class),
                flowContext.get(DepartmentNameKey.class),
                flowContext.get(OperatorIdKey.class),
                operateDate);

    }

}
