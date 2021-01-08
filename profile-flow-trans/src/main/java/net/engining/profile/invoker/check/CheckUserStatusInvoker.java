package net.engining.profile.invoker.check;

import com.alibaba.fastjson.JSON;
import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileUserDto;
import net.engining.profile.enums.OperationType;
import net.engining.profile.enums.UpdateFieldEnum;
import net.engining.profile.enums.UserStatusEnum;
import net.engining.profile.sdk.key.OperationTypeKey;
import net.engining.profile.sdk.key.ProfileUserDtoKey;
import net.engining.profile.sdk.key.RemarksKey;
import net.engining.profile.sdk.key.UserIdKey;
import net.engining.profile.sdk.key.UserStatusKey;
import net.engining.profile.sdk.service.bean.UpdateRecord;
import net.engining.profile.sdk.service.db.ProfileUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static net.engining.profile.sdk.service.constant.ParameterConstants.ONE_SIZE;

/**
 * 用户状态校验
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 13:06
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "用户状态校验",
        requires = {
                UserIdKey.class,
                OperationTypeKey.class
        },
        optional = {
                UserStatusKey.class,
                ProfileUserDtoKey.class
        }
)
public class CheckUserStatusInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileUser表操作服务
     */
    @Autowired
    private ProfileUserService profileUserService;

    @Override
    public void invoke(FlowContext flowContext) {
        ProfileUserDto profileUserDto = flowContext.get(ProfileUserDtoKey.class);
        if (ValidateUtilExt.isNullOrEmpty(profileUserDto)) {
            profileUserDto = profileUserService
                    .getEffectiveProfileUserDtoByUserId(flowContext.get(UserIdKey.class));
        }

        UserStatusEnum status = profileUserDto.getStatus();
        OperationType operationType = flowContext.get(OperationTypeKey.class);
        switch (operationType) {
            case CP:
                // 现阶段没有公共权限，所以修改密码可能要暂时不进行具体的权限控制（拥有授权中心准入权限即可）,所以可能需要在代码中控制
                // TODO 确认锁定后checkToken能否通过，如果能通过需要在代码中另外控制
                if (UserStatusEnum.L.equals(status) || UserStatusEnum.P.equals(status)) {
                    throw new ErrorMessageException(ErrorCode.CheckError,
                            String.format("当前用户状态为%s，无法进行该操作", status.getLabel()));
                }
                break;
            case US:
                UserStatusEnum newStatus = flowContext.get(UserStatusKey.class);
                if (ValidateUtilExt.isNullOrEmpty(newStatus)) {
                    throw new ErrorMessageException(ErrorCode.Null, "请输入变更后的状态");
                }

                if (status.equals(newStatus)) {
                    throw new ErrorMessageException(ErrorCode.CheckError,
                            String.format("用户状态已经是%s，无法继续变更为%s", status.getLabel(), newStatus.getLabel()));
                }

                UpdateRecord updateRecord = new UpdateRecord();
                updateRecord.setUpdateField(UpdateFieldEnum.USER_STATUS);
                updateRecord.setOldValue(status.getLabel());
                updateRecord.setNewValue(newStatus.getLabel());
                List<UpdateRecord> list = new ArrayList<>(ONE_SIZE);
                list.add(updateRecord);
                flowContext.put(RemarksKey.class, JSON.toJSONString(list));
                break;
            default:
                break;
        }
    }

}
