package net.engining.profile.sdk.key;

import net.engining.control.api.ContextKey;
import net.engining.control.api.KeyDefinition;

/**
 * 客户端ID
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/28 15:57
 * @since 1.0.0
 */
@KeyDefinition(name = "客户端ID")
public interface ClientIdKey extends ContextKey<String> {
}
