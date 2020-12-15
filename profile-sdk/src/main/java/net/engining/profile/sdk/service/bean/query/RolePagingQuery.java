package net.engining.profile.sdk.service.bean.query;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

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
    /**
     * 客户端ID
     */
    private List<String> appCdList;

    public RolePagingQuery() {
    }

    public RolePagingQuery(String roleName, List<String> appCdList, Long pageNum, Long pageSize) {
        this.roleName = roleName;
        this.appCdList = appCdList;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

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
}
