package net.engining.profile.sdk.key;

import net.engining.control.api.ContextKey;
import net.engining.control.api.KeyDefinition;
import net.engining.profile.entity.dto.ProfileRoleDto;

/**
 * 角色表数据
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 12:41
 * @since 1.0.0
 */
@KeyDefinition(name = "角色表数据")
public interface ProfileRoleDtoKey extends ContextKey<ProfileRoleDto> {
}
