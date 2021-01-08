package net.engining.profile.invoker.authority;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileUserRoleDto;
import net.engining.profile.sdk.key.OperationDateKey;
import net.engining.profile.sdk.key.OperatorIdKey;
import net.engining.profile.sdk.key.PuIdKey;
import net.engining.profile.sdk.key.RoleIdListKey;
import net.engining.profile.sdk.service.db.ProfileUserRoleService;
import net.engining.profile.sdk.service.db.ProfileUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 新增用户角色对应关系记录
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/7 15:04
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "新增用户角色对应关系记录",
        requires = {
                OperatorIdKey.class,
                PuIdKey.class
        },
        optional = {
                RoleIdListKey.class,
                OperationDateKey.class
        }
)
public class AddProfileUserRolesInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileUser表操作服务
     */
    @Autowired
    private ProfileUserService profileUserService;
    /**
     * ProfileUserRole表操作服务
     */
    @Autowired
    private ProfileUserRoleService profileUserRoleService;

    @Override
    public void invoke(FlowContext flowContext) {
        String operatorId = flowContext.get(OperatorIdKey.class);
        Date operateDate = flowContext.get(OperationDateKey.class);
        if (ValidateUtilExt.isNullOrEmpty(operateDate)) {
            operateDate = new Date();
        }

        String puId = flowContext.get(PuIdKey.class);

        List<String> roleIdList = flowContext.get(RoleIdListKey.class);
        for (String roleId : roleIdList) {
            ProfileUserRoleDto profileUserRoleDto = new ProfileUserRoleDto();
            profileUserRoleDto.setPuId(puId);
            profileUserRoleDto.setRoleId(roleId);
            profileUserRoleDto.setCreateUser(operatorId);
            profileUserRoleDto.setCreateTimestamp(operateDate);
            profileUserRoleService.addProfileUserRole(profileUserRoleDto);
        }
    }

}
