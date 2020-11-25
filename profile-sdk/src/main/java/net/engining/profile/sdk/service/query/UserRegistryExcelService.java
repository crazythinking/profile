package net.engining.profile.sdk.service.query;

import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.profile.enums.OperationType;
import net.engining.profile.enums.UserStatusEnum;
import net.engining.profile.sdk.service.bean.param.UserRegistryDetailsRes;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author tuyi
 */
@Service
public class UserRegistryExcelService {

  SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");

  public void excelFile(List<UserRegistryDetailsRes> dataList, HttpServletResponse response) {
    byte[] uft8bom={(byte)0xef,(byte)0xbb,(byte)0xbf};
    response.setContentType("application/ms-txt.numberformat:@");
    response.setCharacterEncoding("utf-8");
    response.setHeader("Cache-Control", "max-age=30");
    try (
            OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(),"UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);
    ) {
      osw.write(new String( uft8bom));
      osw.write("\ufeff");
      bw.append(" 操作员ID,操作员姓名,机构号,部门名称,邮箱,状态,变更日期,变更类型,录入操作员ID,复核操作员ID" ).append("\r");
      if (dataList != null ) {
        dataList.forEach(e -> {
          String oper ="";
          for (OperationType f : OperationType.values()) {
           if(f.getValue().equals(e.getOperType())){
            oper=  f.getLabel();
            }
          }
          String status ="";
          for(UserStatusEnum statusDef:UserStatusEnum.values()){
            if(statusDef.getValue().equals(e.getStatus())){
              status=statusDef.getLabel();
            }
          }
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(e.getUserId()).append(",").append(e.getName()).append(",")
                  .append(e.getOrgId()).append(",").append(e.getBranchId()).append(",")
                  .append(e.getEmail()).append(",").append(status).append(",").append( e.getOperTime().toString().substring(0,10)).append(",")
                  .append(oper).append(",").append(e.getOperRecUserId()).append(",").append(e.getReViewUserId())
                  .toString();
          try {

            bw.append(stringBuilder).append("\r");
          } catch (IOException ex) {
            throw new ErrorMessageException(ErrorCode.SystemError, "文件生成时发生IOException异常");
          }
        });
      }
    } catch (Exception ex) {
      throw new ErrorMessageException(ErrorCode.SystemError, "文件生成时发生IOException异常");
    }
  }
}
