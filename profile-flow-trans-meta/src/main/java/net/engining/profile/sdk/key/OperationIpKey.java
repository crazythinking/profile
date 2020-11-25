package net.engining.profile.sdk.key;

import net.engining.control.api.ContextKey;
import net.engining.control.api.KeyDefinition;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/30 13:20
 * @since 1.0.0
 */
@KeyDefinition(name = "来源IP")
public interface OperationIpKey extends ContextKey<String> {
}
