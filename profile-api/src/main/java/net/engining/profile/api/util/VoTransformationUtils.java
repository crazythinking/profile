package net.engining.profile.api.util;

import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.api.bean.response.BasePagingQueryResponse;
import net.engining.profile.api.bean.vo.RoleSimpleVo;
import net.engining.profile.sdk.service.bean.dto.RoleSimpleDto;
import net.engining.profile.sdk.service.util.DataConvert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.engining.profile.sdk.service.constant.ParameterConstants.EMPTY_SIZE;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/29 19:55
 * @since 1.0.0
 */
public class VoTransformationUtils {

    /**
     * 分页查询结果转换
     *
     * @param source 业务层分页查询结果
     * @param target 表现层分页查询结果
     * @param convert 具体数据的转换方法
     */
    public static <K, T> void convertToPagingQueryResponse(FetchResponse<K> source,
                                                                                  BasePagingQueryResponse<T> target,
                                                                                  DataConvert<K, T> convert) {
        target.setTotalNum(source.getRowCount());
        List<K> sourceData = source.getData();
        if (ValidateUtilExt.isNullOrEmpty(sourceData)) {
            target.setData(new ArrayList<>(EMPTY_SIZE));
        } else {
            List<T> targetData = convert.convert(sourceData);
            target.setData(targetData);
        }
    }

    /**
     * 角色简单信息集合转换
     *
     * @param dtoList 业务层角色简单信息集合
     * @return 表现层角色简单信息集合
     */
    public static List<RoleSimpleVo> convertToRoleSimpleVoList(List<RoleSimpleDto> dtoList) {
        if (ValidateUtilExt.isNullOrEmpty(dtoList)) {
            return Collections.emptyList();
        } else {
            List<RoleSimpleVo> result = new ArrayList<>(dtoList.size());
            for (RoleSimpleDto dto : dtoList) {
                RoleSimpleVo vo = new RoleSimpleVo();
                vo.setRoleId(dto.getRoleId());
                vo.setRoleName(dto.getRoleName());
                result.add(vo);
            }
            return result;
        }
    }

}
