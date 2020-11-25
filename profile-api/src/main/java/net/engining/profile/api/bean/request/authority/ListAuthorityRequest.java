package net.engining.profile.api.bean.request.authority;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.enums.SystemEnum;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 17:24
 * @since 1.0.0
 */
public class ListAuthorityRequest implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    @NotNull(message = "请输入：所属系统")
    @ApiModelProperty(value = "所属系统|枚举", example = "SCAC", notes = "net.engining.profile.enums.SystemEnum", required = true)
    private SystemEnum system;

    public SystemEnum getSystem() {
        return system;
    }

    public void setSystem(SystemEnum system) {
        this.system = system;
    }
}
