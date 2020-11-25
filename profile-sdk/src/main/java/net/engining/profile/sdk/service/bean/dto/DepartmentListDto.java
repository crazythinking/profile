package net.engining.profile.sdk.service.bean.dto;

import java.io.Serializable;

/**
 * 部门列表查询数据
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/7 13:28
 * @since 1.0.0
 */
public class DepartmentListDto implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    private String departmentId;
    /**
     * 部门名称
     */
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
}
