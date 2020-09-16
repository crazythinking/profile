package net.engining.profile.sdk.key;

import net.engining.control.api.ContextKey;
import net.engining.control.api.KeyDefinition;
import net.engining.profile.entity.model.ProfileUser;

/**
 * 用户信息
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/14 14:34
 * @since 1.0.0
 */
@KeyDefinition(name ="用户信息")
public interface ProfileUserKey extends ContextKey<ProfileUser> {
}
