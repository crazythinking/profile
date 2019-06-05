package net.engining.profile.enums;

import net.engining.pg.support.meta.EnumInfo;

/**
 * 系统预设权限ID
 * 
 * @author luxue
 *
 */
@EnumInfo({
	"ProfileUser|\u7528\u6237\u7ba1\u7406"
,	"ProfileBranch|\u5206\u652f\u7ba1\u7406"
,	"ProfileRole|\u89d2\u8272\u7ba1\u7406"
,	"insertQueryIntoPollingTast|Trans\u62a5\u8868\u6216CRF\u62a5\u8868\u751f\u6210Excel\u6587\u4ef6"
,	"fetchPollingTast|Trans\u62a5\u8868\u6216CRF\u62a5\u8868\u751f\u6210Excel\u8fdb\u5ea6\u67e5\u770b"
,	"fileDownload|Trans\u62a5\u8868\u6216CRF\u62a5\u8868Excel\u6587\u4ef6\u4e0b\u8f7d"
,	"accountingRecordInquiryOntheDay|\u5f53\u65e5\u4f1a\u8ba1\u5206\u5f55\u67e5\u8be2"
,	"accountingRecordInquiryDetailsOntheDay|\u5f53\u65e5\u4f1a\u8ba1\u5206\u5f55\u67e5\u8be2\u8be6\u60c5"
,	"historicalAccountingRecordInquiry|Excel\u6587\u4ef6\u4e0b\u8f7d"
,	"historicalAccountingRecordInquiryDetails|\u5386\u53f2\u4f1a\u8ba1\u5206\u5f55\u67e5\u8be2\u8be6\u60c5"
,	"subjectListInquiryOnTheDay|\u5f53\u65e5\u79d1\u76ee\u660e\u7ec6\u8868\u67e5\u8be2"
,	"subjectListInquiryDetailOnTheDay|\u5f53\u65e5\u79d1\u76ee\u660e\u7ec6\u8868\u67e5\u8be2\u8be6\u60c5"
,	"historicalSubjectListInquiry|\u5386\u53f2\u79d1\u76ee\u660e\u7ec6\u8868\u67e5\u8be2"
,	"historicalSubjectListDetailInquiry|\u5386\u53f2\u79d1\u76ee\u660e\u7ec6\u8868\u67e5\u8be2\u8be6\u60c5"
,	"legerQueryOnTheDay|\u5f53\u65e5\u603b\u8d26\u67e5\u8be2"
,	"historicalLegerQuery|\u5386\u53f2\u603b\u5e10\u67e5\u8be2"
,	"transOprQuery|\u8bb0\u8d26\u590d\u6838\u67e5\u8be2"
,	"transOprDetailQuery|\u8bb0\u8d26\u590d\u6838\u660e\u7ec6\u67e5\u8be2"
,	"transOprUpdate|\u8bb0\u8d26\u590d\u6838\u63d0\u4ea4\u3001\u62d2\u7edd"
,	"glTxnOperate|\u8868\u5185\u5916\u65b0\u589e"
,	"glTxnHstQuery|\u7efc\u5408\u8bb0\u8d26\u67e5\u8be2"
,	"vodtlAssSumQuery|\u5f53\u65e5\u8f85\u52a9\u6838\u7b97\u4f59\u989d\u67e5\u8be2"
,	"vodtlAssSumDetailQuery|\u5f53\u65e5\u8f85\u52a9\u6838\u7b97\u4f59\u989d\u660e\u7ec6\u67e5\u8be2"
,	"vodtlAssSumHstQuery|\u5386\u53f2\u8f85\u52a9\u6838\u7b97\u4f59\u989d\u67e5\u8be2"
,	"vodtlAssSumHstDetailQuery|\u5386\u53f2\u8f85\u52a9\u6838\u7b97\u4f59\u989d\u660e\u7ec6\u67e5\u8be2"
,	"getAssist|科目对应辅助核算项"
,	"tradeType|获取交易类型"
})
public enum SecurityAppAuthority {
	
	/**
	 * 用户管理
	 */
	ProfileUser,

	/**
	 * 分支管理
	 */
	ProfileBranch,

	/**
	 * 角色管理
	 */
	ProfileRole,

	/**
	 * Trans报表或CRF报表生成Excel文件
	 */
	insertQueryIntoPollingTast,

