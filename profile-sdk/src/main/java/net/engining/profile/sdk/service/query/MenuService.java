package net.engining.profile.sdk.service.query;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.engining.pg.support.core.context.Provider4Organization;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.support.db.querydsl.JPAFetchResponseBuilder;
import net.engining.pg.support.dstruct.TreeNode;
import net.engining.profile.entity.model.QProfileMenu;
import net.engining.profile.entity.model.QProfileRoleAuth;
import net.engining.profile.entity.model.QProfileUser;
import net.engining.profile.entity.model.QProfileUserRole;
import net.engining.profile.sdk.service.bean.MenuBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Description 菜单service
 * @author heqingxi
 */
@Service
public class MenuService {

    private static final String Q3 = "*";

    private static final String Q4 = "-";

    private static final String Q5 = "svadmin";

    private static final String Q6 = "0";

    private static final String Q7 = "100";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    Provider4Organization provider4Organization;

    /**
     * 菜单查询
     *
     * @param
     * @return
     */
    public String getData(String userId) {
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

        Set<Integer> autuUris = Sets.newHashSet();
        //查询当前角色下所有的权限菜单
        List<Tuple> auths = new JPAQueryFactory(em)
                .select(auth.autuUri, auth.authority)
                .from(auth)
                .where(auth.roleId.in(fetchRoleIds))
                .fetch();

        for (Tuple tuple2 : auths) {
            String autuUri = tuple2.get(auth.autuUri);
            String[] valuest = StringUtils.splitPreserveAllTokens(autuUri, ",");
            for (String v : valuest) {
                if (!v.equals(Q3) && !v.equals(Q4)) {
                    autuUris.add(Integer.valueOf(v));
                }
            }
        }
        JPAQuery<Tuple> jpaQuery = null;
        if (userId.equals(Q5)) {
            jpaQuery = new JPAQueryFactory(em).select(profileMenu.mname, profileMenu.pathUrl,
                    profileMenu.parentId, profileMenu.sortn,
                    profileMenu.icon)
                    .from(profileMenu)
                    .orderBy(profileMenu.sortn.asc());
        }else{
            jpaQuery = new JPAQueryFactory(em)
                    .select(profileMenu.mname, profileMenu.pathUrl,
                            profileMenu.parentId, profileMenu.sortn, profileMenu.icon)
                    .from(profileMenu)
                    .where(profileMenu.sortn.in(autuUris))
                    .orderBy(profileMenu.sortn.asc());
        }
        FetchResponse<Tuple> build = new JPAFetchResponseBuilder<Tuple>().build(jpaQuery);
        //设置菜单树根
        List<TreeNode<String, MenuBean>> rootParentList = new ArrayList<>();
        //设置所有菜单树根
        List<MenuBean> rootAllList = new ArrayList<>();

        for (Tuple tuple2 : build.getData()) {
            MenuBean menuBean = new MenuBean();
            menuBean.setMenu(tuple2.get(profileMenu.mname));
            menuBean.setImgUrl(tuple2.get(profileMenu.pathUrl));
            menuBean.setParentId(tuple2.get(profileMenu.parentId).toString());
            menuBean.setHref(tuple2.get(profileMenu.icon));
            menuBean.setSortn(tuple2.get(profileMenu.sortn).toString());
            menuBean.setActive(false);
            rootAllList.add(menuBean);
        }
        //添加父节点
        rootAllList
                .stream()
                .filter(menuBean -> Q6.equals(menuBean.getParentId()))
                .forEach(menuBean -> {
                            TreeNode<String, MenuBean> treeNode = new TreeNode<>(menuBean.getMenu(),menuBean);
                            rootParentList.add(treeNode);
                        }
                );
        //为各个父节点添加子节点
        rootParentList.forEach(treeNode -> {
            getChild(treeNode,treeNode.getData().getSortn(), rootAllList);
        });

        return JSON.toJSONString(rootParentList);
    }

    /**
     * 递归设置父节点的子节点
     * @param parentTreeNode
     * @param id
     * @param allMenu
     */
    public void getChild(TreeNode<String, MenuBean> parentTreeNode, String id, List<MenuBean> allMenu) {

        //设置当前父节点的孩子
        for (MenuBean menuParentBean : allMenu) {
            if (menuParentBean.getParentId().equals(id)) {
                parentTreeNode.addChild(menuParentBean.getMenu(),menuParentBean);
            }
        }
        for (TreeNode<String, MenuBean> menu : parentTreeNode.getChildren()) {
            //递归添加当前父节点孩子的孩子
            getChild(menu,menu.getData().getSortn(), allMenu);
        }
    }


}