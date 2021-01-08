package net.engining.profile.invoker.check;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileRoleDto;
import net.engining.profile.sdk.key.ClientIdKey;
import net.engining.profile.sdk.key.ProfileRoleDtoKey;
import net.engining.profile.sdk.key.RoleIdKey;
import net.engining.profile.sdk.service.db.ProfileRoleService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 校验角色ID对应的角色是否存在
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/30 13:43
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "校验角色ID对应的角色是否存在",
        requires = {
                RoleIdKey.class
        }
)
public class CheckRoleIdExistsInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileRole表操作服务
     */
    @Autowired
    private ProfileRoleService profileRoleService;

    @Override
    public void invoke(FlowContext flowContext) {
        ProfileRoleDto profileRoleDto = profileRoleService
                .getEffectiveProfileRoleDtoByRoleId(flowContext.get(RoleIdKey.class));
        if (ValidateUtilExt.isNullOrEmpty(profileRoleDto)) {
            throw new ErrorMessageException(ErrorCode.Null, "有效角色不存在");
        }

        flowContext.put(ProfileRoleDtoKey.class, profileRoleDto);
        flowContext.put(ClientIdKey.class, profileRoleDto.getClientId());
    }

}
