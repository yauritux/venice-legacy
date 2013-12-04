package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.FrdParameterRule31SessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule31;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.server.command.RafRpcCommand;

public class SaveGenuineDataCommand implements RafRpcCommand {
	String caseId;
	
	public SaveGenuineDataCommand(String caseId) {
		this.caseId = caseId;
	}
	
	@Override
	public String execute() {
		//get order id
		Locator<Object> locator = null;
		String returnResault="0";
		try{
			locator = new Locator<Object>();
			FrdParameterRule31SessionEJBRemote genuineSessionHome = (FrdParameterRule31SessionEJBRemote) locator.lookup(FrdParameterRule31SessionEJBRemote.class, "FrdParameterRule31SessionEJBBean");
			VenOrderPaymentAllocationSessionEJBRemote allocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			List<VenOrderPaymentAllocation> itemAllocation = allocationSessionHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId = (select u.venOrder.orderId from FrdFraudSuspicionCase u where u.fraudSuspicionCaseId =  "+ caseId+")", 0, 0);
			if(itemAllocation.get(0)!=null?(itemAllocation.get(0).getVenOrderPayment().getMaskedCreditCardNumber()!=null?true:false):false){
					List<FrdParameterRule31> genuineList = genuineSessionHome.queryByRange("select o from FrdParameterRule31 o where o.email ='"+itemAllocation.get(0).getVenOrder().getVenCustomer().getCustomerUserName()+"' and o.noCc ='"+itemAllocation.get(0).getVenOrderPayment().getMaskedCreditCardNumber()+"' ", 0, 0);
					
					if(genuineList.isEmpty()){
						FrdParameterRule31 entityRule31 = new FrdParameterRule31();
						entityRule31.setEmail(itemAllocation.get(0).getVenOrder().getVenCustomer().getCustomerUserName());
						entityRule31.setNoCc(itemAllocation.get(0).getVenOrderPayment().getMaskedCreditCardNumber());		
						genuineSessionHome.persistFrdParameterRule31(entityRule31);
					}else{
						returnResault="-1&Genuine is already Exist!!!";
					}
			}else{
				returnResault="-1&Error data! Allocation data is null!!!";			
			}		
			
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
		return returnResault;
	}

}
