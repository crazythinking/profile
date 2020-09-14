package net.engining.profile.sdk.key;

import net.engining.control.api.ContextKey;
import net.engining.control.api.KeyDefinition;
import net.engining.profile.enums.OperationType;

/**
 * 操作类型
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/14 16:32
 * @since 1.0.0
 */
@KeyDefinition(name = "操作类型")
public interface OperationTypeKey extends ContextKey<OperationType> {
}
