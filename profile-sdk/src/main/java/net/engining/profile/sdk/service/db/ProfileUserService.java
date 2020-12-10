package net.engining.profile.sdk.service.db;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.engining.pg.parameter.ParameterFacility;
import net.engining.pg.support.core.context.Provider4Organization;
import net.engining.pg.support.db.DbConstants;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.support.db.querydsl.JPAFetchResponseBuilder;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileUserDto;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.entity.model.QProfileUser;
import net.engining.profile.enums.UserStatusEnum;
import net.engining.profile.param.SecurityControl;
import net.engining.profile.sdk.service.bean.query.UserPagingQuery;
import net.engining.profile.sdk.service.util.DtoTransformationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.engining.profile.sdk.service.constant.ParameterConstants.ADMIN;
import static net.engining.profile.sdk.service.constant.ParameterConstants.ADMIN_LIST;
import static net.engining.profile.sdk.service.constant.ParameterConstants.DEFAULT_PASSWORD;

/**
 * ProfileUser表操作服务
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/30 13:08
 * @since 1.0.0
 */
@Service
public class ProfileUserService {

    /**
     * 实体服务
     */
    @PersistenceContext
    private EntityManager entityManager;
    /**
     * 机构服务
     */
    @Autowired
    private Provider4Organization provider4Organization;
    /**
     * 密码加密服务
     */
    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * 参数服务
     */
    @Autowired
    private ParameterFacility parameterFacility;

    /**
     * 根据用户ID查询puId
     *
     * @param userId 用户ID
     * @return puId
     */
    public String getPuIdByUserId(String userId) {
        QProfileUser qProfileUser = QProfileUser.profileUser;
        String puId = new JPAQueryFactory(entityManager)
                .select(qProfileUser.puId)
                .from(qProfileUser)
                .where(qProfileUser.userId.eq(userId))
                .fetchFirst();
        return ValidateUtilExt.isNullOrEmpty(puId) ? null : puId;
    }

    /**
     * 分页查询用户表
     *
     * @param query 分页查询参数
     * @return 查询结果
     */
    public FetchResponse<ProfileUserDto> listProfileUserDtoByPaging(UserPagingQuery query) {
        QProfileUser qProfileUser = QProfileUser.profileUser;

        String userId = query.getUserId();
        if (ADMIN_LIST.contains(userId)) {
            return getEmptyProfileUserDtoFetchResponse(query.getPageNum());
        }
        BooleanExpression b1 = ValidateUtilExt.isNullOrEmpty(userId) ? null : qProfileUser.userId.eq(userId);
        String userName = query.getUserName();
        if (ADMIN_LIST.contains(userName)) {
            return getEmptyProfileUserDtoFetchResponse(query.getPageNum());
        }
        BooleanExpression b2 = ValidateUtilExt.isNullOrEmpty(userName)
                ? null : qProfileUser.name.eq(userName);
        String departmentId = query.getDepartmentId();
        BooleanExpression b3 = ValidateUtilExt.isNullOrEmpty(departmentId)
                ? null : qProfileUser.branchId.eq(departmentId);

        JPAQuery<ProfileUser> jpaQuery = new JPAQueryFactory(entityManager)
                .selectFrom(qProfileUser)
                .where(b1, b2, b3, qProfileUser.delFlg.eq(false))
                .orderBy(qProfileUser.mtnTimestamp.desc(), qProfileUser.createTimestamp.desc());

        FetchResponse<ProfileUser> fetchResponse = new JPAFetchResponseBuilder<ProfileUser>()
                .range(query.getRange())
                .build(jpaQuery);

        return DtoTransformationUtils.convertToPagingQueryResult(fetchResponse,
                source -> source.stream().map(ProfileUser::transform2Dto).collect(Collectors.toList()));
    }

    /**
     * 根据用户ID查询用户数据
     *
     * @param userId 用户ID
     * @return 用户数据
     */
    public ProfileUserDto getProfileUserDtoByUserId(String userId) {
        QProfileUser qProfileUser = QProfileUser.profileUser;
        ProfileUser profileUser = new JPAQueryFactory(entityManager)
                .selectFrom(qProfileUser)
                .where(qProfileUser.userId.eq(userId))
                .fetchFirst();
        return ValidateUtilExt.isNullOrEmpty(profileUser) ? null : profileUser.transform2Dto();
    }

