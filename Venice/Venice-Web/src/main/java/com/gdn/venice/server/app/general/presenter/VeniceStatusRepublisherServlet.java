/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdn.venice.server.app.general.presenter;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderPayment;
import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.util.VeniceConstants;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class VeniceStatusRepublisherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    protected static Logger _log = null;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public VeniceStatusRepublisherServlet() {
        super();
        Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
        _log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.general.presenter.VeniceStatusRepublisherServlet");
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service(request, response);
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ids = request.getParameter("ids");
        String status = request.getParameter("status");

        String idArray[] = ids.split(",");
        Locator<Object> locator = null;
        VenOrderSessionEJBRemote orderHome;
        VenOrderItemSessionEJBRemote orderItemHome;
        VenOrderStatus venStatus = new VenOrderStatus();

        if (status.equalsIgnoreCase("VA")) {
            try {
                locator = new Locator<Object>();
                orderHome = (VenOrderSessionEJBRemote) locator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
                VenOrder venOrder;
                VenOrderPayment venOrderPayment;
                for (int i = 0; i < idArray.length; i++) {
                    venOrder = orderHome.queryByRange("select o from VenOrder o join fetch o.venOrderPaymentAllocations where o.wcsOrderId = '" + idArray[i] + "'", 0, 1).get(0);
                    if (venOrder.getVenOrderPaymentAllocations() != null && venOrder.getVenOrderPaymentAllocations().size() > 0) {
                        if (venOrder.getVenOrderStatus().getOrderStatusId() == VeniceConstants.VEN_ORDER_STATUS_VA) {
                        venOrderPayment = venOrder.getVenOrderPaymentAllocations().get(0).getVenOrderPayment();
                            _log.debug("start republish order status Approved VA for: "+idArray[i]);
                            if(orderHome.republish(idArray[i], venOrderPayment))
                            _log.debug("done republish order status");
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (locator != null) {
                    try {
                        locator.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } else if (status.equalsIgnoreCase("FP")) {
            try {
                locator = new Locator<Object>();
                orderHome = (VenOrderSessionEJBRemote) locator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
                VenOrder venOrder;
                for (int i = 0; i < idArray.length; i++) {
                    venOrder = orderHome.queryByRange("select o from VenOrder o where o.wcsOrderId = '" + idArray[i] + "'", 0, 1).get(0);
                    String blockingSource = null;
                    if (venOrder.getVenOrderBlockingSource() != null && venOrder.getVenOrderBlockingSource().getBlockingSourceDesc() != null) {
                        blockingSource = venOrder.getVenOrderBlockingSource().getBlockingSourceDesc();
                        _log.debug("blockingSource is exist: " + blockingSource);
                    }
                    if (venOrder.getVenOrderStatus().getOrderStatusId() == VeniceConstants.VEN_ORDER_STATUS_FP) {
                        _log.debug("start republish order status FP for: "+idArray[i]);
                        if(orderHome.republish(venOrder, blockingSource)){
                        	_log.debug("done republish order status");
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (locator != null) {
                    try {
                        locator.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } else if (status.equalsIgnoreCase("ES")) {
            try {
                locator = new Locator<Object>();
                orderItemHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
                VenOrderItem venOrderItem;
                venStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_ES);
                for (int i = 0; i < idArray.length; i++) {
                    venOrderItem = orderItemHome.queryByRange("select o from VenOrderItem o where o.wcsOrderItemId = '" + idArray[i] + "'", 0, 1).get(0);
                    if (venOrderItem.getVenOrderStatus().getOrderStatusId() == VeniceConstants.VEN_ORDER_STATUS_ES
                            || venOrderItem.getVenOrderStatus().getOrderStatusId() == VeniceConstants.VEN_ORDER_STATUS_PP
                            || venOrderItem.getVenOrderStatus().getOrderStatusId() == VeniceConstants.VEN_ORDER_STATUS_CX
                            || venOrderItem.getVenOrderStatus().getOrderStatusId() == VeniceConstants.VEN_ORDER_STATUS_D) {
                        venOrderItem.setVenOrderStatus(venStatus);
                        _log.debug("start republish order item status ES for: "+idArray[i]);
                        orderItemHome.republish(venOrderItem);
                        _log.debug("done republish order status");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (locator != null) {
                    try {
                        locator.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } else if (status.equalsIgnoreCase("PP")) {
            try {
                locator = new Locator<Object>();
                orderItemHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
                VenOrderItem venOrderItem;
                venStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_PP);
                for (int i = 0; i < idArray.length; i++) {
                    venOrderItem = orderItemHome.queryByRange("select o from VenOrderItem o where o.wcsOrderItemId = '" + idArray[i] + "'", 0, 1).get(0);
                    if (venOrderItem.getVenOrderStatus().getOrderStatusId() == VeniceConstants.VEN_ORDER_STATUS_PP
                            || venOrderItem.getVenOrderStatus().getOrderStatusId() == VeniceConstants.VEN_ORDER_STATUS_CX
                            || venOrderItem.getVenOrderStatus().getOrderStatusId() == VeniceConstants.VEN_ORDER_STATUS_D) {
                        venOrderItem.setVenOrderStatus(venStatus);
                        _log.debug("start republish order item status PP for: "+idArray[i]);
                        orderItemHome.republish(venOrderItem);
                        _log.debug("done republish order status");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (locator != null) {
                    try {
                        locator.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } else if (status.equalsIgnoreCase("CX")) {
            try {
                locator = new Locator<Object>();
                orderItemHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
                VenOrderItem venOrderItem;
                venStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_CX);
                for (int i = 0; i < idArray.length; i++) {
                    venOrderItem = orderItemHome.queryByRange("select o from VenOrderItem o where o.wcsOrderItemId = '" + idArray[i] + "'", 0, 1).get(0);
                    if (venOrderItem.getVenOrderStatus().getOrderStatusId() == VeniceConstants.VEN_ORDER_STATUS_CX
                            || venOrderItem.getVenOrderStatus().getOrderStatusId() == VeniceConstants.VEN_ORDER_STATUS_D) {
                        venOrderItem.setVenOrderStatus(venStatus);
                        _log.debug("start republish order item status CX for: "+idArray[i]);
                        orderItemHome.republish(venOrderItem);
                        _log.debug("done republish order status");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (locator != null) {
                    try {
                        locator.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } else if (status.equalsIgnoreCase("D")) {
            try {
                locator = new Locator<Object>();
                orderItemHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
                VenOrderItem venOrderItem;
                for (int i = 0; i < idArray.length; i++) {
                    venOrderItem = orderItemHome.queryByRange("select o from VenOrderItem o where o.wcsOrderItemId = '" + idArray[i] + "'", 0, 1).get(0);
                    if (venOrderItem.getVenOrderStatus().getOrderStatusId() == VeniceConstants.VEN_ORDER_STATUS_D) {
                        _log.debug("start republish order item status D for: "+idArray[i]);
                        orderItemHome.republish(venOrderItem);
                        _log.debug("done republish order status");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (locator != null) {
                    try {
                        locator.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}
