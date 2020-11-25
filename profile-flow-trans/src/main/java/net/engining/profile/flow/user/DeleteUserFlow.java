package net.engining.profile.flow.user;

import net.engining.control.api.key.ErrorMessagesKey;
import net.engining.control.api.key.FinalResultKey;
import net.engining.control.api.key.TransIdKey;
import net.engining.control.core.flow.AbstractFlow;
import net.engining.control.core.flow.FlowDefinition;
import net.engining.control.core.invoker.DetermineFinalResult;
import net.engining.control.core.invoker.TransactionSeperator;
import net.engining.control.core.invoker.WriteInboundJournal;
import net.engining.control.core.invoker.WriteJournalUpdateResult;
import net.engining.profile.invoker.authority.DeleteProfileUserRolesInvoker;
import net.engining.profile.invoker.check.CheckOperatorIdExistsInvoker;
import net.engining.profile.invoker.check.CheckUserIdExistsInvoker;
import net.engining.profile.invoker.check.NotModifyAdminUserInvoker;
import net.engining.profile.invoker.security.AddOperationLogInvoker;
import net.engining.profile.invoker.user.UpdateProfileUserDelFlgInvoker;
import org.springframework.stereotype.Service;

/**
 * 用户删除
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 12:46
 * @since 1.0.0
 */
@FlowDefinition(
        name = "用户删除",
        invokers = {
                // 日志记录
                WriteInboundJournal.class,
                // 事务分割
                TransactionSeperator.class,
                // 校验操作员是否存在
                CheckOperatorIdExistsInvoker.class,
                // 不能对admin用户进行操作的校验
                NotModifyAdminUserInvoker.class,
                // 校验用户ID对应的用户是否存在
                CheckUserIdExistsInvoker.class,
                // 逻辑删除用户表记录
                UpdateProfileUserDelFlgInvoker.class,
                // 删除用户拥有的角色
                DeleteProfileUserRolesInvoker.class,
                // 记录操作日志
                AddOperationLogInvoker.class,
                // 事务分割
                TransactionSeperator.class,
                // 结果处理
                DetermineFinalResult.class,
                // 更新日志
                WriteJournalUpdateResult.class
        },
        response = {
                TransIdKey.class,
                FinalResultKey.class,
                ErrorMessagesKey.class
        }
)
@Service
public class DeleteUserFlow extends AbstractFlow {
}
