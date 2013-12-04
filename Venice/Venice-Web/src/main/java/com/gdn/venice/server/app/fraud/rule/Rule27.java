package com.gdn.venice.server.app.fraud.rule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.facade.fraud.FraudRuleWithNativeQueryRemote;
import com.gdn.venice.persistence.FrdParameterRule26272829;
import com.gdn.venice.persistence.VenOrder;
/**
 * Class for calculate fraud rule 27: Same Product Name & Customer Email in one week
 * 
 * @author Arifin
 */
public class Rule27 {
	protected static Logger _log = null;
    
    public Rule27() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule27");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 27");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
		    FraudRuleWithNativeQueryRemote rule27SessionHome = (FraudRuleWithNativeQueryRemote) locator.lookup(FraudRuleWithNativeQueryRemote.class, "FraudRuleWithNativeQuerySessionEJBBean");
			
			SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			/*
			 * range tanggal 7 hari kebelakang
			 */
			Date toWeek = DateUtils.addWeeks(venOrder.getOrderDate(),-1);	
			String dateEnd=dd.format(venOrder.getOrderDate().getTime());
			String dateStart=dd.format(toWeek.getTime());
			/*
			 * query order dengan customer yang sama selama 7 hari kebelakang dan dengan menggunakan product id yang sama dengan
			 * product id order baru
			 */
		    List<FrdParameterRule26272829> frdParameterRule27List=rule27SessionHome.queryByRangeRule26272829("select * from Frd_Parameter_Rule_26_27_28_29 where id="+DataConstantNameTokens.FRAUD_RULE_27_ID+
					" and (select count(*) from ven_order_item a"+
					" inner join (select * from ven_order where customer_id="+venOrder.getVenCustomer().getCustomerId()+
					" and order_date  between '"+dateStart+"' and '"+dateEnd+"'"+
					" and order_id<>"+venOrder.getOrderId()+" ) b on a.order_id=b.order_id"+
					" inner join ven_merchant_product c on c.product_id=a.product_id"+
					" where c.product_id in (select product_id  from ven_order_item where order_id="+venOrder.getOrderId()+")"+
					" or c.wcs_product_sku in (select wcs_product_sku from ven_merchant_product where product_id in (select product_id  from ven_order_item where order_id="+venOrder.getOrderId()+")) )>0");   
		    
				if(frdParameterRule27List.size()>0){
					fraudPoint=frdParameterRule27List.get(0).getRiskPoint();		
					_log.info("parameter match");
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
		_log.info("Done execute rule 27, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
