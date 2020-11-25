package net.engining.profile.sdk.service;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.engining.pg.parameter.ParameterFacility;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.profile.entity.model.ProfileRole;
import net.engining.profile.enums.UserStatusEnum;
import net.engining.profile.param.SecurityControl;
import net.engining.profile.security.service.ProfileUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Eric Lu
 */
@Service
public class ProfileRuntimeService {

    @Autowired
    private ParameterFacility facility;

    public ClientWebUser loadCurrentUser() {
        Object od = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ( !(od instanceof ProfileUserDetails) ){
            throw new ErrorMessageException(ErrorCode.SystemError, "用户不存在");
        }
        ProfileUserDetails ud = (ProfileUserDetails) od;
        ClientWebUser cu = new ClientWebUser();
        cu.setAuthorities(Sets.newHashSet(
                //转换成Set<String>
                Collections2.transform(
                        ud.getAuthorities(),
                        new Function<GrantedAuthority, String>() {
                            @Nullable
                            @Override
                            public String apply(@Nullable GrantedAuthority input) {
                                return input.getAuthority();
                            }
                        }
                )
        ));

        cu.setPuId(ud.getPuId());
        Map<String, String> roles = ud.getRoles()
                .stream()
                .collect(Collectors.toMap(ProfileRole::getRoleId, ProfileRole::getRoleName));
        cu.setRoles(roles);
        cu.setLoginId(ud.getUsername());
        cu.setName(ud.getUsername());
        cu.setProps(Maps.newHashMapWithExpectedSize(16));

        cu.setStatus(ud.getStatus());
        Optional<SecurityControl> control = facility.getUniqueParameter(SecurityControl.class);
        cu.setNeedPasswordChange(
                //存在控制参数
                control.isPresent() &&
                //首次登录改密码
                control.get().pwdFirstLoginChgInd &&
                //新用户
                ud.getStatus() == UserStatusEnum.N
        );

        return cu;
    }
}
