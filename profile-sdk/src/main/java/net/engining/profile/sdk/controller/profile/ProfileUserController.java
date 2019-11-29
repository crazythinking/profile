package net.engining.profile.sdk.controller.profile;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.engining.pg.support.core.context.Provider4Organization;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.WebCommonUtils;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.entity.model.ProfileSecoperLog;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.enums.OperationType;
import net.engining.profile.sdk.service.ProfileUserService;
import net.engining.profile.sdk.service.UserManagerBean;
import net.engining.profile.sdk.service.bean.profile.MgmWebUser;
import net.engining.profile.sdk.service.bean.profile.ProfileUserForm;
import net.engining.profile.sdk.service.bean.profile.UsersFilter;
import net.engining.profile.sdk.service.profile.UserService;
import net.engining.profile.security.ProfileSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author yangxing
 */
@RequestMapping("/proUser")
@RestController
@Api(value="ProfileUserController")
public class ProfileUserController {
	
	@Autowired
	private ProfileUserService profileUserService;
	
	@Autowired
	private ProfileSecurityService profileSecurityService;
	
	@Autowired
	private Provider4Organization provider4Organization;

	@Autowired
	private UserService userService;

    @PersistenceContext
    private EntityManager em;

//    @PreAuthorize("hasAuthority('Users')")
	@ApiOperation(value="根据机构和分部获取用户列表", notes="")
	@RequestMapping(value="/users", method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse getUsers(@RequestBody UsersFilter usersFilter) {
		FetchResponse<UserManagerBean> rsp = profileUserService.fetchUsers4Branch(usersFilter.getBranchId(),usersFilter.getName(), provider4Organization.getCurrentOrganizationId(), usersFilter.getRange());
		return new CommonWithHeaderResponseBuilder<Void, FetchResponse<UserManagerBean>>()
				.build()
				.setResponseData(rsp);
	}

//    @PreAuthorize("hasAuthority('Maintenance')")
	@ApiOperation(value="根据用户userId获取用户信息", notes="")
	@RequestMapping(value="/userId", method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse getUserByName(@RequestBody ProfileUserForm user, HttpServletRequest request) {
		FetchResponse<Map<String, Object>> rsp = profileUserService.getUserInfoByUserId(user.getUserId());
        Date date = new Date();
		Object puid =getPuid(user.getOperUserId());
		//userNo是操作员的puid，beOper是被操作员的登录id
		logSecuOperation(puid.toString(), OperationType.QU, WebCommonUtils.getIpAddress(request),date,user.getUserId());
		return new CommonWithHeaderResponseBuilder<Void,FetchResponse<Map<String, Object>>>().build()
				.setResponseData(rsp);
	}

	//	@PreAuthorize("hasAuthority('ProfileUser')")
	@ApiOperation(value="登录时根据用户userId获取用户信息", notes="")
	@RequestMapping(value="/loginUserId", method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse loginUserByName(@RequestBody ProfileUserForm user, HttpServletRequest request) {
		FetchResponse<Map<String, Object>> rsp = profileUserService.getUserInfoByUserId(user.getUserId());
		Date date = new Date();
		Object puid =getPuid(user.getOperUserId());
		//userNo是操作员的puid，beOper是被操作员的登录id
		logSecuOperation(puid.toString(), OperationType.LG, WebCommonUtils.getIpAddress(request),date,user.getUserId());
		return new CommonWithHeaderResponseBuilder<Void,FetchResponse<Map<String, Object>>>()
				.build()
				.setResponseData(rsp);
	}

//    @PreAuthorize("hasAuthority('PuId')")
	@ApiOperation(value="获取用户信息", notes="")
	@RequestMapping(value="/puId", method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse getUser(@RequestBody ProfileUserForm user, HttpServletRequest request) {
		ProfileUser profileUser = profileSecurityService.getUserInfo(user.getPuId());
		MgmWebUser mgmWebUser = userService.mgmWebUser(profileUser);
        Date date = new Date();
		Object puid =getPuid(user.getOperUserId());
        logSecuOperation(puid.toString(), OperationType.QU, WebCommonUtils.getIpAddress(request),date,user.getUserId());
		return new CommonWithHeaderResponseBuilder<Void,MgmWebUser>()
				.build()
				.setResponseData(mgmWebUser);
	}

    @PreAuthorize("hasAuthority('AddUser')")
	@ApiOperation(value="添加用户", notes="")
    @RequestMapping(value="/addUser", method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse addUser(@RequestBody MgmWebUser user, HttpServletRequest request) {
		userService.validateUser(user.getUserId());
		ProfileUser profileUser = userService.profileUserForm(user);
		Date date = new Date();
		Object puid =getPuid(user.getOperUserId());
		//userNo是操作员的puid，beOper是被操作员的登录id
		logSecuOperation(puid.toString(), OperationType.AD, WebCommonUtils.getIpAddress(request),date,user.getUserId());

		return new CommonWithHeaderResponseBuilder<Void,Void>().build();
		
	}


    @PreAuthorize("hasAuthority('RemoveUserByPuId')")
	@ApiOperation(value="删除某个用户", notes="")
    @RequestMapping(value="/removeUserByPuId", method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse removeUser(@RequestBody ProfileUserForm user, HttpServletRequest request) {
		List<String> usrs = new ArrayList<String>();
		usrs.add(user.getPuId());
		ProfileUser profileUserInfo = profileUserService.findProfileUserInfo(user.getPuId());
		profileUserService.deleteProfileUsers(usrs);
		String userId=profileUserInfo.getUserId();
		Date date = new Date();
		Object puid =getPuid(user.getOperUserId());
		//userNo是操作员的puid，beOper是被操作员的登录id
		logSecuOperation(puid.toString(), OperationType.DE, WebCommonUtils.getIpAddress(request),date,userId);
		return new CommonWithHeaderResponseBuilder<Void,Void>().build();
	}

    @PreAuthorize("hasAuthority('Maintenance')")
	@ApiOperation(value="删除多个用户", notes="")
    @RequestMapping(value="/removeUsers", method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse removeUser(@RequestBody String[] puIds) {
		List<String> usrs = Arrays.asList(puIds);
		profileUserService.deleteProfileUsers(usrs);
		return new CommonWithHeaderResponseBuilder<Void,Void>().build();
	}

    @PreAuthorize("hasAuthority('UpdateUser')")
	@ApiOperation(value="更新某个用户", notes="")
    @RequestMapping(value="/updateUser", method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse updateUser(@RequestBody ProfileUserForm user, HttpServletRequest request) {
		ProfileUser profileUser = userService.profileUser(user);
		Date date = new Date();
		Object puid =getPuid(user.getOperUserId());
		//userNo是操作员的puid，beOper是被操作员的登录id
		logSecuOperation(puid.toString(), OperationType.UP, WebCommonUtils.getIpAddress(request),date,user.getUserId());
		return new CommonWithHeaderResponseBuilder<Void,Void>().build();
	}

	@Transactional(rollbackFor = Exception.class)
	public void logSecuOperation(String userNo, OperationType operationType, String opIp, Date opTime, String beOper){
		ProfileSecoperLog ssCustSecoperLog = new ProfileSecoperLog();
		ssCustSecoperLog.setPuId(userNo);
		ssCustSecoperLog.setOperType(operationType);
		ssCustSecoperLog.setOperIp(opIp);
		ssCustSecoperLog.setOperTime(opTime);
		ssCustSecoperLog.setBeoperatedId(beOper);
		em.persist(ssCustSecoperLog);
	}

	public Object getPuid(String userId){
		FetchResponse<Map<String, Object>> rsp = profileUserService.getUserInfoByUserId(userId);
		List<Map<String, Object>> useData = rsp.getData();
		Map<String, Object> usetDataMap = useData.get(0);
		Object puid = usetDataMap.get("puId");
		return puid;
	}



}