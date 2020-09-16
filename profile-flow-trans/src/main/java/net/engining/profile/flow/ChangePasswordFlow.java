package net.engining.profile.flow;

import net.engining.control.api.key.ErrorMessagesKey;
import net.engining.control.api.key.FinalResultKey;
import net.engining.control.api.key.TransIdKey;
import net.engining.control.core.flow.AbstractFlow;
import net.engining.control.core.flow.FlowDefinition;
import net.engining.control.core.invoker.DetermineFinalResult;
import net.engining.control.core.invoker.TransactionSeperator;
import net.engining.control.core.invoker.WriteInboundJournal;
import net.engining.control.core.invoker.WriteJournalUpdateResult;
import net.engining.profile.invoker.AddProfileSecoperLogInvoker;
import net.engining.profile.invoker.ChangePasswordInvoker;
import net.engining.profile.invoker.CheckOldPasswordIsRightInvoker;
import net.engining.profile.invoker.CheckOperatorIsSelfInvoker;
import net.engining.profile.invoker.CheckUserExistsInvoker;
import net.engining.profile.invoker.PasswordValidateInvoker;
import org.springframework.stereotype.Service;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/14 14:20
 * @since 1.0.0
 */
@FlowDefinition(
        invokers = {
                // 日志记录
                WriteInboundJournal.class,
                // 事务分割
                TransactionSeperator.class,
                // 校验用户信息是否存在
                CheckUserExistsInvoker.class,
                // 校验操作人是否是自己
                CheckOperatorIsSelfInvoker.class,
                // 校验旧密码是否正确
                CheckOldPasswordIsRightInvoker.class,
                // 密码校验
                PasswordValidateInvoker.class,
                // 修改密码
                ChangePasswordInvoker.class,
                // 操作安全日志记录
                AddProfileSecoperLogInvoker.class,
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
public class ChangePasswordFlow extends AbstractFlow {
}
