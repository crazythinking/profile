package net.engining.profile.sdk.service.db;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.engining.pg.support.core.context.Provider4Organization;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.support.db.querydsl.JPAFetchResponseBuilder;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileBranchDto;
import net.engining.profile.entity.model.ProfileBranch;
import net.engining.profile.entity.model.QProfileBranch;
import net.engining.profile.sdk.service.bean.query.DepartmentPagingQuery;
import net.engining.profile.sdk.service.util.DtoTransformationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ProfileBranch表操作服务
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/30 9:43
 * @since 1.0.0
 */
@Service
public class ProfileBranchService {

    /**
     * 实体服务
     */
    @PersistenceContext
    private EntityManager entityManager;
    /**
     * ParameterSeqence表操作服务
     */
    @Autowired
    private ParameterSeqenceService parameterSeqenceService;
    /**
     * 机构服务
     */
    @Autowired
    private Provider4Organization provider4Organization;

    /**
     * 根据部门ID查询对应的记录数
     *
     * @param branchId 部门ID
     * @return 记录数
     */
    public ProfileBranchDto getEffectiveProfileBranchDtoByBranchId(String branchId) {
        ProfileBranch profileBranch = getEffectiveProfileBranchByBranchId(branchId);
        return ValidateUtilExt.isNullOrEmpty(profileBranch) ? null : profileBranch.transform2Dto();
    }

    /**
     * 新增部门记录
     *
     * @param branchName 部门名称
     * @param operatorId 操作员ID
     * @param operationDate 操作时间
     * @return 部门ID
     */
    public String addProfileBranch(String branchName, String operatorId, Date operationDate) {
        String departmentId = parameterSeqenceService.createNewDepartmentId();

        ProfileBranch profileBranch = new ProfileBranch();
        profileBranch.setOrgId(provider4Organization.getCurrentOrganizationId());
        profileBranch.setBranchId(departmentId);
        profileBranch.setBranchName(branchName);
        profileBranch.setDelFlg(false);
        profileBranch.setCreateUser(operatorId);
        profileBranch.setCreateTimestamp(operationDate);
        profileBranch.setMtnUser(profileBranch.getCreateUser());
        profileBranch.setMtnTimestamp(profileBranch.getCreateTimestamp());
        entityManager.persist(profileBranch);
        return departmentId;
    }

    /**
     * 修改部门表数据
     *
     * @param branchId 部门ID
     * @param branchName 部门名称
     * @param operatorId 操作员ID
     * @param operationDate 操作时间
     */
    public void updateProfileBranch(String branchId, String branchName,
                                    String operatorId, Date operationDate) {
        ProfileBranch profileBranch = getEffectiveProfileBranchByBranchId(branchId);
        profileBranch.setBranchName(branchName);
        profileBranch.setMtnUser(operatorId);
        profileBranch.setMtnTimestamp(operationDate);
    }

    /**
     * 逻辑删除部门表数据
     *
     * @param branchId 部门ID
     * @param operatorId 操作员ID
     * @param operationDate 操作时间
     */
    public void updateProfileBranchDelFlg(String branchId, String operatorId, Date operationDate) {
        ProfileBranch profileBranch = getEffectiveProfileBranchByBranchId(branchId);
        profileBranch.setDelFlg(true);
        profileBranch.setMtnUser(operatorId);
        profileBranch.setMtnTimestamp(operationDate);
    }

    /**
     * 分页查询部门表
     *
     * @param query 分页查询参数
     * @return 查询结果
     */
    public FetchResponse<ProfileBranchDto> listProfileBranchDtoByPaging(DepartmentPagingQuery query) {
        QProfileBranch qProfileBranch = QProfileBranch.profileBranch;
        JPAQuery<ProfileBranch> jpaQuery = new JPAQueryFactory(entityManager)
                .selectFrom(qProfileBranch)
                .where(qProfileBranch.delFlg.eq(false))
                .orderBy(qProfileBranch.mtnTimestamp.desc(), qProfileBranch.branchId.asc());
        FetchResponse<ProfileBranch> fetchResponse = new JPAFetchResponseBuilder<ProfileBranch>()
                .range(query.getRange())
                .build(jpaQuery);

        return DtoTransformationUtils.convertToPagingQueryResult(fetchResponse,
                source -> source.stream().map(ProfileBranch::transform2Dto).collect(Collectors.toList()));
    }

    /**
     * 获取全部的部门ID和部门名称
     *
     * @return 查询结果
     */
    public List<ProfileBranchDto> getAllBranchIdAndBranchName() {
        QProfileBranch qProfileBranch = QProfileBranch.profileBranch;
        List<Tuple> tupleList = new JPAQueryFactory(entityManager)
                .select(qProfileBranch.branchId, qProfileBranch.branchName)
                .from(qProfileBranch)
                .where(qProfileBranch.delFlg.eq(false))
                .orderBy(qProfileBranch.createTimestamp.asc(), qProfileBranch.branchId.asc())
                .fetch();
        if (ValidateUtilExt.isNullOrEmpty(tupleList)) {
            return null;
        }

        List<ProfileBranchDto> result = new ArrayList<>(tupleList.size());
        for (Tuple tuple : tupleList) {
            ProfileBranchDto profileBranchDto = new ProfileBranchDto();
            profileBranchDto.setBranchId(tuple.get(qProfileBranch.branchId));
            profileBranchDto.setBranchName(tuple.get(qProfileBranch.branchName));
            result.add(profileBranchDto);
        }
        return result;
    }

    /**
     * 根据部门ID查询有效的部门名称
     *
     * @param branchId 机构ID
     * @return 查询结果
     */
    public String getEffectiveBranchNameByBranchId(String branchId) {
        QProfileBranch qProfileBranch = QProfileBranch.profileBranch;
        return new JPAQueryFactory(entityManager)
                .select(qProfileBranch.branchName)
                .from(qProfileBranch)
                .where(qProfileBranch.branchId.eq(branchId), qProfileBranch.delFlg.eq(false))
                .fetchFirst();
    }

    /**
     * 根据部门ID查询有效的部门信息
     *
     * @param branchId 部门ID
     * @return 部门表数据
     */
    private  ProfileBranch getEffectiveProfileBranchByBranchId(String branchId) {
        QProfileBranch qProfileBranch = QProfileBranch.profileBranch;;
        return new JPAQueryFactory(entityManager)
                .selectFrom(qProfileBranch)
                .where(qProfileBranch.branchId.eq(branchId), qProfileBranch.delFlg.eq(false))
                .fetchFirst();
    }

}
