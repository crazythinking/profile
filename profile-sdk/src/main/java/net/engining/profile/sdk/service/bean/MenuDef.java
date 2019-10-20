package net.engining.profile.sdk.service.bean;

import net.engining.pg.support.enums.BaseEnum;

/**
 * @author heqingxi
 */
public enum MenuDef implements BaseEnum<String> {

    /**
     * 核销处理
     */
    WriteOffProce("WriteOffProce","38,39,40"),
    /**
     * 核销综合处理
     */
    WriteOffCompre("WriteOffCompre","39,39"),

    /**
     * 核销资产减值
     */

    WriteOffImpa("WriteOffImpa","38,40"),
    /**
     * 用户管理
     */
    ProfileUser("ProfileUser", "1,2,3,4"),
    /**
     * 角色管理
     */
    ProfileRole("ProfileRole", "1,5"),
    /**
     * 用户登记薄详情查询
     */
    userRegistrySearch("userRegistrySearch", "1,4"),
    /**
     * 用户登记薄查询Excel
     */
    userRegistryExport("userRegistryExport", "1,4"),
    /**
     * 密码规则确认
     */
    passwordRuleManager("passwordRuleManager", "1,3"),
    /**
     * 密码规则查询
     */
    passwordRuleSearch("passwordRuleSearch", "1,3"),
    // 价税分离参数设置
    /**
     * 价税分离更新
     */
    taxRateRecordUpdate("taxRateRecordUpdate", "6,7"),
    /**
     * 价税分离参数新增
     */
    leviedSeparateInsert("leviedSeparateInsert", "6,7"),
    /**
     * 价税分离参数修改
     */
    leviedSeparateUpdate("leviedSeparateUpdate", "6,7"),
    /**
     * 价税分离参数删除
     */
    leviedSeparateDelete("leviedSeparateDelete", "6,7"),
    /**
     * 价税分离参数查询
     */
    leviedSeparateSreach("leviedSeparateSreach", "6,7"),

    // FIXME: 2019/9/12 会计科目配置
    /**
     * 会计科目新增
     */
    subjectInsert("subjectInsert", "6,8"),
    /**
     * 会计科目修改
     */
    subjectUpdate("subjectUpdate", "6,8"),
    /**
     * 会计科目详情
     */
    subjectDetails("subjectDetails", "6,8"),
    /**
     * 会计科目查询
     */
    subjectSearch("subjectSearch", "6,8"),
    /**
     * 将查询报表的信息插入CSV表中
     */
    subjectDetaiExport("subjectDetaiExport", "6,8"),
    /**
     * 获取计税应税科目
     */
    getTaxableSubjectCd("getTaxableSubjectCd", "6,8"),

    // FIXME: 2019/9/12 五级分类配置
    /**
     * 五级分类修改
     */
    fiveCategoriesUpdate("fiveCategoriesUpdate", "6,9"),
    /**
     * 五级分类分页查询
     */
    fiveCategoriesSearch("fiveCategoriesSearch", "6,9"),

    // FIXME: 2019/9/12 套型配置
    /**
     * 查询套型
     */
    queryTxn("queryTxn", "6,10"),
    /**
     * 新增套型
     */
    addTxnInformation("addTxnInformation", "6,10"),
    /**
     * 修改套型
     */
    updateTxnInformation("updateTxnInformation", "6,10"),
    /**
     * 查询套型详情
     */
    queryTxnDetail("queryTxnDetail", "6,10"),
    /**
     * 获取记账码关联数据
     */
    getTxndetail("getTxndetail", "6,10"),
    // FIXME: 2019/9/12 交易码映射配置
    /**
     * 交易码映射配置信息修改
     */
    microEntriesUpdate("microEntriesUpdate", "6,11"),
    /**
     * 交易码映射配置信息添加
     */
    microEntriesAdd("microEntriesAdd", "6,11"),
    /**
     * 交易码映射配置删除
     */
    microEntriesDelete("microEntriesDelete", "6,11"),

    /**
     * 交易码映射配置查询
     */
    microfinanceQurey("microfinanceQurey", "6,11"),
    // FIXME: 2019/9/12 核算交易码维护
    /**
     * 查询核算交易码
     */
    queryPostCode("queryPostCode", "6,12"),
    /**
     * 核算交易码新增
     */
    addPostCode("addPostCode", "6,12"),
    /**
     * 核算交易码修改
     */
    updatePostCode("updatePostCode", "6,12"),
    /**
     * 核算交易码删除
     */
    deletePostCode("deletePostCode", "6,12"),

