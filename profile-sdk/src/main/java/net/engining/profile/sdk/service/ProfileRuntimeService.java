package net.engining.profile.sdk.service;

import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import net.engining.pg.parameter.ParameterFacility;
import net.engining.profile.entity.enums.StatusDef;
import net.engining.profile.entity.model.ProfileRole;
import net.engining.profile.param.SecurityControl;
import net.engining.profile.security.ProfileUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
        ProfileUserDetails ud = (ProfileUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ClientWebUser cu = new ClientWebUser();
        cu.setAuthorities(Sets.newHashSet(
                //转换成Set<String>
                Collections2.transform(
                        ud.getAuthorities(),
                        input -> input.getAuthority()
                )
        ));

        cu.setPuId(ud.getPuId());
        Map<String, String> roles = ud.getRoles()
                .stream()
                .collect(Collectors.toMap(ProfileRole::getRoleId, ProfileRole::getRoleName));
        cu.setRoles(roles);
        cu.setLoginId(ud.getUsername());
        cu.setName(ud.getUsername());
        cu.setProps(new HashMap<>(16));

        cu.setStatus(ud.getStatus());
        Optional<SecurityControl> control = facility.getUniqueParameter(SecurityControl.class);
        cu.setNeedPasswordChange(
                //存在控制参数
                control.isPresent() &&
                //首次登录改密码
                control.get().pwdFirstLoginChgInd &&
                //新用户
                ud.getStatus() == StatusDef.N
        );

        return cu;
    }
}
