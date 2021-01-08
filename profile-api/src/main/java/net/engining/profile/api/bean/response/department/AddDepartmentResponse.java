package net.engining.profile.api.bean.response.department;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 新增部门结果
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 19:46
 * @since 1.0.0
 */
public class AddDepartmentResponse implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID|1-6位数字", example = "100001", required = true)
    private String departmentId;

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "AddDepartmentResponse{" +
                "departmentId='" + departmentId + '\'' +
                '}';
    }
}
