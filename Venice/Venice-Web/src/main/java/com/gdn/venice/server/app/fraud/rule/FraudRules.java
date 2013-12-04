package com.gdn.venice.server.app.fraud.rule;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.facade.FrdBlacklistReasonSessionEJBRemote;
import com.gdn.venice.facade.FrdCustomerWhitelistBlacklistSessionEJBRemote;
import com.gdn.venice.facade.FrdEntityBlacklistSessionEJBRemote;
import com.gdn.venice.facade.FrdFraudCaseHistorySessionEJBRemote;
import com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote;
import com.gdn.venice.facade.FrdFraudSuspicionPointSessionEJBRemote;
import com.gdn.venice.facade.FrdRuleConfigTresholdSessionEJBRemote;
import com.gdn.venice.facade.VenAddressSessionEJBRemote;
import com.gdn.venice.facade.VenOrderAddressSessionEJBRemote;
import com.gdn.venice.facade.VenOrderContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.persistence.FrdBlacklistReason;
import com.gdn.venice.persistence.FrdCustomerWhitelistBlacklist;
import com.gdn.venice.persistence.FrdEntityBlacklist;
import com.gdn.venice.persistence.FrdFraudCaseHistory;
import com.gdn.venice.persistence.FrdFraudCaseHistoryPK;
import com.gdn.venice.persistence.FrdFraudCaseStatus;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.persistence.FrdFraudSuspicionPoint;
import com.gdn.venice.persistence.FrdRuleConfigTreshold;
import com.gdn.venice.persistence.VenAddress;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderAddress;
import com.gdn.venice.persistence.VenOrderContactDetail;
import com.gdn.venice.persistence.VenOrderItemContactDetail;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;

/**
 * Class for calling and execute fraud rules
 * 
 * @author Roland
 */

public class FraudRules {
	protected static Logger _log = null;
    
