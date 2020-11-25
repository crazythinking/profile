package net.engining.profile.invoker.check;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileUserDto;
import net.engining.profile.sdk.key.UserIdKey;
import net.engining.profile.sdk.service.db.ProfileUserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/30 16:58
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "校验用户ID是否重复",
        requires = {
                UserIdKey.class
        }
)
public class CheckUserIdRepeatsInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileUser表操作服务
     */
    @Autowired
    private ProfileUserService profileUserService;

    @Override
    public void invoke(FlowContext flowContext) {
        ProfileUserDto profileUserDto = profileUserService
                .getProfileUserDtoByUserId(flowContext.get(UserIdKey.class));
        if (ValidateUtilExt.isNotNullOrEmpty(profileUserDto)) {
            throw new ErrorMessageException(ErrorCode.CheckError, "用户ID已存在");
        }
    }

}
