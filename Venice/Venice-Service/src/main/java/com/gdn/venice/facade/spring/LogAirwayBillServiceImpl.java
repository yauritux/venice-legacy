package com.gdn.venice.facade.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import com.gdn.venice.dao.LogAirwayBillDAO;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.LogApprovalStatus;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.util.VeniceConstants;


@Service
public class LogAirwayBillServiceImpl implements LogAirwayBillService{
	
	@Autowired
	LogAirwayBillDAO logAirwayBillDAO;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addDummyLogAirwayBillForNewlyFPOrderItem(VenOrderItem orderItem) {
		if(orderItem.getLogAirwayBills().size() > 0){
			logAirwayBillDAO.delete(orderItem.getLogAirwayBills());
		}
		
		LogAirwayBill newLogAirwayBill = new LogAirwayBill();
		
		LogApprovalStatus logApprovalStatus = new LogApprovalStatus();
        logApprovalStatus.setApprovalStatusId(VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_NEW);
		
		newLogAirwayBill.setVenOrderItem(orderItem);
		newLogAirwayBill.setLogApprovalStatus1(logApprovalStatus);
		newLogAirwayBill.setLogApprovalStatus2(logApprovalStatus);
		newLogAirwayBill.setMtaData(false);
		
		logAirwayBillDAO.save(newLogAirwayBill);
	}
}
