package net.engining.profile.flow.authority;

import net.engining.control.api.key.ErrorMessagesKey;
import net.engining.control.api.key.FinalResultKey;
import net.engining.control.api.key.TransIdKey;
import net.engining.control.core.flow.AbstractFlow;
import net.engining.control.core.flow.FlowDefinition;
import net.engining.control.core.invoker.DetermineFinalResult;
import net.engining.control.core.invoker.TransactionSeperator;
import net.engining.control.core.invoker.WriteInboundJournal;
import net.engining.control.core.invoker.WriteJournalUpdateResult;
import net.engining.profile.invoker.authority.AddProfileUserRolesInvoker;
import net.engining.profile.invoker.authority.DeleteProfileUserRolesInvoker;
import net.engining.profile.invoker.check.CheckOperatorIdExistsInvoker;
import net.engining.profile.invoker.check.CheckRoleIdListExistInvoker;
import net.engining.profile.invoker.check.CheckSameUserRoleChangedInvoker;
import net.engining.profile.invoker.check.CheckUserIdExistsInvoker;
import net.engining.profile.invoker.security.AddOperationLogInvoker;
import org.springframework.stereotype.Service;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/7 14:41
 * @since 1.0.0
 */
@FlowDefinition(
        name = "分配角色",
        invokers = {
                // 日志记录
                WriteInboundJournal.class,
                // 事务分割
                TransactionSeperator.class,
                // 校验操作员是否存在
                CheckOperatorIdExistsInvoker.class,
                // 校验用户ID对应的用户是否存在
                CheckUserIdExistsInvoker.class,
                // 校验用户拥有角色是否发生改变
                CheckSameUserRoleChangedInvoker.class,
                // 校验角色ID集合对应的角色是否存在
                CheckRoleIdListExistInvoker.class,
                // 删除用户拥有的角色
                DeleteProfileUserRolesInvoker.class,
                // 新增用户角色对应关系记录
                AddProfileUserRolesInvoker.class,
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
public class DistributeRolesFlow extends AbstractFlow {
}
