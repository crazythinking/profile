package net.engining.profile.flow.department;

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
import net.engining.profile.invoker.department.AddProfileBranchInvoker;
import net.engining.profile.invoker.security.AddOperationLogInvoker;
import net.engining.profile.invoker.user.UpdateProfileUserStatusInvoker;
import net.engining.profile.sdk.key.DepartmentIdKey;
import org.springframework.stereotype.Service;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 14:36
 * @since 1.0.0
 */
@FlowDefinition(
        name = "部门新增",
        invokers = {
                // 日志记录
                WriteInboundJournal.class,
                // 事务分割
                TransactionSeperator.class,
                // 校验操作员是否存在
                CheckOperatorIdExistsInvoker.class,
                // 新增部门表记录
                AddProfileBranchInvoker.class,
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
                DepartmentIdKey.class,
                TransIdKey.class,
                FinalResultKey.class,
                ErrorMessagesKey.class
        }
)
@Service
public class AddDepartmentFlow extends AbstractFlow {
}
