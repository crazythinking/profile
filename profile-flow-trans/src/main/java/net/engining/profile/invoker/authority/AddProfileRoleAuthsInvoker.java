package net.engining.profile.invoker.authority;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileRoleAuthDto;
import net.engining.profile.sdk.key.OperationDateKey;
import net.engining.profile.sdk.key.OperatorIdKey;
import net.engining.profile.sdk.key.ProfileRoleAuthDtoListKey;
import net.engining.profile.sdk.key.RoleIdKey;
import net.engining.profile.sdk.service.db.ProfileRoleAuthService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 新增角色权限对应关系
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/7 15:56
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "新增角色权限对应关系",
        requires = {
                RoleIdKey.class,
                OperatorIdKey.class
        },
        optional = {
                ProfileRoleAuthDtoListKey.class,
                OperationDateKey.class
        }
)
public class AddProfileRoleAuthsInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileRoleAuth表操作服务
     */
    @Autowired
    private ProfileRoleAuthService profileRoleAuthService;

    @Override
    public void invoke(FlowContext flowContext) {
        String roleId = flowContext.get(RoleIdKey.class);
        String operatorId = flowContext.get(OperatorIdKey.class);
        Date operateDate = flowContext.get(OperationDateKey.class);
        if (ValidateUtilExt.isNullOrEmpty(operateDate)) {
            operateDate = new Date();
        }

        List<ProfileRoleAuthDto> list = flowContext.get(ProfileRoleAuthDtoListKey.class);
        for (ProfileRoleAuthDto profileRoleAuthDto : list) {
            profileRoleAuthDto.setRoleId(roleId);
            profileRoleAuthDto.setCreateUser(operatorId);
            profileRoleAuthDto.setCreateTimestamp(operateDate);
            profileRoleAuthService.addProfileRoleAuth(profileRoleAuthDto);
        }
    }

}
