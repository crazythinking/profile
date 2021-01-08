package net.engining.profile.sdk.service.db;

import com.querydsl.jpa.impl.JPAQueryFactory;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileMenuDto;
import net.engining.profile.entity.model.ProfileMenu;
import net.engining.profile.entity.model.QProfileMenu;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ProfileMenu表操作服务
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/28 15:48
 * @since 1.0.0
 */
@Service
public class ProfileMenuService {

    /**
     * 实体服务
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 根据菜单ID和客户端ID查询菜单ID
     *
     * @param menuIdList 菜单ID
     * @param appCd 客户端ID
     * @return 查询结果
     */
    public List<String> listMenuIdByMenuIdListAndAppCd(List<String> menuIdList, String appCd) {
        QProfileMenu qProfileMenu = QProfileMenu.profileMenu;
        return new JPAQueryFactory(entityManager)
                .select(qProfileMenu.menuCd)
                .from(qProfileMenu)
                .where(qProfileMenu.menuCd.in(menuIdList), qProfileMenu.appCd.eq(appCd), qProfileMenu.delFlg.eq(false))
                .fetch();
    }

    /**
     * 根据客户端ID查询菜单
     *
     * @param appCd 客户端ID
     * @return 查询结果
     */
    public List<ProfileMenuDto> listProfileMenuDtoByAppCd(String appCd) {
        QProfileMenu qProfileMenu = QProfileMenu.profileMenu;
        List<ProfileMenu> profileMenuList = new JPAQueryFactory(entityManager)
                .selectFrom(qProfileMenu)
                .where(qProfileMenu.appCd.eq(appCd), qProfileMenu.delFlg.eq(false))
                .fetch();
        return ValidateUtilExt.isNullOrEmpty(profileMenuList) ? null
                : profileMenuList.stream().map(ProfileMenu::transform2Dto).collect(Collectors.toList());
    }
}
