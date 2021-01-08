package net.engining.profile.invoker.check;

import com.querydsl.jpa.impl.JPAQueryFactory;
import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.model.QProfileRole;
import net.engining.profile.sdk.key.RoleIdKey;
import net.engining.profile.sdk.key.RoleNameKey;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/12/15 10:26
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "校验角色名称是否重复",
        requires = {
                RoleNameKey.class
        },
        optional = {
                RoleIdKey.class
        }
)
public class CheckRoleNameRepeatsInvoker extends AbstractSkippableInvoker {

    /**
     * 实体服务
     */
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void invoke(FlowContext flowContext) {
        QProfileRole qProfileRole = QProfileRole.profileRole;
        String roleId = new JPAQueryFactory(entityManager)
                .select(qProfileRole.roleId)
                .from(qProfileRole)
                .where(qProfileRole.roleName.eq(flowContext.get(RoleNameKey.class)))
                .fetchFirst();

        if (ValidateUtilExt.isNotNullOrEmpty(roleId) && !roleId.equals(flowContext.get(RoleIdKey.class))) {
            throw new ErrorMessageException(ErrorCode.CheckError, "角色名称重复");
        }
    }

}
