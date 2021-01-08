package net.engining.profile.invoker.check;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileUserDto;
import net.engining.profile.sdk.key.ProfileUserDtoKey;
import net.engining.profile.sdk.key.PuIdKey;
import net.engining.profile.sdk.key.UserIdKey;
import net.engining.profile.sdk.service.db.ProfileRoleService;
import net.engining.profile.sdk.service.db.ProfileUserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 12:09
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "校验用户ID对应的用户是否存在",
        requires = {
                UserIdKey.class
        }
)
public class CheckUserIdExistsInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileUser表操作服务
     */
    @Autowired
    private ProfileUserService profileUserService;

    @Override
    public void invoke(FlowContext flowContext) {
        ProfileUserDto profileUserDto = profileUserService
                .getEffectiveProfileUserDtoByUserId(flowContext.get(UserIdKey.class));
        if (ValidateUtilExt.isNullOrEmpty(profileUserDto)) {
            throw new ErrorMessageException(ErrorCode.Null, "有效用户不存在");
        }

        flowContext.put(ProfileUserDtoKey.class, profileUserDto);
        flowContext.put(PuIdKey.class, profileUserDto.getPuId());
    }

}
