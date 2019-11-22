package net.engining.profile.sdk.service.bean.profile;

import net.engining.pg.support.db.querydsl.Range;

/**
 * @author yangxing
 */
public class BranchFilter {

    private String superiorId;
    private Range range;

    public String getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(String superiorId) {
        this.superiorId = superiorId;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

}
