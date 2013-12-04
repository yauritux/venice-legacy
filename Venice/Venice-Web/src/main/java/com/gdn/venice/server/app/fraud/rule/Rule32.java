package com.gdn.venice.server.app.fraud.rule;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule32SessionEJBRemote;
import com.gdn.venice.facade.VenMerchantProductSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule32;
import com.gdn.venice.persistence.VenMerchantProduct;
import com.gdn.venice.persistence.VenOrderItem;

/**
 * Class for calculate fraud rule 32: Collection Blacklist  
 *
 * @author Arifin
 */

public class Rule32 {
	protected static Logger _log = null;
    
    public Rule32() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule32");
    }
    
    public int execute(Long orderId) throws Exception{
    	_log.info("Start execute rule 32");
        int fraudPoint=0;  
        Locator<Object> locator = null;
      
        try{
            locator = new Locator<Object>();
            VenOrderItemSessionEJBRemote orderItemSessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
            VenMerchantProductSessionEJBRemote merchantProductSessionHome = (VenMerchantProductSessionEJBRemote) locator.lookup(VenMerchantProductSessionEJBRemote.class, "VenMerchantProductSessionEJBBean");
            FrdParameterRule32SessionEJBRemote rule32SessionHome = (FrdParameterRule32SessionEJBRemote) locator.lookup(FrdParameterRule32SessionEJBRemote.class, "FrdParameterRule32SessionEJBBean");
            /*
             * get data orderItem
             */
            List<VenOrderItem> orderItemList = orderItemSessionHome.queryByRange("select o from VenOrderItem o where o.venOrder.orderId="+orderId, 0, 0);
            /*
             * cari jenis collectionnya dan cek apakah termasuk didalam collection blacklist. jika termasuk didalam collection blacklist maka
             */
            if(orderItemList.size()>0){  
				List<String> catList = new ArrayList<String>();
                for(int i=0;i<orderItemList.size();i++){      
                    List<VenMerchantProduct> merchantProductList = merchantProductSessionHome.queryByRange("select o from VenMerchantProduct o where o.productId="+orderItemList.get(i).getVenMerchantProduct().getProductId(), 0, 1);
                    if(merchantProductList.size()>0){  
                        String category="";  
                        boolean trueOrFalse = true;
                        for(int j=0;j<merchantProductList.get(0).getVenProductCategories().size();j++){      
                			if(merchantProductList.get(0).getVenProductCategories().get(j).getLevel().equals(2)){
				                            category=merchantProductList.get(0).getVenProductCategories().get(j).getProductCategory().trim();
				                            for(String itemCat : catList){
												if(itemCat.equals(category)){
													trueOrFalse=false;		
													break;
												}
											}
				                            if(trueOrFalse){
													catList.add(category);
						                            String[] catItem=null;
						                            if(category.contains("(")){
						                                    catItem = category.split("\\(");
						                                    category=catItem[0].trim();
						                            }                                                                          
						                            List<FrdParameterRule32> parameterList = rule32SessionHome.queryByRange("select o from FrdParameterRule32 o where UPPER(o.productType)='"+category.toUpperCase()+"'", 0, 1);                                  
						                            if(parameterList.size()>0){                                              
						                                    //tambahkan risk point untuk setiap order item yg category nya match dengan parameter
						                                    fraudPoint+=parameterList.get(0).getRiskPoint();
						                                    _log.info("category match: "+parameterList.get(0).getProductType());
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
        _log.info("Done execute rule 32, fraudPoint is: "+fraudPoint);
        return fraudPoint;
    }
}
