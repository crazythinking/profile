package net.engining.profile.sdk.key;

import net.engining.control.api.ContextKey;
import net.engining.control.api.KeyDefinition;

import java.util.List;

/**
 * 角色ID集合
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/7 14:44
 * @since 1.0.0
 */
@KeyDefinition(name = "角色ID集合")
public interface RoleIdListKey extends ContextKey<List<String>> {
}
