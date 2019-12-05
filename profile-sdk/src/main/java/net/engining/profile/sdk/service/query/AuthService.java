package net.engining.profile.sdk.service.query;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.engining.pg.support.core.context.Provider4Organization;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.db.DbConstants;
import net.engining.pg.support.dstruct.TreeNode;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.model.*;
import net.engining.profile.sdk.service.bean.MenuOrAuthBean;
import net.engining.profile.sdk.service.bean.profile.MenuOrAuthInfo;
import net.engining.profile.config.props.ProfileParamProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @Description 菜单权限相关service
 * RoleId全局不可重复，MenuCd，Authority在同App_Cd范围内 不可重复；
 * ProfileParamProperties判断是否使用了授权中心
 * oauth中心模式缓存的map中有多颗appcd对应的树
 * 本地profile模式缓存的map中只有一颗树，且map key为"-",即DbConstants.NULL
 * @author sangchunhua
 */
@Service
public class AuthService implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(getClass());
    /**
     * 根级菜单编号
     */
    private static final String ROOT_MENU_ID = "0";
    /**
     * 全部权限树的cache key
     */
    public static final String ALL_AUTH_KEY = "allAuth";
    /**
     * (roleId)   -   Map<appcd,TreeNode<String, MenuOrAuthBean>>
     */
    private LoadingCache<String, List<String>> roleAuthCache;
    /**
     * 没把全部用户的菜单树放缓存是因为，用户过多占太多内存
     * (userId)   -   Map<appcd,TreeNode<String, MenuOrAuthBean>>
     */
    private LoadingCache<String, Map<String,TreeNode<String, MenuOrAuthBean>>> userMenuCache;
    /**
     * (allAuth)   -   Map<appcd,TreeNode<String, MenuOrAuthBean>>
     */
    private LoadingCache<String, Map<String,TreeNode<String, MenuOrAuthBean>>> allAuthCache;
    /**
     * A time source
     */
    private Ticker ticker = Ticker.systemTicker();

    @PersistenceContext
    private EntityManager em;

    @Autowired
    Provider4Organization provider4Organization;

    @Autowired
    ProfileParamProperties profileParamProperties;

    /**
     * 菜单树查询
     * @param userId 用户id
     * @param appCd 应用代码
     * @return
     */
    public String getMenuData(String userId, String appCd) {
        //检查appcd
        boolean isAuth = checkAppCd(appCd);

        Map<String,TreeNode<String, MenuOrAuthBean>> treeParentNode = null;
        try {
            //从本地缓存获取
            treeParentNode = userMenuCache.get(StringUtils.join(userId));
        } catch (ExecutionException e) {
            treeParentNode = getMenuTreeByUserid(userId);
        }
        //转换树为jsonStr
        String treeJsonStr = getTreeJsonString(appCd, isAuth, treeParentNode);

        return treeJsonStr;
    }

    /**
     * 获得全部的菜单接口权限树(不分Roleid)
     * @param appCd 应用代码
     * @return
     */
    public String getAuthorityData(String appCd) {
        //检查appcd
        boolean isAuth = checkAppCd(appCd);

        Map<String,TreeNode<String, MenuOrAuthBean>> treeParentNode = null;
        try {
            //从本地缓存获取
            treeParentNode = allAuthCache.get(StringUtils.join(ALL_AUTH_KEY));
        } catch (ExecutionException e) {
            treeParentNode = getAllAuthTreeNode();
        }

        //转换树为jsonStr
        String treeJsonStr = getTreeJsonString(appCd, isAuth, treeParentNode);

        return treeJsonStr;
    }

    /**
     * 角色权限分配
     *
     * @param roleId 角色id
     * @param authInfoList 权限集合
     */
    @Transactional(rollbackFor = Exception.class)
    public void distributionProfileRole(String roleId, List<MenuOrAuthInfo> authInfoList) {
        // 如果对应的权限存在就进行删除然后在进行添加操作
        QProfileRoleAuth qProfileRoleAuth = QProfileRoleAuth.profileRoleAuth;
        long n2 = new JPAQueryFactory(em)
                .delete(qProfileRoleAuth)
                .where(qProfileRoleAuth.roleId.eq(roleId))
                .execute();
        log.debug("删除了{}条ProfileRoleAuth", n2);
        //添加权限
        authInfoList.forEach(authInfo -> {
            ProfileRoleAuth profileRoleAuth = new ProfileRoleAuth();
            profileRoleAuth.fillDefaultValues();
            profileRoleAuth.setRoleId(roleId);
            profileRoleAuth.setAuthority(authInfo.getAuthority());
            profileRoleAuth.setAutuUri(authInfo.getAutuUri());

            em.persist(profileRoleAuth);
        });

        //刷新角色已有权限缓存
        this.refreshRoleAuthCacheByRoleId(roleId);
        //刷新用户菜单权限缓存
        this.refreshUserMenuCacheByRoleId(roleId);
    }

    /**
     * 获取角色对应的权限主方法
     * @param roleId
     * @return
     */
    public List<String> fetchRoleAuthByRoleId(String roleId) {
        List<String> authList = null;
        try {
            //从本地缓存获取
            authList = roleAuthCache.get(StringUtils.join(roleId));
        } catch (ExecutionException e) {
            authList = getRoleAuthByRoleId(roleId);
        }

        return authList;
    }

    /**
     * 获取角色对应的权限
     */
    public List<String> getRoleAuthByRoleId(String roleId) {
        QProfileRoleAuth qProfileBranch = QProfileRoleAuth.profileRoleAuth;
        List<String> authList = new JPAQueryFactory(em)
                .select(qProfileBranch.authority)
                .from(qProfileBranch)
                .where(qProfileBranch.roleId.eq(roleId))
                .fetch();
        return authList;
    }

    /**
     * 校验appcd在auth中心模式下不能为空
     * @param appCd
     * @return
     */
    public boolean checkAppCd(String appCd) {
        //是否远程auth
        boolean isAuth = profileParamProperties.isAuthEnabled();
        if (isAuth && ValidateUtilExt.isNullOrEmpty(appCd)){
            throw new ErrorMessageException(ErrorCode.CheckError,"appCd不能为空！");
        }
        return isAuth;
    }

    /**
     * 转换树的jsonString
     * @param appCd
     * @param isAuth
     * @param treeParentNode
     * @return
     */
    private String getTreeJsonString(String appCd, boolean isAuth, Map<String, TreeNode<String, MenuOrAuthBean>> treeParentNode) {
        String treeJsonStr = null;
        if (isAuth){
            //auth中心时为多颗appcd对应的树
            treeJsonStr = JSON.toJSONString(treeParentNode.get(appCd));
        }else {
            //本地时map中只存了一棵树
            treeJsonStr = JSON.toJSONString(treeParentNode.get(DbConstants.NULL));
        }
        return treeJsonStr;
    }

    /**
     * 处理生成所需菜单树
     * @param rootAllList 用户角色下所有菜单list
     * @return
     */
    private TreeNode<String, MenuOrAuthBean> createTreeNode(List<MenuOrAuthBean> rootAllList)
    {
        //添加父根节点
        TreeNode<String, MenuOrAuthBean> treeParentNode = getParentNode(ROOT_MENU_ID,ROOT_MENU_ID);
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
    private TreeNode<String, MenuOrAuthBean> getParentNode(String key, String id)
    {
        MenuOrAuthBean parentMenu = new MenuOrAuthBean();
        parentMenu.setCd(key);
        parentMenu.setId(id);
        return new TreeNode<>(key, parentMenu);
    }

    /**
     * 递归设置父节点的子节点
     * @param parentTreeNode
     * @param id
     * @param allMenu
     */
    private void getChild(TreeNode<String, MenuOrAuthBean> parentTreeNode, String id, List<MenuOrAuthBean> allMenu) {

        //设置当前父节点的孩子
        for (MenuOrAuthBean menuParentBean : allMenu) {
            if (menuParentBean.getParentId().equals(id)) {
                parentTreeNode.addChild(menuParentBean.getCd(),menuParentBean);
            }
        }
        for (TreeNode<String, MenuOrAuthBean> menu : parentTreeNode.getChildren()) {
            //递归添加当前父节点孩子的孩子
            getChild(menu,menu.getData().getId(), allMenu);
        }
    }

    /**
     * 获得用户的菜单树主方法
     * @param userId
     * @return
     */
    private Map<String,TreeNode<String, MenuOrAuthBean>> getMenuTreeByUserid(String userId) {
        //菜单表
        QProfileMenu profileMenu = QProfileMenu.profileMenu;
        //权限表
        QProfileRoleAuth auth = QProfileRoleAuth.profileRoleAuth;
        //用户表
        QProfileUser profileUser = QProfileUser.profileUser;
        //用户角色表
        QProfileUserRole profileUserRole = QProfileUserRole.profileUserRole;

        //查询当前用户的角色下所有的权限菜单，autuUri为"-"
        List<Tuple> auths = new JPAQueryFactory(em)
                .select(auth.autuUri, auth.authority)
                .from(auth, profileUser, profileUserRole)
                .where(
                        profileUserRole.puId.eq(profileUser.puId),
                        auth.roleId.eq(profileUserRole.roleId),
                        profileUser.userId.eq(userId),
                        auth.autuUri.eq(DbConstants.NULL)
                )
                .fetch();
        //去重权限
        Set<String> authoritys = auths
                .stream()
                .map(tuple -> tuple.get(auth.authority))
                .collect(Collectors.toSet());

        List<Tuple> jpaQuery = new JPAQueryFactory(em)
                .select(profileMenu.id,profileMenu.mname, profileMenu.menuCd,
                        profileMenu.parentId, profileMenu.sortn, profileMenu.appCd)
                .from(profileMenu)
                .where(profileMenu.menuCd.in(authoritys))
                .orderBy(profileMenu.sortn.asc())
                .fetch();
        List<MenuOrAuthBean> rootAllList = getMenuBeans(profileMenu, jpaQuery);

        Map<String,TreeNode<String, MenuOrAuthBean>> map = null;
        //远程oauth中心
        if (profileParamProperties.isAuthEnabled()) {
            map = createMenuTreeNodeMapWithAuth(rootAllList);
        }else {
            //本地local
            map = createMenuTreeNodeMapWithlocal(rootAllList);
        }
        return map;
    }

    /**
     * 获得用户userid下的菜单树（oauth中心模式）
     * 按照app_cd分组菜单树，map中多棵树
     * @param rootAllList
     * @return
     */
    private Map<String, TreeNode<String, MenuOrAuthBean>> createMenuTreeNodeMapWithAuth(List<MenuOrAuthBean> rootAllList) {
        //按照app_cd分组菜单树
        Map<String, TreeNode<String, MenuOrAuthBean>> treeNodeMap = Maps.newHashMap();
        Set<String> appCdSet = rootAllList.stream().map(MenuOrAuthBean::getCd).collect(Collectors.toSet());
        for (String cd : appCdSet) {
            List<MenuOrAuthBean> cdMenuOrAuthList = new ArrayList<>();
            for (MenuOrAuthBean menuOrAuthBean : rootAllList) {
                if (cd.equals(menuOrAuthBean.getCd())) {
                    cdMenuOrAuthList.add(menuOrAuthBean);
                }
            }
            TreeNode<String, MenuOrAuthBean> treeNode = createTreeNode(cdMenuOrAuthList);
            treeNodeMap.put(cd, treeNode);
        }
        return treeNodeMap;
    }

    /**
     * 获得用户userid下的菜单树（本地profile模式）
     * map中只有一颗树
     * @param rootAllList
     * @return
     */
    private Map<String, TreeNode<String, MenuOrAuthBean>> createMenuTreeNodeMapWithlocal(List<MenuOrAuthBean> rootAllList) {
        //按照app_cd分组菜单树
        Map<String, TreeNode<String, MenuOrAuthBean>> treeNodeMap = Maps.newHashMap();
        TreeNode<String, MenuOrAuthBean> treeNode = createTreeNode(rootAllList);
        //本地profile模式map中key为"-",即DbConstants.NULL，便于统一修改
        treeNodeMap.put(DbConstants.NULL, treeNode);
        return treeNodeMap;
    }

    /**
     * 生成所有菜单List
     * @param profileMenu
     * @param jpaQuery
     * @return
     */
    private List<MenuOrAuthBean> getMenuBeans(QProfileMenu profileMenu, List<Tuple> jpaQuery) {
        //所有菜单
        List<MenuOrAuthBean> rootAllList = new ArrayList<>();

        for (Tuple tuple : jpaQuery) {
            MenuOrAuthBean MenuOrAuthBean = new MenuOrAuthBean();
            MenuOrAuthBean.setId(String.valueOf(tuple.get(profileMenu.id)));
            MenuOrAuthBean.setCd(tuple.get(profileMenu.menuCd));
            MenuOrAuthBean.setName(tuple.get(profileMenu.mname));
            MenuOrAuthBean.setAutuUri(DbConstants.NULL);
            MenuOrAuthBean.setParentId(String.valueOf(tuple.get(profileMenu.parentId)));
            MenuOrAuthBean.setSortn(String.valueOf(tuple.get(profileMenu.sortn)));
            rootAllList.add(MenuOrAuthBean);
        }
        return rootAllList;
    }

    /**
     * 生成所有权限接口List
     * @param profileMenuInterf
     * @param jpaQuery
     * @return
     */
    private List<MenuOrAuthBean> getAuthBeans(QProfileMenuInterf profileMenuInterf, List<Tuple> jpaQuery) {
        //所有菜单
        List<MenuOrAuthBean> rootAllList = new ArrayList<>();

        for (Tuple tuple : jpaQuery) {
            MenuOrAuthBean MenuOrAuthBean = new MenuOrAuthBean();
            MenuOrAuthBean.setId(String.valueOf(tuple.get(profileMenuInterf.id)));
            MenuOrAuthBean.setCd(tuple.get(profileMenuInterf.interfCd));
            MenuOrAuthBean.setName(tuple.get(profileMenuInterf.iname));
            MenuOrAuthBean.setAutuUri(String.valueOf(tuple.get(profileMenuInterf.menuId)));
            MenuOrAuthBean.setParentId(String.valueOf(tuple.get(profileMenuInterf.menuId)));
            MenuOrAuthBean.setSortn("0");
            rootAllList.add(MenuOrAuthBean);
        }
        return rootAllList;
    }

    /**
     * 获得全部总的菜单树(oauth模式)
     * 多颗appcd的树
     * @return
     */
    private Map<String,TreeNode<String, MenuOrAuthBean>> getAllMenuTreeNodeWithAuth() {
        List<MenuOrAuthBean> rootAllList = getAllMenuBeans();
        //按照app_cd分组菜单树
        Map<String, TreeNode<String, MenuOrAuthBean>> treeNodeMap = createMenuTreeNodeMapWithAuth(rootAllList);
        return treeNodeMap;
    }

    /**
     * 获得全部总的菜单树（local模式）
     * 只有一棵树
     * @return
     */
    private TreeNode<String, MenuOrAuthBean> getAllMenuTreeNodeWithLocal() {
        List<MenuOrAuthBean> rootAllList = getAllMenuBeans();
        TreeNode<String, MenuOrAuthBean> treeNode = createTreeNode(rootAllList);
        return treeNode;
    }

    /**
     * 根据总的菜单树挂载接口权限，生成总的菜单权限树(oauth中心模式)
     * @return Map<String,TreeNode<String, MenuOrAuthBean>>
     *     String：AppCd
     *     TreeNode<String, MenuOrAuthBean>:AppCd对应的总菜单权限树
     */
    private Map<String,TreeNode<String, MenuOrAuthBean>> createAllAuthTreeNodeWithAuth(){
        //获取总的菜单树
        Map<String, TreeNode<String, MenuOrAuthBean>> treeNodeMap = getAllMenuTreeNodeWithAuth();
        //获得全部接口(全部appcd)表接口
        List<MenuOrAuthBean> interAllList = getAllInterBeans();
        //挂载接口权限到各appcd对应菜单树
        for (String cd : treeNodeMap.keySet()) {
            //只挂载当前appcd下的接口
            List<MenuOrAuthBean> currentAuthList = interAllList
                    .stream()
                    .filter(menuOrAuthBean -> cd.equals(menuOrAuthBean.getAppCd()))
                    .collect(Collectors.toList());
            TreeNode<String, MenuOrAuthBean> currentCdTree = treeNodeMap.get(cd);
            getChild(currentCdTree,currentCdTree.getData().getId(),currentAuthList);
        }

        return treeNodeMap;
    }

    /**
     * 根据总的菜单树挂载接口权限，生成总的菜单权限树(本地profile中心模式)
     * @return Map<String,TreeNode<String, MenuOrAuthBean>>
     *     String：AppCd
     *     TreeNode<String, MenuOrAuthBean>:AppCd对应的总菜单权限树
     */
    private Map<String,TreeNode<String, MenuOrAuthBean>> createAllAuthTreeNodeWithLocal(){
        //获取总的菜单树
        TreeNode<String, MenuOrAuthBean> treeNode = getAllMenuTreeNodeWithLocal();
        //获得全部接口(全部appcd)表接口
        List<MenuOrAuthBean> interAllList = getAllInterBeans();
        //挂载接口权限到菜单树
        getChild(treeNode,treeNode.getData().getId(),interAllList);
        Map<String,TreeNode<String, MenuOrAuthBean>> treeNodeMap = Maps.newHashMap();
        treeNodeMap.put(DbConstants.NULL,treeNode);
        return treeNodeMap;
    }

    /**
     * 获得全部权限树主方法
     * @return
     */
    private Map<String,TreeNode<String, MenuOrAuthBean>> getAllAuthTreeNode()
    {
        Map<String,TreeNode<String, MenuOrAuthBean>> map = null;
        //远程auth中心
        if (profileParamProperties.isAuthEnabled()) {
            map = createAllAuthTreeNodeWithAuth();
        }else {
            //本地local
            map = createAllAuthTreeNodeWithLocal();
        }
        return map;
    }

    /**
     * 获得全部menu类
     * @return
     */
    private List<MenuOrAuthBean> getAllMenuBeans() {
        //菜单表
        QProfileMenu profileMenu = QProfileMenu.profileMenu;
        List<Tuple> jpaQuery = new JPAQueryFactory(em).select(profileMenu.id,profileMenu.mname,
                profileMenu.menuCd,profileMenu.parentId,
                profileMenu.sortn, profileMenu.appCd)
                .from(profileMenu)
                .orderBy(profileMenu.sortn.asc())
                .fetch();
        return getMenuBeans(profileMenu, jpaQuery);
    }

    /**
     * 获得全部接口类
     * @return
     */
    private List<MenuOrAuthBean> getAllInterBeans() {
        //接口表
        QProfileMenuInterf profileMenuInterf = QProfileMenuInterf.profileMenuInterf;
        List<Tuple> jpaQuery = new JPAQueryFactory(em).select(profileMenuInterf.id,
                profileMenuInterf.appCd, profileMenuInterf.interfCd,profileMenuInterf.menuId,
                profileMenuInterf.iname)
                .from(profileMenuInterf)
                .where(profileMenuInterf.menuId.isNotNull())
                .fetch();
        return getAuthBeans(profileMenuInterf, jpaQuery);
    }

    /**
     * 初始化全部菜单权限分配cache
     * 同时对本地模式无appcd支持
     */
    public void initAllAuthTreeCache()
    {
        allAuthCache = CacheBuilder.newBuilder()
                .ticker(ticker)
                //最大条数
                .maximumSize(1)
                .build(new CacheLoader<String, Map<String,TreeNode<String, MenuOrAuthBean>>>() {

                    @Override
                    public Map<String,TreeNode<String, MenuOrAuthBean>> load(String key) throws Exception {
                        Map<String,TreeNode<String, MenuOrAuthBean>> treeNodeMap = null;
                        if (ValidateUtilExt.isNullOrEmpty(key)) {
                            return null;
                        }
                        treeNodeMap = getAllAuthTreeNode();
                        return treeNodeMap;
                    }

                });
    }

    /**
     * 初始化用户权限菜单cache
     * 同时对本地模式无appcd支持
     */
    public void initUserMenuTreeCache()
    {
        userMenuCache = CacheBuilder.newBuilder()
                .ticker(ticker)
                //当缓存项在指定的时间段内没有被读或写就会被回收
                .expireAfterAccess(Duration.ofDays(1))
                //最大条数
                .maximumSize(100)
                .build(new CacheLoader<String, Map<String,TreeNode<String, MenuOrAuthBean>>>() {

                    @Override
                    public Map<String,TreeNode<String, MenuOrAuthBean>> load(String key) throws Exception {
                        Map<String,TreeNode<String, MenuOrAuthBean>> treeNodeMap = null;
                        if (ValidateUtilExt.isNullOrEmpty(key)) {
                            return null;
                        }
                        treeNodeMap = getMenuTreeByUserid(key);
                        return treeNodeMap;
                    }

                });
    }

    /**
     * 初始化角色已有权限cache
     * 同时对本地模式无appcd支持
     * RoleAuth|roleid
     */
    public void initRoleAuthCache()
    {
        roleAuthCache = CacheBuilder.newBuilder()
                .ticker(ticker)
                //当缓存项在指定的时间段内没有被读或写就会被回收
                .expireAfterAccess(Duration.ofDays(1))
                //最大条数
                .maximumSize(100)
                .build(new CacheLoader<String, List<String>>() {

                    @Override
                    public List<String> load(String key) throws Exception {
                        List<String> authList = null;
                        if (ValidateUtilExt.isNullOrEmpty(key)) {
                            return null;
                        }
                        authList = getRoleAuthByRoleId(key);
                        return authList;
                    }

                });
    }

    /**
     * 刷新角色已有权限cache
     * 需要在分配角色权限后刷新缓存
     * @param roleId roleId
     */
    public void refreshRoleAuthCacheByRoleId(String roleId)
    {
        roleAuthCache.refresh(roleId);
    }

    /**
     * 刷新全部权限菜单cache
     * @param key allAuth
     */
    public void refreshAllAuthCache(String key)
    {
        allAuthCache.refresh(key);
    }

    /**
     * 刷新用户权限菜单cache
     * @param userId userId
     */
    public void refreshUserMenuCacheByUserId(String userId)
    {
        userMenuCache.refresh(userId);
    }

    /**
     * 刷新用户权限菜单cache
     * 需要在分配角色权限后重新刷新缓存
     * @param roleId roleId
     */
    public void refreshUserMenuCacheByRoleId(String roleId)
    {
        //通过roleId查出userid进行缓存刷新
        //用户角色表
        QProfileUserRole profileUserRole = QProfileUserRole.profileUserRole;
        //用户表
        QProfileUser profileUser = QProfileUser.profileUser;
        List<String> userIds = new JPAQueryFactory(em)
                .select(profileUser.userId)
                .from(profileUser, profileUserRole)
                .where(profileUser.puId.eq(profileUserRole.puId),
                        profileUserRole.roleId.eq(roleId))
                .fetch();
        //刷新userMenuCache缓存
        userIds.stream()
                .distinct()
                .forEach(this::refreshUserMenuCacheByUserId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("初始化菜单树缓存cache...");
        //全部appcd权限树
        initAllAuthTreeCache();
        //用户菜单树
        initUserMenuTreeCache();
        //触发缓存
        Map<String,TreeNode<String, MenuOrAuthBean>> treeParentNode = allAuthCache.get(StringUtils.join(ALL_AUTH_KEY));
        log.debug("菜单树缓存cache加载完成...");
    }
}