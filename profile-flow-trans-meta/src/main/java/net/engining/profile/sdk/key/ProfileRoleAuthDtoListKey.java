package net.engining.profile.sdk.key;

import net.engining.control.api.ContextKey;
import net.engining.control.api.KeyDefinition;
import net.engining.profile.entity.dto.ProfileRoleAuthDto;

import java.util.List;

/**
 * 权限表记录
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/9 18:57
 * @since 1.0.0
 */
@KeyDefinition(name = "权限表记录")
public interface ProfileRoleAuthDtoListKey extends ContextKey<List<ProfileRoleAuthDto>> {
}
