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
import net.engining.profile.sdk.key.DepartmentIdKey;
import net.engining.profile.sdk.key.ProfileRoleDtoKey;
import net.engining.profile.sdk.key.RemarksKey;
import net.engining.profile.sdk.key.RoleIdKey;
import net.engining.profile.sdk.key.RoleNameKey;
import net.engining.profile.sdk.service.bean.UpdateRecord;
import net.engining.profile.sdk.service.db.ProfileBranchService;
import net.engining.profile.sdk.service.db.ProfileRoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 校验角色信息是否发生修改
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 12:40
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "校验角色信息是否发生修改",
        requires = {
                RoleIdKey.class,
                RoleNameKey.class,
                DepartmentIdKey.class
        },
        optional = {
                ProfileRoleDtoKey.class
        }
)
public class CheckSameRoleChangedInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileRole表操作服务
     */
    @Autowired
    private ProfileRoleService profileRoleService;
    /**
     * ProfileBranch表操作服务
     */
    @Autowired
    private ProfileBranchService profileBranchService;

    @Override
    public void invoke(FlowContext flowContext) {
        String newRoleName = flowContext.get(RoleNameKey.class);
        String newDepartmentId = flowContext.get(DepartmentIdKey.class);
        ProfileRoleDto profileRoleDto = flowContext.get(ProfileRoleDtoKey.class);
        if (ValidateUtilExt.isNullOrEmpty(profileRoleDto)) {
            profileRoleDto = profileRoleService
                    .getEffectiveProfileRoleDtoByRoleId(flowContext.get(RoleIdKey.class));
        }

        String oldRoleName = profileRoleDto.getRoleName();
        String oldDepartmentId = profileRoleDto.getBranchId();

        int size = 2;
        List<UpdateRecord> updateRecordList = new ArrayList<>(size);
        if (!oldRoleName.equals(newRoleName)) {
            UpdateRecord updateRecord = new UpdateRecord();
            updateRecord.setUpdateField(UpdateFieldEnum.ROLE_NAME);
            updateRecord.setOldValue(oldRoleName);
            updateRecord.setNewValue(newRoleName);
            updateRecordList.add(updateRecord);
        }
        if (!oldDepartmentId.equals(newDepartmentId)) {
            UpdateRecord updateRecord = new UpdateRecord();
            updateRecord.setUpdateField(UpdateFieldEnum.DEPARTMENT_NAME);
            String oldDepartmentName = profileBranchService.getEffectiveBranchNameByBranchId(oldDepartmentId);
            updateRecord.setOldValue(oldDepartmentName);
            String newDepartmentName = profileBranchService.getEffectiveBranchNameByBranchId(newDepartmentId);
            updateRecord.setNewValue(newDepartmentName);
            updateRecordList.add(updateRecord);
        }

        if (ValidateUtilExt.isNullOrEmpty(updateRecordList)) {
            throw new ErrorMessageException(ErrorCode.CheckError, "角色信息未发生修改");
        } else {
            flowContext.put(RemarksKey.class, JSON.toJSONString(updateRecordList));
        }
    }

}
