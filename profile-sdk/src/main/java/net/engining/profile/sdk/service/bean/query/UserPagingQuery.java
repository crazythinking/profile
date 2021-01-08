package net.engining.profile.sdk.service.bean.query;

import java.util.List;

/**
 * 用户分页查询参数
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/30 14:49
 * @since 1.0.0
 */
public class UserPagingQuery extends BasePagingQuery {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 部门ID
     */
    private String departmentId;
    /**
     * 服务端ID
     */
    private List<String> appCdList;

    public UserPagingQuery() {
    }

    public UserPagingQuery(String userId, String userName, String departmentId, Long pageNum, Long pageSize) {
        this.userId = userId;
        this.userName = userName;
        this.departmentId = departmentId;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public List<String> getAppCdList() {
        return appCdList;
    }

    public void setAppCdList(List<String> appCdList) {
        this.appCdList = appCdList;
    }
}
