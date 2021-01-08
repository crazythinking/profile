package net.engining.profile.api.bean.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 部门数据
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/29 10:26
 * @since 1.0.0
 */
public class DepartmentSimpleVo implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID", example = "100001", required = true)
    private String departmentId;
    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称", example = "信息科技部", required = true)
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
        return "DepartmentSimpleVo{" +
                "departmentId='" + departmentId + '\'' +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}
