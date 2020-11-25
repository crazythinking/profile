package net.engining.profile.sdk.service.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.enums.UserStatusEnum;

import java.io.Serializable;
import java.util.List;

/**
 * 业务层用户列表展示数据
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/30 15:31
 * @since 1.0.0
 */
public class UserListDto implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 部门ID
     */
    private String departmentId;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 用户状态
     */
    private UserStatusEnum userStatus;
    /**
     * 角色名称集合
     */
    private List<String> roleNameList;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public UserStatusEnum getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatusEnum userStatus) {
        this.userStatus = userStatus;
    }

    public List<String> getRoleNameList() {
        return roleNameList;
    }

    public void setRoleNameList(List<String> roleNameList) {
        this.roleNameList = roleNameList;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
