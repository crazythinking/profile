package net.engining.profile.sdk.service.bean.profile;

import net.engining.pg.support.meta.PropertyInfo;

/**
 * @author yangxing
 */
public class LoginForm
{
    @PropertyInfo(name = "用户名", hint = "请输入用户名")
    private String username;

    @PropertyInfo(name = "密码", hint = "请输入密码")
    private String password;

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
