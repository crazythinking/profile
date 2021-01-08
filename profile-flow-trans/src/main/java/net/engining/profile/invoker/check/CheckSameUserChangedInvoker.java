package net.engining.profile.invoker.check;

import com.alibaba.fastjson.JSON;
import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileUserDto;
import net.engining.profile.enums.UpdateFieldEnum;
import net.engining.profile.sdk.key.DepartmentIdKey;
import net.engining.profile.sdk.key.ProfileUserDtoKey;
import net.engining.profile.sdk.key.RemarksKey;
import net.engining.profile.sdk.key.UserIdKey;
import net.engining.profile.sdk.key.UserNameKey;
import net.engining.profile.sdk.service.bean.UpdateRecord;
import net.engining.profile.sdk.service.db.ProfileBranchService;
import net.engining.profile.sdk.service.db.ProfileUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 校验用户信息是否修改
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 12:34
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "校验用户信息是否修改",
        requires = {
                UserIdKey.class,
                UserNameKey.class,
                DepartmentIdKey.class
        },
        optional = {
                ProfileUserDtoKey.class
        }
)
public class CheckSameUserChangedInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileBranch表操作服务
     */
    @Autowired
    private ProfileBranchService profileBranchService;
    /**
     * ProfileUser表操作服务
     */
    @Autowired
    private ProfileUserService profileUserService;

    @Override
    public void invoke(FlowContext flowContext) {
        String newUserName = flowContext.get(UserNameKey.class);
        String newDepartmentId = flowContext.get(DepartmentIdKey.class);
        ProfileUserDto profileUserDto = flowContext.get(ProfileUserDtoKey.class);
        if (ValidateUtilExt.isNullOrEmpty(profileUserDto)) {
            profileUserDto = profileUserService
                    .getEffectiveProfileUserDtoByUserId(flowContext.get(UserIdKey.class));
        }

        String oldUserName = profileUserDto.getName();
        String oldDepartmentId = profileUserDto.getBranchId();

        int size = 2;
        List<UpdateRecord> updateRecordList = new ArrayList<>(size);
        if (!oldUserName.equals(newUserName)) {
            UpdateRecord updateRecord = new UpdateRecord();
            updateRecord.setUpdateField(UpdateFieldEnum.USER_NAME);
            updateRecord.setOldValue(oldUserName);
            updateRecord.setNewValue(newUserName);
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
            throw new ErrorMessageException(ErrorCode.CheckError, "用户信息未发生修改");
        } else {
            flowContext.put(RemarksKey.class, JSON.toJSONString(updateRecordList));
        }
    }

}
