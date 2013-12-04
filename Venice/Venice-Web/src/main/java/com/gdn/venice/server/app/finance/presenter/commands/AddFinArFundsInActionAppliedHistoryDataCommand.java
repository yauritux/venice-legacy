package com.gdn.venice.server.app.finance.presenter.commands;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.FinArFundsInActionAppliedHistorySessionEJBRemote;
import com.gdn.venice.facade.FinArFundsInAllocatePaymentSessionEJBRemote;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.facade.FinArFundsInRefundSessionEJBRemote;
import com.gdn.venice.persistence.FinArFundsInActionApplied;
import com.gdn.venice.persistence.FinArFundsInActionAppliedHistory;
import com.gdn.venice.persistence.FinArFundsInAllocatePayment;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.persistence.FinArFundsInRefund;
import com.gdn.venice.util.VeniceConstants;

public class AddFinArFundsInActionAppliedHistoryDataCommand {
	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	ArrayList<Long> approvedFundInReconRecordList;
	
	/**
	 * id  FundInReconRecord
	 * @param rafDsRequest
	 */
	public AddFinArFundsInActionAppliedHistoryDataCommand(ArrayList<Long> approvedFundInReconRecordList) {
		this.approvedFundInReconRecordList=approvedFundInReconRecordList;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	public boolean execute() {		
		Locator<Object> locator=null;		
		try{
			locator = new Locator<Object>();
			
			FinArFundsInAllocatePaymentSessionEJBRemote finArFundsInAllocateHome = (FinArFundsInAllocatePaymentSessionEJBRemote) locator
			.lookup(FinArFundsInAllocatePaymentSessionEJBRemote.class, "FinArFundsInAllocatePaymentSessionEJBBean");
			
			FinArFundsInReconRecordSessionEJBRemote sessionHome = (FinArFundsInReconRecordSessionEJBRemote) locator
					.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");
			
			FinArFundsInActionAppliedHistorySessionEJBRemote actionHome = (FinArFundsInActionAppliedHistorySessionEJBRemote) locator
			.lookup(FinArFundsInActionAppliedHistorySessionEJBRemote.class, "FinArFundsInActionAppliedHistorySessionEJBBean");
			
			FinArFundsInRefundSessionEJBRemote refundRecordHome = (FinArFundsInRefundSessionEJBRemote) locator
			.lookup(FinArFundsInRefundSessionEJBRemote.class, "FinArFundsInRefundSessionEJBBean");
							

			/*
			 * Nested loops to map the account data from the requests to
			 * the object structure for persistence
			 */
			List<FinArFundsInActionAppliedHistory> FinArFundsInActionAppliedHistoryList = new ArrayList<FinArFundsInActionAppliedHistory>();
			for(int i=0;i< approvedFundInReconRecordList.size();i++){
				Long dataID = approvedFundInReconRecordList.get(i);
				FinArFundsInActionAppliedHistory finArFundsInActionAppliedHistoryItem = new FinArFundsInActionAppliedHistory();
				BigDecimal amount = new BigDecimal(0);
				Date newDate = new Date();
				FinArFundsInActionApplied action =null;
				String referenceId ="";
				
				List<FinArFundsInReconRecord> finArFundsInReconRecordList = sessionHome.queryByRange("select o from FinArFundsInReconRecord o where o.reconciliationRecordId="+dataID, 0, 0);
				if(finArFundsInReconRecordList.get(0).getFinArFundsInActionApplied().getActionAppliedId().equals(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_NONE) ){
					List<FinArFundsInAllocatePayment> allocateList = finArFundsInAllocateHome.queryByRange("select o from FinArFundsInAllocatePayment o where o.idReconRecordDest="+dataID+" and o.isactive=true", 0, 0);
					if(!allocateList.isEmpty()){
						action = new FinArFundsInActionApplied();
						action.setActionAppliedId(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_RECEIVE);						
						amount=allocateList.get(0).getAmount();
						newDate=allocateList.get(0).getAllocationTimestamp();
						List<FinArFundsInReconRecord> allocateRecordList = sessionHome.queryByRange("select o from FinArFundsInReconRecord o where o.reconciliationRecordId="+allocateList.get(0).getIdReconRecordSource(), 0, 0);
						if(!allocateList.isEmpty()){
							referenceId=allocateRecordList.get(0).getVenOrderPayment()!=null?allocateRecordList.get(0).getVenOrderPayment().getReferenceId():allocateRecordList.get(0).getNomorReff();
						}
					}
				}else{
					action = finArFundsInReconRecordList.get(0).getFinArFundsInActionApplied();					
					if(action.getActionAppliedId().equals(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_ALLOCATED) ){
						List<FinArFundsInAllocatePayment> allocateList = finArFundsInAllocateHome.queryByRange("select o from FinArFundsInAllocatePayment o where o.idReconRecordSource="+dataID+" and o.isactive=true", 0, 0);
						if(!allocateList.isEmpty()){
							amount=allocateList.get(0).getAmount();
							newDate=allocateList.get(0).getAllocationTimestamp();
							List<FinArFundsInReconRecord> allocateRecordList = sessionHome.queryByRange("select o from FinArFundsInReconRecord o where o.reconciliationRecordId="+allocateList.get(0).getIdReconRecordDest(), 0, 0);
							if(!allocateList.isEmpty()){
								referenceId=allocateRecordList.get(0).getVenOrderPayment().getReferenceId();
							}
						}
					}else if(action.getActionAppliedId().equals(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REFUNDED_BANK) || 
							action.getActionAppliedId().equals(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REFUNDED_CUSTOMER)
							){						
							List<FinArFundsInRefund> refundAmountList  = refundRecordHome.queryByRange("select o from FinArFundsInRefund o where o.finArFundsInReconRecord.reconciliationRecordId="+dataID, 0, 0);
							Double refundAmount = new Double(finArFundsInReconRecordList.get(0).getRefundAmount()!=null?finArFundsInReconRecordList.get(0).getRefundAmount()+"":"0");
							if(!refundAmountList.isEmpty()){
								for(FinArFundsInRefund j : refundAmountList ){
											refundAmount=refundAmount-new Double(j.getApAmount()+"");
								}
							}							
							amount=new BigDecimal(refundAmount);
					}
				}
				finArFundsInActionAppliedHistoryItem.setActionTakenTimestamp(newDate);
				finArFundsInActionAppliedHistoryItem.setAmount(amount);
				finArFundsInActionAppliedHistoryItem.setReferenceId(referenceId);
				finArFundsInActionAppliedHistoryItem.setFinArFundsInActionApplied(action);
				finArFundsInActionAppliedHistoryItem.setFinArFundsInReconRecords(finArFundsInReconRecordList.get(0));
				
				FinArFundsInActionAppliedHistoryList.add(finArFundsInActionAppliedHistoryItem);
				
			}			
			actionHome.persistFinArFundsInActionAppliedHistoryList(FinArFundsInActionAppliedHistoryList);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return true;
	}
}
