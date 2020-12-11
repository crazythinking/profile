package net.engining.profile.sdk.service.util;

import net.engining.profile.enums.SystemEnum;

/**
 * 业务层工具方法
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/29 19:29
 * @since 1.0.0
 */
public class ServiceUtils {

    /**
     * 根据客户端ID获取所属系统
     */
    public static SystemEnum getSystemByClientId(String clientId) {
        clientId = clientId.toUpperCase();
        SystemEnum[] systems = SystemEnum.values();
        for (SystemEnum system : systems) {
            if (clientId.contains(system.getValue())) {
                return system;
            }
        }
        return null;
    }

    /**
     * 根据所属系统获取appId
     *
     * @param system 所属系统
     * @return appId
     */
    public static String getAppIdBySystem(SystemEnum system) {
        return system.getValue().toLowerCase() + "-mgm-app";
    }

    /**
     * 根据所属系统获取appId
     *
     * @param system 所属系统
     * @return appId
     */
    public static String getAppIdBySystem(String system) {
        return system + "-mgm-app";
    }

    /**
     * 根据所属系统获取svId
     *
     * @param system 所属系统
     * @return appId
     */
    public static String getSvIdBySystem(SystemEnum system) {
        return system.getValue().toLowerCase() + "-mgm-sv";
    }

    /**
     * 根据所属系统获取svId
     *
     * @param system 所属系统
     * @return appId
     */
    public static String getSvIdBySystem(String system) {
        return system + "-mgm-sv";
    }

}
