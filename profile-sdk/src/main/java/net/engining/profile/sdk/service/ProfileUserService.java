package net.engining.profile.sdk.service;

import com.google.common.base.Strings;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.engining.pg.parameter.ParameterFacility;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.support.db.querydsl.JPAFetchResponseBuilder;
import net.engining.pg.support.db.querydsl.Range;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.model.*;
import net.engining.profile.param.SecurityControl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Service
public class ProfileUserService{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ParameterFacility parameterFacility;

	/**
	 * 根据分支机构编码,用户id,用户姓名（模糊）查找机构下的用户对象
	 * @param branchId
	 * @param name
	 * @param orgId
	 * @param range
	 * @return
	 */
	public FetchResponse<UserManagerBean> fetchUsers4Branch(String branchId,String name,String orgId,Range range) {
		QProfileUser q = QProfileUser.profileUser;
		QProfileUserRole r = QProfileUserRole.profileUserRole;
		QProfileRole p = QProfileRole.profileRole;
		JPAQuery<Tuple> query = new JPAQueryFactory(em)
				.select(q.puId,q.branchId,q.name,q.email,q.orgId,q.pwdExpDate,q.pwdTries,q.status,q.userId).from(q)
				.orderBy(q.branchId.asc(),q.userId.asc());
		if(!Strings.isNullOrEmpty(branchId)){
			query.where(q.branchId.eq(branchId.trim()).and(q.orgId.eq(orgId)));
		}
		if (!Strings.isNullOrEmpty(name)){
			query.where(q.name.like("%"+name+"%"));
		}
		FetchResponse<Tuple> build = new JPAFetchResponseBuilder<Tuple>().range(range).build(query);
		FetchResponse<UserManagerBean> fetchResponse = new FetchResponse<>();
		List<UserManagerBean> mapList = new ArrayList<>();
		for (Tuple tuple : build.getData()) {
			UserManagerBean userManagerBean = new UserManagerBean();
			userManagerBean.setPuId(tuple.get(q.puId));
			userManagerBean.setBranchId(tuple.get(q.branchId));
			userManagerBean.setName(tuple.get(q.name));
			userManagerBean.setEmail(tuple.get(q.email));
			userManagerBean.setOrgId(tuple.get(q.orgId));
			userManagerBean.setPwdExpDate(tuple.get(q.pwdExpDate));
			userManagerBean.setPwdTries(tuple.get(q.pwdTries));
			userManagerBean.setStatus(tuple.get(q.status));
			userManagerBean.setUserId(tuple.get(q.userId));
			List<String> list = new JPAQueryFactory(em)
					.select(p.roleName).from(r, p)
					.where(r.puId.eq(tuple.get(q.puId)),r.roleId.eq(p.roleId)).fetch();
			userManagerBean.setRoleName(list);
			mapList.add(userManagerBean);
		}
		fetchResponse.setData(mapList);
		fetchResponse.setRowCount(range.getLength());
		fetchResponse.setStart(range.getStart());
		return fetchResponse;
	}
/**
 * 根据userId查询用户信息
 * @param userId
 * @return
 */
	public FetchResponse<Map<String, Object>> getUserInfoByUserId(String userId) {
		QProfileUser q = QProfileUser.profileUser;
		JPAQuery<Tuple> query = new JPAQueryFactory(em)
				.select(q.puId,q.branchId,q.name,q.email,q.orgId,q.pwdExpDate,q.pwdTries,q.status,q.userId).from(q);
		if(StringUtils.isNotBlank(userId)){
			query.where(q.userId.eq(userId)).fetch();
		}
		return new JPAFetchResponseBuilder<Map<String, Object>>().buildAsMap(query,
				q.puId,q.branchId,q.name,q.email,q.orgId,q.pwdExpDate,q.pwdTries,q.status,q.userId);
				
	}
	/**
	 * 清除用户相关Profile信息
	 * @param puIds
	 */
	@Transactional
	public void deleteProfileUsers(List<String> puIds) {
		QProfileUser qProfileUser = QProfileUser.profileUser;
		QProfilePwdHist qProfilePwdHist = QProfilePwdHist.profilePwdHist;
		QProfileUserRole qProfileUserRole = QProfileUserRole.profileUserRole;
		
		long n1 = new JPAQueryFactory(em).delete(qProfilePwdHist).where(qProfilePwdHist.puId.in(puIds)).execute();
		logger.debug("删除了{}条ProfilePwdHist",n1);
		long n2 = new JPAQueryFactory(em).delete(qProfileUserRole).where(qProfileUserRole.puId.in(puIds)).execute();
		logger.debug("删除了{}条ProfileUserRole",n2);
		long n3 = new JPAQueryFactory(em).delete(qProfileUser).where(qProfileUser.puId.in(puIds)).execute();
		logger.debug("删除了{}条ProfileUser",n3);
//		for(String puId : puIds)
//		{
//			
//			JPAQuery query = new JPAQuery(em).from(qProfilePwdHist);
//			for(ProfilePwdHist uPwdHist: query.where(qProfilePwdHist.puId.eq(puId)).list(qProfilePwdHist)){
//				em.remove(uPwdHist);
//			}
//			
//			query = new JPAQuery(em).from(qProfileUserRole);
//			for(ProfileUserRole userRole: query.where(qProfileUserRole.puId.eq(puId)).list(qProfileUserRole)){
//				em.remove(userRole);
//			}
//			
//			ProfileUser user = em.find(ProfileUser.class, puId);
//			if(user != null)
//				em.remove(user);
//		}	
	}

	/**
	 * 根据用户登陆Id查找用户信息
	 * @param userId
	 * @return
	 */
	public ProfileUser findProfileUserInfoByUserId(String userId) {
		QProfileUser q = QProfileUser.profileUser;
		JPAQuery<ProfileUser> query = new JPAQueryFactory(em).select(q).from(q).where(q.userId.eq(userId));
		return query.fetchOne();
	}
	
	/**
	 * 根据ProfileUser主键查用户信息
	 * @param puId
	 * @return
	 */
	public ProfileUser findProfileUserInfo(String puId) {
		
		return em.find(ProfileUser.class, puId);
	}

	/**
	 * @param user
	 */
	@Transactional
	public void updateProfileUser(ProfileUser user) {
		
		ProfileUser orginUser = em.find(ProfileUser.class, user.getPuId());
		//TODO set需要修改的值
		orginUser.setName(user.getName());
		orginUser.setEmail(user.getEmail());
		orginUser.setMtnUser(user.getMtnUser());
		orginUser.setStatus(user.getStatus());
//		orginUser.setUserId(user.getUserId());
	}
	
	@Transactional
	public void addProfileUser(ProfileUser user) throws ErrorMessageException{
		
		Optional<SecurityControl> sc = parameterFacility.getUniqueParameter(SecurityControl.class);

		String value = user.getPassword();
		if (sc.isPresent() && sc.get().complexPwdInd &&
			(!value.matches(".*[a-z].*")||!value.matches(".*[A-Z].*")||!value.matches(".*\\d.*")))
		{
			throw new ErrorMessageException(ErrorCode.CheckError, "密码必须由大、小写字母及数字组成");
		}
		
		user.fillDefaultValues();
		if(em.find(ProfileUser.class, user.getUserId()) != null){
			throw new ErrorMessageException(ErrorCode.CheckError, "添加用户失败:用户已存在");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		em.persist(user);
	}

}
