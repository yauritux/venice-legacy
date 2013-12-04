package com.gdn.venice.server.app.finance.presenter.commands;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinApManualJournalTransactionSessionEJBRemote;
import com.gdn.venice.facade.FinJournalTransactionSessionEJBRemote;
import com.gdn.venice.facade.FinSalesRecordSessionEJBRemote;
import com.gdn.venice.facade.VenSettlementRecordSessionEJBRemote;
import com.gdn.venice.persistence.FinApManualJournalTransaction;
import com.gdn.venice.persistence.FinJournalTransaction;
import com.gdn.venice.persistence.FinSalesRecord;
import com.gdn.venice.persistence.VenSettlementRecord;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.util.VeniceConstants;

public class FetchMerchantPaymentDataCommand implements RafDsCommand {
	RafDsRequest request;
	protected static Logger _log = null;
	
	public FetchMerchantPaymentDataCommand(RafDsRequest request) {
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.finance.presenter.commands.FetchMerchantPaymentDataCommand");
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();		
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			
			FinSalesRecordSessionEJBRemote finSalesRecordSessionHome = (FinSalesRecordSessionEJBRemote) locator
			.lookup(FinSalesRecordSessionEJBRemote.class, "FinSalesRecordSessionEJBBean");
						
			FinApManualJournalTransactionSessionEJBRemote finApManualJournalTransactionSessionHome = (FinApManualJournalTransactionSessionEJBRemote) locator
			.lookup(FinApManualJournalTransactionSessionEJBRemote.class, "FinApManualJournalTransactionSessionEJBBean");
			
			FinJournalTransactionSessionEJBRemote finJournalTransactionSessionHome = (FinJournalTransactionSessionEJBRemote) locator
			.lookup(FinJournalTransactionSessionEJBRemote.class, "FinJournalTransactionSessionEJBBean");
			
			//Gets the list of selected Sales Record Ids
			HashMap<String,String> salesRecordIdMap = Util.formHashMapfromXML( request.getParams().get(DataNameTokens.FINSALESRECORD_SALESRECORDID));
			
			String salesRecordIds = "", salesRecordIdsForParam="", finApManualJournalTransactionsIds = "";;
			
			for (int i=0;i<salesRecordIdMap.size();i++) {
				salesRecordIds += salesRecordIdMap.get("SalesRecord"+i);
				if (i<salesRecordIdMap.size()-1) {
					salesRecordIds += ",";
				}
			}
			
			_log.debug("salesRecordIds: "+salesRecordIds);
			List<FinSalesRecord> finSalesRecordList = null;			
			FinSalesRecord salesRecord = new FinSalesRecord();
	 	
			String selectSalesRecord = "select o from FinSalesRecord o where o.salesRecordId in (" + salesRecordIds + ") order by o.venOrderItem.venMerchantProduct.venMerchant.venParty.fullOrLegalName";
			
			finSalesRecordList = finSalesRecordSessionHome.queryByRange(selectSalesRecord, 0, 0);	
			
			_log.debug("finSalesRecordList size: "+finSalesRecordList.size());
			BigDecimal apAmount = new BigDecimal(0);
			BigDecimal arAmount = new BigDecimal(0);
			BigDecimal pph23 = new BigDecimal(0);
			
			for (int i=0;i<finSalesRecordList.size();i++) {
				salesRecord = finSalesRecordList.get(i);
				
				String partyName = salesRecord.getVenOrderItem().getVenMerchantProduct()!=null?(
						salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant()!=null?(
								salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty()!=null?
										salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName():null):null):null;																				
																			
				_log.debug("partyName: "+partyName);
				
				if (partyName!=null) {
					salesRecordIdsForParam+= salesRecord.getSalesRecordId();
					salesRecordIdsForParam+="#";

					if(i>0){
						String partyNameLast = finSalesRecordList.get(i-1).getVenOrderItem().getVenMerchantProduct()!=null?(
								finSalesRecordList.get(i-1).getVenOrderItem().getVenMerchantProduct().getVenMerchant()!=null?(
										finSalesRecordList.get(i-1).getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty()!=null?
												finSalesRecordList.get(i-1).getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName():null):null):null;
												
						_log.debug("partyNameLast: "+partyNameLast);
						
						if(!partyName.equals(partyNameLast)){		
							_log.debug("different party, reset ap amount, dll");
							apAmount = new BigDecimal(0);
							arAmount = new BigDecimal(0);
							pph23 = new BigDecimal(0);
							
							salesRecordIdsForParam="";
							salesRecordIdsForParam+= salesRecord.getSalesRecordId();
							salesRecordIdsForParam+="#";
							
							finApManualJournalTransactionsIds="";
						}
					}
					
					HashMap<String, String> map = new HashMap<String, String>();						
					map.put(DataNameTokens.FINAPPAYMENT_APPAYMENTID, new Integer(i).toString());
					map.put(DataNameTokens.FINAPPAYMENT_VENPARTY_FULLORLEGALNAME, 
							(salesRecord.getVenOrderItem()!=null && 
							salesRecord.getVenOrderItem().getVenMerchantProduct()!=null &&
							salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant()!=null &&
							salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty()!=null)?
							salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName():"");																
					
					/*
					 * query to get settlement record id
					 */
					VenSettlementRecord venSettlementRecord=null;

					VenSettlementRecordSessionEJBRemote VenSettlementRecordHome = (VenSettlementRecordSessionEJBRemote) locator.lookup(VenSettlementRecordSessionEJBRemote.class, "VenSettlementRecordSessionEJBBean");
					List<VenSettlementRecord>VenSettlementRecordList = VenSettlementRecordHome.queryByRange("select o from VenSettlementRecord o where o.venOrderItem.orderItemId = " +  salesRecord.getVenOrderItem().getOrderItemId(), 0, 0);
					if(VenSettlementRecordList.size()>0){
						venSettlementRecord=VenSettlementRecordList.get(0);
						
						if(venSettlementRecord==null){
							throw new Exception("Settlement record not found");
						}
					}
					
					String commissionType = venSettlementRecord.getCommissionType();
					
					//nilai AP payment = sum hutang merchant, maka cara menghitungnya juga seperti ketika hitung hutang merchant.
					_log.info("venSettlementRecord.getCommissionType(): "+venSettlementRecord.getCommissionType());
					if(venSettlementRecord!=null){							
						if (salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName().equals(partyName)) {												
							if(commissionType.equals(VeniceConstants.VEN_SETTLEMENT_RECORD_COMMISSIONTYPE_REBATE)){
								apAmount=apAmount.add(salesRecord.getVenOrderItem().getTotal());
								_log.debug("apAmount: "+apAmount);
							}else if(commissionType.equals(VeniceConstants.VEN_SETTLEMENT_RECORD_COMMISSIONTYPE_COMMISSION)){
								apAmount=apAmount.add(salesRecord.getMerchantPaymentAmount());
								_log.debug("apAmount: "+apAmount);
							}
						}
					}				
									
					if(salesRecord.getPph23Amount()!=null){
						pph23 = pph23.add(salesRecord.getPph23Amount());
						_log.debug("pph23: "+pph23);
					}														
					
					String selectManualJournalTransaction = "select o from FinApManualJournalTransaction o where o.venParty.fullOrLegalName = '" + partyName + 
						"' and o.finJournalTransaction.finAccount.accountDesc='PIUTANG MERCHANT' and (o.finJournalTransaction.unpaidTransactionAmount <> 0 or o.finJournalTransaction.unpaidTransactionAmount is null)" +
						" and o.finApPayment is null and o.venOrder.orderId="+salesRecord.getVenOrderItem().getVenOrder().getOrderId();
					
					List<FinApManualJournalTransaction> finApManualJournalTransactionList = finApManualJournalTransactionSessionHome.queryByRange(selectManualJournalTransaction, 0, 0);
					
					for (int j=0;j<finApManualJournalTransactionList.size();j++) {
						FinApManualJournalTransaction finApManualJournalTransaction = finApManualJournalTransactionList.get(j);
						
						FinJournalTransaction journalTrans = new FinJournalTransaction();
						journalTrans = finApManualJournalTransaction.getFinJournalTransaction();
						
						//jumlahkan ar amount, tapi tidak boleh melebihi jumlah ap amount				
						BigDecimal compareAmount = new BigDecimal(0);
						if(commissionType.equals(VeniceConstants.VEN_SETTLEMENT_RECORD_COMMISSIONTYPE_COMMISSION)) {
							compareAmount=apAmount.add(pph23);
						}else {
							compareAmount=apAmount;
						}
						
						if(arAmount.compareTo(compareAmount)<=0){
							//jika unpaid tidak null maka yg dijumlahkan adalah unpaid amount nya
							if(finApManualJournalTransaction.getFinJournalTransaction().getUnpaidTransactionAmount()!=null){
								arAmount = arAmount.add(finApManualJournalTransaction.getFinJournalTransaction().getUnpaidTransactionAmount());
							}else{										
								arAmount = arAmount.add(finApManualJournalTransaction.getFinJournalTransaction().getTransactionAmount());
								
								//jika ada selisih ap dan ar maka simpan di unpaid amount
								if(arAmount.compareTo(compareAmount)>0){
									journalTrans.setUnpaidTransactionAmount(arAmount.subtract(apAmount));
								}
							}
							
							finApManualJournalTransactionsIds += finApManualJournalTransaction.getManualJournalTransactionId();
							finApManualJournalTransactionsIds += "#";

						}else if(arAmount.compareTo(compareAmount)>0){
							//jika sudah melebihi jumlah ap amount, set unpaid sesuai sisa ar amount yg tidak dijumlahkan
							journalTrans.setUnpaidTransactionAmount(finApManualJournalTransaction.getFinJournalTransaction().getTransactionAmount());	
						}							
						finJournalTransactionSessionHome.mergeFinJournalTransaction(journalTrans);							
					}
												
					_log.debug("apAmount total: "+apAmount);
					_log.debug("arAmount total: "+arAmount);
					_log.debug("pph23: "+pph23);
					
					BigDecimal balance = new BigDecimal(0);
					if(commissionType.equals(VeniceConstants.VEN_SETTLEMENT_RECORD_COMMISSIONTYPE_REBATE)){
						balance = apAmount.subtract(arAmount);
					}else if(commissionType.equals(VeniceConstants.VEN_SETTLEMENT_RECORD_COMMISSIONTYPE_COMMISSION)){							
						balance = (apAmount.add(pph23)).subtract(arAmount);
					}
					_log.debug("balance: "+balance);
					
					boolean addToList=false;
					if(i!=finSalesRecordList.size()-1){
						String partyNameNext = finSalesRecordList.get(i+1).getVenOrderItem().getVenMerchantProduct()!=null?(
								finSalesRecordList.get(i+1).getVenOrderItem().getVenMerchantProduct().getVenMerchant()!=null?(
										finSalesRecordList.get(i+1).getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty()!=null?
												finSalesRecordList.get(i+1).getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName():null):null):null;
												
						if(!partyName.equals(partyNameNext)){
							addToList=true;			
						}
					}
					
					if(i==finSalesRecordList.size()-1){
						addToList=true;
					}
								
					if(addToList){
						if(salesRecordIdsForParam.endsWith("#")){
							salesRecordIdsForParam=salesRecordIdsForParam.substring(0, salesRecordIdsForParam.lastIndexOf("#"));
						}
						if(finApManualJournalTransactionsIds.endsWith("#")){
							finApManualJournalTransactionsIds=finApManualJournalTransactionsIds.substring(0, finApManualJournalTransactionsIds.lastIndexOf("#"));
						}
						
						_log.debug("salesRecordIdsForParam: "+salesRecordIdsForParam);
						map.put(DataNameTokens.FINAPPAYMENT_AMOUNT, apAmount.toString());
						map.put(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT, arAmount.toString());
						map.put(DataNameTokens.FINAPPAYMENT_BALANCE, balance.toString());
						map.put(DataNameTokens.FINAPPAYMENT_FINSALESRECORDS, salesRecordIdsForParam);
						map.put(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTID, "1120101");
						map.put(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS, finApManualJournalTransactionsIds);
						map.put(DataNameTokens.FINSALESRECORD_PPH23_AMOUNT, pph23!=null?pph23.toString():"0");
						dataList.add(map);
					}
				}								
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+dataList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}	
}
