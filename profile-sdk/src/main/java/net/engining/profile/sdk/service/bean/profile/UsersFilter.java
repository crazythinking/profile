package net.engining.profile.sdk.service.bean.profile;

import net.engining.pg.support.db.querydsl.Range;

import java.io.Serializable;

/**
 *功能描述
 *@modificator liudongjing
 *@Date 2019/8/21
 */

/**
 * @author liudongjing
 */
public class UsersFilter implements Serializable{

    private static final long serialVersionUID = 4603644745622122258L;
    private String branchId;
    private Range range;
    private String name;

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
