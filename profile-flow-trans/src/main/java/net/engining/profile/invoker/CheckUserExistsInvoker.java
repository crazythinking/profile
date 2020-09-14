package net.engining.profile.invoker;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.sdk.key.ProfileUserKey;
import net.engining.profile.sdk.key.PuIdKey;
import net.engining.profile.sdk.key.UserIdKey;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/14 14:30
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "校验用户信息是否存在",
        requires = {
                PuIdKey.class
        }
)
public class CheckUserExistsInvoker extends AbstractSkippableInvoker {

    /**
     * 实体服务
     */
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void invoke(FlowContext flowContext) {
        String puId = flowContext.get(PuIdKey.class);
        ProfileUser profileUser = entityManager.find(ProfileUser.class, puId);
        if (ValidateUtilExt.isNullOrEmpty(profileUser)) {
            throw new ErrorMessageException(ErrorCode.Null, String.format("%s用户信息不存在", puId));
        }

        flowContext.put(ProfileUserKey.class, profileUser);
        flowContext.put(UserIdKey.class, profileUser.getUserId());
    }

}