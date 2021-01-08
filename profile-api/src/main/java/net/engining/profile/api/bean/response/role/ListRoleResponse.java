package net.engining.profile.api.bean.response.role;

import io.swagger.annotations.ApiModelProperty;
import net.engining.profile.api.bean.response.BasePagingQueryResponse;
import net.engining.profile.api.bean.vo.RoleListVo;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.List;

/**
 * 角色列表分页查询结果
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/28 9:36
 * @since 1.0.0
 */
public class ListRoleResponse<RoleListVo> extends BasePagingQueryResponse<RoleListVo> {
}
