package net.engining.profile.api.bean.request.department;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.api.bean.request.BaseOperateRequest;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 19:43
 * @since 1.0.0
 */
public class UpdateDepartmentRequest extends BaseOperateRequest {
    /**
     * 部门ID
     */
    @NotBlank(message = "请输入：部门ID")
    @Length(max = 6, message = "部门ID的字段长度不能超过6个数字")
    @ApiModelProperty(value = "部门ID|1-6位数字", example = "100001", required = true)
    private String departmentId;
    /**
     * 部门名称
     */
    @NotBlank(message = "请输入：部门名称")
    @Length(max = 25, message = "部门名称的字段长度不能超过25个中文字符")
    @ApiModelProperty(value = "部门名称|1-25个中文字符", example = "信息科技部", required = true)
    private String departmentName;

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "UpdateDepartmentRequest{" +
                "departmentId='" + departmentId + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", operatorId='" + operatorId + '\'' +
                '}';
    }
}
