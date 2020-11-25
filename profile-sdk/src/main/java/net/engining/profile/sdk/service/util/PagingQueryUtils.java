package net.engining.profile.sdk.service.util;

import net.engining.profile.sdk.service.bean.query.DepartmentPagingQuery;
import net.engining.profile.sdk.service.bean.query.OperationLogPagingQuery;
import net.engining.profile.sdk.service.bean.query.RolePagingQuery;
import net.engining.profile.sdk.service.bean.query.UserPagingQuery;

import java.util.Date;

/**
 * 辅助分页查询
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/29 19:39
 * @since 1.0.0
 */
public class PagingQueryUtils {

    /**
     * 初始化角色分页查询参数
     *
     * @param roleName 角色名称
     * @param pageNum 起始数
     * @param pageSize 查询笔数
     * @return 角色分页查询参数
     */
    public static RolePagingQuery initRolePagingQuery(String roleName, Long pageNum, Long pageSize) {
        RolePagingQuery query = new RolePagingQuery();
        query.setRoleName(roleName);
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);
        return  query;
    }

    /**
     * 初始化用户分页查询参数
     *
     * @param userId 用户ID
     * @param userName 用户名称
     * @param departmentId 部门ID
     * @param pageNum 起始数
     * @param pageSize 查询笔数
     * @return 用户分页查询参数
     */
    public static UserPagingQuery initUserPagingQuery(String userId, String userName, String departmentId,
                                                      Long pageNum, Long pageSize) {
        UserPagingQuery query = new UserPagingQuery();
        query.setUserId(userId);
        query.setUserName(userName);
        query.setDepartmentId(departmentId);
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);
        return query;
    }

    /**
     * 初始化部门分页查询参数
     *
     * @param pageNum 起始数
     * @param pageSize 查询笔数
     * @return 部门分页查询参数
     */
    public static DepartmentPagingQuery initDepartmentPagingQuery(Long pageNum, Long pageSize) {
        DepartmentPagingQuery query = new DepartmentPagingQuery();
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);
        return query;
    }

    /**
     * 初始化操作日志分页查询参数
     *
     * @param userId 用户ID
     * @param userName 用户名称
     * @param puId 用户表ID
     * @param startDate 起始日期
     * @param endDate 结束日期
     * @param pageNum 起始数
     * @param pageSize 查询笔数
     * @return 用户分页查询参数
     */
    public static OperationLogPagingQuery initOperationLogPagingQuery(String userId, String userName,
                                                                      String puId, Date startDate, Date endDate,
                                                                      Long pageNum, Long pageSize) {
        OperationLogPagingQuery query = new OperationLogPagingQuery();
        query.setUserId(userId);
        query.setUserName(userName);
        query.setPuId(puId);
        query.setStartDate(startDate);
        query.setEndDate(endDate);
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);
        return query;
    }

}
