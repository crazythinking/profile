package net.engining.profile.security.service;

import net.engining.pg.support.db.querydsl.FetchResponse;
import net.engining.profile.entity.model.ProfileSecoperLog;
import net.engining.profile.enums.OperationType;
import net.engining.profile.sdk.service.ProfileUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
@Service
public class ProfileSecurityLoggerService {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private ProfileUserService profileUserService;
	
	@Transactional(rollbackFor = Exception.class)
	public void logSecuOperation(String userNo, OperationType operationType, String opIp, Date opTime,String beopered){
		ProfileSecoperLog ssCustSecoperLog = new ProfileSecoperLog();
		ssCustSecoperLog.setPuId(userNo);
		ssCustSecoperLog.setOperType(operationType);
		ssCustSecoperLog.setOperIp(opIp);
		ssCustSecoperLog.setOperTime(opTime);
		ssCustSecoperLog.setBeoperatedId(beopered);
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
