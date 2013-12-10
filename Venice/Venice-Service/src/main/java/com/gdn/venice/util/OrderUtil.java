package com.gdn.venice.util;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.integration.jaxb.Order;
import com.gdn.integration.jaxb.Payment;
import com.gdn.venice.constants.VenPaymentTypeConstants;
import com.gdn.venice.constants.VenWCSPaymentTypeConstants;
import com.gdn.venice.exception.InvalidOrderException;
import com.gdn.venice.persistence.VenOrderPayment;
import com.gdn.venice.persistence.VenPaymentType;
import com.gdn.venice.validator.OrderValidator;


public class OrderUtil {
	private static Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
	private static final Logger LOG = loggerFactory.getLog4JLogger(OrderUtil.class.getName());
	
	public static void checkOrder(Order order, OrderValidator validator) throws InvalidOrderException {
		LOG.debug("Order=" + order);
		validator.checkOrder(order);
		LOG.debug("Order has passed the validator");
	}
	
	/**
	 * get VenOrderPayment's payment type based on WCS Payment Type
	 * 
	 */
	public static VenOrderPayment getVenOrderPaymentByWCSPaymentType(VenOrderPayment venOrderPayment, Payment payment) {
		VenPaymentType venPaymentType = new VenPaymentType();
		
		if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(
				VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_DebitMandiri.desc())) {
			venPaymentType.setPaymentTypeCode(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.desc());
			venPaymentType.setPaymentTypeId(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.id());
		} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(
				VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_KlikBCA.desc())) {
			venPaymentType.setPaymentTypeCode(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.desc());
			venPaymentType.setPaymentTypeId(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.id());
		} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(
				VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_KlikPAYFullPayment.desc())) {
			venPaymentType.setPaymentTypeCode(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.desc());
			venPaymentType.setPaymentTypeId(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.id());
		}else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(
				VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_KlikPAYKartuKredit.desc())) {
			venPaymentType.setPaymentTypeCode(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.desc());
			venPaymentType.setPaymentTypeId(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.id());
		}else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(
				VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_KlikPAYKlikBCA.desc())) {
			venPaymentType.setPaymentTypeCode(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.desc());
			venPaymentType.setPaymentTypeId(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.id());
		}else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(
				VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_KlikPAYInstallment.desc())) {
			venPaymentType.setPaymentTypeCode(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.desc());
			venPaymentType.setPaymentTypeId(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.id());
		} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(
				VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_KlikPAYXPercentInstallment.desc())) {
			venPaymentType.setPaymentTypeCode(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.desc());
			venPaymentType.setPaymentTypeId(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.id());
		} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(
				VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_KlikPAYZeroPercentInstallment.desc())) {
			venPaymentType.setPaymentTypeCode(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.desc());
			venPaymentType.setPaymentTypeId(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.id());
		} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(
				VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_MIGSCreditCard.desc())) {
			venPaymentType.setPaymentTypeCode(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_CC.desc());
			venPaymentType.setPaymentTypeId(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_CC.id());
		} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(
				VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_VirtualAccount.desc())) {
			venPaymentType.setPaymentTypeCode(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_VA.desc());
			venPaymentType.setPaymentTypeId(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_VA.id());
		} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(
				VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_CSPayment.desc())) {
			venPaymentType.setPaymentTypeCode(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_CS.desc());
			venPaymentType.setPaymentTypeId(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_CS.id());
		} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(
				VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_MIGSBCAInstallment.desc())){
			venPaymentType.setPaymentTypeCode(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_CC.desc());
			venPaymentType.setPaymentTypeId(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_CC.id());
		} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(
				VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_CIMBClicks.desc())) {
			venPaymentType.setPaymentTypeCode(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.desc());
			venPaymentType.setPaymentTypeId(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.id());
		}  else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(
				VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_XLTunai.desc())) {
			CommonUtil.logDebug(LOG, "payment type XLTunai, reference id: "+payment.getReferenceId());
			venPaymentType.setPaymentTypeCode(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.desc());
			venPaymentType.setPaymentTypeId(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.id());
			venOrderPayment.setReferenceId(payment.getReferenceId());
		} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(
				VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_MandiriInstallment.desc())){
			venPaymentType.setPaymentTypeCode(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_CC.desc());
			venPaymentType.setPaymentTypeId(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_CC.id());
		} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(
				VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_BIIngkisan.desc())){
			venPaymentType.setPaymentTypeCode(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.desc());
			venPaymentType.setPaymentTypeId(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.id());
		} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(
				VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_BRI.desc())){
			venPaymentType.setPaymentTypeCode(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.desc());
			venPaymentType.setPaymentTypeId(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_IB.id());
		} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_MandiriDebit.desc())){
			venPaymentType.setPaymentTypeCode(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_CC.desc());
			venPaymentType.setPaymentTypeId(VenPaymentTypeConstants.VEN_PAYMENT_TYPE_CC.id());
		}		

		venOrderPayment.setVenPaymentType(venPaymentType);
		
		return venOrderPayment;
	}
}
