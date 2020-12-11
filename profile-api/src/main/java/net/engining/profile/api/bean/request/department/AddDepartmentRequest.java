package net.engining.profile.api.bean.request.department;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.api.bean.request.BaseOperateRequest;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 部门新增请求
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 19:40
 * @since 1.0.0
 */
public class AddDepartmentRequest extends BaseOperateRequest {
    /**
     * 部门名称
     */
    @NotBlank(message = "请输入：部门名称")
    @Length(max = 25, message = "部门名称的字段长度不能超过25个中文字符")
    @ApiModelProperty(value = "部门名称|1-25个中文字符", example = "信息科技部", required = true)
    private String departmentName;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "AddDepartmentRequest{" +
                "departmentName='" + departmentName + '\'' +
                ", operatorId='" + operatorId + '\'' +
                '}';
    }
}