    /**
     * 查询交易码
     */
    findPostCodeBySysTxnCode("findPostCodeBySysTxnCode", "6,12"),
    // FIXME: 2019/9/12 账龄组配置
    /**
     * 账龄组查询
     */
    ageGroupSearch("ageGroupSearch", "6,13"),
    /**
     * 账龄组修改
     */
    ageGroupUpdate("ageGroupUpdate", "6,13"),
    // FIXME: 2019/9/12 记账码配置
    /**
     * 记账码条件查询
     */
    accountCodeSearch("accountCodeSearch", "6,14"),
    /**
     * 记账码会计分录详情查询
     */
    accountEntriesSearch("accountEntriesSearch", "6,14"),
    /**
     * 记账码会计分录添加
     */
    accountEntriesAdd("accountEntriesAdd", "6,14"),
    /**
     * 记账码会计分录删除
     */
    accountEntriesDelete("accountEntriesDelete", "6,14"),
    /**
     * 记账码会计分录修改
     */
    accountEntriesUpdate("accountEntriesUpdate", "6,14"),
    /**
     * 交易码查询条件查询
     */
    transactionCodeSearch("transactionCodeSearch", "6,14"),
    /**
     * 套型关联内容查询
     */
    nestAssocistionSearch("nestAssocistionSearch", "6,14"),
    /**
     * 金额成分查询
     */
    amountIngredients("amountIngredients", "6,14"),
    /**
     * 查询该记账码对应的套型数据
     */
    nestAssocistionHaving("nestAssocistionHaving", "6,14"),
    /**
     * 交易码查询
     */
    codeSearch("codeSearch", "6,14"),
    /**
     * 套型关联数据提交
     */
    nestAssocistionAdd("nestAssocistionAdd", "6,14"),
    /**
     * 记账码配置详情查询
     */
    detailSearch("detailSearch", "6,14"),
    /**
     * 表内外借贷会计科目查询
     */
    accountBorrowSearch("accountBorrowSearch", "6,14"),
    // FIXME: 2019/9/12 试算平衡
    /**
     * 试算平衡导出、试算平衡、获取业务日期
     */
    trialBanlance("trialBanlance", "20,21"),

    // FIXME: 2019/9/12 总分核对
    /**
     * 总分核对，总分核对导出
     */
    totalScoreCheck("totalScoreCheck", "20,22"),
    /**
     * 获取所有末级科目
     */
    getAllLastSubjectCd("getAllLastSubjectCd", "20,22"),

    // FIXME: 2019/9/12 明细核对
    /**
     * 明细核对、明细核对导出
     */
    detailCheck("detailCheck", "20,23"),

    // FIXME: 2019/9/12 扎账查询
    /**
     * 扎账查询
     */
    settleAccountListInquiry("settleAccountListInquiry", "20,25"),

    // FIXME: 2019/9/12 清算科目试算
    /**
     * 清算科目试算查询
     */
    clearSubjectTriaListInquiry("clearSubjectTriaListInquiry", "20,24"),

    /**
     * 清算科目试算详情查询
     */
    clearSubjectTriaDetailListInquiry("clearSubjectTriaDetailListInquiry", "20,24"),

    // FIXME: 2019/9/12 当日会计明细查询
    /**
     * 当日会计分录查询、当日会计分录查询Excel
     */
    accountingRecordInquiryOntheDay("accountingRecordInquiryOntheDay", "26,27"),
    /**
     * 当日会计分录查询详情
     */
    accountingRecordInquiryDetailsOntheDay("accountingRecordInquiryDetailsOntheDay", "26,27"),
    /**
     * 历史会计分录查询、历史会计分录查询Excel
     */
    historicalAccountingRecordInquiry("historicalAccountingRecordInquiry", "26,28"),
    /**
     * 历史会计分录查询详情
     */
    historicalAccountingRecordInquiryDetails("historicalAccountingRecordInquiryDetails", "26,28"),
    // FIXME: 2019/9/12 科目明细查询
    /**
     * 历史科目明细表查询、历史根据科目层级和科目类型查询科目号
     */
    historicalSubjectListInquiry("historicalSubjectListInquiry", "26,29"),
    /**
     * 历史科目明细表查询详情
     */
    historicalSubjectListDetailInquiry("historicalSubjectListDetailInquiry", "26,29"),

    // FIXME: 2019/9/12 辅助余额表查询
    /**
     * 辅助核算余额查询、导出
     */
    vodtlAssSumQuery("vodtlAssSumQuery", "26,30"),

    // FIXME: 2019/9/12 辅助余额表查询
    /**
     * 科目对应辅助核算项
     */
    getAssist("getAssist", "26,30"),