	/**
	 * Trans报表或CRF报表生成Excel进度查看
	 */
	fetchPollingTast,

	/**
	 * Trans报表或CRF报表Excel文件下载
	 */
	fileDownload,
	
	subjectList,

	/**
	 * 当日会计分录查询
	 */
	accountingRecordInquiryOntheDay,
	/**
	 * 当日会计分录查询详情
	 */
	accountingRecordInquiryDetailsOntheDay,
	/**
	 * 历史会计分录查询
	 */
	historicalAccountingRecordInquiry,
	/**
	 * 历史会计分录查询详情
	 */
	historicalAccountingRecordInquiryDetails,
	/**
	 * 当日科目明细表查询
	 */
	subjectListInquiryOnTheDay,
	/**
	 * 当日科目明细表查询详情
	 */
	subjectListInquiryDetailOnTheDay,
	/**
	 * 历史科目明细表查询
	 */
	historicalSubjectListInquiry,
	/**
	 * 历史科目明细表查询详情
	 */ 
	historicalSubjectListDetailInquiry,
	/**
	 * 当日总账查询
	 */
	legerQueryOnTheDay,
	/**
	 * 历史总帐查询
	 */
	historicalLegerQuery,
	
	
	
	
	
	/**
	 * 记账复核查询
	 */
	transOprQuery,
	/**
	 * 记账复核明细查询
	 */
	transOprDetailQuery,
	/**
	 *  记账复核提交、拒绝
	 */
	transOprUpdate,
	/**
	 * 表内外新增
	 */
	glTxnOperate,
	/**
	 * 综合记账查询
	 */
	glTxnHstQuery,
	/**
	 * 综合记账明细查询
	 */
	glTxnHstDetailQuery,
	/**
	 * 当日辅助核算余额查询
	 */
	vodtlAssSumQuery,
	/**
	 * 当日辅助核算余额明细查询
	 */
	vodtlAssSumDetailQuery,
	/**
	 * 历史辅助核算余额查询
	 */
	vodtlAssSumHstQuery,
	/**
	 * 历史辅助核算余额明细查询
	 */
	vodtlAssSumHstDetailQuery,
	
	/**
	 * 科目对应辅助核算项
	 */
	getAssist,
	/**
	 * 获取交类型
	 */
	tradeType,
	
	
	
	
	
	
	
	/**
	 * 试算平衡
	 */
	trialBanlance,
	/**
	 * 总分核对
	 */
	totalScoreCheck,
	/**
	 * 明细核对
	 */
	detailCheck,
	/**
	 * 损益结转
	 */
	profitAndLoss,
	/**
	 * 损益结转提交
	 */
	profitAndLossSubmit,
	/**
	 * 获取交易类型,当日记账明细查询
	 */
	accountDetailedInquiry,
	/**
	 * 当日记账明细查询详情
	 */
	accountDetailedInquiryList,
	/**
	 * 历史记账明细查询
	 */
	historicalAccountDetailedInquiry,
	/**
	 * 历史记账明细查询详情
	 */
	historicalAccountDetailedInquiryList,
	
	
	
	
	
	/**
	 * 会计科目查询
	 */
	subjectRecord,
	/**
	 * 会计科目查询详情
	 */
	subjectRecordInquiryDetails,
	/**
	 * 会计科目新增
	 */
	subjectRecordInsert,
	/**
	 * 价税分离查询详情
	 */
	taxRateRecordInquiryDetails,
	/**
	 * 价税分离更新
	 */
	taxRateRecordUpdate,
	/**
	 * 会计分录查询
	 */
	txnSubjectParamRecord,
	/**
	 * 参数维护历史查询
	 */
	parameterAuditRecord,
	/**
	 * 会计分录添加
	 */
	txnSubjectParamInsert,
	/**
	 * 入账交易码添加
	 */
	postCodeInsert,
	/**
	 * 单个入账交易码添加
	 */
	postCodeInsertOne,
	/**
	 * 直接记账会计分录查询
	 */
	directAccountRecord,
	/**
	 * 直接记账会计分录新增
	 */
	directAccountInsert,
	/**
	 * 会计科目查询Excel
	 */
	subjectRecordExportExcel,
	/**
	 * 参数维护历史查询Excel
	 */
	parameterAuditRecordExportExcel
}