    public FraudRules() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.FraudRules");
    }
    
	public boolean calculateFraudRules(VenOrder venOrder){
		_log.info("Check whitelist and blacklist");
		Boolean isFP=false;
		Boolean isFC=false;
		String descResultCal="Calculated by System";
				
		if(isIPAddressWhitelistBlacklist(venOrder.getIpAddress(), "blacklist")){
			isFC=true;
			descResultCal=descResultCal + "\nIP BlackList";
		}
		
		if(isIPAddressWhitelistBlacklist(venOrder.getIpAddress(), "whitelist")){
			isFP=true;
			descResultCal=descResultCal + "\nIP WhiteList";
		}
		
		ArrayList<String>reasonList =isCustomerWhitelistBlacklist(venOrder);
		if(reasonList.size()>0){
			isFC=true;
			for(String itemReason : reasonList){
				if(!descResultCal.contains(itemReason)){
					descResultCal=descResultCal + "\n" +itemReason;
				}				
			}
		}		
				
		_log.info("Calculate fraud rules");
		boolean isSuccessCalculate=true;
		int totalFraudPoints=0;
		int pointRule1=0, pointRule2=0, pointRule3=0, pointRule4=0, pointRule5=0, pointRule6=0, pointRule7=0, pointRule8=0, pointRule9=0, pointRule10=0, pointRule11=0, pointRule12=0, 
		pointRule13=0, pointRule14=0, pointRule15=0, pointRule16=0, pointRule17=0, pointRule18=0, pointRule19=0, pointRule20=0, pointRule21=0, pointRule22=0, pointRule23=0, 
		pointRule24=0, pointRule25=0, pointRule26=0, pointRule27=0, pointRule28=0, pointRule29=0, pointRule30=0, pointRule31=0, pointRule32=0, pointRule33=0, pointRule34=0,
		pointRule35=0, pointRule36=0,pointRule37=0, pointRule38=0, pointRule39=0, pointRule40=0;
		
		Locator<Object> locator = null;
		try{
			Rule1 rule1 = new Rule1();
			pointRule1=rule1.execute(venOrder);
			
			Rule2 rule2 = new Rule2();
			pointRule2=rule2.execute(venOrder);	
			
			Rule3 rule3 = new Rule3();
			pointRule3=rule3.execute(venOrder);	
			
			Rule4 rule4 = new Rule4();
			pointRule4=rule4.execute(venOrder);	
			
			Rule5 rule5 = new Rule5();
			pointRule5=rule5.execute(venOrder);
			
			Rule6 rule6 = new Rule6();
			pointRule6=rule6.execute(venOrder);
			
			Rule7 rule7 = new Rule7();
			pointRule7=rule7.execute(venOrder);
			
			Rule8 rule8 = new Rule8();
			pointRule8=rule8.execute(venOrder);
			
			Rule9 rule9 = new Rule9();
			pointRule9=rule9.execute(venOrder);
			
			Rule10 rule10 = new Rule10();
			pointRule10=rule10.execute(venOrder);
			
			Rule11 rule11 = new Rule11();
			pointRule11=rule11.execute(venOrder);
			
			Rule12 rule12 = new Rule12();
			pointRule12=rule12.execute(venOrder);
			
			Rule13 rule13 = new Rule13();
			pointRule13=rule13.execute(venOrder);
			
			Rule14 rule14 = new Rule14();
			pointRule14=rule14.execute(venOrder);
			
			Rule15 rule15 = new Rule15();
			pointRule15=rule15.execute(venOrder);			
			
			Rule16 rule16 = new Rule16();
			pointRule16=rule16.execute(venOrder);			
			
			Rule17 rule17 = new Rule17();
			pointRule17=rule17.execute(venOrder);		
			
			Rule18 rule18 = new Rule18();
			pointRule18=rule18.execute(venOrder.getOrderId());	
			
			Rule19 rule19 = new Rule19();
			pointRule19=rule19.execute(venOrder.getOrderId());	
			
			Rule20 rule20 = new Rule20();
			pointRule20=rule20.execute(venOrder);	
			
			Rule21 rule21 = new Rule21();
			pointRule21=rule21.execute(venOrder);	
			
			Rule22 rule22 = new Rule22();
			pointRule22=rule22.execute(venOrder);	
			
			Rule23 rule23 = new Rule23();
			pointRule23=rule23.execute(venOrder.getOrderId());			
			
			Rule24 rule24 = new Rule24();
			pointRule24=rule24.execute(venOrder);
			
			Rule25 rule25 = new Rule25();
			pointRule25=rule25.execute(venOrder.getOrderId());
			pointRule25=pointRule25>0?(pointRule6+pointRule9+pointRule11+pointRule17+pointRule18+pointRule19)*-1:0;
			
			Rule26 rule26 = new Rule26();
			pointRule26=rule26.execute(venOrder);
			
			Rule27 rule27 = new Rule27();
			pointRule27=rule27.execute(venOrder);
			
			Rule28 rule28 = new Rule28(); 
			pointRule28=rule28.execute(venOrder);
			
			Rule29 rule29 = new Rule29();
			pointRule29=rule29.execute(venOrder);
			
			Rule30 rule30 = new Rule30();
			pointRule30=rule30.execute(venOrder.getOrderId());
			
			Rule31 rule31 = new Rule31();
			pointRule31=rule31.execute(venOrder.getOrderId());
			pointRule31=pointRule31>0?(pointRule7+pointRule8+pointRule10+pointRule16)*-1:0;
			
			Rule32 rule32 = new Rule32();
			pointRule32=rule32.execute(venOrder.getOrderId());		
			
			if(isFC) pointRule36=500;
			
			Rule33 rule33 = new Rule33();
			pointRule33=rule33.execute(venOrder);
			if(pointRule33<0) { 
				pointRule33=pointRule33-pointRule7-pointRule8-pointRule10-pointRule16; 
				/*
				 * jika cc tidak terdaftar di bin (yaitu pointRule15>0) dan eci 5
				 * maka status order di SF dengan cara menginisialisasi FP dan FC adalah true
				 */
				if(pointRule15>0) isFC=true;						
				isFP=true;
				descResultCal=descResultCal + " : E-Commerce Indicator 5";
			}else if(pointRule33>0){ 
				isFC=true;	
				descResultCal=descResultCal + " : E-Commerce Indicator 7";	
			}
			
			Rule34 rule34 = new Rule34();
			pointRule34=rule34.execute(venOrder.getOrderId(), venOrder.getIpAddress());
			
			Rule35 rule35 = new Rule35();
			pointRule35=rule35.execute(venOrder);
			if(pointRule35==50){
				locator = new Locator<Object>();
				FrdBlacklistReasonSessionEJBRemote blacklistReasonSessionHome = (FrdBlacklistReasonSessionEJBRemote) locator.lookup(FrdBlacklistReasonSessionEJBRemote.class, "FrdBlacklistReasonSessionEJBBean");
				List<FrdBlacklistReason>greyListReasonList = blacklistReasonSessionHome.queryByRange("select o from FrdBlacklistReason o where o.orderId="+venOrder.getOrderId()+" and o.blacklistReason like '%grey list%'", 0, 0);
					
				if(greyListReasonList.size()>0){
					ArrayList<String> greyListReason=new ArrayList<String>();
					for(int i=0;i<greyListReasonList.size();i++){
						greyListReason.add(greyListReasonList.get(i).getBlacklistReason());
					}
					
					for(String reason : greyListReason){
						if(!descResultCal.contains(reason)){
							descResultCal=descResultCal + "\n" +reason;
						}				
					}
				}
			}
			
			Rule37 rule37 = new Rule37();
			pointRule37=rule37.execute(venOrder.getOrderId(),venOrder.getWcsOrderId());		
			
			Rule38 rule38 = new Rule38();
			pointRule38=rule38.execute(venOrder.getOrderId(),venOrder.getWcsOrderId());		
			
			Rule39 rule39 = new Rule39();
			pointRule39=rule39.execute(venOrder.getOrderId());
			
			Rule40 rule40 = new Rule40();
			pointRule40=rule40.execute(venOrder.getOrderId());
												
			totalFraudPoints = pointRule1+pointRule2+pointRule3+pointRule4+pointRule5+pointRule6+pointRule7+pointRule8+pointRule9+pointRule10+pointRule11+pointRule12+pointRule13+
			pointRule14+pointRule15+pointRule16+pointRule17+pointRule18+pointRule19+pointRule20+pointRule21+pointRule22+pointRule23+pointRule24+pointRule25+pointRule26+pointRule27+
			pointRule28+pointRule29+pointRule30+pointRule31+pointRule32+pointRule33+pointRule34+pointRule35+pointRule36+pointRule37+pointRule38+pointRule39+pointRule40;
			_log.info("Done calculate fraud rules, total fraud points is: "+totalFraudPoints);
		}catch(Exception e){
			_log.error("Fraud calculate failed for wcs order id: "+venOrder.getWcsOrderId());
			isSuccessCalculate=false;			
			e.printStackTrace();
		}finally{
			try {
				if(locator!=null){
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//insert data to fraud suspicion case
		if(isSuccessCalculate){
			_log.debug("insert fraud suspicion case record");
			List<FrdFraudSuspicionCase> fraudCaseList =null;
			try {
				locator = new Locator<Object>();
				FrdFraudSuspicionCaseSessionEJBRemote fraudCaseSessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) locator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");
				FrdFraudSuspicionPointSessionEJBRemote fraudPointSessionHome = (FrdFraudSuspicionPointSessionEJBRemote) locator.lookup(FrdFraudSuspicionPointSessionEJBRemote.class, "FrdFraudSuspicionPointSessionEJBBean");
				FrdFraudCaseHistorySessionEJBRemote fraudCaseHistorySessionHome = (FrdFraudCaseHistorySessionEJBRemote) locator.lookup(FrdFraudCaseHistorySessionEJBRemote.class, "FrdFraudCaseHistorySessionEJBBean");
				FrdRuleConfigTresholdSessionEJBRemote configSessionHome = (FrdRuleConfigTresholdSessionEJBRemote) locator.lookup(FrdRuleConfigTresholdSessionEJBRemote.class, "FrdRuleConfigTresholdSessionEJBBean");
				
				String queryMinimalPoint = "select o from FrdRuleConfigTreshold o where o.key = 'FRAUD_PASS_MIN_RISK_POINT'";
				List<FrdRuleConfigTreshold> configList = configSessionHome.queryByRange(queryMinimalPoint, 0, 1);
				int minimalPoint=0;
				if(configList.size()>0){
					minimalPoint = new Integer (configList.get(0).getValue());
				}
				
				String fraudStatus="";
				FrdFraudCaseStatus frdFraudCaseStatus= new FrdFraudCaseStatus();
				if(isFP==true && isFC==false){
					fraudStatus=DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSCODE_FP;
					frdFraudCaseStatus.setFraudCaseStatusId(DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSID_FP);
				}else if(isFP==false && isFC==true){
					fraudStatus=DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSCODE_FC;
					frdFraudCaseStatus.setFraudCaseStatusId(DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSID_FC);
				}else if(isFP==true && isFC==true){
					fraudStatus=DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSCODE_SF;
					frdFraudCaseStatus.setFraudCaseStatusId(DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSID_SF);
				}else if(isFP==false && isFC==false){
					if(totalFraudPoints>minimalPoint){
						_log.info("totalFraudPoints > minimalPoint, set status to SF");
						fraudStatus=DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSCODE_SF;
						frdFraudCaseStatus.setFraudCaseStatusId(DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSID_SF);
					}else if(totalFraudPoints<=minimalPoint){
						_log.info("totalFraudPoints <= minimalPoint, set status to FP");
						fraudStatus=DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSCODE_FP;
						frdFraudCaseStatus.setFraudCaseStatusId(DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSID_FP);
					}
				}
				_log.debug("Fraud status is: "+fraudStatus);	
				
				FrdFraudSuspicionCase  fraudCase = new FrdFraudSuspicionCase();
				fraudCase.setVenOrder(venOrder);
				fraudCase.setFraudCaseDateTime(new Timestamp(System.currentTimeMillis()));
				fraudCase.setFraudTotalPoints(totalFraudPoints);
				fraudCase.setIlogFraudStatus(fraudStatus);				
				fraudCase.setFrdFraudCaseStatus(frdFraudCaseStatus);
				fraudCase.setFraudCaseDesc(descResultCal);
				fraudCase.setSuspicionReason("Calculated by System");
				fraudCase.setFraudSuspicionNotes("Calculated by System");
				
				String selectFraudCase = "select o from FrdFraudSuspicionCase o where o.venOrder.orderId = " + venOrder.getOrderId();
				fraudCaseList = fraudCaseSessionHome.queryByRange(selectFraudCase, 0, 1);
				
				if(fraudCaseList==null || fraudCaseList.size()==0){	
					
						fraudCase = fraudCaseSessionHome.persistFrdFraudSuspicionCase(fraudCase);
										
						//insert data to fraud suspicion points
						_log.debug("insert fraud suspicion point");		
						
						FrdFraudSuspicionPoint fraudPoint = new FrdFraudSuspicionPoint();
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_1);
						fraudPoint.setRiskPoints(pointRule1);				
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);				
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_2);
						fraudPoint.setRiskPoints(pointRule2);				
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_3);
						fraudPoint.setRiskPoints(pointRule3);				
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_4);
						fraudPoint.setRiskPoints(pointRule4);				
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_5);
						fraudPoint.setRiskPoints(pointRule5);				
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_6);
						fraudPoint.setRiskPoints(pointRule6);				
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_7);
						fraudPoint.setRiskPoints(pointRule7);				
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_8);
						fraudPoint.setRiskPoints(pointRule8);				
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_9);
						fraudPoint.setRiskPoints(pointRule9);				
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_10);
						fraudPoint.setRiskPoints(pointRule10);				
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_11);
						fraudPoint.setRiskPoints(pointRule11);				
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_12);
						fraudPoint.setRiskPoints(pointRule12);				
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_13);
						fraudPoint.setRiskPoints(pointRule13);				
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_14);
						fraudPoint.setRiskPoints(pointRule14);				
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);		
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_15);
						fraudPoint.setRiskPoints(pointRule15);				
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_16);
						fraudPoint.setRiskPoints(pointRule16);				
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_17);
						fraudPoint.setRiskPoints(pointRule17);				
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
		
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_18);
						fraudPoint.setRiskPoints(pointRule18);				
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);	
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_19);
						fraudPoint.setRiskPoints(pointRule19);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);	
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_20);
						fraudPoint.setRiskPoints(pointRule20);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);	
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_21);
						fraudPoint.setRiskPoints(pointRule21);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);	
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_22);
						fraudPoint.setRiskPoints(pointRule22);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);	
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_23);
						fraudPoint.setRiskPoints(pointRule23);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);	
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_24);
						fraudPoint.setRiskPoints(pointRule24);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);	
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_25);
						fraudPoint.setRiskPoints(pointRule25);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);	
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_26);
						fraudPoint.setRiskPoints(pointRule26);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);		
		
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_27);
						fraudPoint.setRiskPoints(pointRule27);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);				
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_28);
						fraudPoint.setRiskPoints(pointRule28);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);				
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_29);
						fraudPoint.setRiskPoints(pointRule29);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);				
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_30);
						fraudPoint.setRiskPoints(pointRule30);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);				
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_31);
						fraudPoint.setRiskPoints(pointRule31);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);				
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_32);
						fraudPoint.setRiskPoints(pointRule32);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_33);
						fraudPoint.setRiskPoints(pointRule33);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);

						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_34);
						fraudPoint.setRiskPoints(pointRule34);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_35);
						fraudPoint.setRiskPoints(pointRule35);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_36);
						fraudPoint.setRiskPoints(pointRule36);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);						
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_37);
						fraudPoint.setRiskPoints(pointRule37);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_38);
						fraudPoint.setRiskPoints(pointRule38);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_39);
						fraudPoint.setRiskPoints(pointRule39);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						fraudPoint.setFrdFraudSuspicionCase(fraudCase);
						fraudPoint.setFraudRuleName(DataConstantNameTokens.FRAUD_RULE_40);
						fraudPoint.setRiskPoints(pointRule40);
						fraudPointSessionHome.persistFrdFraudSuspicionPoint(fraudPoint);
						
						//add fraud case history
						_log.debug("add fraud case history");
						FrdFraudCaseHistoryPK frdFraudCaseHistoryPK = new FrdFraudCaseHistoryPK();
						frdFraudCaseHistoryPK.setFraudSuspicionCaseId(fraudCase.getFraudSuspicionCaseId());
						frdFraudCaseHistoryPK.setFraudCaseHistoryDate(new Timestamp(System.currentTimeMillis()));
						
						FrdFraudCaseHistory entityFraudHistory = new FrdFraudCaseHistory();
						entityFraudHistory.setFrdFraudCaseStatus(frdFraudCaseStatus);
						entityFraudHistory.setId(frdFraudCaseHistoryPK);
						entityFraudHistory.setFraudCaseHistoryNotes("Calculated by System");
						fraudCaseHistorySessionHome.persistFrdFraudCaseHistory(entityFraudHistory);
				}else{
					_log.error("Persist Fraud Suspicion Case OrderId : "+venOrder.getWcsOrderId() + " already exist");
					isSuccessCalculate=false;
				}							
			} catch (Exception e) {
				isSuccessCalculate=false;
				_log.error("Fraud calculate failed when persisting fraud points for wcs order id: "+venOrder.getWcsOrderId());
				e.printStackTrace();
			}finally{
				try {
					if(locator!=null){
						locator.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return isSuccessCalculate;
	}
	
	public boolean isIPAddressWhitelistBlacklist(String ipAddress, String type){
		Boolean result=false;
		Locator<Object> locator = null;		
		try {
			locator = new Locator<Object>();
			FrdEntityBlacklistSessionEJBRemote sessionHome = (FrdEntityBlacklistSessionEJBRemote) locator.lookup(FrdEntityBlacklistSessionEJBRemote.class, "FrdEntityBlacklistSessionEJBBean");
			List<FrdEntityBlacklist> ipBlacklistList = sessionHome.queryByRange("select o from FrdEntityBlacklist o where o.blackOrWhiteList = upper('"+type+"') and o.blacklistString = '"+ipAddress+"'", 0, 0);
			if(ipBlacklistList.size()>0){
				_log.info("ip "+type+" found");
				result=true;
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(locator!=null){
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		return result;
	}
	
	public ArrayList<String> isCustomerWhitelistBlacklist(VenOrder venOrder){
		Boolean result=false;
		Locator<Object> locator = null;	
		ArrayList<String> blacklistReason=new ArrayList<String>();
		try {			
			locator = new Locator<Object>();
			FrdCustomerWhitelistBlacklistSessionEJBRemote sessionHome = (FrdCustomerWhitelistBlacklistSessionEJBRemote) locator.lookup(FrdCustomerWhitelistBlacklistSessionEJBRemote.class, "FrdCustomerWhitelistBlacklistSessionEJBBean");
			FrdBlacklistReasonSessionEJBRemote blacklistReasonSessionHome = (FrdBlacklistReasonSessionEJBRemote) locator.lookup(FrdBlacklistReasonSessionEJBRemote.class, "FrdBlacklistReasonSessionEJBBean");
			VenOrderAddressSessionEJBRemote orderAddressSessionHome = (VenOrderAddressSessionEJBRemote) locator.lookup(VenOrderAddressSessionEJBRemote.class, "VenOrderAddressSessionEJBBean");
			VenOrderContactDetailSessionEJBRemote orderContactDetailSessionHome = (VenOrderContactDetailSessionEJBRemote) locator.lookup(VenOrderContactDetailSessionEJBRemote.class, "VenOrderContactDetailSessionEJBBean");
			VenOrderItemContactDetailSessionEJBRemote orderItemContactDetailSessionHome = (VenOrderItemContactDetailSessionEJBRemote) locator.lookup(VenOrderItemContactDetailSessionEJBRemote.class, "VenOrderItemContactDetailSessionEJBBean");
			VenAddressSessionEJBRemote shippingAddressSessionHome = (VenAddressSessionEJBRemote) locator.lookup(VenAddressSessionEJBRemote.class, "VenAddressSessionEJBBean");
			VenOrderPaymentAllocationSessionEJBRemote allocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			
			
			List<FrdCustomerWhitelistBlacklist> customerNameBlacklistList = sessionHome.queryByRange("select o from FrdCustomerWhitelistBlacklist o where upper(o.customerFullName)<>'ANONYMOUS' and upper(o.customerFullName) = '"+venOrder.getVenCustomer().getVenParty().getFullOrLegalName().toUpperCase()+"'", 0, 0);
			if(customerNameBlacklistList.size()>0){
				_log.info("customer name blacklist found");
				result=true;
				
				blacklistReason.add("Customer name blacklist");
			}
			
			List<FrdCustomerWhitelistBlacklist> customerAddressBlacklistList = null;
			List<VenOrderAddress> orderAddressBlacklistList = orderAddressSessionHome.queryByRange("select o from VenOrderAddress o where o.venOrder.orderId ="+venOrder.getOrderId(), 0, 1);
			
			if(orderAddressBlacklistList.size()>0){
				if(orderAddressBlacklistList.get(0).getVenAddress().getStreetAddress1()!=null){
					customerAddressBlacklistList = sessionHome.queryByRange("select o from FrdCustomerWhitelistBlacklist o where upper(o.address) like '%"+(orderAddressBlacklistList.get(0).getVenAddress().getStreetAddress1().toUpperCase())+"%'", 0, 0);
					if(customerAddressBlacklistList.size()>0){
						_log.info("customer address blacklist found");
						result=true;
													
						blacklistReason.add("Customer address blacklist");
					}
				}
			}
			
			List<FrdCustomerWhitelistBlacklist> emailBlacklistList = null;
			List<VenOrderContactDetail> contactDetailEmailBlacklistList = orderContactDetailSessionHome.queryByRange("select o from VenOrderContactDetail o where o.venOrder.orderId = "+venOrder.getOrderId()+" and o.venContactDetail.venContactDetailType.contactDetailTypeId ="+DataConstantNameTokens.VEN_CONTACT_DETAIL_ID_EMAIL, 0, 1);
			if(contactDetailEmailBlacklistList.size()>0){
				if(contactDetailEmailBlacklistList.get(0).getVenContactDetail().getContactDetail()!=null){
					emailBlacklistList = sessionHome.queryByRange("select o from FrdCustomerWhitelistBlacklist o where upper(o.email) like '"+contactDetailEmailBlacklistList.get(0).getVenContactDetail().getContactDetail().toUpperCase()+"'", 0, 0);
					if(emailBlacklistList.size()>0){
						_log.info("customer email blacklist found");
						result=true;
						
						blacklistReason.add("Customer email blacklist");
					}
				}
			}
			
			List<FrdCustomerWhitelistBlacklist> PhoneBlacklistList = null;
			List<VenOrderContactDetail> contactDetailPhoneBlacklistList = orderContactDetailSessionHome.queryByRange("select o from VenOrderContactDetail o where o.venOrder.orderId = "+venOrder.getOrderId()+" and (o.venContactDetail.venContactDetailType.contactDetailTypeId ="+DataConstantNameTokens.VEN_CONTACT_DETAIL_ID_PHONE+" or o.venContactDetail.venContactDetailType.contactDetailTypeId ="+DataConstantNameTokens.VEN_CONTACT_DETAIL_ID_MOBILE+")", 0, 0);
			if(contactDetailPhoneBlacklistList.size()>0){
				for(int i=0;i<contactDetailPhoneBlacklistList.size();i++){
					PhoneBlacklistList = sessionHome.queryByRange("select o from FrdCustomerWhitelistBlacklist o where o.phoneNumber like '"+contactDetailPhoneBlacklistList.get(i).getVenContactDetail().getContactDetail()+"' or o.handphoneNumber like '"+contactDetailPhoneBlacklistList.get(i).getVenContactDetail().getContactDetail()+"'", 0, 0);
					if(PhoneBlacklistList.size()>0){
						_log.info("customer phone blacklist found");
						result=true;
						
						blacklistReason.add("Customer phone blacklist");
					}
				}
			}			
			
			/**
			 *  cek blacklist shipping phone and hancphone
			 */
			List<FrdCustomerWhitelistBlacklist> shippingPhoneBlacklistList = null;
			List<VenOrderItemContactDetail> shippingContactDetailPhoneBlacklistList = orderItemContactDetailSessionHome.queryByRange("select o from VenOrderItemContactDetail o where o.venOrderItem.venOrder.orderId = "+venOrder.getOrderId()+" and (o.venContactDetail.venContactDetailType.contactDetailTypeId ="+DataConstantNameTokens.VEN_CONTACT_DETAIL_ID_PHONE+" or o.venContactDetail.venContactDetailType.contactDetailTypeId ="+DataConstantNameTokens.VEN_CONTACT_DETAIL_ID_MOBILE+")", 0, 0);
			if(shippingContactDetailPhoneBlacklistList.size()>0){
				for(int i=0;i<shippingContactDetailPhoneBlacklistList.size();i++){
					shippingPhoneBlacklistList = sessionHome.queryByRange("select o from FrdCustomerWhitelistBlacklist o where o.shippingPhoneNumber like '"+shippingContactDetailPhoneBlacklistList.get(i).getVenContactDetail().getContactDetail()+"' or o.shippingHandphoneNumber like '"+shippingContactDetailPhoneBlacklistList.get(i).getVenContactDetail().getContactDetail()+"'", 0, 0);
					if(shippingPhoneBlacklistList.size()>0){
						_log.info("Shipping phone blacklist found");
						result=true;
						
						blacklistReason.add("Shipping phone blacklist");
					}
				}
			}			
			
			/**
			 * cek blacklist shipping address
			 */
			List<FrdCustomerWhitelistBlacklist> shippingAddress = null;
			List<VenAddress> shippingAddressBlacklistList = shippingAddressSessionHome.queryByRange("select o from VenAddress o where o.addressId in (select u.venAddress.addressId from VenOrderItem u where u.venOrder.orderId="+venOrder.getOrderId()+")", 0, 1);
			if(shippingAddressBlacklistList.size()>0){
				for(int i=0;i<shippingAddressBlacklistList.size();i++){
					shippingAddress = sessionHome.queryByRange("select o from FrdCustomerWhitelistBlacklist o where o.shippingAddress like '"+(shippingAddressBlacklistList.get(i).getStreetAddress1() == null?"":shippingAddressBlacklistList.get(i).getStreetAddress1())+"'", 0, 0);
					if(shippingAddress.size()>0){
						_log.info("Shipping Address blacklist found");
						result=true;
						
						blacklistReason.add("Shipping Address blacklist");
					}
				}
			}			
			
			/**
			 *  cek blacklist credit card
			 */			
			List<FrdCustomerWhitelistBlacklist> allocationBlacklistList = null;
			List<VenOrderPaymentAllocation> ccBlacklistList = allocationSessionHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId = "+venOrder.getOrderId(), 0, 0);
			if(ccBlacklistList.size()>0){				
				for(int i=0;i<ccBlacklistList.size();i++){
					if(ccBlacklistList.get(i).getVenOrderPayment().getMaskedCreditCardNumber()!=null){
							allocationBlacklistList = sessionHome.queryByRange("select o from FrdCustomerWhitelistBlacklist o where o.ccNumber like '"+ccBlacklistList.get(i).getVenOrderPayment().getMaskedCreditCardNumber()+"' ", 0, 0);
							if(allocationBlacklistList.size()>0){
								_log.info("Credit Card blacklist found");
								result=true;
								
								blacklistReason.add("Credit Card blacklist");
						}
					}
				}
			}			
			
			//insert blacklist reason if it is blacklisted
			if(result==true){
				FrdBlacklistReason reason = new FrdBlacklistReason();
				reason.setOrderId(venOrder.getOrderId());
				reason.setWcsOrderId(venOrder.getWcsOrderId());
				for(int i=0;i<blacklistReason.size();i++){
					reason.setBlacklistReason(blacklistReason.get(i));
					blacklistReasonSessionHome.persistFrdBlacklistReason(reason);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(locator!=null){
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		return blacklistReason;
	}
}
