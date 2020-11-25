package net.engining.profile.sdk.service.db;

import com.querydsl.jpa.impl.JPAQueryFactory;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileRoleAuthDto;
import net.engining.profile.entity.model.ProfileRoleAuth;
import net.engining.profile.entity.model.QProfileRoleAuth;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ProfileRoleAuth表操作服务
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/9 18:51
 * @since 1.0.0
 */
@Service
public class ProfileRoleAuthService {

    /**
     * 实体服务
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 根据角色ID删除满足条件的记录
     *
     * @param roleId 角色ID
     * @return 删除的记录数
     */
    public Long deleteProfileRoleAuthsByRoleId(String roleId) {
        QProfileRoleAuth qProfileRoleAuth = QProfileRoleAuth.profileRoleAuth;
        return new JPAQueryFactory(entityManager)
                .delete(qProfileRoleAuth)
                .where(qProfileRoleAuth.roleId.eq(roleId))
                .execute();
    }

    /**
     * 新增权限表记录
     *
     * @param dto 新增数据
     */
    public void addProfileRoleAuth(ProfileRoleAuthDto dto) {
        ProfileRoleAuth profileRoleAuth = new ProfileRoleAuth();
        profileRoleAuth.transfrom2Entity(dto);
        entityManager.persist(profileRoleAuth);
    }

    /**
     * 根据角色ID查询对应的权限关系
     *
     * @param roleId 角色ID
     * @return 查询结果
     */
    public List<String> listProfileRoleAuthDtoByRoleId(String roleId) {
        QProfileRoleAuth qProfileRoleAuth = QProfileRoleAuth.profileRoleAuth;
        return new JPAQueryFactory(entityManager)
                .select(qProfileRoleAuth.authority)
                .from(qProfileRoleAuth)
                .where(qProfileRoleAuth.roleId.eq(roleId))
                .fetch();
    }

}
