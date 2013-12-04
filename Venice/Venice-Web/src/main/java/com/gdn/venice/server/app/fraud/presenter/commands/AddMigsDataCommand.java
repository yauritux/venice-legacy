package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.Date;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.SQLDateUtility;
import com.gdn.venice.facade.VenMigsTransactionSessionEJBRemote;
import com.gdn.venice.facade.VenMigsUploadMasterSessionEJBRemote;
import com.gdn.venice.facade.VenMigsUploadTemporarySessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentSessionEJBRemote;
import com.gdn.venice.persistence.VenMigsTransaction;
import com.gdn.venice.persistence.VenMigsUploadMaster;
import com.gdn.venice.persistence.VenMigsUploadTemporary;
import com.gdn.venice.persistence.VenOrderPayment;

public class AddMigsDataCommand {
	String username;
	
	public AddMigsDataCommand(String username) {
		this.username = username;
	}
	
	public String execute() {
		String response = "Proccessing MIGS has been finished...";
		
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			VenMigsUploadTemporarySessionEJBRemote sessionHome = (VenMigsUploadTemporarySessionEJBRemote) locator.lookup(VenMigsUploadTemporarySessionEJBRemote.class, "VenMigsUploadTemporarySessionEJBBean");

			List<VenMigsUploadTemporary> migsUploadList = null;
			String query = "select o from VenMigsUploadTemporary o";
			migsUploadList = sessionHome.queryByRange(query, 0, 0);
			
			//TODO Seharusnya pakai transaction
			for (int i = 0; i < migsUploadList.size(); i++) {
				VenMigsUploadTemporary migsUpload = migsUploadList.get(i);

				VenMigsUploadMasterSessionEJBRemote masterHome = (VenMigsUploadMasterSessionEJBRemote) locator.lookup(VenMigsUploadMasterSessionEJBRemote.class, "VenMigsUploadMasterSessionEJBBean");
				
				VenMigsUploadMaster master;						
				try{
					 master = masterHome.queryByRange("select o from VenMigsUploadMaster o where o.authorisationCode = '" + migsUpload.getAuthorisationCode() + "' and o.transactionId = '" + migsUpload.getTransactionId() + "'", 0, 1).get(0);
				} catch (IndexOutOfBoundsException e){
					master = null;
				}
				
				if (migsUpload.getAction().equalsIgnoreCase("submit")) {
					VenOrderPaymentSessionEJBRemote venOrderPaymentSessionHome = (VenOrderPaymentSessionEJBRemote) locator.lookup(VenOrderPaymentSessionEJBRemote.class, "VenOrderPaymentSessionEJBBean");
					
					List<VenOrderPayment> orderPaymentList = null;
					String queryOrderPayment = "select op from VenOrderPaymentAllocation opa inner join opa.venOrder o inner join opa.venOrderPayment op" +
					" where o.wcsOrderId = '" + isNull(migsUpload.getMerchantTransactionReference(), "").replaceAll("-.*$", "") +
					"' and op.referenceId = '" + isNull(migsUpload.getAuthorisationCode(), "") + "'";

					VenMigsTransactionSessionEJBRemote migsTransactionSessionHome = (VenMigsTransactionSessionEJBRemote) locator.lookup(VenMigsTransactionSessionEJBRemote.class, "VenMigsTransactionSessionEJBBean");
					
					orderPaymentList = venOrderPaymentSessionHome.queryByRange(queryOrderPayment, 0, 0);
					for (int j = 0; j < orderPaymentList.size(); j++) {
						//Pindahkan MIGS dari tabel temporary ke tabel transaction
						VenMigsTransaction entityMigsTransaction = new VenMigsTransaction();
						entityMigsTransaction.setVenOrderPayment(orderPaymentList.get(j));
						entityMigsTransaction.setTransactionId(migsUpload.getTransactionId());
						entityMigsTransaction.setTransactionDate(migsUpload.getTransactionDate());
						entityMigsTransaction.setMerchantId(migsUpload.getMerchantId());
						entityMigsTransaction.setOrderReference(migsUpload.getOrderReference());
						entityMigsTransaction.setOrderId(migsUpload.getOrderId());
						entityMigsTransaction.setMerchantTransactionReference(migsUpload.getMerchantTransactionReference());
						entityMigsTransaction.setTransactionType(migsUpload.getTransactionType());
						entityMigsTransaction.setAcquirerId(migsUpload.getAcquirerId());
						entityMigsTransaction.setBatchNumber(migsUpload.getBatchNumber());
						entityMigsTransaction.setCurrency(migsUpload.getCurrency());
						entityMigsTransaction.setAmount(migsUpload.getAmount());
						entityMigsTransaction.setRrn(migsUpload.getRrn());
						entityMigsTransaction.setResponseCode(migsUpload.getResponseCode());
						entityMigsTransaction.setAcquirerResponseCode(migsUpload.getAcquirerResponseCode());
						entityMigsTransaction.setAuthorisationCode(migsUpload.getAuthorisationCode());
						entityMigsTransaction.setOperator(migsUpload.getOperator());
						entityMigsTransaction.setMerchantTransactionSource(migsUpload.getMerchantTransactionSource());
						entityMigsTransaction.setOrderDate(migsUpload.getOrderDate());
						entityMigsTransaction.setCardType(migsUpload.getCardType());
						entityMigsTransaction.setCardNumber(migsUpload.getCardNumber());
						entityMigsTransaction.setCardExpiryMonth(migsUpload.getCardExpiryMonth());
						entityMigsTransaction.setCardExpiryYear(migsUpload.getCardExpiryYear());
						entityMigsTransaction.setDialectCscResultCode(migsUpload.getDialectCscResultCode());
						entityMigsTransaction.setCreatedBy(username);
						entityMigsTransaction.setCreatedDate(SQLDateUtility.utilDateToSqlTimestamp(new Date()));
						entityMigsTransaction.setIsActive(new Boolean(true));
						entityMigsTransaction.setComment(migsUpload.getComment());
						entityMigsTransaction.setEcommerceIndicator(migsUpload.getEcommerceIndicator());
						entityMigsTransaction.setFileName(migsUpload.getFileName());
						migsTransactionSessionHome.persistVenMigsTransaction(entityMigsTransaction);
						
						//Remove dari temporary tabel
						VenMigsUploadTemporary entityMigsUpload = new VenMigsUploadTemporary();
						entityMigsUpload.setMigsId(migsUpload.getMigsId());
						sessionHome.removeVenMigsUploadTemporary(entityMigsUpload);
						
						//Update data nomer kartu kredit di tabel order payment
						VenOrderPayment entityOrderPayment;
						try{
							entityOrderPayment = venOrderPaymentSessionHome.queryByRange("select o from VenOrderPayment o where o.orderPaymentId="+orderPaymentList.get(j).getOrderPaymentId(), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							entityOrderPayment = new VenOrderPayment();
							entityOrderPayment.setOrderPaymentId(orderPaymentList.get(j).getOrderPaymentId());
						}
						entityOrderPayment.setMaskedCreditCardNumber(migsUpload.getCardNumber());
						if(entityOrderPayment.getThreeDsSecurityLevelAuth()==null && migsUpload.getEcommerceIndicator()!=null){
							entityOrderPayment.setThreeDsSecurityLevelAuth(migsUpload.getEcommerceIndicator());
						}
						venOrderPaymentSessionHome.mergeVenOrderPayment(entityOrderPayment);
					}
					if(master!=null){
						master.setAction("SUBMIT");
						masterHome.mergeVenMigsUploadMaster(master);
					}
				} else if (migsUpload.getAction().equalsIgnoreCase("remove")) {
					VenMigsUploadTemporary entityMigsUpload = new VenMigsUploadTemporary();
					entityMigsUpload.setMigsId(migsUpload.getMigsId());
					sessionHome.removeVenMigsUploadTemporary(entityMigsUpload);
					if(master!=null){
						master.setAction("REMOVE");
						masterHome.mergeVenMigsUploadMaster(master);
					}
				} else if (migsUpload.getAction().equalsIgnoreCase("keep")) {
					if(master!=null){
						master.setAction("KEEP");
						masterHome.mergeVenMigsUploadMaster(master);
					}
				} else if (migsUpload.getAction().equalsIgnoreCase("overwrite")) {
					VenOrderPaymentSessionEJBRemote venOrderPaymentSessionHome = (VenOrderPaymentSessionEJBRemote) locator.lookup(VenOrderPaymentSessionEJBRemote.class, "VenOrderPaymentSessionEJBBean");
					
					List<VenOrderPayment> orderPaymentList = null;
					String queryOrderPayment = "select op from VenOrderPaymentAllocation opa inner join opa.venOrder o inner join opa.venOrderPayment op" +
					" where o.wcsOrderId = '" + isNull(migsUpload.getMerchantTransactionReference(), "").replaceAll("-.*$", "") +
					"' and op.referenceId = '" + isNull(migsUpload.getAuthorisationCode(), "") + "'";
					
					orderPaymentList = venOrderPaymentSessionHome.queryByRange(queryOrderPayment, 0, 0);
					for (int j = 0; j < orderPaymentList.size(); j++) {
						//Pindahkan MIGS dari tabel temporary ke tabel transaction
						VenMigsTransactionSessionEJBRemote migsTransactionSessionHome = (VenMigsTransactionSessionEJBRemote) locator.lookup(VenMigsTransactionSessionEJBRemote.class, "VenMigsTransactionSessionEJBBean");
						
						//Update yang lama, dengan merubah status menjadi isactive = false
						String queryMigsTransaction = "select o from VenMigsTransaction o" +
						" where o.merchantTransactionReference = '" + isNull(migsUpload.getMerchantTransactionReference(), "") +
						"' and o.authorisationCode = '" + isNull(migsUpload.getAuthorisationCode(), "") + "'";
						
						List<VenMigsTransaction> migsTransactionList = migsTransactionSessionHome.queryByRange(queryMigsTransaction, 0, 0);
						for (int k = 0; k < migsTransactionList.size(); k++) {
							VenMigsTransaction entityMigsTransactionRemove;
							try
							{
								entityMigsTransactionRemove = migsTransactionSessionHome.queryByRange("select o from VenMigsTransaction o where o.migsId="+migsTransactionList.get(0).getMigsId(), 0, 1).get(0);
							}catch(IndexOutOfBoundsException e){
								entityMigsTransactionRemove = new VenMigsTransaction();
								entityMigsTransactionRemove.setMigsId(migsTransactionList.get(0).getMigsId());
							}
							entityMigsTransactionRemove.setIsActive(new Boolean(false));
							migsTransactionSessionHome.mergeVenMigsTransaction(entityMigsTransactionRemove);
						}
						
						//Tambahkan yang baru
						VenMigsTransaction entityMigsTransaction = new VenMigsTransaction();
						entityMigsTransaction.setVenOrderPayment(orderPaymentList.get(j));
						entityMigsTransaction.setTransactionId(migsUpload.getTransactionId());
						entityMigsTransaction.setTransactionDate(migsUpload.getTransactionDate());
						entityMigsTransaction.setMerchantId(migsUpload.getMerchantId());
						entityMigsTransaction.setOrderReference(migsUpload.getOrderReference());
						entityMigsTransaction.setOrderId(migsUpload.getOrderId());
						entityMigsTransaction.setMerchantTransactionReference(migsUpload.getMerchantTransactionReference());
						entityMigsTransaction.setTransactionType(migsUpload.getTransactionType());
						entityMigsTransaction.setAcquirerId(migsUpload.getAcquirerId());
						entityMigsTransaction.setBatchNumber(migsUpload.getBatchNumber());
						entityMigsTransaction.setCurrency(migsUpload.getCurrency());
						entityMigsTransaction.setAmount(migsUpload.getAmount());
						entityMigsTransaction.setRrn(migsUpload.getRrn());
						entityMigsTransaction.setResponseCode(migsUpload.getResponseCode());
						entityMigsTransaction.setAcquirerResponseCode(migsUpload.getAcquirerResponseCode());
						entityMigsTransaction.setAuthorisationCode(migsUpload.getAuthorisationCode());
						entityMigsTransaction.setOperator(migsUpload.getOperator());
						entityMigsTransaction.setMerchantTransactionSource(migsUpload.getMerchantTransactionSource());
						entityMigsTransaction.setOrderDate(migsUpload.getOrderDate());
						entityMigsTransaction.setCardType(migsUpload.getCardType());
						entityMigsTransaction.setCardNumber(migsUpload.getCardNumber());
						entityMigsTransaction.setCardExpiryMonth(migsUpload.getCardExpiryMonth());
						entityMigsTransaction.setCardExpiryYear(migsUpload.getCardExpiryYear());
						entityMigsTransaction.setDialectCscResultCode(migsUpload.getDialectCscResultCode());
						entityMigsTransaction.setCreatedBy(username);
						entityMigsTransaction.setCreatedDate(SQLDateUtility.utilDateToSqlTimestamp(new Date()));
						entityMigsTransaction.setIsActive(new Boolean(true));
						entityMigsTransaction.setComment(migsUpload.getComment());
						entityMigsTransaction.setEcommerceIndicator(migsUpload.getEcommerceIndicator());
						entityMigsTransaction.setFileName(migsUpload.getFileName());
						migsTransactionSessionHome.persistVenMigsTransaction(entityMigsTransaction);
						
						//Remove dari temporary tabel
						VenMigsUploadTemporary entityMigsUpload = new VenMigsUploadTemporary();
						entityMigsUpload.setMigsId(migsUpload.getMigsId());
						sessionHome.removeVenMigsUploadTemporary(entityMigsUpload);
						
						//Update data nomer kartu kredit di tabel order payment
						VenOrderPayment entityOrderPayment;
						try
						{
							entityOrderPayment = venOrderPaymentSessionHome.queryByRange("select o from VenOrderPayment o where o.orderPaymentId="+orderPaymentList.get(j).getOrderPaymentId(), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							entityOrderPayment = new VenOrderPayment();
							entityOrderPayment.setOrderPaymentId(orderPaymentList.get(j).getOrderPaymentId());
						}
						entityOrderPayment.setMaskedCreditCardNumber(migsUpload.getCardNumber());
						venOrderPaymentSessionHome.mergeVenOrderPayment(entityOrderPayment);
					}
					if(master!=null){
						master.setAction("OVERWRITE");
						masterHome.mergeVenMigsUploadMaster(master);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (locator != null) {
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return response;
	}

	private String isNull(Object object, String replacement) {
		return object == null ? replacement : object.toString();
	}
}