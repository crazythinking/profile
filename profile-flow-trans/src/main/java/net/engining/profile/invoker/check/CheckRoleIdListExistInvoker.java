package net.engining.profile.invoker.check;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileRoleDto;
import net.engining.profile.sdk.key.RoleIdListKey;
import net.engining.profile.sdk.service.db.ProfileRoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 校验角色ID集合对应的角色是否存在
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/7 14:45
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "校验角色ID集合对应的角色是否权不存在",
        optional = {
                RoleIdListKey.class
        }
)
public class CheckRoleIdListExistInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileRole表操作服务
     */
    @Autowired
    private ProfileRoleService profileRoleService;

    @Override
    public void invoke(FlowContext flowContext) {
        List<String> roleIdList = flowContext.get(RoleIdListKey.class);
        if (ValidateUtilExt.isNotNullOrEmpty(roleIdList)) {
            List<ProfileRoleDto> profileRoleDtoList = profileRoleService.listEffectiveProfileRoleDtoByRoleId(roleIdList);
            for (String roleId : roleIdList) {
                boolean success = profileRoleDtoList.removeIf(profileRoleDto ->
                        profileRoleDto.getRoleId().equals(roleId));
                if (!success) {
                    throw new ErrorMessageException(ErrorCode.CheckError, roleId + "不存在");
                }
            }
        }
    }

}
