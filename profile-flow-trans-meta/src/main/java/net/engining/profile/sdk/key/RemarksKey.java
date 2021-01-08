package net.engining.profile.sdk.key;

import com.sun.jna.platform.win32.WinBase;
import net.engining.control.api.ContextKey;
import net.engining.control.api.KeyDefinition;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/30 13:23
 * @since 1.0.0
 */
@KeyDefinition(name = "备注")
public interface RemarksKey extends ContextKey<String> {
}
