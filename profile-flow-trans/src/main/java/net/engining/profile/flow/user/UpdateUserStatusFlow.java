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
import net.engining.profile.invoker.check.CheckOperatorIdExistsInvoker;
import net.engining.profile.invoker.check.CheckUserIdExistsInvoker;
import net.engining.profile.invoker.check.CheckUserStatusInvoker;
import net.engining.profile.invoker.check.NotModifyAdminUserInvoker;
import net.engining.profile.invoker.security.AddOperationLogInvoker;
import net.engining.profile.invoker.user.UpdateProfileUserStatusInvoker;
import org.springframework.stereotype.Service;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 12:58
 * @since 1.0.0
 */
@FlowDefinition(
        name = "修改用户状态",
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
                // 用户状态校验
                CheckUserStatusInvoker.class,
                // 修改用户状态
                UpdateProfileUserStatusInvoker.class,
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
public class UpdateUserStatusFlow extends AbstractFlow {
}
