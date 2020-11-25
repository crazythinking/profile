package net.engining.profile.flow.role;

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
import net.engining.profile.invoker.check.CheckRoleIdExistsInvoker;
import net.engining.profile.invoker.check.CheckSameRoleChangedInvoker;
import net.engining.profile.invoker.check.NotModifyAdminRoleInvoker;
import net.engining.profile.invoker.role.UpdateProfileRoleInvoker;
import net.engining.profile.invoker.security.AddOperationLogInvoker;
import org.springframework.stereotype.Service;

/**
 * 角色修改
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/30 13:42
 * @since 1.0.0
 */
@FlowDefinition(
        name = "角色修改",
        invokers = {
                // 日志记录
                WriteInboundJournal.class,
                // 事务分割
                TransactionSeperator.class,
                // 校验操作员是否存在
                CheckOperatorIdExistsInvoker.class,
                // 不能对admin角色进行操作的校验
                NotModifyAdminRoleInvoker.class,
                // 校验角色ID对应的角色是否存在
                CheckRoleIdExistsInvoker.class,
                // 校验角色信息是否发生修改
                CheckSameRoleChangedInvoker.class,
                // 校验部门ID对应的部门是否存在
                CheckDepartmentIdExistsInvoker.class,
                // 角色修改
                UpdateProfileRoleInvoker.class,
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
public class UpdateRoleFlow extends AbstractFlow {
}
