package net.engining.profile.api.bean.request;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/12/15 14:38
 * @since 1.0.0
 */
public class ListSimpleRoleRequest implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 客户端ID
     */
    @ApiModelProperty(value = "客户端ID")
    private List<String> appCdList;

    public List<String> getAppCdList() {
        return appCdList;
    }

    public void setAppCdList(List<String> appCdList) {
        this.appCdList = appCdList;
    }

    @Override
    public String toString() {
        return "ListSimpleRoleRequest{" +
                "appCdList=" + appCdList +
                '}';
    }
}
