package net.engining.profile.invoker.check;

import com.alibaba.fastjson.JSON;
import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileBranchDto;
import net.engining.profile.enums.UpdateFieldEnum;
import net.engining.profile.sdk.key.DepartmentIdKey;
import net.engining.profile.sdk.key.DepartmentNameKey;
import net.engining.profile.sdk.key.ProfileBranchDtoKey;
import net.engining.profile.sdk.key.RemarksKey;
import net.engining.profile.sdk.service.bean.UpdateRecord;
import net.engining.profile.sdk.service.db.ProfileBranchService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static net.engining.profile.sdk.service.constant.ParameterConstants.ONE_SIZE;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/10 14:15
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "校验部门信息是否发生修改",
        requires = {
                DepartmentIdKey.class,
                DepartmentNameKey.class
        },
        optional = {
                ProfileBranchDtoKey.class
        }
)
public class CheckSameDepartmentChangedInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileBranch表操作服务
     */
    @Autowired
    private ProfileBranchService profileBranchService;

    @Override
    public void invoke(FlowContext flowContext) {
        String newDepartmentName = flowContext.get(DepartmentNameKey.class);
        ProfileBranchDto profileBranchDto = flowContext.get(ProfileBranchDtoKey.class);
        if (ValidateUtilExt.isNullOrEmpty(profileBranchDto)) {
            profileBranchDto = profileBranchService
                    .getEffectiveProfileBranchDtoByBranchId(flowContext.get(DepartmentIdKey.class));
        }

        String oldDepartmentName = profileBranchDto.getBranchName();
        UpdateRecord updateRecord = null;
        if (!oldDepartmentName.equals(newDepartmentName)) {
            updateRecord = new UpdateRecord();
            updateRecord.setUpdateField(UpdateFieldEnum.DEPARTMENT_NAME);
            updateRecord.setOldValue(oldDepartmentName);
            updateRecord.setNewValue(newDepartmentName);
        }

        if (ValidateUtilExt.isNullOrEmpty(updateRecord)) {
            throw new ErrorMessageException(ErrorCode.CheckError, "部门信息未发生改变");
        } else {
            List<UpdateRecord> list = new ArrayList<>(ONE_SIZE);
            list.add(updateRecord);
            flowContext.put(RemarksKey.class, JSON.toJSONString(list));
        }
    }

}
