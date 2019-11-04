package net.engining.profile.security;

import net.engining.profile.entity.model.ProfileSecoperLog;
import net.engining.profile.enums.OperationType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@Service
public class ProfileSecurityLoggerService {

	@PersistenceContext
	private EntityManager em;
	
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
	
}
