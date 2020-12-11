package net.engining.profile.sdk.service.db;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.engining.pg.support.core.context.Provider4Organization;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.support.db.querydsl.JPAFetchResponseBuilder;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileRoleDto;
import net.engining.profile.entity.model.ProfileRole;
import net.engining.profile.entity.model.QProfileRole;
import net.engining.profile.enums.SystemEnum;
import net.engining.profile.sdk.service.bean.query.RolePagingQuery;
import net.engining.profile.sdk.service.util.DtoTransformationUtils;
import net.engining.profile.sdk.service.util.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static net.engining.profile.sdk.service.constant.ParameterConstants.SUPERADMIN;

/**
 * ProfileRole表操作服务
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/29 13:33
 * @since 1.0.0
 */
@Service
public class ProfileRoleService {

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
     * ParameterSeqence表操作服务
     */
    @Autowired
    private ParameterSeqenceService parameterSeqenceService;

    /**
     * 分页查询ProfileRole表
     *
     * @param query 分页查询参数
     * @return 查询结果
     */
    public FetchResponse<ProfileRoleDto> listProfileRoleDtoByPaging(RolePagingQuery query) {
        QProfileRole qProfileRole = QProfileRole.profileRole;

        String roleName = query.getRoleName();
        BooleanExpression b = ValidateUtilExt.isNullOrEmpty(roleName) ?
                null : qProfileRole.roleName.eq(roleName);

        JPAQuery<ProfileRole> jpaQuery = new JPAQueryFactory(entityManager)
                .selectFrom(qProfileRole)
                .where(b, qProfileRole.roleId.ne(SUPERADMIN), qProfileRole.delFlg.eq(false))
                .orderBy(qProfileRole.mtnTimestamp.desc(), qProfileRole.createTimestamp.desc());
        FetchResponse<ProfileRole> fetchResponse = new JPAFetchResponseBuilder<ProfileRole>()
                .range(query.getRange())
                .build(jpaQuery);

        return DtoTransformationUtils.convertToPagingQueryResult(fetchResponse,
                sourceList -> sourceList.stream()
                        .map(ProfileRole::transform2Dto)
                        .collect(Collectors.toList()));
    }

    /**
     * 根据角色名称查询对应的记录
     *
     * @param roleName 角色名称
     * @return 角色数据
     */
    public ProfileRoleDto getProfileRoleDtoByRoleName(String roleName) {
        QProfileRole qProfileRole = QProfileRole.profileRole;
        ProfileRole profileRole = new JPAQueryFactory(entityManager)
                .selectFrom(qProfileRole)
                .where(qProfileRole.roleName.eq(roleName))
                .fetchFirst();
        return returnProfileRoleDto(profileRole);

    }

    /**
     * 新增ProfileRole表记录
     *
     * @param roleName 角色名
     * @param departmentId 部门ID
     * @param systemId 系统ID
     * @param operatorId 操作员
     * @param operateDate 操作时间
     * @return 角色ID
     */
    @Transactional(rollbackFor = Exception.class)
    public String addProfileRole(String roleName, String departmentId, String systemId,
                                 String operatorId, Date operateDate) {
        String roleId = parameterSeqenceService.createNewRoleId();
        String orgId = provider4Organization.getCurrentOrganizationId();

        ProfileRole profileRole = new ProfileRole();
        profileRole.setRoleId(roleId);
        profileRole.setOrgId(orgId);
        profileRole.setClientId(ServiceUtils.getAppIdBySystem(systemId));
        profileRole.setAppCd(ServiceUtils.getSvIdBySystem(systemId));
        profileRole.setBranchId(departmentId);
        profileRole.setRoleName(roleName);
        profileRole.setDelFlg(false);
        profileRole.setCreateUser(operatorId);
        profileRole.setCreateTimestamp(operateDate);
        profileRole.setMtnUser(profileRole.getCreateUser());
        profileRole.setMtnTimestamp(profileRole.getCreateTimestamp());
        entityManager.persist(profileRole);

        return roleId;
    }

    /**
     * 根据角色ID查询有效对应的记录
     *
     * @param roleId 角色ID
     * @return 角色数据
     */
    public ProfileRoleDto getEffectiveProfileRoleDtoByRoleId(String roleId) {
        QProfileRole qProfileRole = QProfileRole.profileRole;
        ProfileRole profileRole = new JPAQueryFactory(entityManager)
                .selectFrom(qProfileRole)
                .where(qProfileRole.roleId.eq(roleId), qProfileRole.delFlg.eq(false))
                .fetchFirst();
        return returnProfileRoleDto(profileRole);
    }

    /**
     * 修改角色表数据
     *
     * @param roleId 角色ID
     * @param departmentId 部门ID
     * @param roleName 角色名称
     * @param operatorId 操作员ID
     * @param operateDate 操作时间
     */
    public void updateProfileRole(String roleId, String departmentId, String roleName,
                                  String operatorId, Date operateDate) {
        ProfileRole profileRole = entityManager.find(ProfileRole.class, roleId);
        profileRole.setBranchId(departmentId);
        profileRole.setRoleName(roleName);
        profileRole.setMtnUser(operatorId);
        profileRole.setMtnTimestamp(operateDate);
    }

    /**
     * 根据角色ID集合查询有效的角色数据
     *
     * @param roleIdList 角色ID集合
     * @return 查询结果
     */
    public List<ProfileRoleDto> listEffectiveProfileRoleDtoByRoleId(List<String> roleIdList) {
        QProfileRole qProfileRole = QProfileRole.profileRole;
        List<ProfileRole> profileRoleList = new JPAQueryFactory(entityManager)
                .selectFrom(qProfileRole)
                .where(qProfileRole.roleId.in(roleIdList), qProfileRole.delFlg.eq(false))
                .fetch();
        return ValidateUtilExt.isNullOrEmpty(profileRoleList)
                ? null : profileRoleList.stream()
                .map(ProfileRole::transform2Dto)
                .collect(Collectors.toList());
    }

    /**
     * 获取全部的角色ID、角色名称
     *
     * @return 查询结果
     */
    public List<ProfileRoleDto> getAllRoleIdAndRoleName() {
        QProfileRole qProfileRole = QProfileRole.profileRole;
        List<Tuple> tupleList = new JPAQueryFactory(entityManager)
                .select(qProfileRole.appCd, qProfileRole.roleId, qProfileRole.roleName)
                .from(qProfileRole)
                .where(qProfileRole.delFlg.eq(false))
                .groupBy(qProfileRole.appCd, qProfileRole.roleId, qProfileRole.roleName)
                .orderBy(qProfileRole.createTimestamp.asc(), qProfileRole.roleId.asc())
                .fetch();
        if (ValidateUtilExt.isNullOrEmpty(tupleList)) {
            return null;
        }

        List<ProfileRoleDto> result = new ArrayList<>(tupleList.size());
        for (Tuple tuple : tupleList) {
            ProfileRoleDto profileRoleDto = new ProfileRoleDto();
            profileRoleDto.setRoleId(tuple.get(qProfileRole.roleId));
            profileRoleDto.setRoleName(tuple.get(qProfileRole.roleName));
            result.add(profileRoleDto);
        }

        return result;
    }

    /**
     * 返回角色表查询结果
     *
     * @param profileRole 角色数据
     * @return 角色数据
     */
    private ProfileRoleDto returnProfileRoleDto(ProfileRole profileRole) {
        return ValidateUtilExt.isNullOrEmpty(profileRole) ? null : profileRole.transform2Dto();
    }

}