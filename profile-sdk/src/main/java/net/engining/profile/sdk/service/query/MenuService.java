package net.engining.profile.sdk.service.query;


import com.alibaba.fastjson.JSON;
import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.engining.pg.props.CommonProperties;
import net.engining.pg.support.core.context.Provider4Organization;
import net.engining.pg.support.dstruct.TreeNode;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.model.QProfileMenu;
import net.engining.profile.entity.model.QProfileRoleAuth;
import net.engining.profile.entity.model.QProfileUser;
import net.engining.profile.entity.model.QProfileUserRole;
import net.engining.profile.sdk.service.bean.MenuBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @Description 菜单service
 * @author heqingxi
 */
@Service
public class MenuService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final String Q3 = "*";

    private static final String Q4 = "-";

    private static final String Q5 = "admin";

    private static final String Q6 = "0";

    private static final String Q7 = "100";

    public static final String ALL_MENU_KEY = "allMenu";

    public static final String SPLIT = "|";

    /**
     * (userId|appName)   -   TreeNode<String, MenuBean>
     */
    private LoadingCache<String, TreeNode<String, MenuBean>> cache;
    /**
     * A time source
     */
    private Ticker ticker = Ticker.systemTicker();

    @PersistenceContext
    private EntityManager em;

    @Autowired
    Provider4Organization provider4Organization;

    @Autowired
    CommonProperties commonProperties;

    /**
     * 菜单查询
     *
     * @param
     * @return
     */
    public String getData(String userId) {

        TreeNode<String, MenuBean> treeParentNode = null;
        try {
            //从本地缓存获取
            treeParentNode = cache.get(StringUtils.join(userId,SPLIT,commonProperties.getAppname()));
        } catch (ExecutionException e) {
            treeParentNode = getMenuBeanTreeNode(userId);
        }
        return JSON.toJSONString(treeParentNode);
    }

    public String getAuthorityData() {

        TreeNode<String, MenuBean> treeParentNode = null;
        try {
            //从本地缓存获取
            treeParentNode = cache.get(StringUtils.join(ALL_MENU_KEY,SPLIT,commonProperties.getAppname()));
        } catch (ExecutionException e) {
            treeParentNode = getAllMenuTreeNode();
        }
        return JSON.toJSONString(treeParentNode);
    }


    /**
     * 处理生成所需菜单树
     * @param rootAllList 用户角色下所有菜单list
     * @return
     */
    private TreeNode<String, MenuBean> createTreeNode(List<MenuBean> rootAllList)
    {
        //添加父根节点
        TreeNode<String, MenuBean> treeParentNode = getParentNode(Q6,Q6);
        //为父根节点添加子节点
        getChild(treeParentNode,treeParentNode.getData().getId(), rootAllList);
        return treeParentNode;
    }
    /**
     * 生成主父菜单
     * @param key key
     * @param id 菜单id
     * @return
     */
    private TreeNode<String, MenuBean> getParentNode(String key, String id)
    {
        MenuBean parentMenu = new MenuBean();
        parentMenu.setMenu(key);
        parentMenu.setId(id);
        parentMenu.setActive(false);
        return new TreeNode<>(key, parentMenu);
    }

    /**
     * 递归设置父节点的子节点
     * @param parentTreeNode
     * @param id
     * @param allMenu
     */
    private void getChild(TreeNode<String, MenuBean> parentTreeNode, String id, List<MenuBean> allMenu) {

        //设置当前父节点的孩子
        for (MenuBean menuParentBean : allMenu) {
            if (menuParentBean.getParentId().equals(id)) {
                parentTreeNode.addChild(menuParentBean.getMenu(),menuParentBean);
            }
        }
        for (TreeNode<String, MenuBean> menu : parentTreeNode.getChildren()) {
            //递归添加当前父节点孩子的孩子
            getChild(menu,menu.getData().getId(), allMenu);
        }
    }

    private TreeNode<String, MenuBean> getMenuBeanTreeNode(String userId) {
        //菜单表
        QProfileMenu profileMenu = QProfileMenu.profileMenu;
        //权限表
        QProfileRoleAuth auth = QProfileRoleAuth.profileRoleAuth;
        //用户表
        QProfileUser profileUser = QProfileUser.profileUser;
        //用户角色表
        QProfileUserRole profileUserRole = QProfileUserRole.profileUserRole;

        //查询当前用户角色id
        List<String> fetchRoleIds = new JPAQueryFactory(em)
                .select(profileUserRole.roleId)
                .from(profileUser, profileUserRole)
                .where(
                        profileUser.userId.eq(userId),
                        profileUserRole.puId.eq(profileUser.puId)
                )
                .fetch();

        //查询当前角色下所有的权限菜单
        List<Tuple> auths = new JPAQueryFactory(em)
                .select(auth.autuUri, auth.authority)
                .from(auth)
                .where(auth.roleId.in(fetchRoleIds))
                .fetch();

        Set<String> authoritys = auths
                .stream()
                .map(tuple -> tuple.get(auth.authority))
                .collect(Collectors.toSet());

        List<Tuple> jpaQuery = null;
        if (userId.equals(Q5)) {
            jpaQuery = new JPAQueryFactory(em).select(profileMenu.id,profileMenu.mname, profileMenu.pathUrl,
                    profileMenu.parentId, profileMenu.sortn,
                    profileMenu.icon)
                    .from(profileMenu)
                    .orderBy(profileMenu.sortn.asc())
                    .fetch();
        }else{
            jpaQuery = new JPAQueryFactory(em)
                    .select(profileMenu.id,profileMenu.mname, profileMenu.pathUrl,
                            profileMenu.parentId, profileMenu.sortn, profileMenu.icon)
                    .from(profileMenu)
                    .where(profileMenu.menuCd.in(authoritys))
                    .orderBy(profileMenu.sortn.asc())
                    .fetch();
        }
        List<MenuBean> rootAllList = getMenuBeans(profileMenu, jpaQuery);
        return createTreeNode(rootAllList);
    }

    /**
     * 生成用户角色所有菜单List
     * @param profileMenu
     * @param jpaQuery
     * @return
     */
    private List<MenuBean> getMenuBeans(QProfileMenu profileMenu, List<Tuple> jpaQuery) {
        //所有菜单
        List<MenuBean> rootAllList = new ArrayList<>();

        for (Tuple tuple : jpaQuery) {
            MenuBean menuBean = new MenuBean();
            menuBean.setId(String.valueOf(tuple.get(profileMenu.id)));
            menuBean.setMenu(tuple.get(profileMenu.mname));
            menuBean.setImgUrl(tuple.get(profileMenu.pathUrl));
            menuBean.setParentId(String.valueOf(tuple.get(profileMenu.parentId)));
            menuBean.setHref(tuple.get(profileMenu.icon));
            menuBean.setSortn(String.valueOf(tuple.get(profileMenu.sortn)));
            menuBean.setActive(false);
            rootAllList.add(menuBean);
        }
        return rootAllList;
    }

    /**
     * 初始化用户权限菜单cache
     */
    public void initCache()
    {
        cache = CacheBuilder.newBuilder()
                .ticker(ticker)
                .build(new CacheLoader<String, TreeNode<String, MenuBean>>() {

                    @Override
                    public TreeNode<String, MenuBean> load(String key) throws Exception {
                        TreeNode<String, MenuBean> treeNode = null;
                        String[] userId = StringUtils.splitPreserveAllTokens(key,SPLIT);
                        String userIdKey = userId[0];
                        if (ValidateUtilExt.isNullOrEmpty(userIdKey)) {
                            return null;
                        }
                        //全部菜单
                        if (ALL_MENU_KEY.equals(userIdKey)) {
                            treeNode = getAllMenuTreeNode();
                        }else {
                            //用户菜单
                            treeNode = getMenuBeanTreeNode(userIdKey);
                        }
                        return treeNode;
                    }

                });
    }

    /**
     * 获得全部总的菜单树
     * @return
     */
    private TreeNode<String, MenuBean> getAllMenuTreeNode() {
        TreeNode<String, MenuBean> treeNode = null;
        QProfileMenu profileMenu = QProfileMenu.profileMenu;
        List<Tuple> jpaQuery = new JPAQueryFactory(em).select(profileMenu.id,profileMenu.mname,
                profileMenu.pathUrl,profileMenu.parentId,
                profileMenu.sortn, profileMenu.icon)
                .from(profileMenu)
                .orderBy(profileMenu.sortn.asc())
                .fetch();
        List<MenuBean> rootAllList = getMenuBeans(profileMenu, jpaQuery);
        treeNode = createTreeNode(rootAllList);
        return treeNode;
    }

    /**
     * 刷新用户权限菜单cache
     * @param key userId
     */
    public void refreshCache(String key)
    {
        cache.refresh(key);
    }

//    @Override
//    public void afterPropertiesSet() throws Exception {
//        initCache();
//    }

}