package net.engining.profile.invoker.password;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.parameter.ParameterFacility;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.profile.entity.dto.ProfileUserDto;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.param.SecurityControl;
import net.engining.profile.sdk.key.OriginalPasswordKey;
import net.engining.profile.sdk.key.PasswordKey;
import net.engining.profile.sdk.key.ProfileUserDtoKey;
import net.engining.profile.security.validator.PasswordComplexityValidator;
import net.engining.profile.security.validator.PasswordReuseCountValidator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/14 15:36
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "密码校验",
        requires = {
                ProfileUserDtoKey.class,
                PasswordKey.class,
                OriginalPasswordKey.class
        }
)
public class PasswordValidateInvoker extends AbstractSkippableInvoker {

    /**
     * 密码复杂度校验
     */
    @Autowired
    private PasswordComplexityValidator passwordComplexityValidator;
    /**
     * 密码循环使用次数校验
     */
    @Autowired
    private PasswordReuseCountValidator passwordReuseCountValidator;
    /**
     * 参数服务
     */
    @Autowired
    private ParameterFacility parameterFacility;

    @Override
    public void invoke(FlowContext flowContext) {
        String password = flowContext.get(PasswordKey.class);
        String originalPassword = flowContext.get(OriginalPasswordKey.class);
        ProfileUserDto profileUserDto = flowContext.get(ProfileUserDtoKey.class);

        if (password.equals(originalPassword)) {
            throw new ErrorMessageException(ErrorCode.CheckError, "新密码不能和原密码相同");
        }

        ProfileUser profileUser = new ProfileUser();
        profileUser.transfrom2Entity(profileUserDto);
        // 全局用户安全控制规则
        SecurityControl securityControl = parameterFacility.loadUniqueParameter(SecurityControl.class);

        // 参照之前逻辑，采用默认提供的两个校验
        // 密码复杂度校验
        passwordComplexityValidator.validate(profileUser, password, securityControl);
        // 密码循环使用次数校验
        passwordReuseCountValidator.validate(profileUser, password, securityControl);
    }

}
