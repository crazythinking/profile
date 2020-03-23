package net.engining.profile.api.controller.profile;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.engining.pg.support.core.context.Provider4Organization;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.WebCommonUtils;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.entity.model.ProfileUser;
import net.engining.profile.enums.OperationType;
import net.engining.profile.sdk.service.ProfileUserService;
import net.engining.profile.sdk.service.UserManagerBean;
import net.engining.profile.sdk.service.bean.profile.*;
import net.engining.profile.sdk.service.profile.UserService;
import net.engining.profile.security.service.ProfileSecurityLoggerService;
import net.engining.profile.security.service.ProfileSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author yangxing
 */
@RequestMapping("/proUser")
@RestController
@Api(value="ProfileUserController",description = "用户服务模块")
public class ProfileUserController {

	@Autowired
	private ProfileUserService profileUserService;

	/**
	 * 安全服务
	 */
	@Autowired
	private ProfileSecurityService profileSecurityService;

	/**
	 * 机构服务
	 */
	@Autowired
	private Provider4Organization provider4Organization;

	/**
	 * 用户服务
	 */
	@Autowired
	private UserService userService;

	/**
	 * 安全日志服务
	 */
	@Autowired
	private ProfileSecurityLoggerService profileSecurityLoggerService;

	    @PreAuthorize("hasAuthority('users')")
	@ApiOperation(value="根据机构和分部获取用户列表", notes="根据机构和分部获取用户列表")
	@RequestMapping(value="/users", method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse getUsers(@RequestBody @Validated UsersFilter usersFilter) {
		FetchResponse<UserManagerBean> rsp = profileUserService.fetchUsers4Branch(
				usersFilter.getBranchId(),
				usersFilter.getName(),
				provider4Organization.getCurrentOrganizationId(),
				usersFilter.getRange()
		);
		return new CommonWithHeaderResponseBuilder<Void, FetchResponse<UserManagerBean>>()
				.build()
				.setResponseData(rsp);
	}

	    @PreAuthorize("hasAuthority('Maintenance')")
	@ApiOperation(value="根据用户userId获取用户信息", notes="根据用户userId获取用户信息")
	@RequestMapping(value="/userId", method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse getUserByName(@RequestBody @Validated ProfileUserForm user,
										   HttpServletRequest request) {
		FetchResponse<Map<String, Object>> rsp = profileUserService.getUserInfoByUserId(user.getUserId());
		Date date = new Date();
		Object puid =profileSecurityLoggerService.getPuid(
				user.getOperUserId());
		//userNo是操作员的puid，beOper是被操作员的登录id
		profileSecurityLoggerService.logSecuOperation(puid.toString(),
				OperationType.QU,
				WebCommonUtils.getIpAddress(request),
				date,user.getUserId()
		);
		return new CommonWithHeaderResponseBuilder<Void,FetchResponse<Map<String, Object>>>().build()
				.setResponseData(rsp);
	}

		@PreAuthorize("hasAuthority('ProfileUser')")
	@ApiOperation(value="登录时根据用户userId获取用户信息", notes="登录时根据用户userId获取用户信息")
	@RequestMapping(value="/loginUserId", method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse loginUserByName(@RequestBody LoginUserIdForm user,
											 HttpServletRequest request) {
		FetchResponse<Map<String, Object>> rsp = profileUserService.getUserInfoByUserId(user.getUserId());
		Date date = new Date();
		Object puid =profileSecurityLoggerService.getPuid(user.getOperUserId());
		//userNo是操作员的puid，beOper是被操作员的登录id
		profileSecurityLoggerService.logSecuOperation(
				puid.toString(),
				OperationType.LG,
				WebCommonUtils.getIpAddress(request),
				date,user.getUserId()
		);
		return new CommonWithHeaderResponseBuilder<Void,FetchResponse<Map<String, Object>>>()
				.build()
				.setResponseData(rsp);
	}

	    @PreAuthorize("hasAuthority('PuId')")
	@ApiOperation(value="获取用户信息", notes="获取用户信息")
	@RequestMapping(value="/puId", method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse getUser(@RequestBody @Validated ProfileUserForm user,
									 HttpServletRequest request) {
		ProfileUser profileUser = profileSecurityService.getUserInfo(user.getPuId());
		MgmWebUser mgmWebUser = userService.mgmWebUser(profileUser);
		Date date = new Date();
		Object puid =profileSecurityLoggerService.getPuid(user.getOperUserId());
		profileSecurityLoggerService.logSecuOperation(
				puid.toString(),
				OperationType.QU,
				WebCommonUtils.getIpAddress(request),date,
				user.getUserId()
		);
		return new CommonWithHeaderResponseBuilder<Void,MgmWebUser>()
				.build()
				.setResponseData(mgmWebUser);
	}

	@PreAuthorize("hasAuthority('AddUser')")
	@ApiOperation(value="添加用户", notes="添加用户")
	@RequestMapping(value="/addUser", method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse addUser(@RequestBody @Validated MgmWebUser user,
									 HttpServletRequest request) {
		if (ValidateUtilExt.isNullOrEmpty(user.getPassword())) {
			throw new ErrorMessageException(ErrorCode.BadRequest, "密码不能为空！");
		}
		userService.validateUser(user.getUserId());
		ProfileUser profileUser = userService.profileUserForm(user);
		Date date = new Date();
		Object puid =profileSecurityLoggerService.getPuid(user.getOperUserId());
		//userNo是操作员的puid，beOper是被操作员的登录id
		profileSecurityLoggerService.logSecuOperation(
				puid.toString(),
				OperationType.AD,
				WebCommonUtils.getIpAddress(request),
				date,user.getUserId()
		);

		return new CommonWithHeaderResponseBuilder<Void,Void>().build();

	}


	@PreAuthorize("hasAuthority('RemoveUserByPuId')")
	@ApiOperation(value="删除某个用户", notes="删除某个用户")
	@RequestMapping(value="/removeUserByPuId", method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse removeUser(@RequestBody @Validated ProfileUserForm user,
										HttpServletRequest request) {
		if (user.getPuId().equals(user.getOperUserId())) {
			throw new ErrorMessageException(ErrorCode.CheckError, "您无法删除您自己的用户！");
		}
		List<String> usrs = new ArrayList<>(1);
		usrs.add(user.getPuId());
		ProfileUser profileUserInfo = profileUserService.findProfileUserInfo(user.getPuId());
		if (ADMIN.equals(profileUserInfo.getUserId())) {
			throw new ErrorMessageException(ErrorCode.CheckError, "您无法删除超级管理员！");
		}
        if (SVADMIN.equals(profileUserInfo.getUserId())) {
            throw new ErrorMessageException(ErrorCode.CheckError, "您无法删除监控管理员！");
        }
		profileUserService.deleteProfileUsers(usrs);
		String userId=profileUserInfo.getUserId();
		Date date = new Date();
		Object puid =profileSecurityLoggerService.getPuid(user.getOperUserId());
		//userNo是操作员的puid，beOper是被操作员的登录id
		profileSecurityLoggerService.logSecuOperation(
				puid.toString(),
				OperationType.DE,
				WebCommonUtils.getIpAddress(request),
				date,userId
		);
		return new CommonWithHeaderResponseBuilder<Void,Void>().build();
	}

	    @PreAuthorize("hasAuthority('Maintenance')")
	@ApiOperation(value="删除多个用户", notes="删除多个用户")
	@RequestMapping(value="/removeUsers", method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse removeUser(@RequestBody
										@ApiParam(value = "puIds", required = true)
												String[] puIds) {
		List<String> usrs = Arrays.asList(puIds);
		profileUserService.deleteProfileUsers(usrs);
		return new CommonWithHeaderResponseBuilder<Void,Void>().build();
	}

	@PreAuthorize("hasAuthority('UpdateUser')")
	@ApiOperation(value="更新某个用户", notes="更新某个用户")
	@RequestMapping(value="/updateUser", method= RequestMethod.POST)
	public @ResponseBody
	CommonWithHeaderResponse updateUser(@RequestBody @Validated ProfileUserUpdateForm user,
										HttpServletRequest request) {
		ProfileUser profileUser = userService.profileUser(user);
		Date date = new Date();
		Object puid =profileSecurityLoggerService.getPuid(user.getOperUserId());
		//userNo是操作员的puid，beOper是被操作员的登录id
		profileSecurityLoggerService.logSecuOperation(
				puid.toString(),
				OperationType.UP,
				WebCommonUtils.getIpAddress(request),
				date,
				user.getUserId()
		);
		return new CommonWithHeaderResponseBuilder<Void,Void>().build();
	}


}