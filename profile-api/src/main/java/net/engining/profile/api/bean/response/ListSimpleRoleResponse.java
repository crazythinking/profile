package net.engining.profile.api.bean.response;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/12/15 11:12
 * @since 1.0.0
 */
public class ListSimpleRoleResponse implements Serializable {

    /**
     * 展示客户端ID
     */
    @ApiModelProperty(value = "客户端ID")
    private List<String> shownList;
    /**
     * 全部客户端ID
     */
    @ApiModelProperty(value = "客户端ID")
    private List<String> allList;

    public List<String> getShownList() {
        return shownList;
    }

    public void setShownList(List<String> shownList) {
        this.shownList = shownList;
    }

    public List<String> getAllList() {
        return allList;
    }

    public void setAllList(List<String> allList) {
        this.allList = allList;
    }

    @Override
    public String toString() {
        return "ListSimpleRoleRequest{" +
                "shownList=" + shownList +
                ", allList=" + allList +
                '}';
    }
}
