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
 * Class for calculate fraud rule 29: Same Customer Address in one week
 * 
 * @author Arifin
 */
public class Rule29 {
	protected static Logger _log = null;
    
    public Rule29() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule29");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 29");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
			FraudRuleWithNativeQueryRemote rule29SessionHome = (FraudRuleWithNativeQueryRemote) locator.lookup(FraudRuleWithNativeQueryRemote.class, "FraudRuleWithNativeQuerySessionEJBBean");
				/*
				 * range tanggal 7 hari kebelakang
				 */
			SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date toWeek = DateUtils.addWeeks(venOrder.getOrderDate(),-1);	
			String dateEnd=dd.format(venOrder.getOrderDate().getTime());
			String dateStart=dd.format(toWeek.getTime());
			/*
			 * query order selama 7 hari kebelakang 
			 * cari apakah ada address seperti order ini selama 7 hari sebelumnya. jika ada maka akan kena risk point
			 */
			
			if(venOrder.getVenCustomer()!=null){
				if(venOrder.getVenCustomer().getVenParty()!=null){
					List<FrdParameterRule26272829> frdParameterRule29List=rule29SessionHome.queryByRangeRule26272829("select * from Frd_Parameter_Rule_26_27_28_29 where id="+DataConstantNameTokens.FRAUD_RULE_29_ID+
							" and (select count(*) from ven_order a inner join  ven_order_address b on a.order_id=b.order_id inner join ven_address c on b.address_id=c.address_id where a.order_date between '"+ dateStart +"' and '"+dateEnd+"' and a.order_id<>"+venOrder.getOrderId()+" and  c.street_address_1 is not null"+
							" and c.street_address_1 in (select e.street_address_1 from ven_address e inner join ven_order_address f on f.address_id=e.address_id where f.order_id="+venOrder.getOrderId()+"  ) )>0");
					
						if(frdParameterRule29List.size()>0){
							fraudPoint=frdParameterRule29List.get(0).getRiskPoint();	
							_log.info("parameter match");
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
		_log.info("Done execute rule 29, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
