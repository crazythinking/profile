package net.engining.profile.api.bean.request.authority;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
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

    @Length(max = 10, message = "所属系统的字段长度不能超过10个字母")
    @NotBlank(message = "请输入：所属系统")
    @ApiModelProperty(value = "所属系统|1-10个字母", example = "SCAC", required = true)
    private String system;

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    @Override
    public String toString() {
        return "ListAuthorityRequest{" +
                "system='" + system + '\'' +
                '}';
    }
}
