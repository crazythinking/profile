package net.engining.profile.sdk.key;

import net.engining.control.api.ContextKey;
import net.engining.control.api.KeyDefinition;
import net.engining.profile.entity.dto.ProfileBranchDto;

/**
 * 部门表数据
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/10 14:20
 * @since 1.0.0
 */
@KeyDefinition(name = "部门表数据")
public interface ProfileBranchDtoKey extends ContextKey<ProfileBranchDto> {
}
