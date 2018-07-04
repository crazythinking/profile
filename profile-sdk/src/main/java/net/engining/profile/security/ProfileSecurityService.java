package net.engining.profile.security;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import net.engining.pg.parameter.ParameterFacility;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.profile.entity.enums.StatusDef;
import net.engining.profile.entity.model.ProfilePwdHist;
import net.engining.profile.entity.model.ProfileRole;
import net.engining.profile.entity.model.ProfileRoleAuth;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.entity.model.QProfileRole;
import net.engining.profile.entity.model.QProfileRoleAuth;
import net.engining.profile.entity.model.QProfileUser;
import net.engining.profile.entity.model.QProfileUserRole;
import net.engining.profile.param.SecurityControl;
import net.engining.profile.security.validator.PasswordComplexityValidator;
import net.engining.profile.security.validator.PasswordExpireValidator;
import net.engining.profile.security.validator.PasswordReuseCountValidator;
import net.engining.profile.security.validator.SecurityControlValidator;
import net.engining.profile.security.validator.UsernameFormatValidator;

/**
 * 权限安全服务，通过DB提供安全服务
 * 
 */
@Service
public class ProfileSecurityService {

	@PersistenceContext
	private EntityManager em;

	private List<SecurityControlValidator> usernameValidators = new ArrayList<SecurityControlValidator>();

	private List<SecurityControlValidator> authenticationValidators = new ArrayList<SecurityControlValidator>();

	private List<SecurityControlValidator> newPasswordValidators = new ArrayList<SecurityControlValidator>();

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private ParameterFacility parameterFacility;

	/**
	 * 创建用户
	 * @param userInfo
	 * @param password
	 * @throws ErrorMessageException
	 */
	@Transactional
	public void createNewUser(ProfileUser userInfo, String password) throws ErrorMessageException {
		SecurityControl securityControl = parameterFacility.loadUniqueParameter(SecurityControl.class);
		// 用户名校验
		for (SecurityControlValidator validator : usernameValidators) {
			validator.validate(userInfo, null, securityControl);
		}
		// 密码校验
		for (SecurityControlValidator passwordValidator : newPasswordValidators) {
			passwordValidator.validate(userInfo, userInfo.getPassword(), securityControl);
		}
		// 计算密码有效期
		userInfo.setPwdExpDate(getExpiredDate(Calendar.getInstance().getTime(), securityControl.pwdExpireDays));
		// 用户所属机构
		// 用户状态为新用户
		// FIXME 暂时由页面决定用户状态，后续再讨论
		// userInfo.setStatus(StatusDef.N);
		// 加密密码
		userInfo.setPassword(passwordEncoder.encode(password));
		// 保存用户
		userInfo.fillDefaultValues();
		em.persist(userInfo);
		// 密码历史
		addPasswordHistory(userInfo.getPuId(), passwordEncoder.encode(password));
	}

	/**
	 * 默认构造方法，加载所有的安全控制验证器，可通过注入覆盖
	 */
	@PostConstruct
	public void init() {
		// 添加用户名格式验证器
		usernameValidators.add(applicationContext.getBean(UsernameFormatValidator.class));
		// 添加用户认证安全控制器
		authenticationValidators.add(applicationContext.getBean(PasswordExpireValidator.class));
		// 添加修改密码用户安全控制器
		newPasswordValidators.add(applicationContext.getBean(PasswordComplexityValidator.class));
		newPasswordValidators.add(applicationContext.getBean(PasswordReuseCountValidator.class));
	}

	@Transactional
	public ProfileUser getUserInfo(String puId) throws ErrorMessageException {
		ProfileUser user = em.find(ProfileUser.class, puId);
		if (user == null)
			throw new ErrorMessageException(ErrorCode.BadRequest, "无效用户:" + puId);
		return user;
	}

