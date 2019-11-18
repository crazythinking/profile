package net.engining.profile.sdk.service;

import net.engining.pg.parameter.ParameterFacility;
import net.engining.profile.entity.enums.StatusDef;
import net.engining.profile.entity.model.ProfileUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Eric Lu
 */
@Service
public class ProfilePasswordService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ParameterFacility parameterFacility;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public final String resetPwdStr = "";


    /**
     * 重置登陆密码
     *
     * @param puId
     * @param operUser
     */
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(String puId, String operUser) {
        ProfileUser user = em.find(ProfileUser.class, puId);
        checkNotNull(user);

        user.setMtnTimestamp(new Date());
        user.setMtnUser(operUser);
        user.setPassword(passwordEncoder.encode(resetPwdStr));
        user.setPwdTries(0);
        user.setStatus(StatusDef.L);

        logger.info("操作员：{}，重置了用户：{}的密码！", operUser, user.getUserId());
    }

}
