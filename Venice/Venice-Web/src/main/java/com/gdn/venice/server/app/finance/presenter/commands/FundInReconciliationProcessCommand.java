package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.facade.FinArFundsInAllocatePaymentSessionEJBRemote;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.facade.FinSalesRecordSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote;
import com.gdn.venice.persistence.FinApprovalStatus;
import com.gdn.venice.persistence.FinArFundsInAllocatePayment;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.persistence.FinSalesRecord;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.util.VeniceConstants;

public class FundInReconciliationProcessCommand implements RafRpcCommand {

	HashMap<String, String> fundInReconRecordIds;
	String parameter;
	String userName;
	String method;
	HttpServletRequest request;
	String paramIdReport;

	public FundInReconciliationProcessCommand(String parameter,
			String userName, String method, HttpServletRequest request) {
		Pattern p = Pattern.compile("[\\{\\}\\=\\, ]++");
		String[] split = p.split(parameter);
		this.paramIdReport=parameter;
		fundInReconRecordIds = new HashMap<String, String>();
		for (int i = 1; i < split.length; i += 2) {
			String[] getId= split[i + 1].split("&");
			fundInReconRecordIds.put(split[i], getId[0]);		
		}		
		String param = fundInReconRecordIds.toString();
		this.parameter = param;
		this.userName = userName;
		this.method = method;
		this.request = request;
	}

	@Override
	public String execute() {

		ArrayList<FinArFundsInReconRecord> fundInReconRecordList = new ArrayList<FinArFundsInReconRecord>();
		ArrayList<Long> approvedFundInReconRecordList = new ArrayList<Long>();
		Map<String,String> param = new HashMap<String,String>();

		Locator<Object> object = null;
		try {
			object = new Locator<Object>();

			FinArFundsInAllocatePaymentSessionEJBRemote finArFundsInAllocateHome = (FinArFundsInAllocatePaymentSessionEJBRemote) object
			.lookup(FinArFundsInAllocatePaymentSessionEJBRemote.class, "FinArFundsInAllocatePaymentSessionEJBBean");
			
			FinArFundsInReconRecordSessionEJBRemote sessionHome = (FinArFundsInReconRecordSessionEJBRemote) object
					.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");
			 if (method.equals("submitForApproval")){
					String cekReport = cekSubmitReport();				
					if(!cekReport.equals("true")){			
						return "-1" + ":" +cekReport;
					}		
		      }
			 int count=0;
			for (int i = 0; i < fundInReconRecordIds.size(); i++) {
				String fundInReconRecordId = fundInReconRecordIds.get(ProcessNameTokens.FUNDINRECONRECORDID + (i + 1));

				FinArFundsInReconRecord fundInReconRecord;
				List<FinArFundsInAllocatePayment> itemsAllocate = null;
				try{
					fundInReconRecord = sessionHome.queryByRange("select o from FinArFundsInReconRecord o where o.reconciliationRecordId="+fundInReconRecordId, 0, 1).get(0);
					
				}catch(IndexOutOfBoundsException e){
					fundInReconRecord = new FinArFundsInReconRecord();
					fundInReconRecord.setReconciliationRecordId(new Long(fundInReconRecordId));
				}
				itemsAllocate = finArFundsInAllocateHome.queryByRange("select o from FinArFundsInAllocatePayment o where o.idReconRecordDest="+fundInReconRecordId+" and o.isactive=true", 0, 0);

				FinApprovalStatus approvalStatus = new FinApprovalStatus();
				if (method.equals("approveFundInReconRecord")) {
					approvalStatus.setApprovalStatusId(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSID_APPROVED);				
					approvedFundInReconRecordList.add(new Long(fundInReconRecordId));
				} else if (method.equals("rejectFundInReconRecord")) {
					approvalStatus.setApprovalStatusId(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSID_REJECTED);
				} else if (method.equals("submitForApproval")) {					
					if(fundInReconRecord.getVenOrderPayment()!=null?
							fundInReconRecord.getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VeniceConstants.VEN_WCS_PAYMENT_TYPE_VirtualAccount) : 
								fundInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_VA) || 
								fundInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_VA)){
						approvalStatus.setApprovalStatusId(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSID_SUBMITTED);						
						count++;
						param.put(ProcessNameTokens.FUNDINRECONRECORDID+(count), fundInReconRecordId);										
					}else if(!fundInReconRecord.getFinArFundsInActionApplied().getActionAppliedId().equals(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_NONE)
							|| !itemsAllocate.isEmpty()){
						approvalStatus.setApprovalStatusId(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSID_SUBMITTED);	
						count++;
						param.put(ProcessNameTokens.FUNDINRECONRECORDID+(count), fundInReconRecordId);	
					}else{					
						approvalStatus.setApprovalStatusId(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSID_APPROVED);
						approvedFundInReconRecordList.add(new Long(fundInReconRecordId));						
					}
				}

