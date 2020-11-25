package net.engining.profile.sdk.key;

import net.engining.control.api.ContextKey;
import net.engining.control.api.KeyDefinition;

import java.util.Date;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/30 13:22
 * @since 1.0.0
 */
@KeyDefinition(name = "操作时间")
public interface OperationDateKey extends ContextKey<Date> {
}
