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
import net.engining.profile.invoker.check.CheckDepartmentIdExistsInvoker;
import net.engining.profile.invoker.check.CheckOperatorIdExistsInvoker;
import net.engining.profile.invoker.check.CheckSameUserChangedInvoker;
import net.engining.profile.invoker.check.CheckUserIdExistsInvoker;
import net.engining.profile.invoker.check.NotModifyAdminUserInvoker;
import net.engining.profile.invoker.security.AddOperationLogInvoker;
import net.engining.profile.invoker.user.UpdateProfileUserInvoker;
import org.springframework.stereotype.Service;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 11:15
 * @since 1.0.0
 */
@FlowDefinition(
        name = "用户修改",
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
                // 校验用户信息是否修改
                CheckSameUserChangedInvoker.class,
                // 校验部门ID对应的部门是否存在
                CheckDepartmentIdExistsInvoker.class,
                // 更新用户表记录
                UpdateProfileUserInvoker.class,
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
public class UpdateUserFlow extends AbstractFlow {
}
