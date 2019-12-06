package net.engining.profile.sdk.service.param;


import net.engining.pg.parameter.ParameterFacility;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.param.PasswordPattern;
import net.engining.profile.param.SecurityControl;
import net.engining.profile.param.UsernamePattern;
import net.engining.profile.sdk.service.bean.param.PasswordRuleBean;
import net.engining.profile.sdk.service.bean.param.ResultMessageBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO 需要重构
 *
 * @Description 密码规则管理
 * @Author yangli
 */
@Service
public class PasswordRuleManagerService {

    @Autowired
    private ParameterFacility facility;

    private static final String FLAGT = "true";
    private static final String FLAGF = "false";
    private static final Integer DAY = 99999;

    /**
     * 密码规则提交
     *
     * @param passwordRuleBean
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultMessageBean passwordRuleManager(PasswordRuleBean passwordRuleBean) {
        ResultMessageBean resultMessageBean = new ResultMessageBean();
        if (facility.getParameterMap(SecurityControl.class).size() > 0) {
            facility.removeParameter(SecurityControl.class, ParameterFacility.UNIQUE_PARAM_KEY);
        }
        SecurityControl securityControl = new SecurityControl();
        //是否使用密码复杂度  true /使用 false/不使用
        securityControl.complexPwdInd = Boolean.parseBoolean(passwordRuleBean.complexPwdInd);
        if(ValidateUtilExt.isNotNullOrEmpty(passwordRuleBean.complexPwdInd) && FLAGT.equals(passwordRuleBean.complexPwdInd)){
            securityControl.passwordPatterns = new ArrayList<>();
            PasswordPattern passwordPattern1 = new PasswordPattern();
            passwordPattern1.mustMatch = true;
            passwordPattern1.message = "大小写字母+数字,长度为" + passwordRuleBean.getMinimumLength() +  "~" + passwordRuleBean.getMaximumLength() + "个字符 ";
            passwordPattern1.pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{" + passwordRuleBean.getMinimumLength() +  "," + passwordRuleBean.getMaximumLength() + "}$";
            passwordPattern1.weights = 100;
            securityControl.passwordPatterns.add(passwordPattern1);
        }
        //过期设置
        if(ValidateUtilExt.isNotNullOrEmpty(passwordRuleBean.getExpirationSettings()) && FLAGT.equals(passwordRuleBean.getExpirationSettings())){
            //密码有效期, 传进来的是月份，乘以30，转换为天数
            securityControl.pwdExpireDays = passwordRuleBean.getPwdExpireDays() * 30;
        } else if(ValidateUtilExt.isNotNullOrEmpty(passwordRuleBean.getExpirationSettings()) && FLAGF.equals(passwordRuleBean.getExpirationSettings())){
            securityControl.pwdExpireDays = DAY;
        }
        //首次登陆修改
        securityControl.pwdFirstLoginChgInd = Boolean.parseBoolean(passwordRuleBean.getPwdFirstLoginChgInd());
        //连续输错次数
        securityControl.pwdTries = passwordRuleBean.getPwdTries();
        securityControl.pwdCycleCnt = 3;
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
        //TODO ？？
        resultMessageBean.setKey("1111");
        resultMessageBean.setMessage("提交成功");
        return resultMessageBean;
    }

    /**
     * 密码规则查询
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public PasswordRuleBean passwordRuleSearch() {
        PasswordRuleBean passwordRuleBean = new PasswordRuleBean();
        SecurityControl securityControl = facility.getParameter(SecurityControl.class, ParameterFacility.UNIQUE_PARAM_KEY);
        passwordRuleBean.setComplexPwdInd(securityControl.complexPwdInd + "");
        passwordRuleBean.setPwdFirstLoginChgInd(securityControl.pwdFirstLoginChgInd + "");
        passwordRuleBean.setPwdTries(securityControl.pwdTries);
        if(ValidateUtilExt.isNotNullOrEmpty(securityControl.pwdExpireDays) && DAY.equals(securityControl.pwdExpireDays)){
            passwordRuleBean.setExpirationSettings(FLAGF);
        }else if((ValidateUtilExt.isNotNullOrEmpty(securityControl.pwdExpireDays)) && !(DAY.equals(securityControl.pwdExpireDays))){
            passwordRuleBean.setPwdExpireDays(securityControl.pwdExpireDays / 30);
            passwordRuleBean.setExpirationSettings(FLAGT);
        }
        List<PasswordPattern> patternsList = securityControl.passwordPatterns;
        if(ValidateUtilExt.isNotNullOrEmpty(patternsList)){
            for (PasswordPattern list : patternsList) {
                if(ValidateUtilExt.isNotNullOrEmpty(list.weights)){
                    String pett1 = StringUtils.substringBefore(list.pattern, ",");
                    String patt2 = StringUtils.substringAfter(pett1, "{");
                    passwordRuleBean.setMinimumLength(Integer.parseInt(patt2));

                    String pett3 = StringUtils.substringBefore(list.pattern, "}");
                    String patt4 = StringUtils.substringAfter(pett3, ",");
                    passwordRuleBean.setMaximumLength(Integer.parseInt(patt4));
                }
            }
        }
        return passwordRuleBean;
    }
}
