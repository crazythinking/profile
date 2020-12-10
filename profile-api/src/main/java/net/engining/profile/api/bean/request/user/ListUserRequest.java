package net.engining.profile.api.bean.request.user;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.api.bean.request.BasePagingQueryRequest;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 用户列表查询请求
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 15:32
 * @since 1.0.0
 */
public class ListUserRequest extends BasePagingQueryRequest {
    /**
     * 用户ID
     */
    @Length(max = 30, message = "用户ID的字段长度不能超过30个字符")
    @Length(min = 3, message = "用户ID的字段长度不能少于3个字符")
    @ApiModelProperty(value = "用户ID|3-30位的字母或数字或下划线", example = "000000000")
    private String userId;
    /**
     * 用户名称
     */
    @Length(max = 10, message = "用户名称的字段长度不能超过10个字符")
    @ApiModelProperty(value = "用户名称|1-10位的中文字符", example = "张三")
    private String userName;
    /**
     * 部门ID
     */
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
