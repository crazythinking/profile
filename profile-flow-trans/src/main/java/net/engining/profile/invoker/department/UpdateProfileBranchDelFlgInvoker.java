package net.engining.profile.invoker.department;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.sdk.key.DepartmentIdKey;
import net.engining.profile.sdk.key.OperationDateKey;
import net.engining.profile.sdk.key.OperatorIdKey;
import net.engining.profile.sdk.service.db.ProfileBranchService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 15:05
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "逻辑删除部门信息",
        requires = {
                DepartmentIdKey.class,
                OperatorIdKey.class
        },
        optional = {
                OperationDateKey.class
        }
)
public class UpdateProfileBranchDelFlgInvoker extends AbstractSkippableInvoker {

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

        profileBranchService.updateProfileBranchDelFlg(flowContext.get(DepartmentIdKey.class),
                flowContext.get(OperatorIdKey.class),
                operateDate);
    }

}