	/**
	 * 获取用户角色
	 * @param puId
	 * @return
	 */
	public Iterable<ProfileRole> getUserRoles(String puId) {
		QProfileUserRole qUserRole = QProfileUserRole.profileUserRole;
		QProfileRole qRole = QProfileRole.profileRole;

		return new JPAQueryFactory(em)
				.select(qRole)
				.from(qUserRole, qRole)
				.where(qUserRole.puId.eq(puId), qUserRole.roleId.eq(qRole.roleId))
				.fetch();
	}

	/**
	 * 获取用户权限
	 * @param puId
	 * @return
	 */
	public Iterable<ProfileRoleAuth> getUserAuthorities(String puId) {

		// 查询用户角色
		QProfileUserRole qUserRole = QProfileUserRole.profileUserRole;
		QProfileRoleAuth qRoleAuth = QProfileRoleAuth.profileRoleAuth;

		return new JPAQueryFactory(em)
				.select(qRoleAuth)
				.from(qUserRole, qRoleAuth)
				.where(qUserRole.puId.eq(puId), qUserRole.roleId.eq(qRoleAuth.roleId))
				.distinct()
				.fetch();
	}

	/**
	 * 用户解锁
	 * @param username
	 * @throws ErrorMessageException
	 */
	public void unlockUser(String username) throws ErrorMessageException {
		ProfileUser user = em.find(ProfileUser.class, username);
		if (user == null)
			throw new ErrorMessageException(ErrorCode.BadRequest, "无效用户:" + username);
		user.setStatus(StatusDef.A);
	}

	/**
	 * 修改密码
	 * @param username
	 * @param password
	 * @throws ErrorMessageException
	 */
	@Transactional
	public void changePassword(String username, String password) throws ErrorMessageException {
		ProfileUser user = em.find(ProfileUser.class, username);
		if (user == null)
			throw new ErrorMessageException(ErrorCode.BadRequest, "无效用户:" + username);

		// 这个参数是必须有的，所以不做判空操作
		SecurityControl securityControl = parameterFacility.loadUniqueParameter(SecurityControl.class);

		// 密码修改校验
		for (SecurityControlValidator passwordValidator : newPasswordValidators) {
			passwordValidator.validate(user, password, securityControl);
		}

		user.setPassword(passwordEncoder.encode(password));
		// TODO 在服务中添加了解锁用户方法，当前做法值得商榷
		user.setStatus(StatusDef.A);
		// 重新计算密码有效期
		user.setPwdExpDate(getExpiredDate(Calendar.getInstance().getTime(), securityControl.pwdExpireDays));

		// 记录密码历史表
		addPasswordHistory(user.getPuId(), passwordEncoder.encode(password));
	}

	/**
	 * 获取用户列表通过权限标识
	 * @param auth
	 * @return
	 */
	@Transactional
	public Map<String, String> getUserByAuthority(String auth) {
		// 根据auth 找到所有roleId，然后再根据角色找到userId, 根据userId找 username
		QProfileUserRole qUserRole = QProfileUserRole.profileUserRole;
		QProfileRoleAuth qRoleAuth = QProfileRoleAuth.profileRoleAuth;
		QProfileUser qUser = QProfileUser.profileUser;

		List<Tuple> result = new JPAQueryFactory(em)
				.select(qUser.userId, qUser.name)
				.from(qUser, qUserRole, qRoleAuth)
				.where(qUser.puId.eq(qUserRole.puId), qUserRole.roleId.eq(qRoleAuth.roleId), qRoleAuth.authority.eq(auth))
				.distinct()
				.fetch();

		HashMap<String, String> map = Maps.newHashMap();
		for (Tuple tuple : result) {
			map.put(tuple.get(qUser.userId), tuple.get(qUser.name));
		}

		return map;

	}

	/**
	 * 记录用户的密码历史
	 * 
	 * @param userId
	 *            用户ID
	 * @param password
	 *            密码
	 */
	private void addPasswordHistory(String puId, String password) {
		ProfilePwdHist pwdHist = new ProfilePwdHist();
		pwdHist.setPuId(puId);
		pwdHist.setPassword(password);
		pwdHist.setPwdCreTime(new Date());
		em.persist(pwdHist);
	}

	private Date getExpiredDate(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}

}