    /**
     * 新增用户表记录
     *
     * @param userId       用户ID
     * @param userName     用户姓名
     * @param departmentId 部门ID
     * @param operatorId   操作员ID
     * @param operateDate  操作时间
     * @return 用户表ID
     */
    @Transactional(rollbackFor = Exception.class)
    public String addProfileUser(String departmentId, String userId, String userName,
                                 String operatorId, Date operateDate) {
        ProfileUser profileUser = new ProfileUser();
        profileUser.setOrgId(provider4Organization.getCurrentOrganizationId());
        profileUser.setBranchId(departmentId);
        profileUser.setUserId(userId);
        profileUser.setName(userName);
        profileUser.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        profileUser.setStatus(UserStatusEnum.A);
        profileUser.setEmail(DbConstants.NULL);
        SecurityControl securityControl = parameterFacility.loadUniqueParameter(SecurityControl.class);
        profileUser.setPwdExpDate(getExpiredDate(Calendar.getInstance().getTime(),
                securityControl.pwdExpireDays));
        profileUser.setPwdTries(0);
        profileUser.setCreateUser(operatorId);
        profileUser.setCreateTimestamp(operateDate);
        profileUser.setMtnUser(profileUser.getCreateUser());
        profileUser.setMtnTimestamp(profileUser.getCreateTimestamp());
        profileUser.setDelFlg(false);
        entityManager.persist(profileUser);
        return profileUser.getPuId();
    }

    /**
     * 更新用户表记录
     *
     * @param userId       用户ID
     * @param departmentId 部门ID
     * @param userName     用户姓名
     * @param operatorId   操作员ID
     * @param operateDate  操作时间
     */
    public void updateProfileUser(String userId, String departmentId, String userName,
                                  String operatorId, Date operateDate) {
        ProfileUser profileUser = getEffectiveProfileUserByUserId(userId);
        profileUser.setName(userName);
        profileUser.setBranchId(departmentId);
        profileUser.setMtnUser(operatorId);
        profileUser.setMtnTimestamp(operateDate);
    }

    /**
     * 根据用户ID查询有效的用户数据
     *
     * @param userId 用户ID
     * @return 用户数据
     */
    public ProfileUserDto getEffectiveProfileUserDtoByUserId(String userId) {
        ProfileUser profileUser = getEffectiveProfileUserByUserId(userId);
        return ValidateUtilExt.isNullOrEmpty(profileUser) ? null : profileUser.transform2Dto();
    }

    /**
     * 逻辑删除用户数据
     *
     * @param userId        用户ID
     * @param operatorId    操作员ID
     * @param operationDate 操作时间
     * @return puId
     */
    public String updateProfileUserDelFlg(String userId, String operatorId, Date operationDate) {
        ProfileUser profileUser = getEffectiveProfileUserByUserId(userId);
        profileUser.setDelFlg(true);
        profileUser.setMtnUser(operatorId);
        profileUser.setMtnTimestamp(operationDate);
        return profileUser.getPuId();
    }

    /**
     * 逻辑删除用户数据
     *
     * @param userId        用户ID
     * @param userStatus    用户状态
     * @param operatorId    操作员ID
     * @param operationDate 操作时间
     */
    public void updateProfileUserStatus(String userId, UserStatusEnum userStatus,
                                        String operatorId, Date operationDate) {
        ProfileUser profileUser = getEffectiveProfileUserByUserId(userId);
        profileUser.setStatus(userStatus);
        if (UserStatusEnum.A.equals(userStatus)) {
            profileUser.setPwdTries(0);
        }
        profileUser.setMtnUser(operatorId);
        profileUser.setMtnTimestamp(operationDate);
    }

