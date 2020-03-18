package net.engining.profile.security.service;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.engining.pg.parameter.ParameterFacility;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.config.props.ProfileOauthProperties;
import net.engining.profile.entity.enums.StatusDef;
import net.engining.profile.entity.model.*;
import net.engining.profile.param.SecurityControl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 基于当前Profile项目的数据结构对 {@link UserDetailsService}的一个实现
 *
 * @author binarier
 */
public class ProfileUserDetailsServiceImpl implements UserDetailsService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ParameterFacility facility;

    @Autowired
    ProfileOauthProperties profileAuthProperties;

    /**
     * 提供用户认证所需要的数据库用户实体信息
     * @param username 用户登陆Id
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        QProfileUser qProfileUser = QProfileUser.profileUser;
        QProfileRole qProfileRole = QProfileRole.profileRole;
        ProfileUser profileUser = new JPAQueryFactory(em)
                .select(qProfileUser)
                .from(qProfileUser)
                .where(qProfileUser.userId.eq(username))
                .fetchOne();
        if (!ValidateUtilExt.isNotNullOrEmpty(profileUser)) {
            throw new UsernameNotFoundException(String.format("无法找到用户,%s", username));
        }

        QProfileUserRole qUserRole = QProfileUserRole.profileUserRole;
        QProfileRoleAuth qRoleAuth = QProfileRoleAuth.profileRoleAuth;

        List<Tuple> tuples = new JPAQueryFactory(em)
                .select(qRoleAuth.authority,qProfileRole.appCd)
                .from(qUserRole, qRoleAuth,qProfileRole)
                .where(qUserRole.roleId.eq(qRoleAuth.roleId),
                        qUserRole.puId.eq(profileUser.getPuId()),
                        qProfileRole.roleId.eq(qUserRole.roleId))
                .distinct()
                .fetch();

        List<String> authorities = Lists.newArrayList();
        //远程oauth中心
        if (profileAuthProperties.isOauthed()){
            //App_Cd:Authority
            authorities = tuples.stream()
                    .map(tuple -> StringUtils.join(
                            tuple.get(qProfileRole.appCd),
                            ":",
                            tuple.get(qRoleAuth.authority)
                    ))
                    .collect(Collectors.toList());
        }else {
            //本地profile依赖
            // Authority
            authorities = tuples.stream()
                    .map(tuple -> tuple.get(qRoleAuth.authority))
                    .collect(Collectors.toList());
        }

        //存权限标识
        Collection<GrantedAuthority> grantedAuthorities = Collections2.transform(authorities, new Function<String, GrantedAuthority>() {

            @Override
            @Nullable
            public GrantedAuthority apply(@Nullable String input) {
                return new SimpleGrantedAuthority(input);
            }
        });

        //检查安全控制项内，如果要求首次登录必须修改密码，则不给操作权限
        Optional<SecurityControl> control = facility.getUniqueParameter(SecurityControl.class);
        //新用户只能先改密码
        if (control.isPresent() && control.get().getPwdFirstLoginChgInd() && profileUser.getStatus().equals(StatusDef.N)) {
            grantedAuthorities.clear();
        }

        //这里将用户的权限存入了{@link UserDetails}
        StatusDef status = profileUser.getStatus();
        ProfileUserDetails pud = new ProfileUserDetails(
                profileUser.getPuId(),
                profileUser.getUserId(),
                profileUser.getPassword(),
                status != StatusDef.L,
                true,
                true,
                status != StatusDef.L,
                grantedAuthorities,
                profileUser.getBranchId(),
                status);

        //将用户角色也存入{@link UserDetails}
        QProfileRole qRole = QProfileRole.profileRole;
        pud.setRoles(Sets.newHashSet(
                new JPAQueryFactory(em)
                        .select(qRole)
                        .from(qUserRole, qRole)
                        .where(qRole.roleId.eq(qUserRole.roleId), qUserRole.puId.eq(profileUser.getPuId()))
                        .fetch()
                )
        );
        return pud;
    }

}
