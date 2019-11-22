package net.engining.profile.sdk.service.bean;


import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
/**
 * @Description  菜单对象实体类
 * @Author heqingxi
 */
public class MenuBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("菜单名称")
    private String menu;

    @ApiModelProperty("菜单父id")
    private String parentId;

    @ApiModelProperty("序号id")
    private String sortn;

    @ApiModelProperty("活动")
    private Boolean active;

    @ApiModelProperty("图标路径")
    private String imgUrl;

    @ApiModelProperty("菜单路径")
    private String href;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getSortn() {
        return sortn;
    }

    public void setSortn(String sortn) {
        this.sortn = sortn;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
