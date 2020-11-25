package net.engining.profile.sdk.key;

import net.engining.control.api.ContextKey;
import net.engining.control.api.KeyDefinition;
import net.engining.profile.enums.SystemEnum;

/**
 * 所属系统
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/30 9:16
 * @since 1.0.0
 */
@KeyDefinition(name = "所属系统")
public interface SystemKey extends ContextKey<SystemEnum> {
}
