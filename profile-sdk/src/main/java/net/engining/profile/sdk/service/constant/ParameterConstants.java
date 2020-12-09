package net.engining.profile.sdk.service.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 参数常量
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/29 20:08
 * @since 1.0.0
 */
public class ParameterConstants {
    /**
     * 空集合数量
     */
    public static final int EMPTY_SIZE = 0;
    /**
     * 单元素集合数量
     */
    public static final int ONE_SIZE = 1;
    /**
     * 默认集合数量
     */
    public static final int DEFAULT_SIZE = 16;
    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "Abcd123456";
    /**
     * 超级管理员角色
     */
    public static final String SUPERADMIN = "SUPERADMIN";
    /**
     * 超级管理员用户
     */
    public static final String ADMIN = "admin";
    /**
     * 超级管理员团队
     */
    public static final List<String> ADMIN_LIST = new ArrayList<>(Arrays.asList("admin", "admin1", "admin2", "admin3"));
    /**
     * 横线
     */
    public static final String LINE = "-";
}
