package net.engining.profile.security.validator;

import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.param.SecurityControl;

/**
 * 安全控制验证器接口，通过这个接口实现对安全规范的验证，如：用户有效性验证、密码复杂度验证、密码有效期验证等<br\>
 * 选择不同的安全控制验证器组成集合，来满足不同的需求。
 * 
 * @author zhangkun
 *
 */
public interface SecurityControlValidator {

	/**
	 * 对用户登录和修改密码等操作进行安全性验证，如不符合安全要求则抛出{@link ErrorMessageException}
	 * 
	 * @param user {@link ProfileUser}用户的实体对象，validator可能对用户的{@link net.engining.profile.enums.UserStatusEnum}等状态做一些修改，需要持久化到数据库，如果实体是游离态的，需要在调用后保存。
	 * @param inputPassword 接受用户输入的明文密码
	 * @param securityControl 安全控制参数{@link SecurityControl}
	 * @throws ErrorMessageException 安全验证失败则抛出此异常
	 */
	void validate(ProfileUser user, String inputPassword, SecurityControl securityControl) throws ErrorMessageException;
	
}
