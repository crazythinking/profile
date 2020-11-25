package net.engining.profile.api.bean.request.department;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.api.bean.request.BaseOperateRequest;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 删除部门请求
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 19:45
 * @since 1.0.0
 */
public class DeleteDepartmentRequest extends BaseOperateRequest {
    /**
     * 部门ID
     */
    @NotBlank(message = "请输入：部门ID")
    @Length(max = 6, message = "部门ID的字段长度不能超过6个字符")
    @ApiModelProperty(value = "部门ID|1-6位数字", example = "100001", required = true)
    private String departmentId;

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
}
