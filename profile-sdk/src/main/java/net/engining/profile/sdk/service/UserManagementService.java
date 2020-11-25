package net.engining.profile.sdk.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.engining.pg.parameter.ParameterFacility;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.support.db.querydsl.JPAFetchResponseBuilder;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileUserDto;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.entity.model.QProfilePwdHist;
import net.engining.profile.entity.model.QProfileUser;
import net.engining.profile.entity.model.QProfileUserRole;
import net.engining.profile.param.SecurityControl;
import net.engining.profile.sdk.service.bean.dto.RoleSimpleDto;
import net.engining.profile.sdk.service.bean.dto.UserListDto;
import net.engining.profile.sdk.service.bean.profile.ProfileUserUpdateForm;
import net.engining.profile.sdk.service.bean.query.UserPagingQuery;
import net.engining.profile.sdk.service.db.ProfileUserRoleService;
import net.engining.profile.sdk.service.db.ProfileUserService;
import net.engining.profile.sdk.service.util.DtoTransformationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.engining.profile.sdk.service.constant.ParameterConstants.ADMIN;

/**
 * 用户管理服务
 */
@Service
public class UserManagementService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ParameterFacility parameterFacility;

    @Autowired
    private ProfileUserService profileUserService;
    @Autowired
    private ProfileUserRoleService profileUserRoleService;
    @Autowired
    private DepartmentManagementService departmentManagementService;

    /**
     * 分页查询用户数据
     */
    public FetchResponse<UserListDto> listUser(UserPagingQuery query) {
        FetchResponse<ProfileUserDto> fetchResponse = profileUserService.listProfileUserDtoByPaging(query);
        Map<String, String> departmentMap = departmentManagementService.mapAllDepartment();

        return DtoTransformationUtils.convertToPagingQueryResult(fetchResponse, source -> {
            List<UserListDto> data = new ArrayList<>(source.size());
            List<String> puIdList = source.stream().map(ProfileUserDto::getPuId).collect(Collectors.toList());
            Map<String, List<String>> roleNameMap = profileUserRoleService.listRoleNameGroupByPuId(puIdList);
            for (ProfileUserDto profileUserDto : source) {
                UserListDto userListDto = new UserListDto();
                userListDto.setUserId(profileUserDto.getUserId());
                userListDto.setUserName(profileUserDto.getName());
                userListDto.setDepartmentId(profileUserDto.getBranchId());
                userListDto.setDepartmentName(departmentMap.get(userListDto.getDepartmentId()));
                userListDto.setUserStatus(profileUserDto.getStatus());
                userListDto.setRoleNameList(roleNameMap.get(profileUserDto.getPuId()));
                data.add(userListDto);
            }
            return data;
        });
    }

    /**
     * 根据用户ID查询用户拥有的角色的简单信息
     *
     * @param userId 用户ID
     * @return 查询结果
     */
    public List<RoleSimpleDto> listRoleSimpleDtoByUserId(String userId) {
        if (ADMIN.equals(userId)) {
            return null;
        }
        ProfileUserDto profileUserDto = profileUserService.getEffectiveProfileUserDtoByUserId(userId);
        if (ValidateUtilExt.isNullOrEmpty(profileUserDto)) {
            throw new ErrorMessageException(ErrorCode.Null, "有效用户不存在");
        }

        return profileUserRoleService.listRoleSimpleDtoByPuId(profileUserDto.getPuId());
    }

    // ———————————————————— 原方法 ————————————————————
