package net.engining.profile.sdk.key;

import net.engining.control.api.ContextKey;
import net.engining.control.api.KeyDefinition;

import java.util.List;

/**
 * 权限集合
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/7 15:57
 * @since 1.0.0
 */
@KeyDefinition(name = "权限集合")
public interface AuthorityListKey extends ContextKey<List<String>> {
}
