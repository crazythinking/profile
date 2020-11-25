package net.engining.profile.sdk.service.util;

import org.checkerframework.checker.units.qual.K;

import java.util.List;

/**
 * 不同层级FetchResponse中的data的转换
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/29 19:11
 * @since 1.0.0
 */
public interface DataConvert<K, T> {

    /**
     * 转换两个FetchResponse的data
     */
    List<T> convert(List<K> source);

}
