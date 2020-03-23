package net.engining.profile.init.param.security;


import net.engining.pg.parameter.ParameterFacility;
import net.engining.pg.support.init.ParameterInitializer;
import net.engining.profile.param.PasswordPattern;
import net.engining.profile.param.SecurityControl;
import net.engining.profile.param.UsernamePattern;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;


/**
 * 安全控制初始化
 *
 * @author 陈宝
 * @version 1.0
 * @date 2020/3/20 17:43
 * @since 1.0
 */
public class SecurityControlInit implements ParameterInitializer {

    @Autowired
    private ParameterFacility facility;

    @Override
    public void init() throws Exception {
        if (facility.getParameterMap(SecurityControl.class).size() > 0) {
            facility.removeParameter(SecurityControl.class, ParameterFacility.UNIQUE_PARAM_KEY);
        }

        SecurityControl securityControl = new SecurityControl();
        securityControl.complexPwdInd = true;
        securityControl.passwordPatterns = new ArrayList<>();
        PasswordPattern passwordPattern2 = new PasswordPattern();
        passwordPattern2.mustMatch = true;
        passwordPattern2.message = "密码必须包含大写字母";
        passwordPattern2.pattern = ".*[A-Z].*";
        passwordPattern2.weights = 100;
        PasswordPattern passwordPattern3 = new PasswordPattern();
        passwordPattern3.mustMatch = true;
        passwordPattern3.message = "密码必须包含数字";
        passwordPattern3.pattern = ".*[0-9].*";
        passwordPattern3.weights = 100;
        PasswordPattern passwordPattern4 = new PasswordPattern();
        passwordPattern4.mustMatch = true;
        passwordPattern4.message = "长度6-12位";
        passwordPattern4.pattern = ".{6,12}";
        passwordPattern4.weights = 101;
        securityControl.passwordPatterns.add(passwordPattern2);
        securityControl.passwordPatterns.add(passwordPattern3);
        securityControl.passwordPatterns.add(passwordPattern4);
        securityControl.pwdCycleCnt = 3;
        securityControl.pwdExpireDays = 30;
        securityControl.pwdFirstLoginChgInd = false;
        securityControl.pwdTries = 5;
        securityControl.usernamePattern = new ArrayList<>();
        UsernamePattern usernamePattern1 = new UsernamePattern();
        usernamePattern1.pattern = "[a-z,A-Z,0-9,_]*";
        usernamePattern1.message = "用户ID只能包含字母、数字或下划线";
        UsernamePattern usernamePattern2 = new UsernamePattern();
        usernamePattern2.pattern = ".{6,12}";
        usernamePattern2.message = "用户ID长度必须为6-12位";
        securityControl.usernamePattern.add(usernamePattern1);
        securityControl.usernamePattern.add(usernamePattern2);
        facility.addParameter(ParameterFacility.UNIQUE_PARAM_KEY, securityControl);
    }

}
