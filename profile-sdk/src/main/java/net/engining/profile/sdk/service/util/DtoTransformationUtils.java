package net.engining.profile.sdk.service.util;

import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.support.utils.ValidateUtilExt;

import java.util.List;

/**
 * Dto参数转换
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/29 19:14
 * @since 1.0.0
 */
public class DtoTransformationUtils {

    /**
     * 不同层级分页查询参数转换
     *
     * @param source 原分页查询结果
     * @param dataConvert 具体数据转换方法
     * @return 目标分页查询结果
     */
    public static <K, T> FetchResponse<T> convertToPagingQueryResult(FetchResponse<K> source, DataConvert<K, T> dataConvert) {
        FetchResponse<T> target = new FetchResponse<>();
        target.setRowCount(source.getRowCount());
        target.setStart(source.getStart());
        List<K> sourceData = source.getData();
        if (ValidateUtilExt.isNotNullOrEmpty(sourceData)) {
            List<T> targetData = dataConvert.convert(sourceData);
            target.setData(targetData);
        }
        return target;
    }

}
