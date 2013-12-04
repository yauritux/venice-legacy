package com.gdn.venice.facade.kpi;

import java.util.Date;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.djarum.raf.utilities.SQLDateUtility;
import com.gdn.venice.facade.KpiMeasurementPeriodSessionEJBLocal;
import com.gdn.venice.facade.KpiPartyMeasurementPeriodSessionEJBLocal;
import com.gdn.venice.facade.KpiPartyPeriodTransactionSessionEJBLocal;
import com.gdn.venice.facade.VenPartySessionEJBLocal;
import com.gdn.venice.persistence.KpiKeyPerformanceIndicator;
import com.gdn.venice.persistence.KpiMeasurementPeriod;
import com.gdn.venice.persistence.KpiPartyMeasurementPeriod;
import com.gdn.venice.persistence.KpiPartyMeasurementPeriodPK;
import com.gdn.venice.persistence.KpiPartyPeriodTransaction;
import com.gdn.venice.persistence.VenParty;

/**
 * KPI_TransactionPosterSessionEJBBean.java
 * 
 * Session Bean implementation class KPI_TransactionPosterSessionEJBBean
 * Used to provide an interface to post KPI transactions
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
@Stateless(mappedName = "KPI_TransactionPosterSessionEJBBean")
@WebService(serviceName = "KPI_TransactionPosterSessionEJBBean", portName = "KPI_TransactionPosterSessionEJBPort", endpointInterface = "com.gdn.venice.facade.kpi.KPI_TransactionPosterSessionEJBRemote", targetNamespace = "http://kpi.venice.gdn.com/services")
public class KPI_TransactionPosterSessionEJBBean implements KPI_TransactionPosterSessionEJBRemote, KPI_TransactionPosterSessionEJBLocal{
	protected static Logger _log = null;

    /**
     * Default constructor. 
     */
    public KPI_TransactionPosterSessionEJBBean() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.facade.kpi.KPI_TransactionPosterSessionEJBBean");
    }
    
    /* (non-Javadoc)
     * @see com.gdn.venice.facade.kpi.KPI_TransactionPosterSessionEJBRemote#postKpiTransaction(java.lang.Long, java.lang.Long, java.lang.Long, java.lang.Integer, java.lang.String)
     */
    public Boolean postKpiTransaction(Long kpiId, Long periodId, Long partyId, Integer value, String reason){
    	_log.debug("Posting KPI transaction kpiId:" + kpiId + " periodId:" + periodId + " partyId:" + partyId + " value:" + value + " reason:" + reason);
    	Locator<Object> locator = null;
    	try {
			locator = new Locator<Object>();
			
			KpiPartyPeriodTransactionSessionEJBLocal kpiPartyPeriodTransactionHome = (KpiPartyPeriodTransactionSessionEJBLocal) locator
			.lookupLocal(KpiPartyPeriodTransactionSessionEJBLocal.class, "KpiPartyPeriodTransactionSessionEJBBeanLocal");

			KpiPartyMeasurementPeriodSessionEJBLocal kpiPartyMeasurementPeriodHome = (KpiPartyMeasurementPeriodSessionEJBLocal) locator
			.lookupLocal(KpiPartyMeasurementPeriodSessionEJBLocal.class, "KpiPartyMeasurementPeriodSessionEJBBeanLocal");
			
			KpiMeasurementPeriodSessionEJBLocal kpiMeasurementPeriodHome = (KpiMeasurementPeriodSessionEJBLocal) locator
			.lookupLocal(KpiMeasurementPeriodSessionEJBLocal.class, "KpiMeasurementPeriodSessionEJBBeanLocal");

			VenPartySessionEJBLocal venPartyHome = (VenPartySessionEJBLocal) locator
			.lookupLocal(VenPartySessionEJBLocal.class, "VenPartySessionEJBBeanLocal");

			KpiPartyPeriodTransaction kpiPartyPeriodTransaction = new KpiPartyPeriodTransaction();
			
			KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator = new KpiKeyPerformanceIndicator();
			kpiKeyPerformanceIndicator.setKpiId(kpiId);
			
			/*
			 * Check the party
			 */
			List<VenParty> venPartyList = venPartyHome.queryByRange("select o from VenParty o where o.partyId = " + partyId, 0, 0);
			if(venPartyList.isEmpty()){
				throw new EJBException("Invalid partyId... party does not exist!");
			}
			
			VenParty venParty = venPartyList.get(0);
			
			/*
			 * Check the measurement period
			 */
			
			List<KpiMeasurementPeriod> kpiMeasurementPeriodList = kpiMeasurementPeriodHome.queryByRange("select o from KpiMeasurementPeriod o where o.kpiPeriodId = " + periodId, 0, 0);
			
			if(kpiMeasurementPeriodList.isEmpty()){
				throw new EJBException("Invalid KPI measurement period... period does not exist!");
			}

			KpiMeasurementPeriod kpiMeasurementPeriod = kpiMeasurementPeriodList.get(0);

			/*
			 * Get the existing party measurement period from the database
			 * if it exists else create a new measurement period for the
			 * kpi and party
			 */
			KpiPartyMeasurementPeriod kpiPartyMeasurementPeriod = null;
			List<KpiPartyMeasurementPeriod> kpiPartyMeasurementPeriodList = kpiPartyMeasurementPeriodHome.queryByRange("select o from KpiPartyMeasurementPeriod o where o.id.partyId = " + partyId + " and o.id.kpiPeriodId = " + periodId, 0, 0);
			if(!kpiPartyMeasurementPeriodList.isEmpty()){
				kpiPartyMeasurementPeriod = kpiPartyMeasurementPeriodList.get(0);
			}else{				
				/*
				 * Create a new party measurement period for the party and the KPI
				 */
				KpiPartyMeasurementPeriod newKpiPartyMeasurementPeriod = new KpiPartyMeasurementPeriod();
				
				newKpiPartyMeasurementPeriod.setKpiMeasurementPeriod(kpiMeasurementPeriod);
				newKpiPartyMeasurementPeriod.setVenParty(venParty);
				
				KpiPartyMeasurementPeriodPK id = new KpiPartyMeasurementPeriodPK();
				id.setKpiPeriodId(periodId);
				id.setPartyId(partyId);
				
				newKpiPartyMeasurementPeriod.setId(id);
				
				/*
				 * Persist the new party measurement period
				 */
				kpiPartyMeasurementPeriod = kpiPartyMeasurementPeriodHome.persistKpiPartyMeasurementPeriod(newKpiPartyMeasurementPeriod);
			}
						
			kpiPartyPeriodTransaction.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicator);
			kpiPartyPeriodTransaction.setKpiPartyMeasurementPeriod(kpiPartyMeasurementPeriod);
			kpiPartyPeriodTransaction.setKpiTransactionReason(reason);
			kpiPartyPeriodTransaction.setKpiTransactionValue(value);
			kpiPartyPeriodTransaction.setTransactionTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));
			
			kpiPartyPeriodTransaction = kpiPartyPeriodTransactionHome.persistKpiPartyPeriodTransaction(kpiPartyPeriodTransaction);
			
		} catch (Exception e) {
			String errMsg = "An exception occured when posting KPI transactions:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
			throw new EJBException(errMsg);
		}  finally{
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
