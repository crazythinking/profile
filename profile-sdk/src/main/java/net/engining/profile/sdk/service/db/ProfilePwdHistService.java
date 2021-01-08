package net.engining.profile.sdk.service.db;

import net.engining.profile.entity.model.ProfilePwdHist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

/**
 * ProfilePwdHist表操作服务
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 11:07
 * @since 1.0.0
 */
@Service
public class ProfilePwdHistService {

    /**
     * 实体服务
     */
    @PersistenceContext
    private EntityManager entityManager;
    /**
     * 密码加密服务
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 新增密码历史记录
     *
     * @param puId 用户表ID
     * @param password 新密码
     * @param operatorId 操作员ID
     * @param operationDate 操作时间
     */
    @Transactional(rollbackFor = Exception.class)
    public void addProfilePwdHist(String puId, String password, String operatorId, Date operationDate) {
        ProfilePwdHist profilePwdHist = new ProfilePwdHist();
        profilePwdHist.setPuId(puId);
        profilePwdHist.setPassword(passwordEncoder.encode(password));
        profilePwdHist.setPwdCreTime(operationDate);
        profilePwdHist.setCreateUser(operatorId);
        profilePwdHist.setCreateTimestamp(profilePwdHist.getPwdCreTime());
        entityManager.persist(profilePwdHist);
    }

}
