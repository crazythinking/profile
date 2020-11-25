package net.engining.profile.sdk.service.bean.query;

/**
 * 角色分页查询参数
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/29 13:28
 * @since 1.0.0
 */
public class RolePagingQuery extends BasePagingQuery {
    /**
     * 角色名称
     */
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
