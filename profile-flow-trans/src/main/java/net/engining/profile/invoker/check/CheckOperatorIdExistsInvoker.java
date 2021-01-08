package net.engining.profile.invoker.check;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileUserDto;
import net.engining.profile.sdk.key.OperatorIdKey;
import net.engining.profile.sdk.service.db.ProfileUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;

/**
 * 校验操作员是否存在
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/27 17:00
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "校验操作员是否存在",
        requires = {
                OperatorIdKey.class
        }
)
public class CheckOperatorIdExistsInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileUser表操作服务
     */
    @Autowired
    private ProfileUserService profileUserService;

    @Override
    public void invoke(FlowContext flowContext) {
        ProfileUserDto profileUserDto = profileUserService
                .getEffectiveProfileUserDtoByUserId(flowContext.get(OperatorIdKey.class));
        if (ValidateUtilExt.isNullOrEmpty(profileUserDto)) {
            throw new ErrorMessageException(ErrorCode.Null, "操作员不存在");
        }
    }

}
