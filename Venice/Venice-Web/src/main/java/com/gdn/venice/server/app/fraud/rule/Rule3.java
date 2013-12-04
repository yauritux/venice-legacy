package com.gdn.venice.server.app.fraud.rule;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule3SessionEJBRemote;
import com.gdn.venice.facade.VenMerchantProductSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule3;
import com.gdn.venice.persistence.VenMerchantProduct;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderItem;

/**
 * Class for calculate fraud rule 3: Order that include several of the same time
 * 
 * @author Roland
 */

public class Rule3 {
	protected static Logger _log = null;
    
    public Rule3() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule3");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 3");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			List<FrdParameterRule3> parameterList=null;
			locator = new Locator<Object>();
			VenOrderItemSessionEJBRemote orderItemSessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
			VenMerchantProductSessionEJBRemote merchantProductSessionHome = (VenMerchantProductSessionEJBRemote) locator.lookup(VenMerchantProductSessionEJBRemote.class, "VenMerchantProductSessionEJBBean");
			FrdParameterRule3SessionEJBRemote rule3SessionHome = (FrdParameterRule3SessionEJBRemote) locator.lookup(FrdParameterRule3SessionEJBRemote.class, "FrdParameterRule3SessionEJBBean");
																	
			List<VenOrderItem> orderItemList = orderItemSessionHome.queryByRange("select o from VenOrderItem o where o.venOrder.orderId="+venOrder.getOrderId(), 0, 0);
			
			if(orderItemList.size()>0){	
				String category="";
				List<String> catList = new ArrayList<String>();
				//ambil product category untuk tiap order item (ada 3 level category), kemudian bandingkan category tsb dengan category dari parameter.
				for(int i=0;i<orderItemList.size();i++){	
					List<VenMerchantProduct> merchantProductList = merchantProductSessionHome.queryByRange("select o from VenMerchantProduct o where o.productId="+orderItemList.get(i).getVenMerchantProduct().getProductId(), 0, 1);
					if(merchantProductList.size()>0){	
						String [] categorySplit=null;
						boolean trueOrFalse = true;
						for(int j=0;j<merchantProductList.get(0).getVenProductCategories().size();j++){
							if(merchantProductList.get(0).getVenProductCategories().get(j).getLevel().equals(2)){
								category=merchantProductList.get(0).getVenProductCategories().get(j).getProductCategory();
								for(String itemCat : catList){
									if(itemCat.equals(category)){
										trueOrFalse=false;						
										break;
									}
								}
								if(trueOrFalse){
									catList.add(category);
									if(category.contains("(")){
										categorySplit=category.split("\\(");
										category=categorySplit[0].trim();
									}
									parameterList = rule3SessionHome.queryByRange("select o from FrdParameterRule3 o where UPPER(o.category) = '"+category.toUpperCase()+"' and "+orderItemList.get(i).getQuantity()+">o.minQty", 0, 1);							
									if(parameterList.size()>0){						
										fraudPoint+=parameterList.get(0).getRiskPoint();
										_log.info("category match: "+parameterList.get(0).getCategory()+", min qty: "+parameterList.get(0).getMinQty());
									}
								}
							}
						}
					}
				}				
			}					
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception();
		} finally {
			try {
				if (locator != null) {
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		_log.info("Done execute rule 3, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
