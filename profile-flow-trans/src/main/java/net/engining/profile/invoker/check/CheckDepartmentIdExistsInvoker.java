package net.engining.profile.invoker.check;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileBranchDto;
import net.engining.profile.sdk.key.DepartmentIdKey;
import net.engining.profile.sdk.service.db.ProfileBranchService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 校验部门ID对应的部门是否存在
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/30 9:29
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "校验部门ID对应的部门是否存在",
        requires = {
                DepartmentIdKey.class,
        }
)
public class CheckDepartmentIdExistsInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileBranch表操作服务
     */
    @Autowired
    private ProfileBranchService profileBranchService;

    @Override
    public void invoke(FlowContext flowContext) {
        ProfileBranchDto profileBranchDto = profileBranchService
                .getEffectiveProfileBranchDtoByBranchId(flowContext.get(DepartmentIdKey.class));
        if (ValidateUtilExt.isNullOrEmpty(profileBranchDto)) {
            throw new ErrorMessageException(ErrorCode.CheckError, "有效部门不存在");
        }
    }

}
