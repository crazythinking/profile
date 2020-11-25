package net.engining.profile.api.bean.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 用户列表查询数据
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 15:44
 * @since 1.0.0
 */
public class UserListVo implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称", example = "张三", required = true)
    private String userName;
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", example = "000000000", required = true)
    private String userId;
    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID", example = "100001", required = true)
    private String departmentId;
    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称", example = "科技信息部", required = true)
    private String departmentName;
    /**
     * 用户状态
     */
    @ApiModelProperty(value = "用户状态", example = "正常", required = true)
    private String userStatus;
    /**
     * 角色名称集合
     */
    @ApiModelProperty(value = "角色名称集合", example = "正常")
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

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
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
