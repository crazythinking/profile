package net.engining.profile.sdk.service.db;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sun.org.apache.regexp.internal.RE;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileUserRoleDto;
import net.engining.profile.entity.model.ProfileUserRole;
import net.engining.profile.entity.model.QProfileRole;
import net.engining.profile.entity.model.QProfileUserRole;
import net.engining.profile.sdk.service.bean.dto.RoleSimpleDto;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ProfileUserRole表操作服务
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/30 15:38
 * @since 1.0.0
 */
@Service
public class ProfileUserRoleService {

    /**
     * 实体服务
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 根据puId查询用户拥有的角色并按puId分组
     *
     * @param puIdList puId集合
     * @return 查询结果
     */
    public Map<String, List<String>> listRoleNameGroupByPuId(List<String> puIdList) {
        QProfileUserRole qProfileUserRole = QProfileUserRole.profileUserRole;
        QProfileRole qProfileRole = QProfileRole.profileRole;
        List<Tuple> tupleList = new JPAQueryFactory(entityManager)
                .select(qProfileUserRole.puId, qProfileRole.roleName)
                .from(qProfileUserRole, qProfileRole)
                .where(qProfileUserRole.puId.in(puIdList), qProfileRole.roleId.eq(qProfileUserRole.roleId))
                .orderBy(qProfileUserRole.id.asc())
                .fetch();

        if (ValidateUtilExt.isNullOrEmpty(tupleList)) {
            return null;
        }

        int size = puIdList.size();
        Map<String, List<String>> result = new HashMap<>(size);
        for (Tuple tuple : tupleList) {
            String puId = tuple.get(qProfileUserRole.puId);
            List<String> list = result.get(puId);
            if (ValidateUtilExt.isNullOrEmpty(list)) {
                list = new ArrayList<>(size);
            }
            list.add(tuple.get(qProfileRole.roleName));
            result.put(puId, list);
        }

        return result;
    }

    /**
     * 根据puId删除用户角色对应关系表数据
     *
     * @param puId puId
     * @return 删除条数
     */
    public Long deleteProfileUserRolesByPuId(String puId) {
        QProfileUserRole qProfileUserRole = QProfileUserRole.profileUserRole;
        return new JPAQueryFactory(entityManager)
                .delete(qProfileUserRole)
                .where(qProfileUserRole.puId.eq(puId))
                .execute();
    }

    /**
     * 根据puId查询用户拥有的角色的简单信息
     *
     * @param puId puId
     * @return 角色的简单信息
     */
    public List<RoleSimpleDto> listRoleSimpleDtoByPuId(String puId) {
        QProfileUserRole qProfileUserRole = QProfileUserRole.profileUserRole;
        QProfileRole qProfileRole = QProfileRole.profileRole;
        List<Tuple> tupleList = new JPAQueryFactory(entityManager)
                .select(qProfileUserRole.roleId, qProfileRole.roleName)
                .from(qProfileUserRole, qProfileRole)
                .where(qProfileUserRole.roleId.eq(qProfileRole.roleId), qProfileUserRole.puId.eq(puId))
                .orderBy(qProfileUserRole.createTimestamp.desc(), qProfileUserRole.id.desc())
                .fetch();
        if (ValidateUtilExt.isNullOrEmpty(tupleList)) {
            return null;
        }

        List<RoleSimpleDto> result = new ArrayList<>(tupleList.size());
        for (Tuple tuple : tupleList) {
            RoleSimpleDto dto = new RoleSimpleDto();
            dto.setRoleId(tuple.get(qProfileUserRole.roleId));
            dto.setRoleName(tuple.get(qProfileRole.roleName));
            result.add(dto);
        }
        return result;
     }

     /**
      * 新增用户角色对应关系记录
      *
      * @param dto 新增数据
      */
     public void addProfileUserRole(ProfileUserRoleDto dto) {
         ProfileUserRole profileUserRole = new ProfileUserRole();
         profileUserRole.transfrom2Entity(dto);
         entityManager.persist(profileUserRole);
     }

     /**
      * 根据用户表ID查询用户拥有的角色
      *
      * @param puId 用户表ID
      * @return 查询结果
      */
     public List<String> listRoleIdListByPuId(String puId) {
         QProfileUserRole qProfileUserRole = QProfileUserRole.profileUserRole;
         return new JPAQueryFactory(entityManager)
                 .select(qProfileUserRole.roleId)
                 .from(qProfileUserRole)
                 .where(qProfileUserRole.puId.eq(puId))
                 .fetch();
     }
}
