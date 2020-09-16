package net.engining.profile.sdk.key;

import net.engining.control.api.ContextKey;
import net.engining.control.api.KeyDefinition;

/**
 * 用户ID
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/14 16:31
 * @since 1.0.0
 */
@KeyDefinition(name = "用户ID")
public interface UserIdKey extends ContextKey<String> {
}