package net.engining.profile.invoker.check;

import ch.qos.logback.core.net.server.Client;
import com.alibaba.fastjson.JSON;
import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.AbstractSkippableInvoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.profile.entity.dto.ProfileMenuInterfDto;
import net.engining.profile.entity.dto.ProfileRoleAuthDto;
import net.engining.profile.sdk.key.ClientIdKey;
import net.engining.profile.sdk.key.ProfileRoleAuthDtoListKey;
import net.engining.profile.sdk.service.db.ProfileMenuInterfService;
import net.engining.profile.sdk.service.db.ProfileMenuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static net.engining.profile.sdk.service.constant.ParameterConstants.DEFAULT_SIZE;
import static net.engining.profile.sdk.service.constant.ParameterConstants.LINE;

/**
 * 校验权限是否存在
 *
 * @author zhaoyuanmin
 * @version 1.0.0
 * @date 2020/10/28 15:40
 * @since 1.0.0
 */
@InvokerDefinition(
        name = "校验权限是否存在",
        requires = {
                ProfileRoleAuthDtoListKey.class,
                ClientIdKey.class
        }
)
public class CheckRoleAuthsExistInvoker extends AbstractSkippableInvoker {

    /**
     * ProfileMenu表操作服务
     */
    @Autowired
    private ProfileMenuService profileMenuService;
    /**
     * ProfileMenuInterf表操作服务
     */
    @Autowired
    private ProfileMenuInterfService profileMenuInterfService;

    @Override
    public void invoke(FlowContext flowContext) {
        String clientId = flowContext.get(ClientIdKey.class);
        List<ProfileRoleAuthDto> profileRoleAuthDtoList = flowContext.get(ProfileRoleAuthDtoListKey.class);

        List<String> menuList = new ArrayList<>(DEFAULT_SIZE);
        List<ProfileRoleAuthDto> interfList = new ArrayList<>(DEFAULT_SIZE);
        for (ProfileRoleAuthDto profileRoleAuthDto : profileRoleAuthDtoList) {
            if (LINE.equals(profileRoleAuthDto.getAutuUri())) {
                menuList.add(profileRoleAuthDto.getAuthority());
            } else {
                interfList.add(profileRoleAuthDto);
            }
        }

        boolean b1 = ValidateUtilExt.isNullOrEmpty(menuList);
        boolean b2 = ValidateUtilExt.isNullOrEmpty(interfList);
        if (b1 && !b2) {
            throw new ErrorMessageException(ErrorCode.Null, "请设置上级菜单权限");
        }
        if (!b1) {
            List<String> existMenuList = profileMenuService.listMenuIdByMenuIdListAndAppCd(menuList,
                    clientId);
            if (ValidateUtilExt.isNullOrEmpty(existMenuList)) {
                throw new ErrorMessageException(ErrorCode.CheckError,
                        "如下菜单权限不存在：" + JSON.toJSONString(menuList));
            }
            for (String menuId : menuList) {
                boolean success = existMenuList.removeIf(existMenuId -> existMenuId.equals(menuId));
                if (!success) {
                    throw new ErrorMessageException(ErrorCode.CheckError, menuId + "不存在");
                }
            }
        }
        if (!b2) {
            List<String> interfIdList = interfList.stream()
                    .map(ProfileRoleAuthDto::getAuthority)
                    .collect(Collectors.toList());
            List<ProfileMenuInterfDto> profileMenuInterfDtoList
                    = profileMenuInterfService.listProfileMenuInterfDtoByInterfIdListAndAppCd(interfIdList,
                    clientId);
            if (ValidateUtilExt.isNullOrEmpty(profileMenuInterfDtoList)) {
                throw new ErrorMessageException(ErrorCode.CheckError,
                        "如下接口权限不存在：" + JSON.toJSONString(interfIdList));
            }

            for (ProfileRoleAuthDto profileRoleAuthDto  : interfList) {
                String interfId = profileRoleAuthDto.getAuthority();
                String menuId = profileRoleAuthDto.getAutuUri();
                boolean success = false;
                Iterator<ProfileMenuInterfDto> iterator = profileMenuInterfDtoList.iterator();
                while (iterator.hasNext()) {
                    ProfileMenuInterfDto profileMenuInterfDto = iterator.next();
                    if (interfId.equals(profileMenuInterfDto.getInterfCd())) {
                        if (!menuId.equals(profileMenuInterfDto.getMenuId().toString())) {
                            throw new ErrorMessageException(ErrorCode.CheckError,
                                    interfId + "接口权限的上级菜单ID不正确");
                        }
                        success = true;
                        iterator.remove();
                    }
                }
                if (!success) {
                    throw new ErrorMessageException(ErrorCode.CheckError, interfId + "不存在");
                }
            }
        }
    }
}
