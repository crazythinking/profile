package net.engining.profile.api.bean.response;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.api.bean.vo.DepartmentSimpleVo;
import net.engining.profile.api.bean.vo.RoleSimpleVo;
import net.engining.profile.api.bean.vo.SystemSimpleVo;

import java.io.Serializable;
import java.util.List;

/**
 * 下拉框查询结果
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/29 10:38
 * @since 1.0.0
 */
public class ComboBoxResponse implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 部门数据
     */
    @ApiModelProperty(value = "部门数据", required = true)
    private List<DepartmentSimpleVo> departmentData;
    /**
     * 角色数据
     */
    @ApiModelProperty(value = "角色数据", required = true)
    private List<RoleSimpleVo> roleData;
    /**
     * 系统数据
     */
    @ApiModelProperty(value = "系统数据", required = true)
    private List<SystemSimpleVo> systemData;

    public List<DepartmentSimpleVo> getDepartmentData() {
        return departmentData;
    }

    public void setDepartmentData(List<DepartmentSimpleVo> departmentData) {
        this.departmentData = departmentData;
    }

    public List<RoleSimpleVo> getRoleData() {
        return roleData;
    }

    public void setRoleData(List<RoleSimpleVo> roleData) {
        this.roleData = roleData;
    }

    public List<SystemSimpleVo> getSystemData() {
        return systemData;
    }

    public void setSystemData(List<SystemSimpleVo> systemData) {
        this.systemData = systemData;
    }
}
