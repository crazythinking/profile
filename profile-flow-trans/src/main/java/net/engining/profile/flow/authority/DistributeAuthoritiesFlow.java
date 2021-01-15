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
import net.engining.profile.invoker.authority.AddProfileRoleAuthsInvoker;
import net.engining.profile.invoker.authority.DeleteProfileRoleAuthsInvoker;
import net.engining.profile.invoker.authority.RefreshCacheInvoker;
import net.engining.profile.invoker.check.CheckOperatorIdExistsInvoker;
import net.engining.profile.invoker.check.CheckOperatorIsNotSelfInvoker;
import net.engining.profile.invoker.check.CheckRoleIdExistsInvoker;
import net.engining.profile.invoker.check.CheckSameRoleAuthChangedInvoker;
import net.engining.profile.invoker.check.NotModifyAdminRoleInvoker;
import net.engining.profile.invoker.check.CheckRoleAuthsExistInvoker;
import net.engining.profile.invoker.security.AddOperationLogInvoker;
import org.springframework.stereotype.Service;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/7 15:12
 * @since 1.0.0
 */
@FlowDefinition(
        name = "分配权限",
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
                // 校验角色拥有的权限是否发生改变
                CheckSameRoleAuthChangedInvoker.class,
                // 校验权限是否存在
                CheckRoleAuthsExistInvoker.class,
                // 删除角色已有权限
                DeleteProfileRoleAuthsInvoker.class,
                // 新增角色权限对应关系
                AddProfileRoleAuthsInvoker.class,
                // 更新本地缓存
                RefreshCacheInvoker.class,
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
public class DistributeAuthoritiesFlow extends AbstractFlow {
}
