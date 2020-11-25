package net.engining.profile.sdk.service.db;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.support.db.querydsl.JPAFetchResponseBuilder;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileSecoperLogDto;
import net.engining.profile.entity.model.ProfileRole;
import net.engining.profile.entity.model.ProfileSecoperLog;
import net.engining.profile.entity.model.QProfileSecoperLog;
import net.engining.profile.enums.OperationType;
import net.engining.profile.sdk.service.bean.query.OperationLogPagingQuery;
import net.engining.profile.sdk.service.util.DtoTransformationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ProfileSecoperLog表操作服务
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/9 9:25
 * @since 1.0.0
 */
@Service
public class ProfileSecoperLogService {

    /**
     * 不需要展示的操作类型
     */
    private final List<OperationType> OUT_OF_RANGE = new ArrayList<>(Arrays.asList(OperationType.LG,
            OperationType.SC, OperationType.LO));

    /**
     * 实体服务
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 分页查询操作日志表
     *
     * @param query 查询参数
     * @return 查询结果
     */
    public FetchResponse<ProfileSecoperLogDto> listProfileSecoperLogDtoByPaging(OperationLogPagingQuery query) {
        QProfileSecoperLog qProfileSecoperLog = QProfileSecoperLog.profileSecoperLog;
        String puId = query.getPuId();
        BooleanExpression b1 = ValidateUtilExt.isNullOrEmpty(puId) ? null : qProfileSecoperLog.puId.eq(puId);
        BooleanExpression b2 = null;
        BooleanExpression b3 = null;
        Date startDate = query.getStartDate();
        if (ValidateUtilExt.isNotNullOrEmpty(startDate)) {
            b2 = qProfileSecoperLog.operTime.after(startDate);
            b3 = qProfileSecoperLog.operTime.before(query.getEndDate());
        }

        JPAQuery<ProfileSecoperLog> jpaQuery = new JPAQueryFactory(entityManager)
                .selectFrom(qProfileSecoperLog)
                .where(b1, b2, b3, qProfileSecoperLog.operType.notIn(OUT_OF_RANGE))
                .orderBy(qProfileSecoperLog.operTime.desc(), qProfileSecoperLog.logId.asc());
        FetchResponse<ProfileSecoperLog> fetchResponse = new JPAFetchResponseBuilder<ProfileSecoperLog>()
                .range(query.getRange())
                .build(jpaQuery);

        return DtoTransformationUtils.convertToPagingQueryResult(fetchResponse,
                source -> source.stream()
                        .map(ProfileSecoperLog::transform2Dto)
                        .collect(Collectors.toList()));
    }

}
