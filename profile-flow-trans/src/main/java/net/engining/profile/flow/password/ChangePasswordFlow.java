package net.engining.profile.flow.password;

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
import net.engining.profile.invoker.check.CheckUserStatusInvoker;
import net.engining.profile.invoker.check.NotModifyAdminUserInvoker;
import net.engining.profile.invoker.password.PasswordValidateInvoker;
import net.engining.profile.invoker.check.CheckOldPasswordIsRightInvoker;
import net.engining.profile.invoker.check.CheckOperatorIsSelfInvoker;
import net.engining.profile.invoker.check.CheckPuIdExistsInvoker;
import net.engining.profile.invoker.password.AddProfilePwdHistInvoker;
import net.engining.profile.invoker.security.AddOperationLogInvoker;
import net.engining.profile.invoker.user.UpdateProfileUserPasswordInvoker;
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
                // 校验操作员是否存在
                CheckOperatorIdExistsInvoker.class,
                // 校验用户信息是否存在
                CheckPuIdExistsInvoker.class,
                // 用户状态校验
                CheckUserStatusInvoker.class,
                // 校验操作人是否是自己
                CheckOperatorIsSelfInvoker.class,
                // 不能对admin用户进行操作的校验
//                NotModifyAdminUserInvoker.class,
                // 校验旧密码是否正确
                CheckOldPasswordIsRightInvoker.class,
                // 密码校验
                PasswordValidateInvoker.class,
                // 修改密码
                UpdateProfileUserPasswordInvoker.class,
                // 增加密码历史记录
                AddProfilePwdHistInvoker.class,
                // 操作安全日志记录
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
public class ChangePasswordFlow extends AbstractFlow {
}
