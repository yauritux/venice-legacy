package com.gdn.venice.exportimport.logistics.notification.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.logistics.notification.LogisticsPickupReportSessionEJBBeanRemote;

/**
 * Servlet implementation class LogisticsChangeShippingNotificationServlet
 */
public class LogisticsChangeShippingNotificationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    protected static Logger _log = null;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogisticsChangeShippingNotificationServlet() {
        super();
        Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
        _log = loggerFactory.getLog4JLogger("com.gdn.venice.logistics.notification.servlet.LogisticsInventoryImportServlet");

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//		String wcsOrderItemId = request.getParameter("orderItemId");
        String[] wcsOrderItemIds = request.getParameterValues("orderItemId");

        _log.info("Receiving Change Shipping : ");
        for (int i = 0; i < wcsOrderItemIds.length; i++) {
            _log.info(wcsOrderItemIds[i]);
        }

        if (wcsOrderItemIds != null && wcsOrderItemIds.length > 0) {

            Locator<Object> locator = null;

            try {
                locator = new Locator<Object>();
                LogisticsPickupReportSessionEJBBeanRemote pickupReportHome = (LogisticsPickupReportSessionEJBBeanRemote) locator
                        .lookup(LogisticsPickupReportSessionEJBBeanRemote.class, "LogisticsPickupReportSessionEJBBean");

                pickupReportHome.createChangeShippingNotification(wcsOrderItemIds);

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

        }

    }
}