    /**
     * 修改用户密码
     *
     * @param userId        用户ID
     * @param password      密码
     * @param operatorId    操作员ID
     * @param operationDate 操作时间
     * @return puId
     */
    public String updateProfileUserPassword(String userId, String password,
                                            String operatorId, Date operationDate) {
        ProfileUser profileUser = getEffectiveProfileUserByUserId(userId);
        profileUser.setPassword(passwordEncoder.encode(password));
        profileUser.setPwdTries(0);
        profileUser.setMtnUser(operatorId);
        profileUser.setMtnTimestamp(operationDate);
        // 重置密码时解锁密码锁定，修改密码时在flow中控制密码锁定状态
        if (UserStatusEnum.P.equals(profileUser.getStatus())) {
            profileUser.setStatus(UserStatusEnum.A);
        }
        return profileUser.getPuId();
    }

    /**
     * 根据用户ID或用户名称查询用户表ID
     *
     * @param userId 用户ID
     * @param userName 用户名称
     * @return 用户表ID
     */
    public String getPuIdByUserIdOrUserName(String userId, String userName) {
        QProfileUser qProfileUser = QProfileUser.profileUser;
        BooleanExpression b1 = ValidateUtilExt.isNullOrEmpty(userId) ? null : qProfileUser.userId.eq(userId);
        BooleanExpression b2 = ValidateUtilExt.isNullOrEmpty(userName) ? null : qProfileUser.name.eq(userName);

        return new JPAQueryFactory(entityManager)
                .select(qProfileUser.puId)
                .from(qProfileUser)
                .where(b1, b2, qProfileUser.delFlg.eq(false))
                .fetchFirst();
    }

    /**
     * 查询puId在范围内的记录的用户ID和用户名称
     *
     * @param puIdList puId集合
     * @return 查询结果
     */
    public Map<String, ProfileUserDto> mapUserIdAndNameByPuIdList(List<String> puIdList) {
        QProfileUser qProfileUser = QProfileUser.profileUser;
        List<Tuple> tupleList = new JPAQueryFactory(entityManager)
                .select(qProfileUser.puId, qProfileUser.userId, qProfileUser.name)
                .from(qProfileUser)
                .where(qProfileUser.puId.in(puIdList))
                .fetch();
        if (ValidateUtilExt.isNullOrEmpty(tupleList)) {
            return null;
        }

        Map<String, ProfileUserDto> result = new HashMap<>(tupleList.size());
        for (Tuple tuple : tupleList) {
            ProfileUserDto profileUserDto = new ProfileUserDto();
            profileUserDto.setUserId(tuple.get(qProfileUser.userId));
            profileUserDto.setName(tuple.get(qProfileUser.name));
            result.put(tuple.get(qProfileUser.puId), profileUserDto);
        }
        return result;
    }

    /**
     * 根据用户表ID查询用户数据
     *
     * @param puId 用户表ID
     * @return 用户数据
     */
    public ProfileUserDto getEffectiveProfileUserDtoByPuId(String puId) {
        QProfileUser qProfileUser = QProfileUser.profileUser;
        ProfileUser profileUser = new JPAQueryFactory(entityManager)
                .selectFrom(qProfileUser)
                .where(qProfileUser.puId.eq(puId), qProfileUser.delFlg.eq(false))
                .fetchFirst();
        return ValidateUtilExt.isNullOrEmpty(profileUser) ? null : profileUser.transform2Dto();
    }

    /**
     * 根据用户ID查询有效用户表数据
     *
     * @param userId 用户ID
     * @return 用户表数据
     */
    private ProfileUser getEffectiveProfileUserByUserId(String userId) {
        QProfileUser qProfileUser = QProfileUser.profileUser;
        return new JPAQueryFactory(entityManager)
                .selectFrom(qProfileUser)
                .where(qProfileUser.userId.eq(userId), qProfileUser.delFlg.eq(false))
                .fetchFirst();
    }

    /**
     * 获取空的用户分页查询结果
     *
     * @param start 起始数
     * @return 分页查询结果
     */
    private FetchResponse<ProfileUserDto> getEmptyProfileUserDtoFetchResponse(Long start) {
        FetchResponse<ProfileUserDto> fetchResponse = new FetchResponse<>();
        fetchResponse.setRowCount(0L);
        fetchResponse.setStart(start);
        return fetchResponse;
    }

    /**
     * 密码过期时间
     *
     * @param date ?
     * @param days ?
     * @return ?
     */
    private Date getExpiredDate(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
}