//    /**
//     * 根据分支机构编码,用户id,用户姓名（模糊）查找机构下的用户对象
//     *
//     * @param branchId
//     * @param name
//     * @param orgId
//     * @param range
//     * @return
//     */
//    public FetchResponse<UserManagerBean> fetchUsers4Branch(String branchId, String name, String orgId, Range range) {
//        QProfileUser q = QProfileUser.profileUser;
//        QProfileUserRole r = QProfileUserRole.profileUserRole;
//        QProfileRole p = QProfileRole.profileRole;
//        QProfileBranch branch = QProfileBranch.profileBranch;
//        JPAQuery<Tuple> query = new JPAQueryFactory(em)
//                .select(q.puId, q.branchId, q.name, q.email, q.orgId, q.pwdExpDate, q.pwdTries, q.status, q.userId,branch.branchName).from(q,branch)
//                .orderBy(q.branchId.asc(), q.userId.asc());
//        if (!Strings.isNullOrEmpty(branchId)) {
//            query.where(q.branchId.eq(branchId.trim()).and(q.orgId.eq(orgId)));
//        }
//        if (!Strings.isNullOrEmpty(name)) {
//            query.where(q.name.like("%" + name + "%"));
//        }
//        query.where(branch.branchId.eq(q.branchId));
//        FetchResponse<Tuple> build = new JPAFetchResponseBuilder<Tuple>().range(range).build(query);
//        List<UserManagerBean> mapList = Lists.newArrayList();
//        List<String> puIdList = Lists.newArrayList();
//        for (Tuple tuple : build.getData()) {
//            puIdList.add(tuple.get(q.puId));
//        }
//        JPAQuery<Tuple> userQuery = new JPAQueryFactory(em).select(p.roleId, p.roleName, r.puId).from(r, p).where(r.roleId.eq(p.roleId));
//        if (ValidateUtilExt.isNotNullOrEmpty(puIdList)) {
//            userQuery.where(r.puId.in(puIdList));
//        }
//        FetchResponse<Tuple> buildUser = new JPAFetchResponseBuilder<Tuple>().build(userQuery);
//        FetchResponse<UserManagerBean> fetchResponse = new FetchResponse<>();
//        for (Tuple tuple : build.getData()) {
//            UserManagerBean userManagerBean = new UserManagerBean();
//            userManagerBean.setPuId(tuple.get(q.puId));
//            userManagerBean.setBranchId(tuple.get(q.branchId));
//            userManagerBean.setName(tuple.get(q.name));
//            userManagerBean.setEmail(tuple.get(q.email));
//            userManagerBean.setOrgId(tuple.get(q.orgId));
//            userManagerBean.setPwdExpDate(tuple.get(q.pwdExpDate));
//            userManagerBean.setPwdTries(tuple.get(q.pwdTries));
//            userManagerBean.setStatus(tuple.get(q.status));
//            userManagerBean.setUserId(tuple.get(q.userId));
//            userManagerBean.setBranchName(tuple.get(branch.branchName));
//            List<UserRoleBean> roleBeansList = Lists.newLinkedList();
//            for(Tuple user : buildUser.getData()){
//                if(tuple.get(q.puId).equals(user.get(r.puId))){
//                    UserRoleBean userRoleBean = new UserRoleBean();
//                    userRoleBean.setRoleId(user.get(p.roleId));
//                    userRoleBean.setRoleName(user.get(p.roleName));
//                    roleBeansList.add(userRoleBean);
//                }
//            }
//            userManagerBean.setRoleList(roleBeansList);
//            mapList.add(userManagerBean);
//        }
//        fetchResponse.setData(mapList);
//        fetchResponse.setRowCount(build.getRowCount());
//        fetchResponse.setStart(range.getStart());
//        return fetchResponse;
//    }

    /**
     * 根据userId查询用户信息
     *
     * @param userId
     * @return
     */
    public FetchResponse<Map<String, Object>> getUserInfoByUserId(String userId) {
        QProfileUser q = QProfileUser.profileUser;
        JPAQuery<Tuple> query = new JPAQueryFactory(em)
                .select(q.puId, q.branchId, q.name, q.email, q.orgId, q.pwdExpDate, q.pwdTries, q.status, q.userId).from(q);
        if (StringUtils.isNotBlank(userId)) {
            query.where(q.userId.eq(userId)).fetch();
        }
        return new JPAFetchResponseBuilder<Map<String, Object>>().buildAsMap(query,
                q.puId, q.branchId, q.name, q.email, q.orgId, q.pwdExpDate, q.pwdTries, q.status, q.userId);

    }

    /**
     * 清除用户相关Profile信息
     *
     * @param puIds
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteProfileUsers(List<String> puIds) {

        QProfileUser qProfileUser = QProfileUser.profileUser;
        QProfilePwdHist qProfilePwdHist = QProfilePwdHist.profilePwdHist;
        QProfileUserRole qProfileUserRole = QProfileUserRole.profileUserRole;

        long n1 = new JPAQueryFactory(em).delete(qProfilePwdHist).where(qProfilePwdHist.puId.in(puIds)).execute();
        logger.debug("删除了{}条ProfilePwdHist", n1);
        long n2 = new JPAQueryFactory(em).delete(qProfileUserRole).where(qProfileUserRole.puId.in(puIds)).execute();
        logger.debug("删除了{}条ProfileUserRole", n2);
        long n3 = new JPAQueryFactory(em).delete(qProfileUser).where(qProfileUser.puId.in(puIds)).execute();
        logger.debug("删除了{}条ProfileUser", n3);
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
     *
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
     *
     * @param puId
     * @return
     */
    public ProfileUser findProfileUserInfo(String puId) {

        return em.find(ProfileUser.class, puId);
    }

    /**
     * @param user
     */
    @Transactional(rollbackFor = Exception.class)
    public ProfileUser updateProfileUser(ProfileUserUpdateForm user) {

        ProfileUser orginUser = em.find(ProfileUser.class, user.getPuId());
        orginUser.setName(user.getName());
        orginUser.setEmail(user.getEmail());
        orginUser.setMtnUser(user.getOperUserId());
        orginUser.setStatus(user.getStatus());
        orginUser.setBranchId(user.getBranchId());
        orginUser.setOrgId(user.getOrgId());
		orginUser.setUserId(user.getUserId());
        return orginUser;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addProfileUser(ProfileUser user) throws ErrorMessageException {

        Optional<SecurityControl> sc = parameterFacility.getUniqueParameter(SecurityControl.class);

        String value = user.getPassword();
        if (sc.isPresent() && sc.get().complexPwdInd &&
                (!value.matches(".*[a-z].*") || !value.matches(".*[A-Z].*") || !value.matches(".*\\d.*"))) {
            throw new ErrorMessageException(ErrorCode.CheckError, "密码必须由大、小写字母及数字组成");
        }

        user.fillDefaultValues();
        if (em.find(ProfileUser.class, user.getUserId()) != null) {
            throw new ErrorMessageException(ErrorCode.CheckError, "添加用户失败:用户已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        em.persist(user);
    }

}
