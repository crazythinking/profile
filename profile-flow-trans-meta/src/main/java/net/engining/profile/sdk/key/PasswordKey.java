package net.engining.profile.sdk.key;

import net.engining.control.api.ContextKey;
import net.engining.control.api.KeyDefinition;

/**
 * 密码
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/14 14:45
 * @since 1.0.0
 */
@KeyDefinition(name = "密码")
public interface PasswordKey extends ContextKey<String> {
}
