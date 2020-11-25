package net.engining.profile.sdk.key;

import net.engining.control.api.ContextKey;
import net.engining.control.api.KeyDefinition;
import net.engining.profile.enums.UserStatusEnum;

/**
 * 用户状态
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 13:01
 * @since 1.0.0
 */
@KeyDefinition(name = "用户状态")
public interface UserStatusKey extends ContextKey<UserStatusEnum> {
}
