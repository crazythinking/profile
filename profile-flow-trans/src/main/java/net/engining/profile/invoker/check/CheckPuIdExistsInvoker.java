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
import net.engining.profile.sdk.service.db.ProfileUserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 校验有效用户表ID是否存在
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/14 14:30
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "校验有效用户表ID是否存在",
        requires = {
                PuIdKey.class
        }
)
public class CheckPuIdExistsInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileUser表操作服务
     */
    @Autowired
    private ProfileUserService profileUserService;

    @Override
    public void invoke(FlowContext flowContext) {
        ProfileUserDto profileUserDto = profileUserService.getEffectiveProfileUserDtoByPuId(flowContext.get(PuIdKey.class));
        if (ValidateUtilExt.isNullOrEmpty(profileUserDto)) {
            throw new ErrorMessageException(ErrorCode.Null, "用户信息不存在");
        }

        flowContext.put(ProfileUserDtoKey.class, profileUserDto);
        flowContext.put(UserIdKey.class, profileUserDto.getUserId());
    }

}