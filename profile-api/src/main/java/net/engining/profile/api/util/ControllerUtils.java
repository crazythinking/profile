package net.engining.profile.api.util;

import net.engining.control.api.key.ErrorMessagesKey;
import net.engining.control.sdk.AbstractFlowTransPayload;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.pg.web.bean.DefaultResponseHeader;

import java.util.Date;
import java.util.Map;
import java.util.Set;

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
        Map<ErrorCode, Set<String>> map = (Map<ErrorCode, Set<String>>) t.getDataMap().get(ErrorMessagesKey.class);
        if(ValidateUtilExt.isNotNullOrEmpty(map)) {
            for (ErrorCode code : map.keySet()) {
                if(!code.equals(ErrorCode.Success)){
                    Set<String> messageSet = map.get(code);
                    String errorMessage = null;
                    // 底层包升级后invoker可以并发执行，所有可能会有多个错误
                    if (1 == messageSet.size()) {
                        for (String errorMessageStr : messageSet) {
                            errorMessage = errorMessageStr.substring(errorMessageStr.indexOf(":") + 1);
                        }
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        String comma = "、";
                        String semicolon = "；";
                        int num = 1;
                        for (String errorMessageStr : messageSet) {
                            stringBuilder.append(num)
                                    .append(comma)
                                    .append(errorMessageStr.substring(errorMessageStr.indexOf(":") + 1))
                                    .append(semicolon);
                            num ++;
                        }
                        errorMessage = stringBuilder.toString();
                    }
                    throw new ErrorMessageException(code, errorMessage);
                }
            }
        }
    }

    /**
     * 返回有默认头部的成功结果
     *
     * @param svPrSerialNo 内部交易流水号
     * @param txnSerialNo 原交易流水号
     * @param timestamp 交易时间
     * @param t 体
     * @return 结果
     */
    public static <T> CommonWithHeaderResponse<DefaultResponseHeader, T>
    returnSuccessResponseWithDefualtHead(String svPrSerialNo, String txnSerialNo, Date timestamp, T t) {
        DefaultResponseHeader responseHeader = new DefaultResponseHeader();
        responseHeader.setSvPrSerialNo(svPrSerialNo);
        responseHeader.setTxnSerialNo(txnSerialNo);
        responseHeader.setTimestamp(timestamp);

        return new CommonWithHeaderResponse<DefaultResponseHeader, T>()
                .setResponseHead(responseHeader)
                .setResponseData(t)
                .setStatusCode(ErrorCode.Success.getValue())
                .setStatusDesc(ErrorCode.Success.getLabel());
    }

    /**
     * 返回有默认头部的成功结果
     *
     * @param svPrSerialNo 内部交易流水号
     * @param txnSerialNo 原交易流水号
     * @param timestamp 交易时间
     * @return 结果
     */
    public static CommonWithHeaderResponse<DefaultResponseHeader, Void>
    returnSuccessResponseWithDefualtHead(String svPrSerialNo, String txnSerialNo, Date timestamp) {
        DefaultResponseHeader responseHeader = new DefaultResponseHeader();
        responseHeader.setSvPrSerialNo(svPrSerialNo);
        responseHeader.setTxnSerialNo(txnSerialNo);
        responseHeader.setTimestamp(timestamp);

        return new CommonWithHeaderResponse<DefaultResponseHeader, Void>()
                .setResponseHead(responseHeader)
                .setStatusCode(ErrorCode.Success.getValue())
                .setStatusDesc(ErrorCode.Success.getLabel());
    }

    /**
     * 返回有头部的成功结果
     *
     * @param t 体
     * @return 结果
     */
    public static <T> CommonWithHeaderResponse<Void, T> returnSuccessResponseWithoutHead(T t) {
        return new CommonWithHeaderResponse<Void, T>()
                .setResponseData(t)
                .setStatusCode(ErrorCode.Success.getValue())
                .setStatusDesc(ErrorCode.Success.getLabel());
    }
    /**
     * 返回成功结果
     *
     * @return 结果
     */
    public static CommonWithHeaderResponse<Void, Void> returnSuccessResponseWithNothing() {
        return new CommonWithHeaderResponse<Void, Void>()
                .setStatusCode(ErrorCode.Success.getValue())
                .setStatusDesc(ErrorCode.Success.getLabel());
    }

}
