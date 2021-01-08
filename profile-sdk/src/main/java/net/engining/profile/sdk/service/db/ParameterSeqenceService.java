package net.engining.profile.sdk.service.db;

import net.engining.pg.parameter.entity.model.ParameterSeqence;
import net.engining.profile.entity.model.ProfileBranch;
import net.engining.profile.entity.model.ProfileRole;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * ParameterSeqence表操作服务
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/30 10:40
 * @since 1.0.0
 */
@Service
public class ParameterSeqenceService {

    /**
     * 实体服务
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 创建新的角色ID
     *
     * @return 角色ID
     */
    public String createNewRoleId() {
        ParameterSeqence seqence = entityManager.find(ParameterSeqence.class,
                ProfileRole.class.getCanonicalName());
        Integer paramSeq = seqence.getParamSeq();
        String original = "000000000";
        String roleId = splice(original, paramSeq.toString());
        seqence.setParamSeq(paramSeq + 1);
        return roleId;
    }

    /**
     * 创建新的部门ID
     *
     * @return 部门ID
     */
    public String createNewDepartmentId() {
        ParameterSeqence seqence = entityManager.find(ParameterSeqence.class,
                ProfileBranch.class.getCanonicalName());
        Integer paramSeq = seqence.getParamSeq();
        String original = "100000";
        String departmentId = splice(original, paramSeq.toString());
        seqence.setParamSeq(paramSeq + 1);
        return departmentId;
    }

    /**
     * 拼接参数序列
     *  @param original 初始序列
     * @param paramSeq 当前序列
     */
    private String splice(String original, String paramSeq) {
        int originalLength = original.length();
        int paramSeqLength = paramSeq.length();
        return originalLength < paramSeqLength
                ? paramSeq : original.substring(0, originalLength - paramSeqLength) + paramSeq;
    }
}
