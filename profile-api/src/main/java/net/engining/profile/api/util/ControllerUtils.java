package net.engining.profile.api.util;

import com.google.common.collect.ArrayListMultimap;
import net.engining.control.api.key.ErrorMessagesKey;
import net.engining.control.sdk.AbstractFlowTransPayload;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.utils.ValidateUtilExt;

import java.util.List;

/**
 * controller层公共方法
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/14 17:19
 * @since 1.0.0
 */
public class ControllerUtils {

    /**
     * flow结果处理
     *
     * @param t flow处理结果
     */
    public static void checkFlowResponse(AbstractFlowTransPayload t) {
        @SuppressWarnings("unchecked")
        ArrayListMultimap<ErrorCode, String> map = (ArrayListMultimap<ErrorCode, String>) t.getDataMap().get(ErrorMessagesKey.class);
        if(ValidateUtilExt.isNotNullOrEmpty(map)) {
            for (ErrorCode code : map.keySet()) {
                if(!code.equals(ErrorCode.Success)){
                    List<String> messageList = map.get(code);
                    String errorMessage;
                    // 底层包升级后invoker可以并发执行，所有可能会有多个错误
                    if (1 == messageList.size()) {
                        errorMessage = map.get(code).get(0);
                        errorMessage = errorMessage.substring(errorMessage.indexOf(":") + 1);
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        String comma = "、";
                        String semicolon = "；";
                        for (int i = 0; i < messageList.size(); i++) {
                            int num = i + 1;
                            String errorMessagePart = messageList.get(i);
                            errorMessagePart = errorMessagePart.substring(errorMessagePart.indexOf(":") + 1);
                            stringBuilder.append(num).append(comma).append(errorMessagePart).append(semicolon);
                        }
                        errorMessage = stringBuilder.toString();
                    }
                    throw new ErrorMessageException(code, errorMessage);
                }
            }
        }
    }
}
