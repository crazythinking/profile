package net.engining.profile.sdk.service.bean;


import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
/**
 * @Description  菜单权限公用对象实体类
 * @Author heqingxi
 */
public class MenuOrAuthBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;
    /**
     * cd
     */
    private String cd;
    /**
     * 名称
     */
    private String name;
    /**
     * 父id
     */
    private String parentId;
    /**
     * 权限url
     * 菜单的为"-" 接口的为所属菜单id
     */
    private String autuUri;

    /**
     * 序号id
     */
    private String sortn;
    /**
     * 应用代码
     */
    private String appCd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    public String getAutuUri() {
        return autuUri;
    }

    public void setAutuUri(String autuUri) {
        this.autuUri = autuUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSortn() {
        return sortn;
    }

    public String getAppCd() {
        return appCd;
    }

    public void setAppCd(String appCd) {
        this.appCd = appCd;
    }

    public void setSortn(String sortn) {
        this.sortn = sortn;
    }
}
