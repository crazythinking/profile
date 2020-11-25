package net.engining.profile.sdk.key;

import net.engining.control.api.ContextKey;
import net.engining.control.api.KeyDefinition;
import net.engining.profile.entity.dto.ProfileUserDto;

/**
 * 用户表数据
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 12:14
 * @since 1.0.0
 */
@KeyDefinition(name = "用户表数据")
public interface ProfileUserDtoKey extends ContextKey<ProfileUserDto> {
}