				fundInReconRecord.setFinApprovalStatus(approvalStatus);
				fundInReconRecordList.add(fundInReconRecord);
			}
			
			if(param.size()>0){
				BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName, BPMAdapter.getUserPasswordFromLDAP(userName));
	
				bpmAdapter.synchronize();
				
				String sendParam=param.toString();
	
				HashMap<String, String> taskData = new HashMap<String, String>();
				taskData.put(ProcessNameTokens.FUNDINRECONRECORDID, sendParam);
	
				try {
					bpmAdapter.startBusinessProcess(ProcessNameTokens.FINANCEFUNDINRECONCILIATIONRESULTAPPROVAL, taskData);
				} catch (Exception e) {
					String errorMsg = e.getMessage();
					e.printStackTrace();
					return "-1" + ":" + errorMsg;
				}
			}				
			
			sessionHome.mergeFinArFundsInReconRecordList(fundInReconRecordList);
		} catch (Exception ex) {
			String errorMsg = ex.getMessage();
			ex.printStackTrace();
			try {
				object.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "-1" + ":" + errorMsg;
		} finally {
			try {
				object.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// For Approved Fund In Recon Record, post Cash Receive Journal
		// transactions for the approved funds-in

		Locator<FinanceJournalPosterSessionEJBRemote> financeJournalPosterLocator = null;
		Locator<Object> locator = null;
		if (method.equals("approveFundInReconRecord") || approvedFundInReconRecordList.size()>0) {
			try {
				financeJournalPosterLocator = new Locator<FinanceJournalPosterSessionEJBRemote>();
				FinanceJournalPosterSessionEJBRemote sessionHome = (FinanceJournalPosterSessionEJBRemote) financeJournalPosterLocator
						.lookup(FinanceJournalPosterSessionEJBRemote.class,  "FinanceJournalPosterSessionEJBBean");
				
				AddFinArFundsInActionAppliedHistoryDataCommand createHistory = new AddFinArFundsInActionAppliedHistoryDataCommand(approvedFundInReconRecordList);
				createHistory.execute();
				sessionHome.postCashReceiveJournalTransactions(approvedFundInReconRecordList);

				/*
				 * Setelah cash receive journal di create.
				 * 
				 * after the last order item become cx, reconcile sales journal vs cash receive journal. 
				 * because it is the same as funds in reconcile, we just check the reconcilement status, if not all fund received, then trigger bpm.
				 */
				for (int i = 0; i < fundInReconRecordIds.size(); i++) {
					String fundInReconRecordId = fundInReconRecordIds.get(ProcessNameTokens.FUNDINRECONRECORDID + (i + 1));
					locator = new Locator<Object>();
					VenOrderItemSessionEJBRemote venOrderItemHome = (VenOrderItemSessionEJBRemote) locator
					.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
					
					FinArFundsInReconRecordSessionEJBRemote fundsInReconRecordHome = (FinArFundsInReconRecordSessionEJBRemote) locator
					.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");
					
					FinSalesRecordSessionEJBRemote salesRecordHome = (FinSalesRecordSessionEJBRemote) locator
					.lookup(FinSalesRecordSessionEJBRemote.class, "FinSalesRecordSessionEJBBean");
					
					List<FinArFundsInReconRecord> fundInRecordList = fundsInReconRecordHome.queryByRange("select o from FinArFundsInReconRecord o where o.reconciliationRecordId = " +  fundInReconRecordId, 0, 0);					
									
					Boolean allOrderItemCx=false;
					List< VenOrderItem> venOrderItemList =  venOrderItemHome.queryByRange("select o from VenOrderItem o where o.venOrder.wcsOrderId = '" + fundInRecordList.get(0).getWcsOrderId() + "'", 0, 0);

					for (VenOrderItem item : venOrderItemList) {
						if (item.getVenOrderStatus().getOrderStatusId() == VeniceConstants.VEN_ORDER_STATUS_CX) {
							allOrderItemCx=true;
						}else{
							allOrderItemCx=false;
							break;
						}
					}
						
					if(allOrderItemCx==true){
						System.out.println("All order item status is already CX, Check is sales journal exist");
						@SuppressWarnings("unused")
						Boolean salesJournalExist=false;
						Boolean bpmRun=false;
						List<FinSalesRecord>salesRecordList = salesRecordHome.queryByRange("select o from FinSalesRecord o where o.venOrderItem.venOrder.wcsOrderId = '" +   fundInRecordList.get(0).getWcsOrderId() + "'", 0, 0);

						//there might be an order which has multiple payment, therefore there is multiple recon record
						//here we check all recon record id must have cash receive journal.
						for (FinSalesRecord salesRecord : salesRecordList) {
							if (salesRecord.getFinJournalTransactions()==null || salesRecord.getFinJournalTransactions().isEmpty()) {
								System.out.println("Sales journal not exist");
								salesJournalExist=false;
								break;
							}else{
								System.out.println("Sales journal exist");
								salesJournalExist=true;
								
								//here we check the reconciliation status, if it's not all funds received, trigger bpm.
								String wcsOrderId=salesRecord.getVenOrderItem().getVenOrder().getWcsOrderId();
								List<FinArFundsInReconRecord> fundInRecordList2 = fundsInReconRecordHome.queryByRange("select o from FinArFundsInReconRecord o where o.wcsOrderId = '" +  wcsOrderId + "'", 0, 0);					
								
								long reconStatus = fundInRecordList2.get(0).getFinArReconResult().getReconResultId();
								//just run the bpm once if there is multiple sales record for one order
								if(reconStatus!=VeniceConstants.FIN_AR_RECON_RESULT_ALL && bpmRun==false){
									System.out.println("Recon record status not all funds received and bpm run is false");
																
									BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName, BPMAdapter.getUserPasswordFromLDAP(userName));
									bpmAdapter.synchronize();
									
									HashMap<String, String> taskData = new HashMap<String, String>();
									taskData.put(ProcessNameTokens.FUNDINRECONRECORDID, fundInRecordList2.get(0).getReconciliationRecordId().toString());
									taskData.put(ProcessNameTokens.WCSORDERID, fundInRecordList2.get(0).getWcsOrderId());							

									try {
										System.out.println("Starting bpm");
										bpmAdapter.startBusinessProcess(ProcessNameTokens.FINANCECASHRECEIVEANDSALESJOURNALRECONCILIATION, taskData);
										bpmRun=true;
									} catch (Exception e) {
										System.out.println("Error when starting bpm");
										e.printStackTrace();
									}
								}else{
									System.out.println("Recon record status all funds received, no need to trigger bpm");
								}
							}
						}			
					}
				}				
			
			} catch (Exception ex) {
				String errorMsg = ex.getMessage();
				ex.printStackTrace();
				try {
					financeJournalPosterLocator.close();
					locator.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "-1" + ":" + errorMsg;
			} finally {
				BPMAdapter.removeBPMAdapter(userName);
				try {
					financeJournalPosterLocator.close();
					locator.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return "0";
	}
	
	private String cekSubmitReport(){
		Pattern p = Pattern.compile("[\\{\\}\\=\\, ]++");
		String[] split = p.split(paramIdReport);
		String idReport="";String idRecord="";
		String result="";
		
		for (int i = 1; i < split.length; i += 2) {
			String[] getValue= split[i + 1].split("&");	
			if(idReport.equals("")) {
				idReport=getValue[1];
			}else if (!idReport.contains(getValue[1]) && !idReport.equals("")) {
				result= "Hanya satu report yang submit. Mohon cek lagi";
				break;
			}else idReport=idReport +","+getValue[1];
			
			if(idRecord.equals("")) idRecord=getValue[0];
			else idRecord=idRecord +","+getValue[0];
		}		
		if(result.equals("")){		
				Locator<Object> locators = null;
				try{
				locators = new Locator<Object>();
				
				FinArFundsInReconRecordSessionEJBRemote fundsInReconRecordHome = (FinArFundsInReconRecordSessionEJBRemote) locators
				.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");
		
						String query ="select o from FinArFundsInReconRecord o where o.reconciliationRecordId in ("+idRecord+") and (((select count(a) from FinArFundsInReconRecord a where a.reconciliationRecordId in ("+idRecord+")) = (select count(b) from FinArFundsInReconRecord b where b.finArFundsInReport.paymentReportId in ("+idReport+") and b.finArFundsInActionApplied.actionAppliedId<>"+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED+") ) "+
						"or (select count(c) from FinArFundsInReconRecord c where c.finArFundsInReport.paymentReportId in ("+idReport+") and c.finApprovalStatus.approvalStatusId in ("+VeniceConstants.LOG_APPROVAL_STATUS_APPROVED+","+VeniceConstants.LOG_APPROVAL_STATUS_SUBMITTED+")  ) >=1  )";
						List<FinArFundsInReconRecord> fundInRecordList = fundsInReconRecordHome.queryByRange(query, 0, 0);	
						if(fundInRecordList.size()>0 && !fundInRecordList.isEmpty()){
							result= "true";
						}else{
							result= "Payment dalam satu report harus di submit semua. Mohon cek lagi";
						}	
				
				
				}catch (Exception e) {
				}finally{
					try {
						locators.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		}
		return result;
	}
	
}
