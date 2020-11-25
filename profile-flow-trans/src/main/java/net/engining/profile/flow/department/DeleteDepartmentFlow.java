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
import net.engining.profile.invoker.check.CheckDepartmentIdExistsInvoker;
import net.engining.profile.invoker.check.CheckOperatorIdExistsInvoker;
import net.engining.profile.invoker.department.UpdateProfileBranchDelFlgInvoker;
import net.engining.profile.invoker.security.AddOperationLogInvoker;
import org.springframework.stereotype.Service;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/6 15:04
 * @since 1.0.0
 */
@FlowDefinition(
        name = "部门删除",
        invokers = {
                // 日志记录
                WriteInboundJournal.class,
                // 事务分割
                TransactionSeperator.class,
                // 校验操作员是否存在
                CheckOperatorIdExistsInvoker.class,
                // 校验部门ID对应的部门是否存在
                CheckDepartmentIdExistsInvoker.class,
                // 修改部门表记录
                UpdateProfileBranchDelFlgInvoker.class,
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
public class DeleteDepartmentFlow extends AbstractFlow {
}
