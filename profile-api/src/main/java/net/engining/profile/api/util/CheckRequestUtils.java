package net.engining.profile.api.util;

import cn.hutool.core.date.DateUtil;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.utils.ValidateUtilExt;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/9/29 19:50
 * @since 1.0.0
 */
public class CheckRequestUtils {

    /**
     * 分页参数范围
     */
    private static final List<Long> PAGE_SIZE_RANGE = Arrays.asList(10L, 15L, 20L);
    /**
     * 正则匹配 数字或短横线
     */
    private static final Pattern NUMBER_OR_LINE = Pattern.compile("^[0-9-]+$");
    /**
     * 正则匹配 数字、字母或短横线
     */
    private static final Pattern NUMBER_OR_LETTER_LINE = Pattern.compile("^[0-9A-Za-z-]+$");


    /**
     * 查询笔数校验
     *
     * @param pageSize 查询笔数
     */
    public static void checkPageSizeIsWithinRange(Long pageSize) {
        if (!PAGE_SIZE_RANGE.contains(pageSize)) {
            throw new ErrorMessageException(ErrorCode.BadRequest, "每页查询笔数不在范围内");
        }
    }

    /**
     * 校验目标字段是否全是中文
     *
     * @param source 目标字段
     * @param name 目标字段名称
     */
    public static void checkIsChinese(String source, String name) {
        if (ValidateUtilExt.isNotNullOrEmpty(source) && !ValidateUtilExt.isChinese(source)) {
            throw new ErrorMessageException(ErrorCode.BadRequest, name + "只能由中文组成");
        }
    }

    /**
     * 校验目标字段是否全是数字
     *
     * @param source 目标字段
     * @param name 目标字段名称
     */
    public static void checkIsNumber(String source, String name) {
        if (ValidateUtilExt.isNotNullOrEmpty(source) && !ValidateUtilExt.isNumber(source)) {
            throw new ErrorMessageException(ErrorCode.BadRequest, name + "只能由数字组成");
        }
    }

    /**
     * 校验目标字段是否全是数字或字母
     *
     * @param source 目标字段
     * @param name 目标字段名称
     */
    public static void checkIsNumberOrLetter(String source, String name) {
        if (ValidateUtilExt.isNotNullOrEmpty(source) && !ValidateUtilExt.isEnglishAndDigit(source)) {
            throw new ErrorMessageException(ErrorCode.BadRequest, name + "只能由数字或字母组成");
        }
    }

    /**
     * 校验目标字段是否全是母
     *
     * @param source 目标字段
     * @param name 目标字段名称
     */
    public static void isLetter(String source, String name) {
        if (ValidateUtilExt.isNotNullOrEmpty(source) && !ValidateUtilExt.isEnglish(source)) {
            throw new ErrorMessageException(ErrorCode.BadRequest, name + "只能由字母组成");
        }
    }

    /**
     * 校验起始日期和结束日期并补全生成新的结束日期
     *
     * @param startDateName 起始日期名称
     * @param startDate 起始日期
     * @param endDateName 结束日期
     * @param endDate 结束日期
     * @return 新的结束日期
     */
    public static Date checkStartDateAndEndDate(String startDateName, Date startDate, String endDateName, Date endDate) {
        boolean b1 = ValidateUtilExt.isNullOrEmpty(startDate);
        boolean b2 = ValidateUtilExt.isNullOrEmpty(endDate);

        // 起始日期和结束日期需要同有同无
        if (b1 && !b2) {
            throw new ErrorMessageException(ErrorCode.CheckError, "请选择" + startDateName);
        }
        if (!b1 && b2) {
            throw new ErrorMessageException(ErrorCode.CheckError, "请选择" + endDateName);
        }

        if (!b1) {
            if (endDate.compareTo(startDate) < 0) {
                throw new ErrorMessageException(ErrorCode.CheckError, startDateName + "不能大于" + endDateName);
            }

            // 默认前端传递的时间格式是年月日，结束时间需要补全到23时59分59秒
            String endDateStr = DateUtil.formatDate(endDate) + " 23:59:59";
            return DateUtil.parse(endDateStr);
        }

        return endDate;
    }

    /**
     * 校验目标字段是否全是数字、字母或下划线
     *
     * @param source 目标字段
     * @param name 目标字段名称
     */
    public static void checkIsNumberOrLetterOrUnderline(String source, String name) {
        if (ValidateUtilExt.isNotNullOrEmpty(source) && !ValidateUtilExt.isEnglishAndDigitAndLine(source)) {
            throw new ErrorMessageException(ErrorCode.BadRequest, name + "只能由数字、字母或下划线组成");
        }
    }

    /**
     * 校验目标字段是否全是数字或横线
     *
     * @param source 目标字段
     * @param name 目标字段名称
     */
    public static void checkIsNumberOrLine(String source, String name) {
        Matcher matcher = NUMBER_OR_LINE.matcher(source);
        if (ValidateUtilExt.isNotNullOrEmpty(source) && !matcher.matches()) {
            throw new ErrorMessageException(ErrorCode.BadRequest, name + "只能由数字或短横线组成");
        }
    }

}
