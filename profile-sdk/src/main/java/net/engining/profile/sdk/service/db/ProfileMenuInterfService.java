package net.engining.profile.sdk.service.db;

import com.querydsl.jpa.impl.JPAQueryFactory;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileMenuInterfDto;
import net.engining.profile.entity.model.ProfileMenuInterf;
import net.engining.profile.entity.model.QProfileMenuInterf;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ProfileMenuInterf表操作服务
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/28 16:06
 * @since 1.0.0
 */
@Service
public class ProfileMenuInterfService {

    /**
     * 实体服务
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 根据接口ID和客户端ID查询接口权限
     *
     * @param interfIdList 接口ID
     * @param appCd 客户端ID
     * @return 查询结果
     */
    public List<ProfileMenuInterfDto>
    listProfileMenuInterfDtoByInterfIdListAndAppCd(List<String> interfIdList, String appCd) {
        QProfileMenuInterf qProfileMenuInterf = QProfileMenuInterf.profileMenuInterf;
        List<ProfileMenuInterf> list = new JPAQueryFactory(entityManager)
                .selectFrom(qProfileMenuInterf)
                .where(qProfileMenuInterf.interfCd.in(interfIdList), qProfileMenuInterf.appCd.eq(appCd))
                .fetch();
        return ValidateUtilExt.isNullOrEmpty(list) ? null
                : list.stream().map(ProfileMenuInterf::transform2Dto).collect(Collectors.toList());
    }

    /**
     * 根据客户端ID获取才接口
     *
     * @param appCd 客户端ID
     * @return 查询结果
     */
    public List<ProfileMenuInterfDto> listProfileMenuInterfDtoByAppCd(String appCd) {
        QProfileMenuInterf qProfileMenuInterf = QProfileMenuInterf.profileMenuInterf;
        List<ProfileMenuInterf> list = new JPAQueryFactory(entityManager)
                .selectFrom(qProfileMenuInterf)
                .where(qProfileMenuInterf.appCd.eq(appCd))
                .fetch();
        return ValidateUtilExt.isNullOrEmpty(list) ? null
                : list.stream().map(ProfileMenuInterf::transform2Dto).collect(Collectors.toList());
    }
}
