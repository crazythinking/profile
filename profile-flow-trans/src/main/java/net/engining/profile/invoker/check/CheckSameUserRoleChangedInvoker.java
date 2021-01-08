package net.engining.profile.invoker.check;

import com.alibaba.fastjson.JSON;
import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileRoleDto;
import net.engining.profile.enums.UpdateFieldEnum;
import net.engining.profile.sdk.key.PuIdKey;
import net.engining.profile.sdk.key.RemarksKey;
import net.engining.profile.sdk.key.RoleIdListKey;
import net.engining.profile.sdk.service.bean.UpdateRecord;
import net.engining.profile.sdk.service.db.ProfileRoleService;
import net.engining.profile.sdk.service.db.ProfileUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.engining.profile.sdk.service.constant.ParameterConstants.ONE_SIZE;

/**
 * 校验用户拥有角色是否发生改变
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/10 15:25
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "校验用户拥有角色是否发生改变",
        requires = {
                PuIdKey.class
        },
        optional = {
                RoleIdListKey.class
        }
)
public class CheckSameUserRoleChangedInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileUserRole表操作服务
     */
    @Autowired
    private ProfileUserRoleService profileUserRoleService;
    /**
     * ProfileRole表操作服务
     */
    @Autowired
    private ProfileRoleService profileRoleService;

    @Override
    public void invoke(FlowContext flowContext) {
        List<String> oldRoleIdList = profileUserRoleService
                .listRoleIdListByPuId(flowContext.get(PuIdKey.class));
        List<String> newRoleIdList  = flowContext.get(RoleIdListKey.class);

        boolean b1 = ValidateUtilExt.isNullOrEmpty(oldRoleIdList);
        boolean b2 = ValidateUtilExt.isNullOrEmpty(newRoleIdList);

        if (b1 && b2) {
            throw new ErrorMessageException(ErrorCode.CheckError, "用户拥有的角色未发生改变");
        }

        List<ProfileRoleDto> profileRoleDtoList = profileRoleService.getAllRoleIdAndRoleName();
        Map<String, String> roleMap = profileRoleDtoList.stream()
                .collect(Collectors.toMap(ProfileRoleDto::getRoleId, ProfileRoleDto::getRoleName));

        UpdateRecord updateRecord = new UpdateRecord();
        updateRecord.setUpdateField(UpdateFieldEnum.USER_ROLE);
        if (!b1 && !b2) {
            if (oldRoleIdList.size() == newRoleIdList.size()) {
                List<String> temporaryRoleIdList = new ArrayList<>(newRoleIdList);
                for (String oldRoleId : oldRoleIdList) {
                    boolean success = temporaryRoleIdList.removeIf(newRoleId -> newRoleId.equals(oldRoleId));
                    if (!success) {
                        break;
                    }
                }
                if (ValidateUtilExt.isNullOrEmpty(temporaryRoleIdList)) {
                    throw new ErrorMessageException(ErrorCode.CheckError, "用户拥有的角色未发生改变");
                }
            }
            updateRecord.setOldValue(JSON.toJSONString(roleIdToRoleName(oldRoleIdList, roleMap)));
            updateRecord.setNewValue(JSON.toJSONString(roleIdToRoleName(newRoleIdList, roleMap)));
        } else if (b1){
            updateRecord.setNewValue(JSON.toJSONString(roleIdToRoleName(newRoleIdList, roleMap)));
        } else {
            updateRecord.setOldValue(JSON.toJSONString(roleIdToRoleName(oldRoleIdList, roleMap)));
        }

        List<UpdateRecord> list = new ArrayList<>(ONE_SIZE);
        list.add(updateRecord);
        flowContext.put(RemarksKey.class, JSON.toJSONString(list));
    }

    /**
     *  通过角色ID获取角色名称
     *
     * @param roleIdList 角色ID
     * @param source 角色信息
     * @return 角色名称
     */
    private List<String> roleIdToRoleName(List<String> roleIdList, Map<String, String> source) {
        return roleIdList.stream().map(source::get).collect(Collectors.toList());
    }

}
