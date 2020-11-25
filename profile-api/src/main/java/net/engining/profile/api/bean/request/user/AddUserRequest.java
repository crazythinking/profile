package net.engining.profile.api.bean.request.user;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.api.bean.request.BaseOperateRequest;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 新增用户请求
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 16:13
 * @since 1.0.0
 */
public class AddUserRequest extends BaseOperateRequest {
    /**
     * 用户ID
     */
    @NotBlank(message = "请输入：用户ID")
    @Length(max = 40, message = "用户ID的字段长度不能超过40个字符")
    @ApiModelProperty(value = "用户ID|1-40位的字母或数字", example = "000000000")
    private String userId;
    /**
     * 用户名称
     */
    @NotBlank(message = "请输入：用户名称")
    @Length(max = 10, message = "用户名称的字段长度不能超过10个字符")
    @ApiModelProperty(value = "用户名称|1-10位的中文字符", example = "张三")
    private String userName;
    /**
     * 部门ID
     */
    @NotBlank(message = "请输入：部门ID")
    @Length(max = 6, message = "部门ID的字段长度不能超过6个字符")
    @ApiModelProperty(value = "部门ID|1-6位数字", example = "100001")
    private String departmentId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
}
