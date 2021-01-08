package net.engining.profile.invoker.check;

import com.alibaba.fastjson.JSON;
import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileMenuDto;
import net.engining.profile.entity.dto.ProfileMenuInterfDto;
import net.engining.profile.entity.dto.ProfileRoleAuthDto;
import net.engining.profile.entity.model.ProfileMenuInterf;
import net.engining.profile.enums.UpdateFieldEnum;
import net.engining.profile.sdk.key.ClientIdKey;
import net.engining.profile.sdk.key.ProfileRoleAuthDtoListKey;
import net.engining.profile.sdk.key.RemarksKey;
import net.engining.profile.sdk.key.RoleIdKey;
import net.engining.profile.sdk.service.bean.UpdateRecord;
import net.engining.profile.sdk.service.db.ProfileMenuInterfService;
import net.engining.profile.sdk.service.db.ProfileMenuService;
import net.engining.profile.sdk.service.db.ProfileRoleAuthService;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.engining.profile.sdk.service.constant.ParameterConstants.DEFAULT_SIZE;
import static net.engining.profile.sdk.service.constant.ParameterConstants.ONE_SIZE;

/**
 * 校验角色拥有的权限是否发生改变
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/10 14:51
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "校验角色拥有的权限是否发生改变",
        requires = {
                RoleIdKey.class,
                ClientIdKey.class
        },
        optional = {
                ProfileRoleAuthDtoListKey.class
        }
)
public class CheckSameRoleAuthChangedInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileRoleAuth表操作服务
     */
    @Autowired
    private ProfileRoleAuthService profileRoleAuthService;
    /**
     * ProfileMenu表操作服务
     */
    @Autowired
    private ProfileMenuService profileMenuService;
    /**
     * ProfileMenuInterf表操作服务
     */
    @Autowired
    private ProfileMenuInterfService profileMenuInterfService;

    @Override
    public void invoke(FlowContext flowContext) {
        String clientId = flowContext.get(ClientIdKey.class);
        List<ProfileRoleAuthDto> profileRoleAuthDtoList = flowContext.get(ProfileRoleAuthDtoListKey.class);
        List<String> oldAuthorityList = profileRoleAuthService
                .listProfileRoleAuthDtoByRoleId(flowContext.get(RoleIdKey.class));
        boolean b1 = ValidateUtilExt.isNullOrEmpty(profileRoleAuthDtoList);
        boolean b2 = ValidateUtilExt.isNullOrEmpty(oldAuthorityList);
        if (b1 && b2) {
            throw new ErrorMessageException(ErrorCode.CheckError, "角色拥有的权限未发生改变");
        }

        List<ProfileMenuDto> profileMenuDtos = profileMenuService.listProfileMenuDtoByAppCd(clientId);
        List<ProfileMenuInterfDto> profileMenuInterfDtoList = profileMenuInterfService.listProfileMenuInterfDtoByAppCd(clientId);
        Map<String, String> authories = new HashMap<>(DEFAULT_SIZE);
        for (ProfileMenuDto profileMenuDto : profileMenuDtos) {
            authories.put(profileMenuDto.getMenuCd(), profileMenuDto.getMname());
        }
        for (ProfileMenuInterfDto profileMenuInterfDto : profileMenuInterfDtoList) {
            authories.put(profileMenuInterfDto.getInterfCd(), profileMenuInterfDto.getIname());
        }

        UpdateRecord updateRecord = new UpdateRecord();
        updateRecord.setUpdateField(UpdateFieldEnum.ROLE_AUTH);
        if (!b1 && !b2) {
            List<String> newAuthorityList = profileRoleAuthDtoList.stream()
                    .map(ProfileRoleAuthDto::getAuthority).collect(Collectors.toList());
            if (newAuthorityList.size() == oldAuthorityList.size()) {
                List<String> temporaryList = new ArrayList<>(newAuthorityList);
                for (String authority : oldAuthorityList) {
                    boolean success = temporaryList.removeIf(target -> target.equals(authority));
                    if (!success) {
                        break;
                    }
                }
                if (ValidateUtilExt.isNullOrEmpty(temporaryList)) {
                    throw new ErrorMessageException(ErrorCode.CheckError, "角色拥有的权限未发生改变");
                }
            }

            updateRecord.setOldValue(JSON.toJSONString(authIdToAuthName(oldAuthorityList, authories)));
            updateRecord.setNewValue(JSON.toJSONString(authIdToAuthName(newAuthorityList, authories)));
        } else if (b2) {
            List<String> newAuthorityList = profileRoleAuthDtoList.stream()
                    .map(ProfileRoleAuthDto::getAuthority).collect(Collectors.toList());
            updateRecord.setNewValue(JSON.toJSONString(authIdToAuthName(newAuthorityList, authories)));
        } else {
            updateRecord.setOldValue(JSON.toJSONString(authIdToAuthName(oldAuthorityList, authories)));
        }

        List<UpdateRecord> list = new ArrayList<>(ONE_SIZE);
        list.add(updateRecord);
        flowContext.put(RemarksKey.class, JSON.toJSONString(list));
    }

    /**
     * 权限ID转换成权限名称
     *
     * @param authIdList 权限ID
     * @param source 权限信息
     * @return 权限名称
     */
    private List<String> authIdToAuthName(List<String> authIdList, Map<String, String> source) {
        return authIdList.stream().map(source::get).collect(Collectors.toList());
    }

}