    // FIXME: 2019/9/12 总账查询
    /**
     * 总账查询文件导出
     */
    legerQuery("legerQuery", "26,31"),
    // FIXME: 2019/9/12 每日计提明细查询
    /**
     * 每日提计明细查询
     */
    dailyAccrualsDetailQuery("dailyAccrualsDetailQuery", "26,32"),
    /**
     * 将下载每日计提文件的信息插入 POLLING_TASK 表
     */
    insertPollingTaskForDailyAccrual("insertPollingTaskForDailyAccrual", "26,32"),
    /**
     * 每日计提下载文件
     */
    dailyAccrualfileDownload("dailyAccrualfileDownload", "26,32"),
    // FIXME: 2019/9/12 综合记账查询
    /**
     * 综合记账查询
     */
    glTxnHstQuery("glTxnHstQuery", "26,33"),
    /**
     * 综合记账明细查询
     */
    glTxnHstDetailQuery("glTxnHstDetailQuery", "26,32"),
    // FIXME: 2019/9/12 新Trans报表接口
    /**
     * 获取Trans报表信息
     */
    getTrans("getTrans", "35,36"),
    /**
     * 获取Trans报表统计信息
     */
    getSum("getSum", "35,36"),
    insertPollingTaskForTrans("insertPollingTaskForTrans", "35,36"),
    /**
     * 下载trans报表
     */
    transFileDownload("transFileDownload", "35,36"),
    // FIXME: 2019/9/12 crf报表查询
    // 获取科目列表
    /**
     * 获取CRF报表的查询列表
     */
    findCrfReport("findCrfReport", "35,37"),
    /**
     * 导出CRF报表
     */
    exportCrfReport("exportCrfReport", "35,37"),

    // FIXME: 2019/9/12 参数维护历史查询
    /**
     * 参数维护历史信息查询
     */
    paraMaintenSearch("paraMaintenSearch", "41,42"),
    /**
     * 参数维护查询Excel
     */
    parameterMaintenanceExport("parameterMaintenanceExport", "41,42"),
    // FIXME: 2019/9/12 操作日志查询
    /**
     * 操作日志查询
     */
    operationalLogSearch("operationalLogSearch", "41,43"),
    /**
     * 操作日志查询Excel
     */
    operationalLogExport("operationalLogExport", "41,43"),
    // 记账复核
    /**
     * 记账复核查询
     */
    query("query", "44,45"),
    /**
     * 记账复核修改
     */
    update("update", "44,45"),
    // todo 系统参数复核
    /**
     * 查询参数维护复核数据
     */
    parameterCheckQuery("parameterCheckQuery", "44,46"),
    /**
     * 参数维护复核详情
     */
    parameterCheckDetail("parameterCheckDetail", "44,46"),
    /**
     * 参数维护复核同意
     */
    parameterCheckAssent("parameterCheckAssent", "44,46"),
    /**
     * 参数维护复核拒绝
     */
    parameterCheckRefuse("parameterCheckRefuse", "44,46"),
    // FIXME: 2019/9/12 表内记账
    // FIXME: 2019/9/12 表外记账
    /**
     * 表内外记账新增
     */
    glTxnOperate("glTxnOperate", "15,16,17"),
    /**
     * 表内外记账查询
     */
    getGlTxnOperateList("getGlTxnOperateList", "15,16,17"),

    /**
     * 表内外记账详情
     */
    getGlTxnOperateDetail("getGlTxnOperateDetail", "15,16,17"),
    /**
     * 表内外记账查询导出excel
     */
    glTxnOperateExcel("glTxnOperateExcel", "15,16,17"),
    // FIXME: 2019/9/12 损益结转
    /**
     * 损益查询
     */
    lossQuery("lossQuery", "15,18"),
    /**
     * 损益结转提交
     */
    profitAndLossSubmit("profitAndLossSubmit", "15,18"),
    /**
     * 根据记账流水号查询损益结转数据
     */
    /*getProfitInfo("getProfitInfo", "根据记账流水号查询损益结转数据"),*/

    // FIXME: 2019/9/12 总账调整
    /**
     * 总账调整查询
     */
    queryAllData("queryAllData", "15,19"),
    /**
     * 总账调整新增
     */
    addData("addData", "15,19"),

    /**
     *功能描述
     *
     * 总账查询
     */
    legerQueryOnTheDay("legerQueryOnTheDay","26,31"),


    /**
     * 总账调整详细查询
     */
    queryAllDataDetail("queryAllDataDetail", "15,19"),

    /**
     * 上传下载
     */
    uploadFile("upLoadFile", "47,48");

    private final String value;

    private final String label;

    MenuDef(String value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
