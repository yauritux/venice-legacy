package com.gdn.venice.server.app.finance.presenter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenSettlementRecordSessionEJBRemote;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenSettlementRecord;

/**
 * Servlet implementation class TransactionFeeAndSettlementUpdaterJavaServlet
 */
public class TransactionFeeAndSettlementUpdaterJavaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static Logger _log = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TransactionFeeAndSettlementUpdaterJavaServlet() {
        super();
        Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
        _log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.finance.presenter.TransactionFeeAndSettlementUpdaterJavaServlet");
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
        String wcsOrderItemIds = request.getParameter("orderItemId");
        String commissionValue = request.getParameter("marginFee");
        String transactionFee = request.getParameter("transxFee");
        _log.info("Update TransactionFee and settlement : ");       
        _log.info(wcsOrderItemIds);       

        if (wcsOrderItemIds != null ) {        	
        	response.getOutputStream().println("true");
            Locator<Object> locator = null;

            try {
                locator = new Locator<Object>();
                VenSettlementRecordSessionEJBRemote settlementRecordHome = (VenSettlementRecordSessionEJBRemote) locator
                        .lookup(VenSettlementRecordSessionEJBRemote.class, "VenSettlementRecordSessionEJBBean");
                VenOrderItemSessionEJBRemote venOrderItemHome = (VenOrderItemSessionEJBRemote) locator
                .lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
                                
                _log.debug("(merge the settlement record");
                List<VenSettlementRecord> venSettlementRecordList = settlementRecordHome.queryByRange("select o from VenSettlementRecord o where o.venOrderItem.wcsOrderItemId='"+wcsOrderItemIds+"'", 0, 0);
               if(!venSettlementRecordList.isEmpty()){
            	   VenSettlementRecord venSettlementRecords = venSettlementRecordList.get(0);
                  	if(venSettlementRecords.getCommissionValue()==null){
                  		venSettlementRecords.setCommissionValue(commissionValue!=null?new BigDecimal(commissionValue):null);
                  		 venSettlementRecords = settlementRecordHome.mergeVenSettlementRecord(venSettlementRecords);
                  	}
               }
                //set transaction fee
		          String sql = "select o from VenOrderItem o where o.wcsOrderItemId ='"+wcsOrderItemIds+"' and o.transactionFeeAmount is null";
		          List<VenOrderItem> venOrderItemList = venOrderItemHome.queryByRange(sql, 0, 0);
		          if(!venOrderItemList.isEmpty()){
		       	   VenOrderItem item = venOrderItemList.get(0);
		       	   item.setTransactionFeeAmount(transactionFee!=null?new BigDecimal(transactionFee):null);
		       	   item=venOrderItemHome.mergeVenOrderItem(item);
		          
		       }

            } catch (Exception e) {
                String errMessage = "Problem create change shipping notification";
                _log.error(errMessage, e);

                throw new ServletException(errMessage, e);

            } finally {
                if (locator != null) {
                    try {
                        locator.close();
                    } catch (Exception e) {
                        _log.error(e.getMessage(), e);
                    }
                }
            }
            
            
        }else{
            response.getOutputStream().println("false");
        }
	}

}
