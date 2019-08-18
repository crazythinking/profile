package net.engining.profile.security;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.engining.profile.entity.model.ProfileSecoperLog;
import net.engining.profile.enums.OperationType;

@Service
public class ProfileSecurityLoggerService {

	@PersistenceContext
	private EntityManager em;
	
	@Transactional
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
