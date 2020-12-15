package net.engining.profile.api.bean.request.role;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.api.bean.request.BasePagingQueryRequest;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 角色列表查询请求
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 9:13
 * @since 1.0.0
 */
public class ListRoleRequest extends BasePagingQueryRequest {
    /**
     *  角色名称
     */
    @Length(max = 50, message = "角色名称的字段长度不能超过50个中文字符")
    @ApiModelProperty(value = "角色名称|1-50个中文字符", example = "管理员")
    private String roleName;
    /**
     * 客户端ID
     */
    @ApiModelProperty(value = "客户端ID")
    private List<String> appCdList;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<String> getAppCdList() {
        return appCdList;
    }

    public void setAppCdList(List<String> appCdList) {
        this.appCdList = appCdList;
    }

    @Override
    public String toString() {
        return "ListRoleRequest{" +
                "roleName='" + roleName + '\'' +
                ", appCdList=" + appCdList +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
