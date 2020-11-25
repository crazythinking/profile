package net.engining.profile.sdk.key;

import net.engining.control.api.ContextKey;
import net.engining.control.api.KeyDefinition;

/**
 * 用户名称
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 10:23
 * @since 1.0.0
 */
@KeyDefinition(name = "用户名称")
public interface UserNameKey extends ContextKey<String> {
}
