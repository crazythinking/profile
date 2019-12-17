package net.engining.profile.sdk.service.bean.profile;

import io.swagger.annotations.ApiModelProperty;
import net.engining.pg.support.db.querydsl.Range;

import javax.validation.constraints.NotBlank;
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

    @NotBlank(message = "分支id")
    @ApiModelProperty(value = "分支id", required = true, example="123")
    private String branchId;

    private Range range;

    @ApiModelProperty(value = "用户姓名", required = false, example="xx")
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
