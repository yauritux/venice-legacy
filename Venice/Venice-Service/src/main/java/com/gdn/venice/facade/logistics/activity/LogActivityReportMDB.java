package com.gdn.venice.facade.logistics.activity;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.ejb.EntityManagerImpl;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.djarum.raf.utilities.SQLDateUtility;
import com.gdn.venice.facade.LogActivityReportUploadSessionEJBLocal;
import com.gdn.venice.facade.LogActivityReportUploadSessionEJBRemote;
import com.gdn.venice.facade.LogAirwayBillReturSessionEJBLocal;
import com.gdn.venice.facade.LogAirwayBillReturSessionEJBRemote;
import com.gdn.venice.facade.LogAirwayBillSessionEJBLocal;
import com.gdn.venice.facade.LogAirwayBillSessionEJBRemote;
import com.gdn.venice.facade.LogLogisticsProviderSessionEJBLocal;
import com.gdn.venice.facade.VenOrderItemSessionEJBLocal;
import com.gdn.venice.facade.VenReturItemSessionEJBLocal;
import com.gdn.venice.facade.util.AWBReconciliation;
import com.gdn.venice.hssf.ExcelToPojo;
import com.gdn.venice.hssf.PojoInterface;
import com.gdn.venice.logistics.dataexport.ActivityInvoiceFailedToUploadExport;
import com.gdn.venice.logistics.dataexport.FailedStatusUpdate;
import com.gdn.venice.logistics.dataimport.DailyReportJNE;
import com.gdn.venice.logistics.dataimport.DailyReportMSG;
import com.gdn.venice.logistics.dataimport.DailyReportNCS;
import com.gdn.venice.logistics.dataimport.LogisticsConstants;
import com.gdn.venice.logistics.integration.AirwayBillEngineClientConnector;
import com.gdn.venice.logistics.integration.AirwayBillEngineConnector;
import com.gdn.venice.logistics.integration.bean.AirwayBillTransaction;
import com.gdn.venice.persistence.LogActivityReportUpload;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.LogAirwayBillRetur;
import com.gdn.venice.persistence.LogApprovalStatus;
import com.gdn.venice.persistence.LogFileUploadLog;
import com.gdn.venice.persistence.LogLogisticService;
import com.gdn.venice.persistence.LogLogisticsProvider;
import com.gdn.venice.persistence.LogReportStatus;
import com.gdn.venice.persistence.LogReportTemplate;
import com.gdn.venice.persistence.VenAddress;
import com.gdn.venice.persistence.VenMerchantProduct;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.persistence.VenRecipient;
import com.gdn.venice.persistence.VenRetur;
import com.gdn.venice.persistence.VenReturItem;
import com.gdn.venice.util.VeniceConstants;

/**
 * Message-Driven Bean implementation class for: LogActivityReportSessionEJBBEan
 *
 */
@MessageDriven(name = "LogActivityReportMDB",
    activationConfig = {@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "SendReceiveQueue")})
public class LogActivityReportMDB implements MessageListener {

    protected static Logger _log = null;
    private static final String ACTIVITY_OR_INVOICE = "activity";
    private static final String FILE_PATH = System.getenv("VENICE_HOME") + LogisticsConstants.ACTIVITY_REPORT_FOLDER;
    private static final SimpleDateFormat formatDate = new SimpleDateFormat(LogisticsConstants.DATE_FORMAT_STRING);
    private SimpleDateFormat fileDateTimeFormat = new SimpleDateFormat(LogisticsConstants.FILE_DATE_TIME_FORMAT);
    private Integer errorRowNumber = 1;
    @PersistenceContext(unitName = "GDN-Venice-Persistence")
    protected EntityManager em;
    @PersistenceUnit
    private EntityManagerFactory emf;
    private AirwayBillEngineConnector awbConn;
    private static final String UPDATE_FILE_LOG_SQL = "update log_file_upload_log "
            + "set "
            + "upload_status = ?, "
            + "failed_file_upload_name = ?, "
            + "failed_file_upload_name_and_loc = ? "
            + "where file_upload_log_id = ?";
    private static final String SELECT_ORDER_ITEM_SQL = "select o.wcs_order_id, s.service_code, oi.*, p.* "
            + "from ven_order_item oi "
            + "inner join ven_order o on o.order_id = oi.order_id "
            + "inner join ven_recipient r on r.recipient_id = oi.recipient_id "
            + "inner join ven_party p on r.party_id = p.party_id "
            + "inner join log_logistic_service s on oi.logistics_service_id=s.logistics_service_id "
            + "where wcs_order_item_id = ?";
    private static final String SELECT_AIRWAY_BILL_SQL_BY_GDN_REF = "select ab.*, lp.* "
            + "from log_airway_bill ab "
            + "inner join log_logistics_provider lp on lp.logistics_provider_id = ab.logistics_provider_id "
            + "where gdn_reference = ?";
    private static final String SELECT_AIRWAY_BILL_SQL_BY_ORDER_ITEM_ID = "select ab.*, lp.* "
            + "from log_airway_bill ab "
            + "inner join log_logistics_provider lp on lp.logistics_provider_id = ab.logistics_provider_id "
            + "where order_item_id = ?";
    private static final String COUNT_AIRWAY_BILL_SQL_BY_GDN_REF = "select count(*) as totalAirwayBill "
            + "from log_airway_bill ab "
            + "inner join log_logistics_provider lp on lp.logistics_provider_id = ab.logistics_provider_id "
            + "where gdn_reference = ?";
    private static final String COUNT_ORDER_ITEM_BY_WCS_ORDER_ITEM_ID = "select count(*) as totalOrderItem from ven_order_item where wcs_order_item_id = ?";
    //retur
    private static final String SELECT_RETUR_ITEM_SQL = "select o.wcs_retur_id, s.service_code, oi.*, p.* "
            + "from ven_retur_item oi "
            + "inner join ven_retur o on o.retur_id = oi.retur_id "
            + "inner join ven_recipient r on r.recipient_id = oi.recipient_id "
            + "inner join ven_party p on r.party_id = p.party_id "
            + "inner join log_logistic_service s on oi.logistics_service_id=s.logistics_service_id "
            + "where wcs_retur_item_id = ?";
    private static final String SELECT_AIRWAY_BILL_RETUR_SQL_BY_GDN_REF = "select ab.*, lp.* "
            + "from log_airway_bill_retur ab "
            + "inner join log_logistics_provider lp on lp.logistics_provider_id = ab.logistics_provider_id "
            + "where gdn_reference = ?";
    private static final String SELECT_AIRWAY_BILL_RETUR_SQL_BY_RETUR_ITEM_ID = "select ab.*, lp.* "
            + "from log_airway_bill_retur ab "
            + "inner join log_logistics_provider lp on lp.logistics_provider_id = ab.logistics_provider_id "
            + "where retur_item_id = ?";
    private static final String COUNT_AIRWAY_BILL_RETUR_SQL_BY_GDN_REF = "select count(*) as totalAirwayBillRetur "
            + "from log_airway_bill_retur ab "
            + "inner join log_logistics_provider lp on lp.logistics_provider_id = ab.logistics_provider_id "
            + "where gdn_reference = ?";
    private static final String COUNT_RETUR_ITEM_BY_WCS_RETUR_ITEM_ID = "select count(*) as totalReturItem from ven_retur_item where wcs_returr_item_id = ?";

    /**
     * Default constructor.
     */
    public LogActivityReportMDB() {
        super();
        Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
        _log = loggerFactory.getLog4JLogger("com.gdn.venice.facade.logistics.activity.LogActivityReportMDB");
    }

    /**
     * @see MessageListener#onMessage(Message)
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void onMessage(Message message) {
        try {
            // init connection to Airway Bill Engine 
            awbConn = new AirwayBillEngineClientConnector();

            ObjectMessage msg = (ObjectMessage) message;
            LogFileUploadLog fileUploadLog = (LogFileUploadLog) msg.getObject();

            if (fileUploadLog.getFileUploadFormat().equals("JNE")) {
                processJNEFormat(fileUploadLog);
            } else if (fileUploadLog.getFileUploadFormat().equals("NCS")) {
                processNCSFormat(fileUploadLog);
            } else if (fileUploadLog.getFileUploadFormat().equals("RPX")) {
                processRPXFormat(fileUploadLog);
            } else if (fileUploadLog.getFileUploadFormat().equals("MSG")) {
                processMSGFormat(fileUploadLog);
            }
        } catch (JMSException e) {
            _log.error("Error on reading message", e);
            e.printStackTrace();
        }
    }

    private void updateFileUploadLog(LogFileUploadLog fileUploadLog) {

        try {
            EntityManager em = emf.createEntityManager();
            @SuppressWarnings("deprecation")
            Connection conn = (Connection) ((EntityManagerImpl) em).getSession().connection();

            PreparedStatement psUpdateFileUploadLog = conn.prepareStatement(UPDATE_FILE_LOG_SQL);

            psUpdateFileUploadLog.setString(1, fileUploadLog.getUploadStatus());
            psUpdateFileUploadLog.setString(2, fileUploadLog.getFailedFileUploadName() != null ? fileUploadLog.getFailedFileUploadName() : "");
            psUpdateFileUploadLog.setString(3, fileUploadLog.getFailedFileUploadNameAndLoc() != null ? fileUploadLog.getFailedFileUploadNameAndLoc() : "");
            psUpdateFileUploadLog.setLong(4, fileUploadLog.getFileUploadLogId());

            psUpdateFileUploadLog.executeUpdate();
            psUpdateFileUploadLog.close();

            conn.close();

        } catch (Exception e) {
            _log.error("Error merging file upload log", e);
        }

    }

    private String getLogisticsTemplateFile(long logisticsProviderId) throws Exception {
        String templateFile = "";

        Locator<Object> locator = new Locator<Object>();
        LogLogisticsProviderSessionEJBLocal LogLogisticsProviderHome = (LogLogisticsProviderSessionEJBLocal) locator.lookupLocal(LogLogisticsProviderSessionEJBLocal.class, "LogLogisticsProviderSessionEJBBeanLocal");
        List<LogLogisticsProvider> LogLogisticsProviderList = LogLogisticsProviderHome.queryByRange("select o from LogLogisticsProvider o where o.logisticsProviderId = " + logisticsProviderId, 0, 0);
        if (LogLogisticsProviderList.size() > 0) {
            templateFile = LogLogisticsProviderList.get(0).getLogReportTemplate2().getTemplateFile();

            if (templateFile.equals("") || templateFile == null) {
                throw new Exception(LogisticsConstants.EXCEPTION_TEXT_TEMPLATE_NOT_FOUND);
            } else {
                _log.debug("logistic provider found, templateFile: " + templateFile);
            }
        }

        return templateFile;
    }

    private VenOrderItem orderItemResultSetMapper(ResultSet rsOrderItem) throws SQLException {
        VenOrderItem venOrderItem = new VenOrderItem();

        rsOrderItem.last();
        int totalOrderItem = rsOrderItem.getRow();
        rsOrderItem.beforeFirst();

        if (totalOrderItem > 0) {
            rsOrderItem.next();

            venOrderItem.setEtd(rsOrderItem.getInt("etd"));
            venOrderItem.setGiftCardFlag(rsOrderItem.getBoolean("gift_card_flag"));
            venOrderItem.setGiftCardNote(rsOrderItem.getString("gift_card_note"));
            venOrderItem.setGiftWrapPrice(rsOrderItem.getBigDecimal("gift_wrap_price"));
            venOrderItem.setGiftWrapFlag(rsOrderItem.getBoolean("gift_wrap_flag"));
            venOrderItem.setInsuranceCost(rsOrderItem.getBigDecimal("insurance_cost"));
            venOrderItem.setMaxEstDate(rsOrderItem.getTimestamp("max_est_date"));
            venOrderItem.setMerchantSettlementFlag(rsOrderItem.getBoolean("merchant_settlement_flag"));
            venOrderItem.setMinEstDate(rsOrderItem.getTimestamp("min_est_date"));
            venOrderItem.setOrderItemId(rsOrderItem.getLong("order_item_id"));
            venOrderItem.setPackageCount(rsOrderItem.getInt("package_count"));
            venOrderItem.setPrice(rsOrderItem.getBigDecimal("price"));
            venOrderItem.setQuantity(rsOrderItem.getInt("quantity"));
            venOrderItem.setSaltCode(rsOrderItem.getString("salt_code"));
            venOrderItem.setShippingCost(rsOrderItem.getBigDecimal("shipping_cost"));
            venOrderItem.setShippingWeight(rsOrderItem.getBigDecimal("shipping_weight"));
            venOrderItem.setSpecialHandlingInstructions(rsOrderItem.getString("special_handling_instructions"));
            venOrderItem.setTotal(rsOrderItem.getBigDecimal("total"));
            venOrderItem.setWcsOrderItemId(rsOrderItem.getString("wcs_order_item_id"));
            venOrderItem.setLogisticsPricePerKg(rsOrderItem.getBigDecimal("logistics_price_per_kg"));

            VenAddress shippingAddress = new VenAddress();
            shippingAddress.setAddressId(rsOrderItem.getLong("shipping_address_id"));
            venOrderItem.setVenAddress(shippingAddress);

            VenMerchantProduct product = new VenMerchantProduct();
            product.setProductId(rsOrderItem.getLong("product_id"));
            venOrderItem.setVenMerchantProduct(product);

            VenOrder order = new VenOrder();
            order.setWcsOrderId(rsOrderItem.getString("wcs_order_id"));
            order.setOrderId(rsOrderItem.getLong("order_id"));
            venOrderItem.setVenOrder(order);

            VenOrderStatus orderStatus = new VenOrderStatus();
            orderStatus.setOrderStatusId(rsOrderItem.getLong("order_status_id"));
            venOrderItem.setVenOrderStatus(orderStatus);

            VenParty venParty = new VenParty();
            venParty.setPartyId(rsOrderItem.getLong("party_id"));
            venParty.setFullOrLegalName(rsOrderItem.getString("full_or_legal_name"));

            VenRecipient venRecipient = new VenRecipient();
            venRecipient.setRecipientId(rsOrderItem.getLong("recipient_id"));
            venRecipient.setVenParty(venParty);
            venOrderItem.setVenRecipient(venRecipient);

            LogLogisticService logisticService = new LogLogisticService();
            logisticService.setLogisticsServiceId(rsOrderItem.getLong("logistics_service_id"));
            logisticService.setServiceCode(rsOrderItem.getString("service_code"));
            venOrderItem.setLogLogisticService(logisticService);
        }

        return venOrderItem;

    }

    private VenReturItem returItemResultSetMapper(ResultSet rsReturItem) throws SQLException {
        VenReturItem venReturItem = new VenReturItem();

        rsReturItem.last();
        int totalReturItem = rsReturItem.getRow();
        rsReturItem.beforeFirst();

        if (totalReturItem > 0) {
            rsReturItem.next();

            venReturItem.setEtd(rsReturItem.getInt("etd"));
            venReturItem.setGiftCardFlag(rsReturItem.getBoolean("gift_card_flag"));
            venReturItem.setGiftCardNote(rsReturItem.getString("gift_card_note"));
            venReturItem.setGiftWrapPrice(rsReturItem.getBigDecimal("gift_wrap_price"));
            venReturItem.setGiftWrapFlag(rsReturItem.getBoolean("gift_wrap_flag"));
            venReturItem.setInsuranceCost(rsReturItem.getBigDecimal("insurance_cost"));
            venReturItem.setMaxEstDate(rsReturItem.getTimestamp("max_est_date"));
            venReturItem.setMerchantSettlementFlag(rsReturItem.getBoolean("merchant_settlement_flag"));
            venReturItem.setMinEstDate(rsReturItem.getTimestamp("min_est_date"));
            venReturItem.setReturItemId(rsReturItem.getLong("retur_item_id"));
            venReturItem.setPackageCount(rsReturItem.getInt("package_count"));
            venReturItem.setPrice(rsReturItem.getBigDecimal("price"));
            venReturItem.setQuantity(rsReturItem.getInt("quantity"));
            venReturItem.setSaltCode(rsReturItem.getString("salt_code"));
            venReturItem.setShippingCost(rsReturItem.getBigDecimal("shipping_cost"));
            venReturItem.setShippingWeight(rsReturItem.getBigDecimal("shipping_weight"));
            venReturItem.setSpecialHandlingInstructions(rsReturItem.getString("special_handling_instructions"));
            venReturItem.setTotal(rsReturItem.getBigDecimal("total"));
            venReturItem.setWcsReturItemId(rsReturItem.getString("wcs_retur_item_id"));
            venReturItem.setLogisticsPricePerKg(rsReturItem.getBigDecimal("logistics_price_per_kg"));

            VenAddress shippingAddress = new VenAddress();
            shippingAddress.setAddressId(rsReturItem.getLong("shipping_address_id"));
            venReturItem.setVenAddress(shippingAddress);

            VenMerchantProduct product = new VenMerchantProduct();
            product.setProductId(rsReturItem.getLong("product_id"));
            venReturItem.setVenMerchantProduct(product);

            VenRetur retur = new VenRetur();
            retur.setWcsReturId(rsReturItem.getString("wcs_retur_id"));
            retur.setReturId(rsReturItem.getLong("retur_id"));
            venReturItem.setVenRetur(retur);

            VenOrderStatus returStatus = new VenOrderStatus();
            returStatus.setOrderStatusId(rsReturItem.getLong("retur_status_id"));
            venReturItem.setVenReturStatus(returStatus);

            VenParty venParty = new VenParty();
            venParty.setPartyId(rsReturItem.getLong("party_id"));
            venParty.setFullOrLegalName(rsReturItem.getString("full_or_legal_name"));

            VenRecipient venRecipient = new VenRecipient();
            venRecipient.setRecipientId(rsReturItem.getLong("recipient_id"));
            venRecipient.setVenParty(venParty);
            venReturItem.setVenRecipient(venRecipient);

            LogLogisticService logisticService = new LogLogisticService();
            logisticService.setLogisticsServiceId(rsReturItem.getLong("logistics_service_id"));
            logisticService.setServiceCode(rsReturItem.getString("service_code"));
            venReturItem.setLogLogisticService(logisticService);
        }

        return venReturItem;

    }

    private LogAirwayBill airwayBillResultSetMapper(ResultSet rsAirwayBill, LogLogisticsProvider logisticsProvider, AirwayBillTransaction airwayBillTransaction, VenOrderItem orderItem) throws SQLException {
        LogAirwayBill existingLogAirwayBill = new LogAirwayBill();

        rsAirwayBill.last();
        int totalAirwayBill = rsAirwayBill.getRow();
        rsAirwayBill.beforeFirst();

        if (totalAirwayBill > 0) {
            rsAirwayBill.next();

            /**
             * Airway Bill Automation Order
             */
            if (airwayBillTransaction != null) {

                existingLogAirwayBill.setAirwayBillId(rsAirwayBill.getLong("airway_bill_id"));
                existingLogAirwayBill.setActivityApprovedByUserId(rsAirwayBill.getString("activity_approved_by_user_id"));
                existingLogAirwayBill.setActivityFileNameAndLoc(rsAirwayBill.getString("activity_file_name_and_loc"));
                existingLogAirwayBill.setActivityResultStatus(rsAirwayBill.getString("activity_result_status"));
                existingLogAirwayBill.setActualPickupDate(airwayBillTransaction.getTanggalActualPickup()); // from airwaybill transaction
                existingLogAirwayBill.setAddress(rsAirwayBill.getString("address"));
                existingLogAirwayBill.setAirwayBillNumber(airwayBillTransaction.getAirwayBillNo()); // from airwaybill transaction
                existingLogAirwayBill.setAirwayBillPickupDateTime(rsAirwayBill.getTimestamp("airway_bill_pickup_date_time"));
                existingLogAirwayBill.setAirwayBillTimestamp(rsAirwayBill.getTimestamp("airway_bill_timestamp"));
                existingLogAirwayBill.setConsignee(rsAirwayBill.getString("consignee"));
                existingLogAirwayBill.setContactPerson(rsAirwayBill.getString("contact_person"));
                existingLogAirwayBill.setContent(airwayBillTransaction.getNamaProduk()); // from airwaybill transaction
                existingLogAirwayBill.setDateOfReturn(rsAirwayBill.getDate("date_of_return"));
                existingLogAirwayBill.setDeliveryOrder(rsAirwayBill.getString("delivery_order"));
                existingLogAirwayBill.setDestCode(airwayBillTransaction.getKodeDestination()); // from airwaybill transaction
                existingLogAirwayBill.setDestination(rsAirwayBill.getString("destination"));
//				existingLogAirwayBill.setGdnReference(airwayBillTransaction.getGdnRef()); // from airwaybill transaction
                existingLogAirwayBill.setGiftWrapCharge(airwayBillTransaction.getGiftWrap()); // from airwaybill transaction
                existingLogAirwayBill.setInsuranceCharge(airwayBillTransaction.getInsuranceCost()); // from airwaybill transaction
                existingLogAirwayBill.setInsuredAmount(airwayBillTransaction.getAirwaybillInsuranceCost()); // from airwaybill transaction
                existingLogAirwayBill.setInvoiceApprovedByUserId(rsAirwayBill.getString("invoice_approved_by_user_id"));
                existingLogAirwayBill.setInvoiceFileNameAndLoc(rsAirwayBill.getString("invoice_file_name_and_loc"));
                existingLogAirwayBill.setInvoiceResultStatus(rsAirwayBill.getString("invoice_result_status"));
                existingLogAirwayBill.setMtaData(rsAirwayBill.getBoolean("mta_data"));
                existingLogAirwayBill.setNoteReturn(rsAirwayBill.getString("note_return"));
                existingLogAirwayBill.setNoteUndelivered(rsAirwayBill.getString("note_undelivered"));
                existingLogAirwayBill.setNumPackages(airwayBillTransaction.getQtyProduk()); // from airwaybill transaction
                existingLogAirwayBill.setOrigin(rsAirwayBill.getString("origin"));
                existingLogAirwayBill.setOtherCharge(rsAirwayBill.getBigDecimal("other_charge"));
                existingLogAirwayBill.setPackageWeight(new BigDecimal(airwayBillTransaction.getWeight())); // from airwaybill transaction
                existingLogAirwayBill.setPricePerKg(airwayBillTransaction.getPricePerKg()); // from airwaybill transaction
                existingLogAirwayBill.setProviderTotalCharge(rsAirwayBill.getBigDecimal("provider_total_charge"));
                existingLogAirwayBill.setReceived(airwayBillTransaction.getReceived()); // from airwaybill transaction
                existingLogAirwayBill.setRecipient(airwayBillTransaction.getRecipient()); // from airwaybill transaction
                existingLogAirwayBill.setRelation(airwayBillTransaction.getRelation()); // from airwaybill transaction
                existingLogAirwayBill.setReturn_(rsAirwayBill.getString("return"));
                existingLogAirwayBill.setService(airwayBillTransaction.getKodeLogistik()); // from airwaybill transaction
                existingLogAirwayBill.setShipper(airwayBillTransaction.getNamaPengirim()); // from airwaybill transaction
                existingLogAirwayBill.setStatus(airwayBillTransaction.getStatus()); // from airwaybill transaction
                existingLogAirwayBill.setTariff(rsAirwayBill.getString("tariff"));
                existingLogAirwayBill.setTotalCharge(rsAirwayBill.getBigDecimal("total_charge"));
                existingLogAirwayBill.setTrackingNumber(rsAirwayBill.getString("tracking_number"));
                existingLogAirwayBill.setType(rsAirwayBill.getString("type"));
                existingLogAirwayBill.setUndelivered(rsAirwayBill.getDate("undelivered"));
                existingLogAirwayBill.setZip(rsAirwayBill.getString("zip"));
                existingLogAirwayBill.setKpiPickupPerfClocked(rsAirwayBill.getBoolean("kpi_pickup_perf_clocked"));
                existingLogAirwayBill.setKpiDeliveryPerfClocked(rsAirwayBill.getBoolean("kpi_delivery_perf_clocked"));
                existingLogAirwayBill.setKpiInvoiceAccuracyClocked(rsAirwayBill.getBoolean("kpi_invoice_accuracy_clocked"));

                LogApprovalStatus logApprovalStatus1 = new LogApprovalStatus();
                logApprovalStatus1.setApprovalStatusId(rsAirwayBill.getLong("invoice_approval_status_id"));
                existingLogAirwayBill.setLogApprovalStatus1(logApprovalStatus1);

                LogApprovalStatus logApprovalStatus2 = new LogApprovalStatus();
                logApprovalStatus2.setApprovalStatusId(rsAirwayBill.getLong("activity_approval_status_id"));
                existingLogAirwayBill.setLogApprovalStatus2(logApprovalStatus2);

                existingLogAirwayBill.setLogLogisticsProvider(logisticsProvider);

                existingLogAirwayBill.setVenOrderItem(orderItem);

                /**
                 * Order before airway bill automation
                 */
            } else {

                existingLogAirwayBill.setAirwayBillId(rsAirwayBill.getLong("airway_bill_id"));
                existingLogAirwayBill.setActivityApprovedByUserId(rsAirwayBill.getString("activity_approved_by_user_id"));
                existingLogAirwayBill.setActivityFileNameAndLoc(rsAirwayBill.getString("activity_file_name_and_loc"));
                existingLogAirwayBill.setActivityResultStatus(rsAirwayBill.getString("activity_result_status"));
                existingLogAirwayBill.setActualPickupDate(rsAirwayBill.getDate("actual_pickup_date"));
                existingLogAirwayBill.setAddress(rsAirwayBill.getString("address"));
                existingLogAirwayBill.setAirwayBillNumber(rsAirwayBill.getString("airway_bill_number"));
                existingLogAirwayBill.setAirwayBillPickupDateTime(rsAirwayBill.getTimestamp("airway_bill_pickup_date_time"));
                existingLogAirwayBill.setAirwayBillTimestamp(rsAirwayBill.getTimestamp("airway_bill_timestamp"));
                existingLogAirwayBill.setConsignee(rsAirwayBill.getString("consignee"));
                existingLogAirwayBill.setContactPerson(rsAirwayBill.getString("contact_person"));
                existingLogAirwayBill.setContent(rsAirwayBill.getString("content"));
                existingLogAirwayBill.setDateOfReturn(rsAirwayBill.getDate("date_of_return"));
                existingLogAirwayBill.setDeliveryOrder(rsAirwayBill.getString("delivery_order"));
                existingLogAirwayBill.setDestCode(rsAirwayBill.getString("dest_code"));
                existingLogAirwayBill.setDestination(rsAirwayBill.getString("destination"));
                existingLogAirwayBill.setGdnReference(rsAirwayBill.getString("gdn_reference"));
                existingLogAirwayBill.setGiftWrapCharge(rsAirwayBill.getBigDecimal("gift_wrap_charge"));
                existingLogAirwayBill.setInsuranceCharge(rsAirwayBill.getBigDecimal("insurance_charge"));
                existingLogAirwayBill.setInsuredAmount(rsAirwayBill.getBigDecimal("insured_amount"));
                existingLogAirwayBill.setInvoiceApprovedByUserId(rsAirwayBill.getString("invoice_approved_by_user_id"));
                existingLogAirwayBill.setInvoiceFileNameAndLoc(rsAirwayBill.getString("invoice_file_name_and_loc"));
                existingLogAirwayBill.setInvoiceResultStatus(rsAirwayBill.getString("invoice_result_status"));
                existingLogAirwayBill.setMtaData(rsAirwayBill.getBoolean("mta_data"));
                existingLogAirwayBill.setNoteReturn(rsAirwayBill.getString("note_return"));
                existingLogAirwayBill.setNoteUndelivered(rsAirwayBill.getString("note_undelivered"));
                existingLogAirwayBill.setNumPackages(rsAirwayBill.getInt("num_packages"));
                existingLogAirwayBill.setOrigin(rsAirwayBill.getString("origin"));
                existingLogAirwayBill.setOtherCharge(rsAirwayBill.getBigDecimal("other_charge"));
                existingLogAirwayBill.setPackageWeight(rsAirwayBill.getBigDecimal("package_weight"));
                existingLogAirwayBill.setPricePerKg(rsAirwayBill.getBigDecimal("price_per_kg"));
                existingLogAirwayBill.setProviderTotalCharge(rsAirwayBill.getBigDecimal("provider_total_charge"));
                existingLogAirwayBill.setReceived(rsAirwayBill.getDate("received"));
                existingLogAirwayBill.setRecipient(rsAirwayBill.getString("recipient"));
                existingLogAirwayBill.setRelation(rsAirwayBill.getString("relation"));
                existingLogAirwayBill.setReturn_(rsAirwayBill.getString("return"));
                existingLogAirwayBill.setService(rsAirwayBill.getString("service"));
                existingLogAirwayBill.setShipper(rsAirwayBill.getString("shipper"));
                existingLogAirwayBill.setStatus(rsAirwayBill.getString("status"));
                existingLogAirwayBill.setTariff(rsAirwayBill.getString("tariff"));
                existingLogAirwayBill.setTotalCharge(rsAirwayBill.getBigDecimal("total_charge"));
                existingLogAirwayBill.setTrackingNumber(rsAirwayBill.getString("tracking_number"));
                existingLogAirwayBill.setType(rsAirwayBill.getString("type"));
                existingLogAirwayBill.setUndelivered(rsAirwayBill.getDate("undelivered"));
                existingLogAirwayBill.setZip(rsAirwayBill.getString("zip"));
                existingLogAirwayBill.setKpiPickupPerfClocked(rsAirwayBill.getBoolean("kpi_pickup_perf_clocked"));
                existingLogAirwayBill.setKpiDeliveryPerfClocked(rsAirwayBill.getBoolean("kpi_delivery_perf_clocked"));
                existingLogAirwayBill.setKpiInvoiceAccuracyClocked(rsAirwayBill.getBoolean("kpi_invoice_accuracy_clocked"));

                LogApprovalStatus logApprovalStatus1 = new LogApprovalStatus();
                logApprovalStatus1.setApprovalStatusId(rsAirwayBill.getLong("invoice_approval_status_id"));
                existingLogAirwayBill.setLogApprovalStatus1(logApprovalStatus1);

                LogApprovalStatus logApprovalStatus2 = new LogApprovalStatus();
                logApprovalStatus2.setApprovalStatusId(rsAirwayBill.getLong("activity_approval_status_id"));
                existingLogAirwayBill.setLogApprovalStatus2(logApprovalStatus2);

                existingLogAirwayBill.setLogLogisticsProvider(logisticsProvider);

                existingLogAirwayBill.setVenOrderItem(orderItem);

            }

        }
//		else{
//			
//			existingLogAirwayBill.setMtaData(false);
//			existingLogAirwayBill.setActualPickupDate(airwayBillTransaction.getTanggalActualPickup()); // from airwaybill transaction
//			existingLogAirwayBill.setAirwayBillNumber(airwayBillTransaction.getAirwayBillNo()); // from airwaybill transaction
//			existingLogAirwayBill.setContent(airwayBillTransaction.getNamaProduk()); // from airwaybill transaction
//			existingLogAirwayBill.setDestCode(airwayBillTransaction.getKodeDestination()); // from airwaybill transaction
//			existingLogAirwayBill.setGdnReference(airwayBillTransaction.getGdnRef()); // from airwaybill transaction
//			existingLogAirwayBill.setGiftWrapCharge(airwayBillTransaction.getGiftWrap()); // from airwaybill transaction
//			existingLogAirwayBill.setInsuranceCharge(airwayBillTransaction.getInsuranceCost()); // from airwaybill transaction
//			existingLogAirwayBill.setInsuredAmount(airwayBillTransaction.getAirwaybillInsuranceCost()); // from airwaybill transaction
//			existingLogAirwayBill.setNumPackages(airwayBillTransaction.getQtyProduk()); // from airwaybill transaction
//			existingLogAirwayBill.setPackageWeight(new BigDecimal(airwayBillTransaction.getWeight())); // from airwaybill transaction
//			existingLogAirwayBill.setPricePerKg(airwayBillTransaction.getPricePerKg()); // from airwaybill transaction
//			existingLogAirwayBill.setReceived(airwayBillTransaction.getReceived()); // from airwaybill transaction
//			existingLogAirwayBill.setRecipient(airwayBillTransaction.getRecipient()); // from airwaybill transaction
//			existingLogAirwayBill.setRelation(airwayBillTransaction.getRelation()); // from airwaybill transaction
//			existingLogAirwayBill.setService(airwayBillTransaction.getKodeLogistik()); // from airwaybill transaction
//			existingLogAirwayBill.setShipper(airwayBillTransaction.getNamaPengirim()); // from airwaybill transaction
//			existingLogAirwayBill.setStatus(airwayBillTransaction.getStatus()); // from airwaybill transaction
//			
//			LogApprovalStatus logApprovalStatus1 = new LogApprovalStatus();
//			logApprovalStatus1.setApprovalStatusId(0L);
//			existingLogAirwayBill.setLogApprovalStatus1(logApprovalStatus1);
//			
//			_log.info("invoice_approval_status_id = " + existingLogAirwayBill.getLogApprovalStatus1());
//				
//			LogApprovalStatus logApprovalStatus2 = new LogApprovalStatus();
//			logApprovalStatus2.setApprovalStatusId(0L);
//			existingLogAirwayBill.setLogApprovalStatus2(logApprovalStatus2);
//			
//			_log.info("activity_approval_status_id = " + existingLogAirwayBill.getLogApprovalStatus2());
//			
//			existingLogAirwayBill.setLogLogisticsProvider(logisticsProvider);
//			
//			existingLogAirwayBill.setVenOrderItem(orderItem);
//		}

        return existingLogAirwayBill;

    }

    private LogAirwayBillRetur airwayBillReturResultSetMapper(ResultSet rsAirwayBill, LogLogisticsProvider logisticsProvider, AirwayBillTransaction airwayBillTransaction, VenReturItem returItem) throws SQLException {
        LogAirwayBillRetur existingLogAirwayBillRetur = new LogAirwayBillRetur();

        rsAirwayBill.last();
        int totalAirwayBill = rsAirwayBill.getRow();
        rsAirwayBill.beforeFirst();

        if (totalAirwayBill > 0) {
            rsAirwayBill.next();

            /**
             * Airway Bill Automation Order
             */
            if (airwayBillTransaction != null) {

                existingLogAirwayBillRetur.setAirwayBillReturId(rsAirwayBill.getLong("airway_bill_retur_id"));
                existingLogAirwayBillRetur.setActivityApprovedByUserId(rsAirwayBill.getString("activity_approved_by_user_id"));
                existingLogAirwayBillRetur.setActivityFileNameAndLoc(rsAirwayBill.getString("activity_file_name_and_loc"));
                existingLogAirwayBillRetur.setActivityResultStatus(rsAirwayBill.getString("activity_result_status"));
                existingLogAirwayBillRetur.setActualPickupDate(airwayBillTransaction.getTanggalActualPickup()); // from airwaybill transaction
                existingLogAirwayBillRetur.setAddress(rsAirwayBill.getString("address"));
                existingLogAirwayBillRetur.setAirwayBillNumber(airwayBillTransaction.getAirwayBillNo()); // from airwaybill transaction
                existingLogAirwayBillRetur.setAirwayBillPickupDateTime(rsAirwayBill.getTimestamp("airway_bill_pickup_date_time"));
                existingLogAirwayBillRetur.setAirwayBillTimestamp(rsAirwayBill.getTimestamp("airway_bill_timestamp"));
                existingLogAirwayBillRetur.setConsignee(rsAirwayBill.getString("consignee"));
                existingLogAirwayBillRetur.setContactPerson(rsAirwayBill.getString("contact_person"));
                existingLogAirwayBillRetur.setContent(airwayBillTransaction.getNamaProduk()); // from airwaybill transaction
                existingLogAirwayBillRetur.setDateOfReturn(rsAirwayBill.getDate("date_of_return"));
                existingLogAirwayBillRetur.setDeliveryOrder(rsAirwayBill.getString("delivery_order"));
                existingLogAirwayBillRetur.setDestCode(airwayBillTransaction.getKodeDestination()); // from airwaybill transaction
                existingLogAirwayBillRetur.setDestination(rsAirwayBill.getString("destination"));
//				existingLogAirwayBill.setGdnReference(airwayBillTransaction.getGdnRef()); // from airwaybill transaction
                existingLogAirwayBillRetur.setGiftWrapCharge(airwayBillTransaction.getGiftWrap()); // from airwaybill transaction
                existingLogAirwayBillRetur.setInsuranceCharge(airwayBillTransaction.getInsuranceCost()); // from airwaybill transaction
                existingLogAirwayBillRetur.setInsuredAmount(airwayBillTransaction.getAirwaybillInsuranceCost()); // from airwaybill transaction
                existingLogAirwayBillRetur.setInvoiceApprovedByUserId(rsAirwayBill.getString("invoice_approved_by_user_id"));
                existingLogAirwayBillRetur.setInvoiceFileNameAndLoc(rsAirwayBill.getString("invoice_file_name_and_loc"));
                existingLogAirwayBillRetur.setInvoiceResultStatus(rsAirwayBill.getString("invoice_result_status"));
                existingLogAirwayBillRetur.setMtaData(rsAirwayBill.getBoolean("mta_data"));
                existingLogAirwayBillRetur.setNoteReturn(rsAirwayBill.getString("note_return"));
                existingLogAirwayBillRetur.setNoteUndelivered(rsAirwayBill.getString("note_undelivered"));
                existingLogAirwayBillRetur.setNumPackages(airwayBillTransaction.getQtyProduk()); // from airwaybill transaction
                existingLogAirwayBillRetur.setOrigin(rsAirwayBill.getString("origin"));
                existingLogAirwayBillRetur.setOtherCharge(rsAirwayBill.getBigDecimal("other_charge"));
                existingLogAirwayBillRetur.setPackageWeight(new BigDecimal(airwayBillTransaction.getWeight())); // from airwaybill transaction
                existingLogAirwayBillRetur.setPricePerKg(airwayBillTransaction.getPricePerKg()); // from airwaybill transaction
                existingLogAirwayBillRetur.setProviderTotalCharge(rsAirwayBill.getBigDecimal("provider_total_charge"));
                existingLogAirwayBillRetur.setReceived(airwayBillTransaction.getReceived()); // from airwaybill transaction
                existingLogAirwayBillRetur.setRecipient(airwayBillTransaction.getRecipient()); // from airwaybill transaction
                existingLogAirwayBillRetur.setRelation(airwayBillTransaction.getRelation()); // from airwaybill transaction
                existingLogAirwayBillRetur.setReturn_(rsAirwayBill.getString("return"));
                existingLogAirwayBillRetur.setService(airwayBillTransaction.getKodeLogistik()); // from airwaybill transaction
                existingLogAirwayBillRetur.setShipper(airwayBillTransaction.getNamaPengirim()); // from airwaybill transaction
                existingLogAirwayBillRetur.setStatus(airwayBillTransaction.getStatus()); // from airwaybill transaction
                existingLogAirwayBillRetur.setTariff(rsAirwayBill.getString("tariff"));
                existingLogAirwayBillRetur.setTotalCharge(rsAirwayBill.getBigDecimal("total_charge"));
                existingLogAirwayBillRetur.setTrackingNumber(rsAirwayBill.getString("tracking_number"));
                existingLogAirwayBillRetur.setType(rsAirwayBill.getString("type"));
                existingLogAirwayBillRetur.setUndelivered(rsAirwayBill.getDate("undelivered"));
                existingLogAirwayBillRetur.setZip(rsAirwayBill.getString("zip"));
                existingLogAirwayBillRetur.setKpiPickupPerfClocked(rsAirwayBill.getBoolean("kpi_pickup_perf_clocked"));
                existingLogAirwayBillRetur.setKpiDeliveryPerfClocked(rsAirwayBill.getBoolean("kpi_delivery_perf_clocked"));
                existingLogAirwayBillRetur.setKpiInvoiceAccuracyClocked(rsAirwayBill.getBoolean("kpi_invoice_accuracy_clocked"));

                LogApprovalStatus logApprovalStatus1 = new LogApprovalStatus();
                logApprovalStatus1.setApprovalStatusId(rsAirwayBill.getLong("invoice_approval_status_id"));
                existingLogAirwayBillRetur.setLogApprovalStatus1(logApprovalStatus1);

                LogApprovalStatus logApprovalStatus2 = new LogApprovalStatus();
                logApprovalStatus2.setApprovalStatusId(rsAirwayBill.getLong("activity_approval_status_id"));
                existingLogAirwayBillRetur.setLogApprovalStatus2(logApprovalStatus2);

                existingLogAirwayBillRetur.setLogLogisticsProvider(logisticsProvider);

                existingLogAirwayBillRetur.setVenReturItem(returItem);

                /**
                 * Order before airway bill automation
                 */
            } else {

                existingLogAirwayBillRetur.setAirwayBillReturId(rsAirwayBill.getLong("airway_bill_retur_id"));
                existingLogAirwayBillRetur.setActivityApprovedByUserId(rsAirwayBill.getString("activity_approved_by_user_id"));
                existingLogAirwayBillRetur.setActivityFileNameAndLoc(rsAirwayBill.getString("activity_file_name_and_loc"));
                existingLogAirwayBillRetur.setActivityResultStatus(rsAirwayBill.getString("activity_result_status"));
                existingLogAirwayBillRetur.setActualPickupDate(rsAirwayBill.getDate("actual_pickup_date"));
                existingLogAirwayBillRetur.setAddress(rsAirwayBill.getString("address"));
                existingLogAirwayBillRetur.setAirwayBillNumber(rsAirwayBill.getString("airway_bill_number"));
                existingLogAirwayBillRetur.setAirwayBillPickupDateTime(rsAirwayBill.getTimestamp("airway_bill_pickup_date_time"));
                existingLogAirwayBillRetur.setAirwayBillTimestamp(rsAirwayBill.getTimestamp("airway_bill_timestamp"));
                existingLogAirwayBillRetur.setConsignee(rsAirwayBill.getString("consignee"));
                existingLogAirwayBillRetur.setContactPerson(rsAirwayBill.getString("contact_person"));
                existingLogAirwayBillRetur.setContent(rsAirwayBill.getString("content"));
                existingLogAirwayBillRetur.setDateOfReturn(rsAirwayBill.getDate("date_of_return"));
                existingLogAirwayBillRetur.setDeliveryOrder(rsAirwayBill.getString("delivery_order"));
                existingLogAirwayBillRetur.setDestCode(rsAirwayBill.getString("dest_code"));
                existingLogAirwayBillRetur.setDestination(rsAirwayBill.getString("destination"));
                existingLogAirwayBillRetur.setGdnReference(rsAirwayBill.getString("gdn_reference"));
                existingLogAirwayBillRetur.setGiftWrapCharge(rsAirwayBill.getBigDecimal("gift_wrap_charge"));
                existingLogAirwayBillRetur.setInsuranceCharge(rsAirwayBill.getBigDecimal("insurance_charge"));
                existingLogAirwayBillRetur.setInsuredAmount(rsAirwayBill.getBigDecimal("insured_amount"));
                existingLogAirwayBillRetur.setInvoiceApprovedByUserId(rsAirwayBill.getString("invoice_approved_by_user_id"));
                existingLogAirwayBillRetur.setInvoiceFileNameAndLoc(rsAirwayBill.getString("invoice_file_name_and_loc"));
                existingLogAirwayBillRetur.setInvoiceResultStatus(rsAirwayBill.getString("invoice_result_status"));
                existingLogAirwayBillRetur.setMtaData(rsAirwayBill.getBoolean("mta_data"));
                existingLogAirwayBillRetur.setNoteReturn(rsAirwayBill.getString("note_return"));
                existingLogAirwayBillRetur.setNoteUndelivered(rsAirwayBill.getString("note_undelivered"));
                existingLogAirwayBillRetur.setNumPackages(rsAirwayBill.getInt("num_packages"));
                existingLogAirwayBillRetur.setOrigin(rsAirwayBill.getString("origin"));
                existingLogAirwayBillRetur.setOtherCharge(rsAirwayBill.getBigDecimal("other_charge"));
                existingLogAirwayBillRetur.setPackageWeight(rsAirwayBill.getBigDecimal("package_weight"));
                existingLogAirwayBillRetur.setPricePerKg(rsAirwayBill.getBigDecimal("price_per_kg"));
                existingLogAirwayBillRetur.setProviderTotalCharge(rsAirwayBill.getBigDecimal("provider_total_charge"));
                existingLogAirwayBillRetur.setReceived(rsAirwayBill.getDate("received"));
                existingLogAirwayBillRetur.setRecipient(rsAirwayBill.getString("recipient"));
                existingLogAirwayBillRetur.setRelation(rsAirwayBill.getString("relation"));
                existingLogAirwayBillRetur.setReturn_(rsAirwayBill.getString("return"));
                existingLogAirwayBillRetur.setService(rsAirwayBill.getString("service"));
                existingLogAirwayBillRetur.setShipper(rsAirwayBill.getString("shipper"));
                existingLogAirwayBillRetur.setStatus(rsAirwayBill.getString("status"));
                existingLogAirwayBillRetur.setTariff(rsAirwayBill.getString("tariff"));
                existingLogAirwayBillRetur.setTotalCharge(rsAirwayBill.getBigDecimal("total_charge"));
                existingLogAirwayBillRetur.setTrackingNumber(rsAirwayBill.getString("tracking_number"));
                existingLogAirwayBillRetur.setType(rsAirwayBill.getString("type"));
                existingLogAirwayBillRetur.setUndelivered(rsAirwayBill.getDate("undelivered"));
                existingLogAirwayBillRetur.setZip(rsAirwayBill.getString("zip"));
                existingLogAirwayBillRetur.setKpiPickupPerfClocked(rsAirwayBill.getBoolean("kpi_pickup_perf_clocked"));
                existingLogAirwayBillRetur.setKpiDeliveryPerfClocked(rsAirwayBill.getBoolean("kpi_delivery_perf_clocked"));
                existingLogAirwayBillRetur.setKpiInvoiceAccuracyClocked(rsAirwayBill.getBoolean("kpi_invoice_accuracy_clocked"));

                LogApprovalStatus logApprovalStatus1 = new LogApprovalStatus();
                logApprovalStatus1.setApprovalStatusId(rsAirwayBill.getLong("invoice_approval_status_id"));
                existingLogAirwayBillRetur.setLogApprovalStatus1(logApprovalStatus1);

                LogApprovalStatus logApprovalStatus2 = new LogApprovalStatus();
                logApprovalStatus2.setApprovalStatusId(rsAirwayBill.getLong("activity_approval_status_id"));
                existingLogAirwayBillRetur.setLogApprovalStatus2(logApprovalStatus2);

                existingLogAirwayBillRetur.setLogLogisticsProvider(logisticsProvider);

                existingLogAirwayBillRetur.setVenReturItem(returItem);

            }

        }

        return existingLogAirwayBillRetur;

    }

    private void filterDataJNE(ArrayList<PojoInterface> dataToFilter, HashMap<String, String> gdnRefNotFoundList, HashMap<String, String> failedRecord, Connection conn) {
        Boolean isDataOK = true;
        String problem = "";
        int length = dataToFilter.size();

        // prepared sql statements
        PreparedStatement psItem = null;
        PreparedStatement psAirwayBill = null;

        try {
            for (int i = 0; i < length; i++) {
                Boolean isRetur = false;
                isDataOK = true;
                DailyReportJNE dailyReportJNE = (DailyReportJNE) dataToFilter.get(i);

                try {

                    String wcsOrderItemId = "";

                    try {
                        StringTokenizer st = new StringTokenizer(dailyReportJNE.getGdnRefNumber());
                        String orderOrRMA = st.nextToken("-");
                        String wcsOrderId = st.nextToken("-");
                        wcsOrderItemId = st.nextToken();
                        Integer sequence = new Integer(st.nextToken());
                        _log.debug("Tokenized string from report to get orderOrRMA:" + orderOrRMA + " OrderId:" + wcsOrderId + " OrderItemId:" + wcsOrderItemId + " Sequence:" + sequence);

                        if (orderOrRMA.equals("R")) {
                            isRetur = true;
                        }

                    } catch (Exception e) {
                        _log.error("GDN Ref Problem", e);
                        failedRecord.put("No : " + dailyReportJNE.getNumber().replace(".0", ""), "Ops please fix GDN Ref Format Problem");

                        dataToFilter.remove(dataToFilter.get(i));
                        _log.debug("data not ok, remove from list: " + dailyReportJNE.getGdnRefNumber());
                        --i;
                        --length;
                        continue;
                    }

                    if (isDataOK == true) {
                        // query order item
                        if (isRetur) {
                            psItem = conn.prepareCall(COUNT_RETUR_ITEM_BY_WCS_RETUR_ITEM_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                            psAirwayBill = conn.prepareCall(COUNT_AIRWAY_BILL_RETUR_SQL_BY_GDN_REF, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        } else {
                            psItem = conn.prepareCall(COUNT_ORDER_ITEM_BY_WCS_ORDER_ITEM_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                            psAirwayBill = conn.prepareCall(COUNT_AIRWAY_BILL_SQL_BY_GDN_REF, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        }

                        psItem.setString(1, wcsOrderItemId);
                        ResultSet rsItem = psItem.executeQuery();
                        rsItem.next();

                        if (isRetur) {
                            if (rsItem.getInt("totaReturItem") == 0) {
                                _log.info("Retur item id not found in venice: " + wcsOrderItemId);
                                isDataOK = false;

                                problem = "Retur item id not found in venice";
                            }
                        } else {
                            if (rsItem.getInt("totalOrderItem") == 0) {
                                _log.info("Order item id not found in venice: " + wcsOrderItemId);
                                isDataOK = false;

                                problem = "Order item id not found in venice";
                            }
                        }

                        // close result set
                        rsItem.close();
                        rsItem = null;
                    }

                    if (isDataOK == true) {
                        // query airway bill
                        String awbStatus = "";

                        try {
                            awbStatus = awbConn.getGDNRefStatus(dailyReportJNE.getGdnRefNumber());
                        } catch (Exception e) {
                            _log.info("GDN Ref " + dailyReportJNE.getGdnRefNumber() + " is not available on Airway Bill Engine", e);
                        }

                        if (awbStatus == null || awbStatus.equals("")) {

                            // check if data comes from before airwaybill automation
                            psAirwayBill.setString(1, dailyReportJNE.getGdnRefNumber());
                            ResultSet rsAirwayBill = psAirwayBill.executeQuery();
                            rsAirwayBill.next();

                            if (isRetur) {
                                if (rsAirwayBill.getInt("totalAirwayBillRetur") == 0) {
                                    _log.info("GDN Ref retur not found in venice: " + dailyReportJNE.getGdnRefNumber());
                                    isDataOK = false;

                                    String gdnRefActual = "";

                                    try {
                                        gdnRefActual = awbConn.getGDNRef(wcsOrderItemId);
                                    } catch (Exception e) {
                                        _log.info("", e);
                                    }

                                    // GDN Ref not found in system
                                    if (gdnRefActual == null || gdnRefActual.equals("")) {
                                        problem = "GDN Ref retur not found in system";
                                        // GDN Ref uploaded doesn't match with system
                                    } else {
                                        problem = "GDN Ref retur on system " + gdnRefActual;
                                    }

                                } else {
                                    _log.info("GDN Ref of an retur from before airway bill automation: " + dailyReportJNE.getGdnRefNumber());
                                }
                            } else {
                                if (rsAirwayBill.getInt("totalAirwayBill") == 0) {
                                    _log.info("GDN Ref order not found in venice: " + dailyReportJNE.getGdnRefNumber());
                                    isDataOK = false;

                                    String gdnRefActual = "";

                                    try {
                                        gdnRefActual = awbConn.getGDNRef(wcsOrderItemId);
                                    } catch (Exception e) {
                                        _log.info("", e);
                                    }

                                    // GDN Ref not found in system
                                    if (gdnRefActual == null || gdnRefActual.equals("")) {
                                        problem = "GDN Ref not found in system";
                                        // GDN Ref uploaded doesn't match with system
                                    } else {
                                        problem = "GDN Ref on system " + gdnRefActual;
                                    }

                                } else {
                                    _log.info("GDN Ref of an order from before airway bill automation: " + dailyReportJNE.getGdnRefNumber());
                                }
                            }

                            rsAirwayBill.close();

                        }
                    }

                    if (isDataOK == false) {
                        dataToFilter.remove(dataToFilter.get(i));
                        gdnRefNotFoundList.put(dailyReportJNE.getGdnRefNumber(), problem);
                        _log.debug("data not ok, remove from list: " + dailyReportJNE.getGdnRefNumber());
                        --i;
                        --length;
                        continue;
                    } else {
                        _log.info("data is ok, keep in the list: " + dailyReportJNE.getGdnRefNumber());
                    }

                } catch (Exception e) {
                    _log.error("Problem during filtering Activity Report Data", e);
                }
            }

            // close prepared statement
            psItem.close();
            psItem = null;

            psAirwayBill.close();
            psAirwayBill = null;

        } catch (Exception e) {
            _log.error("Error on filterDataJNE ", e);
        }
    }

    private void filterDataNCS(ArrayList<PojoInterface> dataToFilter, HashMap<String, String> gdnRefNotFoundList, HashMap<String, String> failedRecord, Connection conn) {
        Boolean isDataOK = true;
        String problem = "";
        int length = dataToFilter.size();

        try {

            // prepared sql statements
            PreparedStatement psItem = null;
            PreparedStatement psAirwayBill = null;

            for (int i = 0; i < length; i++) {
                Boolean isRetur = false;
                isDataOK = true;
                DailyReportNCS dailyReportNCS = (DailyReportNCS) dataToFilter.get(i);

                try {

                    String wcsOrderItemId = "";

                    try {
                        StringTokenizer st = new StringTokenizer(dailyReportNCS.getRefNo());
                        String orderOrRMA = st.nextToken("-");
                        String wcsOrderId = st.nextToken("-");
                        wcsOrderItemId = st.nextToken();
                        Integer sequence = new Integer(st.nextToken());
                        _log.debug("Tokenized string from report to get orderOrRMA:" + orderOrRMA + " OrderId:" + wcsOrderId + " OrderItemId:" + wcsOrderItemId + " Sequence:" + sequence);

                        if (orderOrRMA.equals("R")) {
                            isRetur = true;
                        }

                    } catch (Exception e) {
                        _log.error("GDN Ref Problem", e);
                        failedRecord.put("Tr No : " + dailyReportNCS.getTrNo(), "Ops please fix GDN Ref Problem: " + e.getMessage());

                        dataToFilter.remove(dataToFilter.get(i));
                        _log.debug("data not ok, remove from list: " + dailyReportNCS.getRefNo());
                        --i;
                        --length;
                        continue;
                    }

                    if (isDataOK == true) {
                        //query order item
                        if (isRetur) {
                            psItem = conn.prepareCall(COUNT_RETUR_ITEM_BY_WCS_RETUR_ITEM_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                            psAirwayBill = conn.prepareCall(COUNT_AIRWAY_BILL_RETUR_SQL_BY_GDN_REF, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        } else {
                            psItem = conn.prepareCall(COUNT_ORDER_ITEM_BY_WCS_ORDER_ITEM_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                            psAirwayBill = conn.prepareCall(COUNT_AIRWAY_BILL_SQL_BY_GDN_REF, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        }
                        psItem.setString(1, wcsOrderItemId);
                        ResultSet rsItem = psItem.executeQuery();
                        rsItem.next();


                        if (isRetur) {
                            if (rsItem.getInt("totalReturItem") == 0) {
                                _log.info("Retur item id not found in venice: " + wcsOrderItemId);
                                isDataOK = false;

                                problem = "Retur item id not found in venice";
                            }
                        } else {
                            if (rsItem.getInt("totalOrderItem") == 0) {
                                _log.info("Order item id not found in venice: " + wcsOrderItemId);
                                isDataOK = false;

                                problem = "Order item id not found in venice";
                            }
                        }

                        // close result set
                        rsItem.close();
                        rsItem = null;
                    }

                    if (isDataOK == true) {
                        // query airway bill
                        String awbStatus = "";

                        try {
                            awbStatus = awbConn.getGDNRefStatus(dailyReportNCS.getRefNo());
                        } catch (Exception e) {
                            _log.info("GDN Ref " + dailyReportNCS.getRefNo() + " is not avaiable on Airway Bill Engine", e);
                        }

                        if (awbStatus == null || awbStatus.equals("")) {

                            // check if data comes from before airwaybill automation
                            psAirwayBill.setString(1, dailyReportNCS.getRefNo());
                            ResultSet rsAirwayBill = psAirwayBill.executeQuery();
                            rsAirwayBill.next();

                            if (isRetur) {
                                if (rsAirwayBill.getInt("totalAirwayBillRetur") == 0) {
                                    _log.info("Gdn reference not found in venice: " + dailyReportNCS.getRefNo());
                                    isDataOK = false;
                                    String gdnRefActual = "";

                                    try {
                                        gdnRefActual = awbConn.getGDNRef(wcsOrderItemId);
                                    } catch (Exception e) {
                                        _log.info("", e);
                                    }

                                    // GDN Ref not found in system
                                    if (gdnRefActual == null || gdnRefActual.equals("")) {
                                        problem = "GDN Ref not found in system";
                                        // GDN Ref uploaded doesn't match with system
                                    } else {
                                        problem = "GDN Ref on system " + gdnRefActual;
                                    }

                                } else {
                                    _log.info("GDN Ref of an retur from before airway bill automation: " + dailyReportNCS.getRefNo());
                                }
                            } else {
                                if (rsAirwayBill.getInt("totalAirwayBill") == 0) {
                                    _log.info("Gdn reference not found in venice: " + dailyReportNCS.getRefNo());
                                    isDataOK = false;
                                    String gdnRefActual = "";

                                    try {
                                        gdnRefActual = awbConn.getGDNRef(wcsOrderItemId);
                                    } catch (Exception e) {
                                        _log.info("", e);
                                    }

                                    // GDN Ref not found in system
                                    if (gdnRefActual == null || gdnRefActual.equals("")) {
                                        problem = "GDN Ref not found in system";
                                        // GDN Ref uploaded doesn't match with system
                                    } else {
                                        problem = "GDN Ref on system " + gdnRefActual;
                                    }

                                } else {
                                    _log.info("GDN Ref of an order from before airway bill automation: " + dailyReportNCS.getRefNo());
                                }
                            }

                            rsAirwayBill.close();

                        }
                    }

                    if (isDataOK == false) {
                        dataToFilter.remove(dataToFilter.get(i));
                        gdnRefNotFoundList.put(dailyReportNCS.getRefNo(), problem);
                        _log.debug("data not ok, remove from list: " + dailyReportNCS.getRefNo());
                        --i;
                        --length;
                        continue;
                    } else {
                        _log.info("data is ok, keep in the list: " + dailyReportNCS.getRefNo());
                    }
                } catch (Exception e) {
                    _log.error("Problem during filtering Activity Report Data", e);
                }
            }

            // close prepared statement
            psItem.close();
            psItem = null;

            psAirwayBill.close();
            psAirwayBill = null;

        } catch (Exception e) {
            _log.error("Error on filterDataNCS ", e);
        }
    }

    private void filterDataMSG(ArrayList<PojoInterface> dataToFilter, HashMap<String, String> gdnRefNotFoundList, HashMap<String, String> failedRecord, Connection conn) {
        Boolean isDataOK = true;
        String problem = "";
        int length = dataToFilter.size();

        try {

            // prepared sql statements
            PreparedStatement psItem = null;
            PreparedStatement psAirwayBill = null;

            for (int i = 0; i < length; i++) {
                Boolean isRetur = false;
                isDataOK = true;
                DailyReportMSG dailyReportMSG = (DailyReportMSG) dataToFilter.get(i);

                try {

                    String wcsOrderItemId = "";

                    try {
                        StringTokenizer st = new StringTokenizer(dailyReportMSG.getRefNo());
                        String orderOrRMA = st.nextToken("-");
                        String wcsOrderId = st.nextToken("-");
                        wcsOrderItemId = st.nextToken();
                        Integer sequence = new Integer(st.nextToken());
                        _log.debug("Tokenized string from report to get orderOrRMA:" + orderOrRMA + " OrderId:" + wcsOrderId + " OrderItemId:" + wcsOrderItemId + " Sequence:" + sequence);

                        if (orderOrRMA.equals("R")) {
                            isRetur = true;
                        }

                    } catch (Exception e) {
                        _log.error("GDN Ref Problem", e);
                        failedRecord.put("Tr No : " + dailyReportMSG.getTrNo(), "Ops please fix GDN Ref Problem: " + e.getMessage());

                        dataToFilter.remove(dataToFilter.get(i));
                        _log.debug("data not ok, remove from list: " + dailyReportMSG.getRefNo());
                        --i;
                        --length;
                        continue;
                    }

                    if (isDataOK == true) {
                        //query order item
                        if (isRetur) {
                            psItem = conn.prepareCall(COUNT_RETUR_ITEM_BY_WCS_RETUR_ITEM_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                            psAirwayBill = conn.prepareCall(COUNT_AIRWAY_BILL_RETUR_SQL_BY_GDN_REF, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        } else {
                            psItem = conn.prepareCall(COUNT_ORDER_ITEM_BY_WCS_ORDER_ITEM_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                            psAirwayBill = conn.prepareCall(COUNT_AIRWAY_BILL_SQL_BY_GDN_REF, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        }
                        psItem.setString(1, wcsOrderItemId);
                        ResultSet rsItem = psItem.executeQuery();
                        rsItem.next();


                        if (isRetur) {
                            if (rsItem.getInt("totalReturItem") == 0) {
                                _log.info("Retur item id not found in venice: " + wcsOrderItemId);
                                isDataOK = false;

                                problem = "Retur item id not found in venice";
                            }
                        } else {
                            if (rsItem.getInt("totalOrderItem") == 0) {
                                _log.info("Order item id not found in venice: " + wcsOrderItemId);
                                isDataOK = false;

                                problem = "Order item id not found in venice";
                            }
                        }

                        // close result set
                        rsItem.close();
                        rsItem = null;
                    }

                    if (isDataOK == true) {
                        // query airway bill
                        String awbStatus = "";

                        try {
                            awbStatus = awbConn.getGDNRefStatus(dailyReportMSG.getRefNo());
                        } catch (Exception e) {
                            _log.info("GDN Ref " + dailyReportMSG.getRefNo() + " is not avaiable on Airway Bill Engine", e);
                        }

                        if (awbStatus == null || awbStatus.equals("")) {

                            // check if data comes from before airwaybill automation
                            psAirwayBill.setString(1, dailyReportMSG.getRefNo());
                            ResultSet rsAirwayBill = psAirwayBill.executeQuery();
                            rsAirwayBill.next();

                            if (isRetur) {
                                if (rsAirwayBill.getInt("totalAirwayBillRetur") == 0) {
                                    _log.info("Gdn reference not found in venice: " + dailyReportMSG.getRefNo());
                                    isDataOK = false;
                                    String gdnRefActual = "";

                                    try {
                                        gdnRefActual = awbConn.getGDNRef(wcsOrderItemId);
                                    } catch (Exception e) {
                                        _log.info("", e);
                                    }

                                    // GDN Ref not found in system
                                    if (gdnRefActual == null || gdnRefActual.equals("")) {
                                        problem = "GDN Ref not found in system";
                                        // GDN Ref uploaded doesn't match with system
                                    } else {
                                        problem = "GDN Ref on system " + gdnRefActual;
                                    }

                                } else {
                                    _log.info("GDN Ref of an retur from before airway bill automation: " + dailyReportMSG.getRefNo());
                                }
                            } else {
                                if (rsAirwayBill.getInt("totalAirwayBill") == 0) {
                                    _log.info("Gdn reference not found in venice: " + dailyReportMSG.getRefNo());
                                    isDataOK = false;
                                    String gdnRefActual = "";

                                    try {
                                        gdnRefActual = awbConn.getGDNRef(wcsOrderItemId);
                                    } catch (Exception e) {
                                        _log.info("", e);
                                    }

                                    // GDN Ref not found in system
                                    if (gdnRefActual == null || gdnRefActual.equals("")) {
                                        problem = "GDN Ref not found in system";
                                        // GDN Ref uploaded doesn't match with system
                                    } else {
                                        problem = "GDN Ref on system " + gdnRefActual;
                                    }

                                } else {
                                    _log.info("GDN Ref of an order from before airway bill automation: " + dailyReportMSG.getRefNo());
                                }
                            }

                            rsAirwayBill.close();

                        }
                    }

                    if (isDataOK == false) {
                        dataToFilter.remove(dataToFilter.get(i));
                        gdnRefNotFoundList.put(dailyReportMSG.getRefNo(), problem);
                        _log.debug("data not ok, remove from list: " + dailyReportMSG.getRefNo());
                        --i;
                        --length;
                        continue;
                    } else {
                        _log.info("data is ok, keep in the list: " + dailyReportMSG.getRefNo());
                    }
                } catch (Exception e) {
                    _log.error("Problem during filtering Activity Report Data", e);
                }
            }

            // close prepared statement
            psItem.close();
            psItem = null;

            psAirwayBill.close();
            psAirwayBill = null;

        } catch (Exception e) {
            _log.error("Error on filterDataMSG ", e);
        }
    }

    private void filterDataRPX(ArrayList<PojoInterface> dataToFilter, HashMap<String, String> gdnRefNotFoundList, HashMap<String, String> failedRecord, Connection conn) {
        Boolean isDataOK = true;
        String problem = "";
        int length = dataToFilter.size();

        try {

            // prepared sql statements
            PreparedStatement psItem = null;
            PreparedStatement psAirwayBill = null;

            for (int i = 0; i < length; i++) {
                Boolean isRetur = false;
                isDataOK = true;
                DailyReportNCS dailyReportRPX = (DailyReportNCS) dataToFilter.get(i);

                try {

                    String wcsOrderItemId = "";

                    try {
                        StringTokenizer st = new StringTokenizer(dailyReportRPX.getRefNo());
                        String orderOrRMA = st.nextToken("-");
                        String wcsOrderId = st.nextToken("-");
                        wcsOrderItemId = st.nextToken();
                        Integer sequence = new Integer(st.nextToken());
                        _log.debug("Tokenized string from report to get orderOrRMA:" + orderOrRMA + " OrderId:" + wcsOrderId + " OrderItemId:" + wcsOrderItemId + " Sequence:" + sequence);

                        if (orderOrRMA.equals("R")) {
                            isRetur = true;
                        }

                    } catch (Exception e) {
                        _log.error("GDN Ref Problem", e);
                        failedRecord.put("Tr No : " + dailyReportRPX.getTrNo(), "Ops please fix GDN Ref Problem: " + e.getMessage());

                        dataToFilter.remove(dataToFilter.get(i));
                        _log.debug("data not ok, remove from list: " + dailyReportRPX.getRefNo());
                        --i;
                        --length;
                        continue;
                    }

                    if (isDataOK == true) {
                        //query order item
                        if (isRetur) {
                            psItem = conn.prepareCall(COUNT_RETUR_ITEM_BY_WCS_RETUR_ITEM_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                            psAirwayBill = conn.prepareCall(COUNT_AIRWAY_BILL_RETUR_SQL_BY_GDN_REF, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        } else {
                            psItem = conn.prepareCall(COUNT_ORDER_ITEM_BY_WCS_ORDER_ITEM_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                            psAirwayBill = conn.prepareCall(COUNT_AIRWAY_BILL_SQL_BY_GDN_REF, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        }

                        psItem.setString(1, wcsOrderItemId);
                        ResultSet rsItem = psItem.executeQuery();
                        rsItem.next();

                        if (isRetur) {
                            if (rsItem.getInt("totaReturItem") == 0) {
                                _log.info("Retur item id not found in venice: " + wcsOrderItemId);
                                isDataOK = false;

                                problem = "Retur item id not found in venice";
                            }
                        } else {
                            if (rsItem.getInt("totalOrderItem") == 0) {
                                _log.info("Order item id not found in venice: " + wcsOrderItemId);
                                isDataOK = false;

                                problem = "Order item id not found in venice";
                            }
                        }

                        // close result set
                        rsItem.close();
                        rsItem = null;
                    }

                    if (isDataOK == true) {
                        // query airway bill
                        String awbStatus = "";

                        try {
                            awbStatus = awbConn.getGDNRefStatus(dailyReportRPX.getRefNo());
                        } catch (Exception e) {
                            _log.info("GDN Ref " + dailyReportRPX.getRefNo() + " is not avaiable on Airway Bill Engine", e);
                        }

                        if (awbStatus == null || awbStatus.equals("")) {

                            // check if data comes from before airwaybill automation
                            psAirwayBill.setString(1, dailyReportRPX.getRefNo());
                            ResultSet rsAirwayBill = psAirwayBill.executeQuery();
                            rsAirwayBill.next();

                            if (isRetur) {
                                if (rsAirwayBill.getInt("totalAirwayBillRetur") == 0) {
                                    _log.info("GDN Ref not found in venice: " + dailyReportRPX.getRefNo());
                                    isDataOK = false;
                                    String gdnRefActual = "";

                                    try {
                                        gdnRefActual = awbConn.getGDNRef(wcsOrderItemId);
                                    } catch (Exception e) {
                                        _log.info("", e);
                                    }

                                    // GDN Ref not found in system
                                    if (gdnRefActual == null || gdnRefActual.equals("")) {
                                        problem = "GDN Ref not found in system";
                                        // GDN Ref uploaded doesn't match with system
                                    } else {
                                        problem = "GDN Ref on system " + gdnRefActual;
                                    }

                                } else {
                                    _log.info("GDN Ref of an retur from before airway bill automation: " + dailyReportRPX.getRefNo());
                                }
                            } else {
                                if (rsAirwayBill.getInt("totalAirwayBill") == 0) {
                                    _log.info("GDN Ref not found in venice: " + dailyReportRPX.getRefNo());
                                    isDataOK = false;
                                    String gdnRefActual = "";

                                    try {
                                        gdnRefActual = awbConn.getGDNRef(wcsOrderItemId);
                                    } catch (Exception e) {
                                        _log.info("", e);
                                    }

                                    // GDN Ref not found in system
                                    if (gdnRefActual == null || gdnRefActual.equals("")) {
                                        problem = "GDN Ref not found in system";
                                        // GDN Ref uploaded doesn't match with system
                                    } else {
                                        problem = "GDN Ref on system " + gdnRefActual;
                                    }

                                } else {
                                    _log.info("GDN Ref of an order from before airway bill automation: " + dailyReportRPX.getRefNo());
                                }
                            }

                            rsAirwayBill.close();

                        }
                    }

                    if (isDataOK == false) {
                        dataToFilter.remove(dataToFilter.get(i));
                        gdnRefNotFoundList.put(dailyReportRPX.getRefNo(), problem);
                        _log.debug("data not ok, remove from list: " + dailyReportRPX.getRefNo());
                        --i;
                        --length;
                        continue;
                    } else {
                        _log.info("data is ok, keep in the list: " + dailyReportRPX.getRefNo());
                    }
                } catch (Exception e) {
                    _log.error("Problem during filtering Activity Report Data", e);
                }
            }

            // close prepared statement
            psItem.close();
            psItem = null;

            psAirwayBill.close();
            psAirwayBill = null;

        } catch (Exception e) {
            _log.error("Error on filterDataRPX ", e);
        }
    }

    private LogLogisticsProvider getLogLogisticsProvider(String provider, Connection conn) throws SQLException {

        String logisticsProviderSQL = "select * from log_logistics_provider where logistics_provider_code = '" + provider + "' ";
        PreparedStatement psLogisticsProvider = conn.prepareStatement(logisticsProviderSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = psLogisticsProvider.executeQuery();
        rs.next();

        LogLogisticsProvider logisticsProvider = new LogLogisticsProvider();
        logisticsProvider.setLogisticsProviderId(rs.getLong("logistics_provider_id"));
        logisticsProvider.setLogisticsProviderCode(rs.getString("logistics_provider_code"));


        rs.close();
        rs = null;

        psLogisticsProvider.close();
        psLogisticsProvider = null;

        return logisticsProvider;

    }

    private void processJNEFormat(LogFileUploadLog fileUploadLog) {
        _log.info("Processing JNE Format");

        EntityManager em = emf.createEntityManager();
        @SuppressWarnings("deprecation")
        Connection conn = (Connection) ((EntityManagerImpl) em).getSession().connection();

        /**
         * Get the template file name
         */
        String templateFile = "";

        try {

            templateFile = getLogisticsTemplateFile(VeniceConstants.VEN_LOGISTICS_PROVIDER_JNE);

        } catch (Exception e) {

            _log.error("Error getting template", e);

            fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_FAIL);
            updateFileUploadLog(fileUploadLog);

            return;
        }

        /**
         * Parsing excel file to object
         */
        ExcelToPojo x = null;
        try {
            x = new ExcelToPojo(DailyReportJNE.class, System.getenv("VENICE_HOME") + LogisticsConstants.TEMPLATE_FOLDER + templateFile, fileUploadLog.getFileUploadNameAndLoc(), 5, 0);
            x = x.getPojoToExcel(25, "", "");
        } catch (Exception e) {
            String errMsg = LogisticsConstants.EXCEPTION_TEXT_FILE_PARSE + e.getMessage() + ". Processing row number " + (x != null && x.getErrorRowNumber() != null ? x.getErrorRowNumber() : "1" + "\n");
            _log.error(errMsg, e);
            e.printStackTrace();

            fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_FAIL);
            updateFileUploadLog(fileUploadLog);

            return;
        }

        /* Excel data */
        ArrayList<PojoInterface> result = x.getPojoResult();
        /* Store missing GDN Ref */
        HashMap<String, String> gdnRefNotFoundList = new HashMap<String, String>();
        /* Store data error during processing */
        HashMap<String, String> failedRecord = new HashMap<String, String>();
        /* Store fail status update during processing */
        List<FailedStatusUpdate> failedStatusUpdateList = new ArrayList<FailedStatusUpdate>();

        Locator<Object> locator = null;

        try {

            if (result.isEmpty()) {
                throw new Exception(LogisticsConstants.EXCEPTION_TEXT_ZERO_ROWS_TO_PROCESS);
            } else {
                _log.debug("result size: " + result.size());
            }

            locator = new Locator<Object>();

            LogAirwayBillSessionEJBRemote airwayBillHome = (LogAirwayBillSessionEJBRemote) locator
                    .lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");

            LogAirwayBillReturSessionEJBRemote airwayBillReturHome = (LogAirwayBillReturSessionEJBRemote) locator
                    .lookup(LogAirwayBillReturSessionEJBRemote.class, "LogAirwayBillReturSessionEJBBean");

            LogActivityReportUploadSessionEJBRemote reportUploadHome = (LogActivityReportUploadSessionEJBRemote) locator
                    .lookup(LogActivityReportUploadSessionEJBRemote.class, "LogActivityReportUploadSessionEJBBean");

            LogLogisticsProvider logisticsProvider = null;
            Boolean reportuploadEntryCreated = false;
            LogActivityReportUpload logActivityReportUpload = null;

            /*
             * Jika dalam report terdapat gdn reference with no matching order item id / gdn reference, maka di remove dari list, 
             * gdn ref nya dicatat dan ditampilkan di pop up window setelah upload. sedangkan data lain yang match tetap di proses.
             */
            filterDataJNE(result, gdnRefNotFoundList, failedRecord, conn);

            _log.debug("result size after validation: " + result.size());

            errorRowNumber = 1;

            /* Order Item Prepared Statement */
            PreparedStatement psItem = null;
            /* Airway Bill Prepared Statement */
            PreparedStatement psAirwayBillByGDNRef = null;
            PreparedStatement psAirwayBillByItem = null;

            logisticsProvider = getLogLogisticsProvider("JNE", conn);

            for (PojoInterface element : result) {
                DailyReportJNE dailyReportJNE = (DailyReportJNE) element;

                boolean isDataBeforeAirwaybillAutomation = false;

                long existingOrderItemStatus;
                long newOrderItemStatus;
                String existingAirwayBillTransactionStatus = "";
                String newAirwayBillTransactionStatus = "";
                String airwayBillTransactionLevel = "";

                String relation = "";
                String recipient = "";
                Timestamp received = null;

                String airwayBillNoFromLogistic = dailyReportJNE.getcNoteNumber();
                String airwayBillNoFromEngine = "";

                /* For Data before airway bill automation*/
                String newAirwayBillStatus = "";
                String existingAirwayBillStatus = "";

                // Get the order item id from the GDN reference
                StringTokenizer st = new StringTokenizer(dailyReportJNE.getGdnRefNumber());
                String orderOrRMA = st.nextToken("-");
                String wcsOrderId = st.nextToken("-");
                String wcsOrderItemId = st.nextToken();
                Integer sequence = new Integer(st.nextToken());

                Boolean isRetur = false;
                if (orderOrRMA.equals("R")) {
                    isRetur = true;
                }

                _log.info("Record No " + dailyReportJNE.getNumber().toString().replace(".0", ""));
                _log.info("Tokenized string from report to get orderOrRMA:" + orderOrRMA + " OrderId:" + wcsOrderId + " OrderItemId:" + wcsOrderItemId + " Sequence:" + sequence);

                /**
                 * Get existing order/retur item
                 */
                if (isRetur) {
                    psItem = conn.prepareStatement(SELECT_RETUR_ITEM_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                } else {
                    psItem = conn.prepareStatement(SELECT_ORDER_ITEM_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                }

                psItem.setString(1, wcsOrderItemId);
                ResultSet rsItem = psItem.executeQuery();

                VenOrderItem venOrderItem = new VenOrderItem();
                VenReturItem venReturItem = new VenReturItem();

                if (isRetur) {
                    venReturItem = returItemResultSetMapper(rsItem);

                    /**
                     * get existing retur item status
                     */
                    existingOrderItemStatus = venReturItem.getVenReturStatus().getOrderStatusId();
                } else {
                    venOrderItem = orderItemResultSetMapper(rsItem);

                    /**
                     * get existing order item status
                     */
                    existingOrderItemStatus = venOrderItem.getVenOrderStatus().getOrderStatusId();
                }

                rsItem.close();
                rsItem = null;

                /**
                 * Get existing airwaybill from AWB Engine
                 */
                AirwayBillTransaction airwayBillTransaction = new AirwayBillTransaction();
                try {

                    airwayBillTransaction = awbConn.getAirwayBillTransaction(dailyReportJNE.getGdnRefNumber());

                    /**
                     * get existing awb transaction status & level
                     */
                    existingAirwayBillTransactionStatus = airwayBillTransaction.getStatus();
                    airwayBillTransactionLevel = airwayBillTransaction.getLevel();

                } catch (Exception e) {
                    airwayBillTransaction = new AirwayBillTransaction();
                    _log.info("", e);
                }

                /**
                 * Get existing airwaybill from DB
                 */
                LogAirwayBill existingLogAirwayBill = null;
                LogAirwayBillRetur existingLogAirwayBillRetur = null;

                // check if this is data before airwaybill automation

                if (isRetur) {
                    psAirwayBillByGDNRef = conn.prepareStatement(SELECT_AIRWAY_BILL_RETUR_SQL_BY_GDN_REF, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    psAirwayBillByItem = conn.prepareStatement(SELECT_AIRWAY_BILL_RETUR_SQL_BY_RETUR_ITEM_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                } else {
                    psAirwayBillByGDNRef = conn.prepareStatement(SELECT_AIRWAY_BILL_SQL_BY_GDN_REF, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    psAirwayBillByItem = conn.prepareStatement(SELECT_AIRWAY_BILL_SQL_BY_ORDER_ITEM_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                }

                psAirwayBillByGDNRef.setString(1, dailyReportJNE.getGdnRefNumber());
                ResultSet rsAirwayBillByGDNReff = psAirwayBillByGDNRef.executeQuery();

                rsAirwayBillByGDNReff.last();
                int totalAirwayBill = rsAirwayBillByGDNReff.getRow();
                rsAirwayBillByGDNReff.beforeFirst();

                if (isRetur) {
                    /**
                     * Order before airwaybill automation
                     */
                    if (totalAirwayBill > 0) {
                        existingLogAirwayBillRetur = airwayBillReturResultSetMapper(rsAirwayBillByGDNReff, logisticsProvider, null, venReturItem);
                        isDataBeforeAirwaybillAutomation = true;

                        existingAirwayBillStatus = existingLogAirwayBillRetur.getStatus();
                        /**
                         * Order airwaybill automation
                         */
                    } else {

                        psAirwayBillByItem.setLong(1, venReturItem.getReturItemId());
                        ResultSet rsAirwayBillByReturItem = psAirwayBillByItem.executeQuery();

                        existingLogAirwayBillRetur = airwayBillReturResultSetMapper(rsAirwayBillByReturItem, logisticsProvider, airwayBillTransaction, venReturItem);
                        isDataBeforeAirwaybillAutomation = false;

                        rsAirwayBillByReturItem.close();

                        airwayBillNoFromEngine = airwayBillTransaction.getAirwayBillNo();
                    }
                } else {
                    /**
                     * Order before airwaybill automation
                     */
                    if (totalAirwayBill > 0) {
                        existingLogAirwayBill = airwayBillResultSetMapper(rsAirwayBillByGDNReff, logisticsProvider, null, venOrderItem);
                        isDataBeforeAirwaybillAutomation = true;

                        existingAirwayBillStatus = existingLogAirwayBill.getStatus();
                        /**
                         * Order airwaybill automation
                         */
                    } else {

                        psAirwayBillByItem.setLong(1, venOrderItem.getOrderItemId());
                        ResultSet rsAirwayBillByOrderItem = psAirwayBillByItem.executeQuery();

                        existingLogAirwayBill = airwayBillResultSetMapper(rsAirwayBillByOrderItem, logisticsProvider, airwayBillTransaction, venOrderItem);
                        isDataBeforeAirwaybillAutomation = false;

                        rsAirwayBillByOrderItem.close();

                        airwayBillNoFromEngine = airwayBillTransaction.getAirwayBillNo();
                    }
                }

                rsAirwayBillByGDNReff.close();
                rsAirwayBillByGDNReff = null;

                /**
                 * Populate Upload data to airwaybill object
                 */
//				/* init airwaybill in DB */
//				if(existingLogAirwayBill.getAirwayBillId() == null){
//					existingLogAirwayBill = airwayBillHome.persistLogAirwayBill(existingLogAirwayBill);
//					
//				}					
				/* init new data with existing data */
                LogAirwayBillRetur newLogAirwayBillRetur = null;
                LogAirwayBill newLogAirwayBill = null;

                if (isRetur) {
                    newLogAirwayBillRetur = new LogAirwayBillRetur();
                    newLogAirwayBillRetur = existingLogAirwayBillRetur;
                } else {
                    newLogAirwayBill = new LogAirwayBill();
                    newLogAirwayBill = existingLogAirwayBill;
                }

                /*
                 * Status of the order item must be PU, PP, ES, CX, or D
                 */
                if (existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_PU
                        && existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_PP
                        && existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_ES
                        && existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_CX
                        //jika dalam record yg diupload ada yg statusnya sudah D, tetap bisa masuk tapi hanya update recipient, relation, received date
                        && existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_D) {

                    _log.debug("status not PU, PP, ES, CX or D, status order item: " + venOrderItem.getVenOrderStatus().getOrderStatusCode());
                    String errMsg = LogisticsConstants.EXCEPTION_TEXT_AWB_STATUS_NOT_PU_PP_ES_CX_D + wcsOrderItemId + ". AWB should not be in the report.";
                    _log.error(errMsg);

                } else {

                    _log.debug("status is PU, PP, ES, CX or D");

                    if (!reportuploadEntryCreated) {

                        _log.debug("report not created yet, creating new one");
                        reportuploadEntryCreated = true;
                        logActivityReportUpload = new LogActivityReportUpload();

                        logActivityReportUpload.setFileNameAndLocation(fileUploadLog.getFileUploadNameAndLoc());
                        logActivityReportUpload.setLogLogisticsProvider(logisticsProvider);

                        LogReportStatus logReportStatus = new LogReportStatus();
                        logReportStatus.setReportStatusId(new Long(0));
                        logActivityReportUpload.setLogReportStatus(logReportStatus);
                        LogReportTemplate logReportTemplate = new LogReportTemplate();
                        logReportTemplate.setTemplateId(new Long(6));

                        logActivityReportUpload.setLogReportTemplate(logReportTemplate);
                        logActivityReportUpload.setNumberOfRecords(new Long(result.size()));
                        logActivityReportUpload.setReportDesc(fileUploadLog.getFileUploadName() + ": Logistics activity report");
                        logActivityReportUpload.setReportTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));

                        // Persist the upload file record.
                        logActivityReportUpload = reportUploadHome.persistLogActivityReportUpload(logActivityReportUpload);
                        if (isRetur) {
                            if (existingLogAirwayBillRetur != null) {
                                existingLogAirwayBillRetur.setActivityFileNameAndLoc(logActivityReportUpload.getFileNameAndLocation());
                            }
                        } else {
                            if (existingLogAirwayBill != null) {
                                existingLogAirwayBill.setActivityFileNameAndLoc(logActivityReportUpload.getFileNameAndLocation());
                            }
                        }
                    }

                    //jika status DL dan received date & recipient tidak kosong, maka set status POD
                    //jika status DL dan received date / recipient kosong, maka set status MDE
                    //jika status BA maka set status DEX 14
                    //else set status IP

                    recipient = dailyReportJNE.getRecipient();

                    if (dailyReportJNE.getStatus().equalsIgnoreCase("DL")
                            && (!dailyReportJNE.getReceivedDate().isEmpty() && dailyReportJNE.getReceivedDate() != null)
                            && (!dailyReportJNE.getRecipient().isEmpty() && dailyReportJNE.getRecipient() != null)) { // this is D

                        _log.debug("Masuk DL");

                        /**
                         * set new order item and airwaybill status
                         */
                        newAirwayBillTransactionStatus = AirwayBillTransaction.STATUS_CLOSED;
                        newOrderItemStatus = VeniceConstants.VEN_ORDER_STATUS_D;
                        newAirwayBillStatus = VeniceConstants.LOG_LOGISTICS_STATUS_CODE_POD;

                        if ((dailyReportJNE.getReceiver().equalsIgnoreCase(dailyReportJNE.getRecipient())) || (dailyReportJNE.getReceiver().contains(dailyReportJNE.getRecipient()))) {

                            relation = "Yang bersangkutan";

                        } else if (dailyReportJNE.getRecipient().contains("(") && dailyReportJNE.getRecipient().contains(")")) {

                            relation = dailyReportJNE.getRecipient().substring(dailyReportJNE.getRecipient().indexOf("(") + 1, dailyReportJNE.getRecipient().indexOf(")"));
                            recipient = dailyReportJNE.getRecipient().substring(0, dailyReportJNE.getRecipient().indexOf("("));

                        } else {
                            relation = "Lain-lain";
                        }

                        if (dailyReportJNE.getReceivedDate() != null && !dailyReportJNE.getReceivedDate().isEmpty()) {
                            try {

                                received = new Timestamp(formatDate.parse(dailyReportJNE.getReceivedDate()).getTime());

                            } catch (Exception e) {
                                _log.error("Received Date Problem", e);
                                failedRecord.put("No : " + dailyReportJNE.getNumber().replace(".0", "") + ", GDN Ref : " + dailyReportJNE.getGdnRefNumber(), "Ops please fix Received Date : " + e.getMessage());
                                continue;
                            }
                        }

                        /**
                         * status D but one of received date and recipient is
                         * empy will be considered CX
                         */
                    } else if (dailyReportJNE.getStatus().equalsIgnoreCase("DL") && (dailyReportJNE.getReceivedDate().isEmpty() || dailyReportJNE.getRecipient().isEmpty())) {
                        _log.debug("Masuk DL tanpa received date atau recipient");

                        newAirwayBillTransactionStatus = AirwayBillTransaction.STATUS_SETTLED;
                        newOrderItemStatus = VeniceConstants.VEN_ORDER_STATUS_CX;
                        newAirwayBillStatus = VeniceConstants.LOG_LOGISTICS_STATUS_CODE_MDE;
                        /**
                         * CX
                         */
                    } else {
                        _log.debug("Masuk else IP");
                        newAirwayBillTransactionStatus = AirwayBillTransaction.STATUS_SETTLED;
                        newOrderItemStatus = VeniceConstants.VEN_ORDER_STATUS_CX;
                        newAirwayBillStatus = VeniceConstants.LOG_LOGISTICS_STATUS_CODE_MDE;
                    }

                    /**
                     * set awb status, relation, recipient & received for data
                     * before airwaybill automation
                     */
                    if (isRetur) {
                        if (isDataBeforeAirwaybillAutomation) {
                            newLogAirwayBillRetur.setStatus(newAirwayBillStatus);

                            try {
                                newLogAirwayBillRetur.setAirwayBillPickupDateTime(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportJNE.getcNoteDate())));
                                newLogAirwayBillRetur.setActualPickupDate(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportJNE.getcNoteDate())));
                            } catch (Exception e) {
                                _log.error("CNoteDate problem", e);
                                failedRecord.put("No : " + dailyReportJNE.getNumber().replace(".0", "") + ", GDN Ref : " + dailyReportJNE.getGdnRefNumber(), "Ops please fix CNoteDate : " + e.getMessage());
                                continue;
                            }

                            try {
                                newLogAirwayBillRetur.setNumPackages(new Integer(new Double(dailyReportJNE.getQty()).intValue()));
                            } catch (Exception e) {
                                _log.warn("Problem getting Quantity from GDN Ref " + dailyReportJNE.getGdnRefNumber() + ", value : " + dailyReportJNE.getQty(), e);
                            }
                            try {
                                newLogAirwayBillRetur.setPackageWeight(new BigDecimal(new Double(dailyReportJNE.getWeight()).doubleValue()));
                            } catch (Exception e) {
                                _log.warn("Problem getting Weight from GDN Ref " + dailyReportJNE.getGdnRefNumber() + ", value : " + dailyReportJNE.getWeight(), e);
                            }
                        }
                    } else {
                        if (isDataBeforeAirwaybillAutomation) {
                            newLogAirwayBill.setStatus(newAirwayBillStatus);

                            try {
                                newLogAirwayBill.setAirwayBillPickupDateTime(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportJNE.getcNoteDate())));
                                newLogAirwayBill.setActualPickupDate(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportJNE.getcNoteDate())));
                            } catch (Exception e) {
                                _log.error("CNoteDate problem", e);
                                failedRecord.put("No : " + dailyReportJNE.getNumber().replace(".0", "") + ", GDN Ref : " + dailyReportJNE.getGdnRefNumber(), "Ops please fix CNoteDate : " + e.getMessage());
                                continue;
                            }

                            try {
                                newLogAirwayBill.setNumPackages(new Integer(new Double(dailyReportJNE.getQty()).intValue()));
                            } catch (Exception e) {
                                _log.warn("Problem getting Quantity from GDN Ref " + dailyReportJNE.getGdnRefNumber() + ", value : " + dailyReportJNE.getQty(), e);
                            }
                            try {
                                newLogAirwayBill.setPackageWeight(new BigDecimal(new Double(dailyReportJNE.getWeight()).doubleValue()));
                            } catch (Exception e) {
                                _log.warn("Problem getting Weight from GDN Ref " + dailyReportJNE.getGdnRefNumber() + ", value : " + dailyReportJNE.getWeight(), e);
                            }
                        }
                    }

                    /**
                     * set new awb transaction status, relation, recipient &
                     * received
                     */
                    airwayBillTransaction.setStatus(newAirwayBillTransactionStatus);
                    airwayBillTransaction.setRelation(relation);
                    airwayBillTransaction.setRecipient(recipient);
                    airwayBillTransaction.setReceived(received);
                    airwayBillTransaction.setNamaPengirim(dailyReportJNE.getShipper());

                    try {
                        airwayBillTransaction.setTanggalActualPickup(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportJNE.getcNoteDate())));
                    } catch (Exception e) {
                        _log.error("CNoteDate problem", e);
                        failedRecord.put("No : " + dailyReportJNE.getNumber().replace(".0", "") + ", GDN Ref : " + dailyReportJNE.getGdnRefNumber(), "Ops please fix CNoteDate : " + e.getMessage());
                        continue;
                    }

                    try {
                        airwayBillTransaction.setQtyProduk(new Integer(new Double(dailyReportJNE.getQty()).intValue()));
                    } catch (Exception e) {
                        _log.warn("Problem getting Quantity from GDN Ref " + dailyReportJNE.getGdnRefNumber() + ", value : " + dailyReportJNE.getQty(), e);
                    }

                    try {
                        airwayBillTransaction.setWeight(new Double(dailyReportJNE.getWeight()));
                    } catch (Exception e) {
                        _log.warn("Problem getting Weight from GDN Ref " + dailyReportJNE.getGdnRefNumber() + ", value : " + dailyReportJNE.getWeight(), e);
                    }

                    if (isRetur) {
                        newLogAirwayBillRetur.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                        newLogAirwayBillRetur.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));
                        newLogAirwayBillRetur.setTrackingNumber(dailyReportJNE.getcNoteNumber());
                        newLogAirwayBillRetur.setDeliveryOrder(dailyReportJNE.getcNoteNumber());
                        newLogAirwayBillRetur.setConsignee(dailyReportJNE.getReceiver());
                        newLogAirwayBillRetur.setAddress(dailyReportJNE.getAddress());
                        newLogAirwayBillRetur.setDestCode(dailyReportJNE.getDestination());
                        newLogAirwayBillRetur.setDestination(dailyReportJNE.getCity());
                        newLogAirwayBillRetur.setService("JNE_REG");
                        newLogAirwayBillRetur.setContent(dailyReportJNE.getDescription());
                        newLogAirwayBillRetur.setRelation(relation);
                        newLogAirwayBillRetur.setRecipient(recipient);
                        newLogAirwayBillRetur.setReceived(received);

                        existingLogAirwayBillRetur.setReceived(newLogAirwayBillRetur.getReceived());
                        existingLogAirwayBillRetur.setRecipient(newLogAirwayBillRetur.getRecipient());
                        existingLogAirwayBillRetur.setRelation(newLogAirwayBillRetur.getRelation());

                        //if status already approved don't update awb number
                        if (newLogAirwayBillRetur.getLogApprovalStatus2().getApprovalStatusId() != VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_APPROVED) {
                            if (newLogAirwayBillRetur.getAirwayBillNumber() == null || newLogAirwayBillRetur.getAirwayBillNumber().isEmpty()) {
                                newLogAirwayBillRetur.setAirwayBillNumber(airwayBillNoFromLogistic);
                            } else {
                                if (!newLogAirwayBillRetur.getAirwayBillNumber().equalsIgnoreCase(airwayBillNoFromLogistic)) {
                                    newLogAirwayBillRetur.setAirwayBillNumber(airwayBillNoFromLogistic);
                                }
                            }
                        }

                        newLogAirwayBillRetur.setLogLogisticsProvider(logisticsProvider);
                        newLogAirwayBillRetur.setVenReturItem(venReturItem);
                    } else {
                        newLogAirwayBill.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                        newLogAirwayBill.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));
                        newLogAirwayBill.setTrackingNumber(dailyReportJNE.getcNoteNumber());
                        newLogAirwayBill.setDeliveryOrder(dailyReportJNE.getcNoteNumber());
                        newLogAirwayBill.setConsignee(dailyReportJNE.getReceiver());
                        newLogAirwayBill.setAddress(dailyReportJNE.getAddress());
                        newLogAirwayBill.setDestCode(dailyReportJNE.getDestination());
                        newLogAirwayBill.setDestination(dailyReportJNE.getCity());
                        newLogAirwayBill.setService("JNE_REG");
                        newLogAirwayBill.setContent(dailyReportJNE.getDescription());
                        newLogAirwayBill.setRelation(relation);
                        newLogAirwayBill.setRecipient(recipient);
                        newLogAirwayBill.setReceived(received);

                        existingLogAirwayBill.setReceived(newLogAirwayBill.getReceived());
                        existingLogAirwayBill.setRecipient(newLogAirwayBill.getRecipient());
                        existingLogAirwayBill.setRelation(newLogAirwayBill.getRelation());

                        //if status already approved don't update awb number
                        if (newLogAirwayBill.getLogApprovalStatus2().getApprovalStatusId() != VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_APPROVED) {
                            if (newLogAirwayBill.getAirwayBillNumber() == null || newLogAirwayBill.getAirwayBillNumber().isEmpty()) {
                                newLogAirwayBill.setAirwayBillNumber(airwayBillNoFromLogistic);
                            } else {
                                if (!newLogAirwayBill.getAirwayBillNumber().equalsIgnoreCase(airwayBillNoFromLogistic)) {
                                    newLogAirwayBill.setAirwayBillNumber(airwayBillNoFromLogistic);
                                }
                            }
                        }

                        newLogAirwayBill.setLogLogisticsProvider(logisticsProvider);
                        newLogAirwayBill.setVenOrderItem(venOrderItem);
                    }

                    /*
                     * 2011-05-23 In accordance with JIRA VENICE-16
                     * Pickup Data Late Only reconcile the AWB data if
                     * the existing data has come from MTA or has been
                     * reconciled against data from MTA. Use the new
                     * MtaData flag in the AWB to determine this.
                     */

                    /* Reconcile and merge the airwaybill
                     * If there is no data from MTA then merge the new AWB
                     *     reconciliation will happen via integration later
                     * If there is existing MTA data then merge the existing AWB
                     *     this is only to store the AWB number 
                     */
                    if (existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_D && !dailyReportJNE.getStatus().equalsIgnoreCase("RT")) {
                        /**
                         * Data from MTA exist
                         */
                        if (isRetur) {
                            if (existingLogAirwayBillRetur != null && existingLogAirwayBillRetur.getMtaData()) {
                                _log.debug("mta data true");

                                if ((existingLogAirwayBillRetur.getAirwayBillNumber() == null || existingLogAirwayBillRetur.getAirwayBillNumber().isEmpty()) && existingLogAirwayBillRetur.getLogApprovalStatus2().getApprovalStatusId() != VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_APPROVED) {
                                    existingLogAirwayBillRetur.setAirwayBillNumber(newLogAirwayBillRetur.getAirwayBillNumber());

                                    airwayBillTransaction.setAirwayBillNo(newLogAirwayBillRetur.getAirwayBillNumber());
                                }

                                //Set the activity result status and merge existing airway bill
                                existingLogAirwayBillRetur.setActivityResultStatus(newLogAirwayBillRetur.getActivityResultStatus());

                                //Set these few fields so that they show up in delivery status tracking
                                existingLogAirwayBillRetur.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                                existingLogAirwayBillRetur.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));

                                //Merge the existing airway bill
                                try {
                                    /* set new retur item status */
                                    existingLogAirwayBillRetur.getVenReturItem().getVenReturStatus().setOrderStatusId(newOrderItemStatus);

                                    airwayBillReturHome.mergeLogAirwayBillRetur(existingLogAirwayBillRetur);
                                } catch (Exception e) {
                                    _log.error("mergeLogAirwayBillRetur Problem", e);
                                    failedRecord.put("No : " + dailyReportJNE.getNumber().replace(".0", "") + ", GDN Ref : " + dailyReportJNE.getGdnRefNumber(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                    continue;
                                }

                                /**
                                 * No data from MTA
                                 */
                            } else {

                                _log.debug("no data from mta");

                                newLogAirwayBillRetur.setActivityResultStatus(LogisticsConstants.RESULT_STATUS_NO_DATA_FROM_MTA);
                                newLogAirwayBillRetur.setMtaData(false);
                                newLogAirwayBillRetur.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());

                                /**
                                 * Mark when airway bill no from engine &
                                 * logistics are different
                                 */
                                if (!isDataBeforeAirwaybillAutomation && (!airwayBillNoFromEngine.equals(airwayBillNoFromLogistic))) {
                                    if ((!(existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_SETTLED) && newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)))) {

                                        // represent data from MTA
                                        existingLogAirwayBillRetur.setAirwayBillNumber(airwayBillNoFromEngine);
                                        // represent data from Logistics
                                        newLogAirwayBillRetur.setAirwayBillNumber(airwayBillNoFromLogistic);
                                    }
                                }

                                try {
                                    /* set new retur item status */
                                    newLogAirwayBillRetur.getVenReturItem().getVenReturStatus().setOrderStatusId(newOrderItemStatus);

                                    airwayBillReturHome.mergeLogAirwayBillRetur(newLogAirwayBillRetur);
                                } catch (Exception e) {
                                    _log.error("mergeLogAirwayBillRetur Problem", e);
                                    failedRecord.put("No : " + dailyReportJNE.getNumber().replace(".0", "") + ", GDN Ref : " + dailyReportJNE.getGdnRefNumber(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                    continue;
                                }
                            }
                        } else {
                            if (existingLogAirwayBill != null && existingLogAirwayBill.getMtaData()) {
                                AWBReconciliation awbReconciliation = new AWBReconciliation();
                                /**
                                 * If update status from CX to D no need to
                                 * reconcile again
                                 */
                                _log.debug("mta data true");

                                /**
                                 * Recon order before airway bill automation
                                 */
                                if (isDataBeforeAirwaybillAutomation) {
                                    if (existingAirwayBillStatus == null || (!(existingAirwayBillStatus.equals(VeniceConstants.LOG_LOGISTICS_STATUS_CODE_MDE) && newAirwayBillStatus.equals(VeniceConstants.LOG_LOGISTICS_STATUS_CODE_POD)))) {
                                        _log.debug("reconcile awb");
                                        try {
                                            newLogAirwayBill = awbReconciliation.performActivityReconciliation(conn, logActivityReportUpload, newLogAirwayBill, existingLogAirwayBill, wcsOrderItemId);
                                        } catch (Exception e) {
                                            _log.error("AWBReconciliation Problem", e);
                                            failedRecord.put("No : " + dailyReportJNE.getNumber().replace(".0", "") + ", GDN Ref : " + dailyReportJNE.getGdnRefNumber(), "Ops tell IT about this Reconciliation Problem : " + e.getMessage());
                                            continue;
                                        }
                                    }

                                    /**
                                     * Recon order airway bill automation
                                     */
                                } else {
                                    if ((!(existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_SETTLED) && newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)))) {
                                        _log.debug("reconcile awb");
                                        try {
                                            newLogAirwayBill = awbReconciliation.performActivityReconciliation(conn, logActivityReportUpload, newLogAirwayBill, existingLogAirwayBill, wcsOrderItemId);
                                        } catch (Exception e) {
                                            _log.error("AWBReconciliation Problem", e);
                                            failedRecord.put("No : " + dailyReportJNE.getNumber().replace(".0", "") + ", GDN Ref : " + dailyReportJNE.getGdnRefNumber(), "Ops tell IT about this Reconciliation Problem : " + e.getMessage());
                                            continue;
                                        }
                                    }
                                }

                                if ((existingLogAirwayBill.getAirwayBillNumber() == null || existingLogAirwayBill.getAirwayBillNumber().isEmpty()) && existingLogAirwayBill.getLogApprovalStatus2().getApprovalStatusId() != VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_APPROVED) {
                                    existingLogAirwayBill.setAirwayBillNumber(newLogAirwayBill.getAirwayBillNumber());

                                    airwayBillTransaction.setAirwayBillNo(newLogAirwayBill.getAirwayBillNumber());
                                }

                                //Set the activity result status and merge existing airway bill
                                existingLogAirwayBill.setActivityResultStatus(newLogAirwayBill.getActivityResultStatus());

                                //Set these few fields so that they show up in delivery status tracking
                                existingLogAirwayBill.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                                existingLogAirwayBill.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));

                                //Merge the existing airway bill
                                try {
                                    /* set new order item status */
                                    existingLogAirwayBill.getVenOrderItem().getVenOrderStatus().setOrderStatusId(newOrderItemStatus);

                                    airwayBillHome.mergeLogAirwayBill(existingLogAirwayBill);
                                } catch (Exception e) {
                                    _log.error("mergeLogAirwayBill Problem", e);
                                    failedRecord.put("No : " + dailyReportJNE.getNumber().replace(".0", "") + ", GDN Ref : " + dailyReportJNE.getGdnRefNumber(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                    continue;
                                }

                                /**
                                 * No data from MTA
                                 */
                            } else {

                                _log.debug("no data from mta");

                                newLogAirwayBill.setActivityResultStatus(LogisticsConstants.RESULT_STATUS_NO_DATA_FROM_MTA);
                                newLogAirwayBill.setMtaData(false);
                                newLogAirwayBill.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());

                                /**
                                 * Mark when airway bill no from engine &
                                 * logistics are different
                                 */
                                if (!isDataBeforeAirwaybillAutomation && (!airwayBillNoFromEngine.equals(airwayBillNoFromLogistic))) {
                                    if ((!(existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_SETTLED) && newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)))) {
                                        _log.debug("reconcile awb");
                                        try {
                                            AWBReconciliation awbReconciliation = new AWBReconciliation();
                                            // represent data from MTA
                                            existingLogAirwayBill.setAirwayBillNumber(airwayBillNoFromEngine);
                                            // represent data from Logistics
                                            newLogAirwayBill.setAirwayBillNumber(airwayBillNoFromLogistic);

                                            newLogAirwayBill = awbReconciliation.performActivityReconciliation(conn, logActivityReportUpload, newLogAirwayBill, existingLogAirwayBill, wcsOrderItemId);
                                        } catch (Exception e) {
                                            _log.error("AWBReconciliation Problem", e);
                                            failedRecord.put("No : " + dailyReportJNE.getNumber().replace(".0", "") + ", GDN Ref : " + dailyReportJNE.getGdnRefNumber(), "Ops tell IT about this Reconciliation Problem : " + e.getMessage());
                                            continue;
                                        }
                                    }
                                }

                                try {

                                    /* set new order item status */
                                    newLogAirwayBill.getVenOrderItem().getVenOrderStatus().setOrderStatusId(newOrderItemStatus);

                                    airwayBillHome.mergeLogAirwayBill(newLogAirwayBill);
                                } catch (Exception e) {
                                    _log.error("mergeLogAirwayBill Problem", e);
                                    failedRecord.put("No : " + dailyReportJNE.getNumber().replace(".0", "") + ", GDN Ref : " + dailyReportJNE.getGdnRefNumber(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                    continue;
                                }

                            }
                        }

                        /**
                         * Update the status of the order items and trigger the
                         * publish
                         */
                        try {

                            if (isDataBeforeAirwaybillAutomation) {
                                if (isRetur) {
                                    updateReturItemBeforeAirwayBillAutomationStatus(venReturItem, existingOrderItemStatus, newOrderItemStatus);
                                } else {
                                    updateOrderItemBeforeAirwayBillAutomationStatus(venOrderItem, existingOrderItemStatus, newOrderItemStatus);
                                }
                            } else {
                                if (isRetur) {
                                    updateReturItemAndAirwayBillTransactionStatus(fileUploadLog.getUploadUsername(),
                                            venReturItem,
                                            airwayBillTransaction,
                                            existingOrderItemStatus,
                                            newOrderItemStatus,
                                            existingAirwayBillTransactionStatus,
                                            newAirwayBillTransactionStatus,
                                            airwayBillTransactionLevel,
                                            airwayBillNoFromEngine,
                                            airwayBillNoFromLogistic,
                                            failedStatusUpdateList);
                                } else {
                                    updateOrderItemAndAirwayBillTransactionStatus(fileUploadLog.getUploadUsername(),
                                            venOrderItem,
                                            airwayBillTransaction,
                                            existingOrderItemStatus,
                                            newOrderItemStatus,
                                            existingAirwayBillTransactionStatus,
                                            newAirwayBillTransactionStatus,
                                            airwayBillTransactionLevel,
                                            airwayBillNoFromEngine,
                                            airwayBillNoFromLogistic,
                                            failedStatusUpdateList);
                                }
                            }

                        } catch (Exception e) {
                            _log.error("update airwaybill status Problem", e);
                            failedRecord.put("No : " + dailyReportJNE.getNumber().replace(".0", "") + ", GDN Ref : " + dailyReportJNE.getGdnRefNumber(), "Ops tell IT about this Data Problem : " + e.getMessage());
                            continue;
                        }

                        _log.debug("done update order item status and trigger the publish");

                    } else if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_D && !dailyReportJNE.getStatus().equalsIgnoreCase("RT")) {

                        if (isRetur) {
                            _log.debug("retur item status already D, only update recipient, relation, received date");

                            existingLogAirwayBillRetur.setReceived(newLogAirwayBill.getReceived());
                            existingLogAirwayBillRetur.setRecipient(newLogAirwayBill.getRecipient());
                            existingLogAirwayBillRetur.setRelation(newLogAirwayBill.getRelation());
                            existingLogAirwayBillRetur.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                            existingLogAirwayBillRetur.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));
                            //Merge the existing airway bill
                            try {
                                /* set new order item status */
                                existingLogAirwayBillRetur.getVenReturItem().getVenReturStatus().setOrderStatusId(newOrderItemStatus);

                                airwayBillReturHome.mergeLogAirwayBillRetur(existingLogAirwayBillRetur);
                            } catch (Exception e) {
                                _log.error("mergeLogAirwayBillRetur Problem", e);
                                failedRecord.put("No : " + dailyReportJNE.getNumber().replace(".0", "") + ", GDN Ref : " + dailyReportJNE.getGdnRefNumber(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                continue;
                            }
                        } else {
                            _log.debug("order item status already D, only update recipient, relation, received date");

                            existingLogAirwayBill.setReceived(newLogAirwayBill.getReceived());
                            existingLogAirwayBill.setRecipient(newLogAirwayBill.getRecipient());
                            existingLogAirwayBill.setRelation(newLogAirwayBill.getRelation());
                            existingLogAirwayBill.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                            existingLogAirwayBill.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));
                            //Merge the existing airway bill
                            try {
                                /* set new order item status */
                                existingLogAirwayBill.getVenOrderItem().getVenOrderStatus().setOrderStatusId(newOrderItemStatus);

                                airwayBillHome.mergeLogAirwayBill(existingLogAirwayBill);
                            } catch (Exception e) {
                                _log.error("mergeLogAirwayBill Problem", e);
                                failedRecord.put("No : " + dailyReportJNE.getNumber().replace(".0", "") + ", GDN Ref : " + dailyReportJNE.getGdnRefNumber(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                continue;
                            }
                        }

                        /**
                         * Update the status of the order items and trigger the
                         * publish
                         */
                        try {

                            if (isDataBeforeAirwaybillAutomation) {
                                if (isRetur) {
                                    updateReturItemBeforeAirwayBillAutomationStatus(venReturItem, existingOrderItemStatus, newOrderItemStatus);
                                } else {
                                    updateOrderItemBeforeAirwayBillAutomationStatus(venOrderItem, existingOrderItemStatus, newOrderItemStatus);
                                }
                            } else {
                                if (isRetur) {
                                    updateReturItemAndAirwayBillTransactionStatus(fileUploadLog.getUploadUsername(),
                                            venReturItem,
                                            airwayBillTransaction,
                                            existingOrderItemStatus,
                                            newOrderItemStatus,
                                            existingAirwayBillTransactionStatus,
                                            newAirwayBillTransactionStatus,
                                            airwayBillTransactionLevel,
                                            airwayBillNoFromEngine,
                                            airwayBillNoFromLogistic,
                                            failedStatusUpdateList);
                                } else {
                                    updateOrderItemAndAirwayBillTransactionStatus(fileUploadLog.getUploadUsername(),
                                            venOrderItem,
                                            airwayBillTransaction,
                                            existingOrderItemStatus,
                                            newOrderItemStatus,
                                            existingAirwayBillTransactionStatus,
                                            newAirwayBillTransactionStatus,
                                            airwayBillTransactionLevel,
                                            airwayBillNoFromEngine,
                                            airwayBillNoFromLogistic,
                                            failedStatusUpdateList);
                                }
                            }
                        } catch (Exception e) {
                            _log.error("update airwaybill status Problem", e);
                            failedRecord.put("No : " + dailyReportJNE.getNumber().replace(".0", "") + ", GDN Ref : " + dailyReportJNE.getGdnRefNumber(), "Ops tell IT about this Data Problem : " + e.getMessage());
                            continue;
                        }

                        _log.debug("done update order item status and trigger the publish");

                    } else if (dailyReportJNE.getStatus().equalsIgnoreCase("RT")) {
                        if (isRetur) {
                            _log.debug("airwaybillRetur status is RT, only update file name and location");
                            existingLogAirwayBillRetur.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());

                            //Merge the existing airway bill retur
                            try {
                                /* set new retur item status */
                                existingLogAirwayBillRetur.getVenReturItem().getVenReturStatus().setOrderStatusId(newOrderItemStatus);

                                airwayBillReturHome.mergeLogAirwayBillRetur(existingLogAirwayBillRetur);
                            } catch (Exception e) {
                                _log.error("mergeLogAirwayBillRetur Problem", e);
                                failedRecord.put("No : " + dailyReportJNE.getNumber().replace(".0", "") + ", GDN Ref : " + dailyReportJNE.getGdnRefNumber(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                continue;
                            }
                        } else {
                            _log.debug("airwaybill status is RT, only update file name and location");
                            existingLogAirwayBill.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());

                            //Merge the existing airway bill
                            try {
                                /* set new order item status */
                                existingLogAirwayBill.getVenOrderItem().getVenOrderStatus().setOrderStatusId(newOrderItemStatus);

                                airwayBillHome.mergeLogAirwayBill(existingLogAirwayBill);
                            } catch (Exception e) {
                                _log.error("mergeLogAirwayBill Problem", e);
                                failedRecord.put("No : " + dailyReportJNE.getNumber().replace(".0", "") + ", GDN Ref : " + dailyReportJNE.getGdnRefNumber(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                continue;
                            }
                        }
                    }

                    //if activityResultStatus OK, then set to approved, this will trigger CX finance.
                    if (isRetur) {
                        if (newLogAirwayBillRetur.getActivityResultStatus().equals("OK")) {
                            _log.debug("activity result status OK, set approval status to approved to trigger CX finance");
                            LogApprovalStatus activityStatusApproved = new LogApprovalStatus();
                            activityStatusApproved.setApprovalStatusId(VeniceConstants.LOG_APPROVAL_STATUS_APPROVED);
                            existingLogAirwayBillRetur.setLogApprovalStatus2(activityStatusApproved);
                            existingLogAirwayBillRetur.setActivityApprovedByUserId("System");
                            airwayBillReturHome.mergeLogAirwayBillRetur(existingLogAirwayBillRetur);
                        }
                    } else {
                        if (newLogAirwayBill.getActivityResultStatus().equals("OK")) {
                            _log.debug("activity result status OK, set approval status to approved to trigger CX finance");
                            LogApprovalStatus activityStatusApproved = new LogApprovalStatus();
                            activityStatusApproved.setApprovalStatusId(VeniceConstants.LOG_APPROVAL_STATUS_APPROVED);
                            existingLogAirwayBill.setLogApprovalStatus2(activityStatusApproved);
                            existingLogAirwayBill.setActivityApprovedByUserId("System");
                            airwayBillHome.mergeLogAirwayBill(existingLogAirwayBill);
                        }
                    }
                }

                errorRowNumber++;

            }

            // close prepared statement
            if (psAirwayBillByGDNRef != null) {
                psAirwayBillByGDNRef.close();
                psAirwayBillByGDNRef = null;
            }

            if (psAirwayBillByItem != null) {
                psAirwayBillByItem.close();
                psAirwayBillByItem = null;
            }

            if (psItem != null) {
                psItem.close();
                psItem = null;
            }

            // close connection
            conn.close();

            if (!reportuploadEntryCreated) {
                String errMsg = "No airway bills were merged for report upload:" + fileUploadLog.getFileUploadName();
                _log.error(errMsg);
            }

            if (gdnRefNotFoundList.size() > 0 || failedRecord.size() > 0 || failedStatusUpdateList.size() > 0) {
                _log.info(gdnRefNotFoundList.size() + " row(s) was not uploaded");
                _log.info(failedRecord.size() + " row(s) has problem when being processed");
                _log.info(failedStatusUpdateList.size() + " row(s) has fail status update");

                FileOutputStream fos = null;

                try {
                    String outputFileName = "ActivityReportFailedToUpload-" + fileDateTimeFormat.format(new Date()) + ".xls";
                    fos = new FileOutputStream(FILE_PATH + outputFileName);

                    HSSFWorkbook wb = new HSSFWorkbook();
                    HSSFSheet sheet = wb.createSheet("ActivityReportFailedToUpload");

                    ActivityInvoiceFailedToUploadExport activityInvoiceFailedToUploadExport = new ActivityInvoiceFailedToUploadExport(wb);
                    wb = activityInvoiceFailedToUploadExport.ExportExcel(gdnRefNotFoundList, failedRecord, failedStatusUpdateList, sheet, ACTIVITY_OR_INVOICE);
                    wb.write(fos);
                    _log.debug("done export excel");

                    fileUploadLog.setFailedFileUploadName(outputFileName);
                    fileUploadLog.setFailedFileUploadNameAndLoc(FILE_PATH + outputFileName);
                    fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_SUCCESS);
                    updateFileUploadLog(fileUploadLog);

                } catch (Exception e) {
                    _log.error("Exception in Excel ActivityReportImportJNEServlet", e);
                    throw new ServletException("Exception in Excel ActivityReportImportJNEServlet", e);
                } finally {
                    if (fos != null) {
                        fos.close();
                    }
                }

            } else {
                String successMsg = LogisticsConstants.UPLOAD_SUCCESS_MESSAGE;
                _log.info(successMsg);

                fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_SUCCESS);
                updateFileUploadLog(fileUploadLog);

            }
        } catch (Exception e) {
            String errMsg = LogisticsConstants.EXCEPTION_TEXT_GENERAL + e.getClass().getName() + ":" + e.getMessage() + ". Processing row number:" + errorRowNumber;
            _log.error(errMsg, e);
            e.printStackTrace();

            fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_FAIL);
            updateFileUploadLog(fileUploadLog);

        } finally {
            try {
                if (locator != null) {
                    locator.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void processRPXFormat(LogFileUploadLog fileUploadLog) {
        _log.info("Processing RPX Format");

        EntityManager em = emf.createEntityManager();
        @SuppressWarnings("deprecation")
        Connection conn = (Connection) ((EntityManagerImpl) em).getSession().connection();

        /**
         * Get the template file name
         */
        String templateFile = "";

        try {

            templateFile = getLogisticsTemplateFile(VeniceConstants.VEN_LOGISTICS_PROVIDER_NCS);

        } catch (Exception e) {

            _log.error("Error getting template", e);

            fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_FAIL);
            updateFileUploadLog(fileUploadLog);

            return;
        }

        /**
         * Parsing excel file to object
         */
        ExcelToPojo x = null;
        try {
            x = new ExcelToPojo(DailyReportNCS.class, System.getenv("VENICE_HOME") + LogisticsConstants.TEMPLATE_FOLDER + templateFile, fileUploadLog.getFileUploadNameAndLoc(), 0, 0);
            x = x.getPojo();
        } catch (Exception e) {
            String errMsg = LogisticsConstants.EXCEPTION_TEXT_FILE_PARSE + e.getMessage() + ". Processing row number " + (x != null && x.getErrorRowNumber() != null ? x.getErrorRowNumber() : "1" + "\n");
            _log.error(errMsg, e);
            e.printStackTrace();

            fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_FAIL);
            updateFileUploadLog(fileUploadLog);

            return;
        }

        /* Excel data */
        ArrayList<PojoInterface> result = x.getPojoResult();
        /* Store missing GDN Ref */
        HashMap<String, String> gdnRefNotFoundList = new HashMap<String, String>();
        /* Store data error during processing */
        HashMap<String, String> failedRecord = new HashMap<String, String>();
        /* Store fail status update during processing */
        List<FailedStatusUpdate> failedStatusUpdateList = new ArrayList<FailedStatusUpdate>();

        Locator<Object> locator = null;

        try {
            if (result.isEmpty()) {
                throw new Exception(LogisticsConstants.EXCEPTION_TEXT_ZERO_ROWS_TO_PROCESS);
            } else {
                _log.debug("result size: " + result.size());
            }

            locator = new Locator<Object>();

            LogAirwayBillSessionEJBRemote airwayBillHome = (LogAirwayBillSessionEJBRemote) locator
                    .lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");

            LogAirwayBillReturSessionEJBRemote airwayBillReturHome = (LogAirwayBillReturSessionEJBRemote) locator
                    .lookup(LogAirwayBillReturSessionEJBRemote.class, "LogAirwayBillReturSessionEJBBean");

            LogActivityReportUploadSessionEJBRemote reportUploadHome = (LogActivityReportUploadSessionEJBRemote) locator
                    .lookup(LogActivityReportUploadSessionEJBRemote.class, "LogActivityReportUploadSessionEJBBean");

            LogLogisticsProvider logisticsProvider = null;
            Boolean reportuploadEntryCreated = false;
            LogActivityReportUpload logActivityReportUpload = null;

            /*
             * Jika dalam report terdapat gdn reference with no matching order item id / gdn reference, maka di remove dari list, 
             * gdn ref nya dicatat dan ditampilkan di pop up window setelah upload. sedangkan data lain yang match tetap di proses.
             */
            filterDataRPX(result, gdnRefNotFoundList, failedRecord, conn);

            _log.debug("result size after validation: " + result.size());

            errorRowNumber = 1;

            /* Order Item Prepared Statement */
            PreparedStatement psItem = null;
            /* Airway Bill Prepared Statement */
            PreparedStatement psAirwayBillByGDNRef = null;
            PreparedStatement psAirwayBillByItem = null;

            logisticsProvider = getLogLogisticsProvider("RPX", conn);

            for (PojoInterface element : result) {
                DailyReportNCS dailyReportRPX = (DailyReportNCS) element;

                boolean isDataBeforeAirwaybillAutomation = true;

                long existingOrderItemStatus;
                long newOrderItemStatus;
                String existingAirwayBillTransactionStatus = "";
                String newAirwayBillTransactionStatus = "";
                String airwayBillTransactionLevel = "";

                String relation = "";
                String recipient = "";
                Timestamp received = null;

                String airwayBillNoFromLogistic = dailyReportRPX.getAwb();
                String airwayBillNoFromEngine = "";

                /* For Data before airway bill automation*/
                String newAirwayBillStatus = "";
                String existingAirwayBillStatus = "";

                // Get the order item id from the GDN reference
                StringTokenizer st = new StringTokenizer(dailyReportRPX.getRefNo());
                String orderOrRMA = st.nextToken("-");
                String wcsOrderId = st.nextToken("-");
                String wcsOrderItemId = st.nextToken();
                Integer sequence = new Integer(st.nextToken());

                Boolean isRetur = false;
                if (orderOrRMA.equals("R")) {
                    isRetur = true;
                }

                _log.info("Record No " + dailyReportRPX.getTrNo());
                _log.info("Tokenized string from report to get orderOrRMA:" + orderOrRMA + " OrderId:" + wcsOrderId + " OrderItemId:" + wcsOrderItemId + " Sequence:" + sequence);

                /**
                 * Get existing order item
                 */
                if (isRetur) {
                    psItem = conn.prepareStatement(SELECT_RETUR_ITEM_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                } else {
                    psItem = conn.prepareStatement(SELECT_ORDER_ITEM_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                }
                psItem.setString(1, wcsOrderItemId);
                ResultSet rsItem = psItem.executeQuery();

                VenOrderItem venOrderItem = new VenOrderItem();
                VenReturItem venReturItem = new VenReturItem();

                if (isRetur) {
                    venReturItem = returItemResultSetMapper(rsItem);

                    /**
                     * get existing retur item status
                     */
                    existingOrderItemStatus = venReturItem.getVenReturStatus().getOrderStatusId();
                } else {
                    venOrderItem = orderItemResultSetMapper(rsItem);

                    /**
                     * get existing order item status
                     */
                    existingOrderItemStatus = venOrderItem.getVenOrderStatus().getOrderStatusId();
                }

                rsItem.close();
                rsItem = null;

                /**
                 * Get existing airwaybill from AWB Engine
                 */
                AirwayBillTransaction airwayBillTransaction = new AirwayBillTransaction();
                try {

                    airwayBillTransaction = awbConn.getAirwayBillTransaction(dailyReportRPX.getRefNo());

                    /**
                     * get existing awb transaction status & level
                     */
                    existingAirwayBillTransactionStatus = airwayBillTransaction.getStatus();
                    airwayBillTransactionLevel = airwayBillTransaction.getLevel();

                } catch (Exception e) {
                    airwayBillTransaction = new AirwayBillTransaction();
                    _log.info("", e);
                }

                /**
                 * Get existing airwaybill from DB
                 */
                LogAirwayBill existingLogAirwayBill = null;
                LogAirwayBillRetur existingLogAirwayBillRetur = null;

                if (isRetur) {
                    psAirwayBillByGDNRef = conn.prepareStatement(SELECT_AIRWAY_BILL_RETUR_SQL_BY_GDN_REF, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    psAirwayBillByItem = conn.prepareStatement(SELECT_AIRWAY_BILL_RETUR_SQL_BY_RETUR_ITEM_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                } else {
                    psAirwayBillByGDNRef = conn.prepareStatement(SELECT_AIRWAY_BILL_SQL_BY_GDN_REF, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    psAirwayBillByItem = conn.prepareStatement(SELECT_AIRWAY_BILL_SQL_BY_ORDER_ITEM_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                }

                psAirwayBillByGDNRef.setString(1, dailyReportRPX.getRefNo());
                ResultSet rsAirwayBillByGDNReff = psAirwayBillByGDNRef.executeQuery();

                rsAirwayBillByGDNReff.last();
                int totalAirwayBill = rsAirwayBillByGDNReff.getRow();
                rsAirwayBillByGDNReff.beforeFirst();

                if (isRetur) {
                    /**
                     * Order before airwaybill automation
                     */
                    if (totalAirwayBill > 0) {
                        existingLogAirwayBillRetur = airwayBillReturResultSetMapper(rsAirwayBillByGDNReff, logisticsProvider, null, venReturItem);
                        isDataBeforeAirwaybillAutomation = true;

                        existingAirwayBillStatus = existingLogAirwayBillRetur.getStatus();
                        /**
                         * Order airwaybill automation
                         */
                    } else {

                        psAirwayBillByItem.setLong(1, venReturItem.getReturItemId());
                        ResultSet rsAirwayBillByReturItem = psAirwayBillByItem.executeQuery();

                        existingLogAirwayBillRetur = airwayBillReturResultSetMapper(rsAirwayBillByReturItem, logisticsProvider, airwayBillTransaction, venReturItem);
                        isDataBeforeAirwaybillAutomation = false;

                        rsAirwayBillByReturItem.close();

                        airwayBillNoFromEngine = airwayBillTransaction.getAirwayBillNo();
                    }
                } else {
                    /**
                     * Order before airwaybill automation
                     */
                    if (totalAirwayBill > 0) {
                        existingLogAirwayBill = airwayBillResultSetMapper(rsAirwayBillByGDNReff, logisticsProvider, null, venOrderItem);
                        isDataBeforeAirwaybillAutomation = true;

                        existingAirwayBillStatus = existingLogAirwayBill.getStatus();
                        /**
                         * Order airwaybill automation
                         */
                    } else {

                        psAirwayBillByItem.setLong(1, venOrderItem.getOrderItemId());
                        ResultSet rsAirwayBillByOrderItem = psAirwayBillByItem.executeQuery();

                        existingLogAirwayBill = airwayBillResultSetMapper(rsAirwayBillByOrderItem, logisticsProvider, airwayBillTransaction, venOrderItem);
                        isDataBeforeAirwaybillAutomation = false;

                        rsAirwayBillByOrderItem.close();

                        airwayBillNoFromEngine = airwayBillTransaction.getAirwayBillNo();
                    }
                }

                rsAirwayBillByGDNReff.close();
                rsAirwayBillByGDNReff = null;

                /**
                 * Populate Upload data to airwaybill object
                 */
//				/* init airwaybill in DB */
//				if(existingLogAirwayBill.getAirwayBillId() == null){
//					existingLogAirwayBill = airwayBillHome.persistLogAirwayBill(existingLogAirwayBill);
//					
//				}					
				/* init new data with existing data */
                LogAirwayBillRetur newLogAirwayBillRetur = null;
                LogAirwayBill newLogAirwayBill = null;

                if (isRetur) {
                    newLogAirwayBillRetur = new LogAirwayBillRetur();
                    newLogAirwayBillRetur = existingLogAirwayBillRetur;
                } else {
                    newLogAirwayBill = new LogAirwayBill();
                    newLogAirwayBill = existingLogAirwayBill;
                }

                /*
                 * Status of the order item must be PU, PP, ES, CX, or D
                 */
                if (existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_PU
                        && existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_PP
                        && existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_ES
                        && existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_CX
                        //jika dalam record yg diupload ada yg statusnya sudah D, tetap bisa masuk tapi hanya update recipient, relation, received date
                        && existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_D) {

                    _log.debug("status not PU, PP, ES, CX or D, status order item: " + venOrderItem.getVenOrderStatus().getOrderStatusCode());
                    String errMsg = LogisticsConstants.EXCEPTION_TEXT_AWB_STATUS_NOT_PU_PP_ES_CX_D + wcsOrderItemId + ". AWB should not be in the report.";
                    _log.error(errMsg);

                } else {

                    _log.debug("status is PU, PP, ES, CX or D");

                    if (!reportuploadEntryCreated) {

                        _log.debug("report not created yet, creating new one");
                        reportuploadEntryCreated = true;
                        logActivityReportUpload = new LogActivityReportUpload();

                        logActivityReportUpload.setFileNameAndLocation(fileUploadLog.getFileUploadNameAndLoc());
                        logActivityReportUpload.setLogLogisticsProvider(logisticsProvider);

                        LogReportStatus logReportStatus = new LogReportStatus();
                        logReportStatus.setReportStatusId(new Long(0));
                        logActivityReportUpload.setLogReportStatus(logReportStatus);
                        LogReportTemplate logReportTemplate = new LogReportTemplate();
                        logReportTemplate.setTemplateId(new Long(4));

                        logActivityReportUpload.setLogReportTemplate(logReportTemplate);
                        logActivityReportUpload.setNumberOfRecords(new Long(result.size()));
                        logActivityReportUpload.setReportDesc(fileUploadLog.getFileUploadName() + ": Logistics activity report");
                        logActivityReportUpload.setReportTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));

                        // Persist the upload file record.
                        logActivityReportUpload = reportUploadHome.persistLogActivityReportUpload(logActivityReportUpload);
                        if (isRetur) {
                            if (existingLogAirwayBillRetur != null) {
                                existingLogAirwayBillRetur.setActivityFileNameAndLoc(logActivityReportUpload.getFileNameAndLocation());
                            }
                        } else {
                            if (existingLogAirwayBill != null) {
                                existingLogAirwayBill.setActivityFileNameAndLoc(logActivityReportUpload.getFileNameAndLocation());
                            }
                        }
                    }

                    //jika status DL dan received date & recipient tidak kosong, maka set status POD
                    //jika status DL dan received date / recipient kosong, maka set status MDE
                    //jika status BA maka set status DEX 14
                    //else set status IP

                    recipient = dailyReportRPX.getRecipient();

                    if (dailyReportRPX.getStatus().equalsIgnoreCase("OK")
                            && (!dailyReportRPX.getReceived().isEmpty() && dailyReportRPX.getReceived() != null)
                            && (!dailyReportRPX.getRecipient().isEmpty() && dailyReportRPX.getRecipient() != null)) { // this is D

                        _log.debug("Masuk OK");

                        /**
                         * set new order item and airwaybill status
                         */
                        newAirwayBillTransactionStatus = AirwayBillTransaction.STATUS_CLOSED;
                        newOrderItemStatus = VeniceConstants.VEN_ORDER_STATUS_D;
                        newAirwayBillStatus = VeniceConstants.LOG_LOGISTICS_STATUS_CODE_POD;

                        if ((dailyReportRPX.getConsignee().equalsIgnoreCase(dailyReportRPX.getRecipient())) || (dailyReportRPX.getConsignee().contains(dailyReportRPX.getRecipient()))) {

                            relation = "Yang bersangkutan";

                        } else if (!dailyReportRPX.getRelation().isEmpty()) {

                            relation = dailyReportRPX.getRelation();

                        } else {
                            relation = "Lain-lain";
                        }

                        if (dailyReportRPX.getReceived() != null && !dailyReportRPX.getReceived().isEmpty()) {
                            try {

                                received = new Timestamp(formatDate.parse(dailyReportRPX.getReceived()).getTime());

                            } catch (Exception e) {
                                _log.error("Received Date Problem", e);
                                failedRecord.put("Tr No : " + dailyReportRPX.getTrNo() + ", GDN Ref : " + dailyReportRPX.getRefNo(), "Ops please fix Received Date : " + e.getMessage());
                                continue;
                            }
                        }

                        /**
                         * status D but one of received date and recipient is
                         * empy will be considered CX
                         */
                    } else if (dailyReportRPX.getStatus().equalsIgnoreCase("OK") && (dailyReportRPX.getReceived().isEmpty() || dailyReportRPX.getRecipient().isEmpty())) {
                        _log.debug("Masuk OK tanpa received date atau recipient");

                        newAirwayBillTransactionStatus = AirwayBillTransaction.STATUS_SETTLED;
                        newOrderItemStatus = VeniceConstants.VEN_ORDER_STATUS_CX;
                        newAirwayBillStatus = VeniceConstants.LOG_LOGISTICS_STATUS_CODE_MDE;
                        /**
                         * CX
                         */
                    } else {
                        _log.debug("Masuk else IP");
                        newAirwayBillTransactionStatus = AirwayBillTransaction.STATUS_SETTLED;
                        newOrderItemStatus = VeniceConstants.VEN_ORDER_STATUS_CX;
                        newAirwayBillStatus = VeniceConstants.LOG_LOGISTICS_STATUS_CODE_MDE;
                    }


                    /**
                     * set awb status, relation, recipient & received for data
                     * before airwaybill automation
                     */
                    if (isRetur) {
                        if (isDataBeforeAirwaybillAutomation) {
                            newLogAirwayBillRetur.setStatus(newAirwayBillStatus);

                            try {
                                newLogAirwayBillRetur.setAirwayBillPickupDateTime(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportRPX.getPuDate())));
                                newLogAirwayBillRetur.setActualPickupDate(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportRPX.getPuDate())));
                            } catch (Exception e) {
                                _log.error("PUtime problem", e);
                                failedRecord.put("Tr No : " + dailyReportRPX.getTrNo() + ", GDN Ref : " + dailyReportRPX.getRefNo(), "Ops please fix PUtime Problem: " + e.getMessage());
                                continue;
                            }

                            try {
                                newLogAirwayBillRetur.setNumPackages(new Integer(new Double(dailyReportRPX.getPieces()).intValue()));
                            } catch (Exception e) {
                                _log.warn("Problem getting Pieces from GDN Ref " + dailyReportRPX.getRefNo() + ", value : " + dailyReportRPX.getPieces(), e);
                            }
                            try {
                                newLogAirwayBillRetur.setPackageWeight(new BigDecimal(new Double(dailyReportRPX.getWeight()).doubleValue()));
                            } catch (Exception e) {
                                _log.warn("Problem getting weight from GDN Ref " + dailyReportRPX.getRefNo() + ", value : " + dailyReportRPX.getWeight(), e);
                            }
                        }
                    } else {
                        if (isDataBeforeAirwaybillAutomation) {
                            newLogAirwayBill.setStatus(newAirwayBillStatus);

                            try {
                                newLogAirwayBill.setAirwayBillPickupDateTime(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportRPX.getPuDate())));
                                newLogAirwayBill.setActualPickupDate(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportRPX.getPuDate())));
                            } catch (Exception e) {
                                _log.error("PUtime problem", e);
                                failedRecord.put("Tr No : " + dailyReportRPX.getTrNo() + ", GDN Ref : " + dailyReportRPX.getRefNo(), "Ops please fix PUtime Problem: " + e.getMessage());
                                continue;
                            }

                            try {
                                newLogAirwayBill.setNumPackages(new Integer(new Double(dailyReportRPX.getPieces()).intValue()));
                            } catch (Exception e) {
                                _log.warn("Problem getting Pieces from GDN Ref " + dailyReportRPX.getRefNo() + ", value : " + dailyReportRPX.getPieces(), e);
                            }
                            try {
                                newLogAirwayBill.setPackageWeight(new BigDecimal(new Double(dailyReportRPX.getWeight()).doubleValue()));
                            } catch (Exception e) {
                                _log.warn("Problem getting weight from GDN Ref " + dailyReportRPX.getRefNo() + ", value : " + dailyReportRPX.getWeight(), e);
                            }
                        }
                    }
                    /**
                     * set new awb transaction status, relation, recipient &
                     * received
                     */
                    airwayBillTransaction.setStatus(newAirwayBillTransactionStatus);
                    airwayBillTransaction.setRelation(relation);
                    airwayBillTransaction.setRecipient(recipient);
                    airwayBillTransaction.setReceived(received);
                    airwayBillTransaction.setNamaPengirim(dailyReportRPX.getShipper());

                    try {
                        airwayBillTransaction.setTanggalActualPickup(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportRPX.getPuDate())));
                    } catch (Exception e) {
                        _log.error("PUtime problem", e);
                        failedRecord.put("Tr No : " + dailyReportRPX.getTrNo() + ", GDN Ref : " + dailyReportRPX.getRefNo(), "Ops please fix PUtime Problem: " + e.getMessage());
                        continue;
                    }

                    try {
                        airwayBillTransaction.setQtyProduk(new Integer(new Double(dailyReportRPX.getPieces()).intValue()));
                    } catch (Exception e) {
                        _log.warn("Problem getting Pieces from GDN Ref " + dailyReportRPX.getRefNo() + ", value : " + dailyReportRPX.getPieces(), e);
                    }

                    try {
                        airwayBillTransaction.setWeight(new Double(dailyReportRPX.getWeight()).doubleValue());
                    } catch (Exception e) {
                        _log.warn("Problem getting weight from GDN Ref " + dailyReportRPX.getRefNo() + ", value : " + dailyReportRPX.getWeight(), e);
                    }

                    if (isRetur) {
                        //if undelivered date and note not empty, set to log airway bill table regardless of status
                        if (dailyReportRPX.getUndelivered() != null && !dailyReportRPX.getUndelivered().isEmpty()) {
                            try {
                                newLogAirwayBillRetur.setUndelivered(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportRPX.getUndelivered())));
                            } catch (Exception e) {
                                _log.error("Undelivered Date Problem", e);
                                failedRecord.put("Tr No : " + dailyReportRPX.getTrNo() + ", GDN Ref : " + dailyReportRPX.getRefNo(), "Ops please fix Undelivered Date : " + e.getMessage());
                                continue;
                            }
                            newLogAirwayBillRetur.setNoteUndelivered(dailyReportRPX.getUndeliveredNote());
                        }

                        newLogAirwayBillRetur.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                        newLogAirwayBillRetur.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));
                        newLogAirwayBillRetur.setTrackingNumber(dailyReportRPX.getTrNo());
                        newLogAirwayBillRetur.setDeliveryOrder(dailyReportRPX.getDoNumber());
                        newLogAirwayBillRetur.setAddress(dailyReportRPX.getAddress());
                        newLogAirwayBillRetur.setConsignee(dailyReportRPX.getConsignee());
                        newLogAirwayBillRetur.setDestCode(dailyReportRPX.getDestCode());
                        newLogAirwayBillRetur.setDestination(dailyReportRPX.getDestination());
                        newLogAirwayBillRetur.setService(dailyReportRPX.getServices());
                        newLogAirwayBillRetur.setContent(dailyReportRPX.getContent());
                        newLogAirwayBillRetur.setRelation(relation);
                        newLogAirwayBillRetur.setRecipient(recipient);
                        newLogAirwayBillRetur.setReceived(received);

                        existingLogAirwayBillRetur.setReceived(newLogAirwayBillRetur.getReceived());
                        existingLogAirwayBillRetur.setRecipient(newLogAirwayBillRetur.getRecipient());
                        existingLogAirwayBillRetur.setRelation(newLogAirwayBillRetur.getRelation());


                        //if status already approved don't update awb number
                        if (existingLogAirwayBillRetur.getLogApprovalStatus2().getApprovalStatusId() != VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_APPROVED) {
                            if (existingLogAirwayBillRetur.getAirwayBillNumber() == null || existingLogAirwayBillRetur.getAirwayBillNumber().isEmpty()) {
                                newLogAirwayBillRetur.setAirwayBillNumber(dailyReportRPX.getAwb());
                            } else {
                                if (!existingLogAirwayBillRetur.getAirwayBillNumber().equalsIgnoreCase(dailyReportRPX.getAwb())) {
                                    newLogAirwayBillRetur.setAirwayBillNumber(dailyReportRPX.getAwb());
                                }
                            }
                        }

                        newLogAirwayBillRetur.setLogLogisticsProvider(logisticsProvider);
                        newLogAirwayBillRetur.setVenReturItem(venReturItem);
                    } else {
                        //if undelivered date and note not empty, set to log airway bill table regardless of status
                        if (dailyReportRPX.getUndelivered() != null && !dailyReportRPX.getUndelivered().isEmpty()) {
                            try {
                                newLogAirwayBill.setUndelivered(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportRPX.getUndelivered())));
                            } catch (Exception e) {
                                _log.error("Undelivered Date Problem", e);
                                failedRecord.put("Tr No : " + dailyReportRPX.getTrNo() + ", GDN Ref : " + dailyReportRPX.getRefNo(), "Ops please fix Undelivered Date : " + e.getMessage());
                                continue;
                            }
                            newLogAirwayBill.setNoteUndelivered(dailyReportRPX.getUndeliveredNote());
                        }

                        newLogAirwayBill.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                        newLogAirwayBill.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));
                        newLogAirwayBill.setTrackingNumber(dailyReportRPX.getTrNo());
                        newLogAirwayBill.setDeliveryOrder(dailyReportRPX.getDoNumber());
                        newLogAirwayBill.setAddress(dailyReportRPX.getAddress());
                        newLogAirwayBill.setConsignee(dailyReportRPX.getConsignee());
                        newLogAirwayBill.setDestCode(dailyReportRPX.getDestCode());
                        newLogAirwayBill.setDestination(dailyReportRPX.getDestination());
                        newLogAirwayBill.setService(dailyReportRPX.getServices());
                        newLogAirwayBill.setContent(dailyReportRPX.getContent());
                        newLogAirwayBill.setRelation(relation);
                        newLogAirwayBill.setRecipient(recipient);
                        newLogAirwayBill.setReceived(received);

                        existingLogAirwayBill.setReceived(newLogAirwayBill.getReceived());
                        existingLogAirwayBill.setRecipient(newLogAirwayBill.getRecipient());
                        existingLogAirwayBill.setRelation(newLogAirwayBill.getRelation());


                        //if status already approved don't update awb number
                        if (existingLogAirwayBill.getLogApprovalStatus2().getApprovalStatusId() != VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_APPROVED) {
                            if (existingLogAirwayBill.getAirwayBillNumber() == null || existingLogAirwayBill.getAirwayBillNumber().isEmpty()) {
                                newLogAirwayBill.setAirwayBillNumber(dailyReportRPX.getAwb());
                            } else {
                                if (!existingLogAirwayBill.getAirwayBillNumber().equalsIgnoreCase(dailyReportRPX.getAwb())) {
                                    newLogAirwayBill.setAirwayBillNumber(dailyReportRPX.getAwb());
                                }
                            }
                        }

                        newLogAirwayBill.setLogLogisticsProvider(logisticsProvider);
                        newLogAirwayBill.setVenOrderItem(venOrderItem);
                    }
                    /*
                     * 2011-05-23 In accordance with JIRA VENICE-16
                     * Pickup Data Late Only reconcile the AWB data if
                     * the existing data has come from MTA or has been
                     * reconciled against data from MTA. Use the new
                     * MtaData flag in the AWB to determine this.
                     */

                    /* Reconcile and merge the airwaybill
                     * If there is no data from MTA then merge the new AWB
                     *     reconciliation will happen via integration later
                     * If there is existing MTA data then merge the existing AWB
                     *     this is only to store the AWB number 
                     */
                    if (existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_D && !dailyReportRPX.getStatus().equalsIgnoreCase("RT")) {
                        /**
                         * Data from MTA exist
                         */
                        if (isRetur) {
                            if (existingLogAirwayBillRetur != null && existingLogAirwayBillRetur.getMtaData()) {
                                newLogAirwayBillRetur.setAirwayBillReturId(existingLogAirwayBillRetur.getAirwayBillReturId());
                                /**
                                 * If update status from CX to D no need to
                                 * reconcile again
                                 */
                                _log.debug("mta data true");

                                if ((existingLogAirwayBillRetur.getAirwayBillNumber() == null || existingLogAirwayBillRetur.getAirwayBillNumber().isEmpty()) && existingLogAirwayBillRetur.getLogApprovalStatus2().getApprovalStatusId() != VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_APPROVED) {
                                    existingLogAirwayBillRetur.setAirwayBillNumber(newLogAirwayBillRetur.getAirwayBillNumber());

                                    airwayBillTransaction.setAirwayBillNo(newLogAirwayBillRetur.getAirwayBillNumber());
                                }

                                //Set the activity result status and merge existing airway bill
                                existingLogAirwayBillRetur.setActivityResultStatus(newLogAirwayBillRetur.getActivityResultStatus());

                                //Set these few fields so that they show up in delivery status tracking
                                existingLogAirwayBillRetur.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                                existingLogAirwayBillRetur.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));

                                //Merge the existing airway bill
                                try {
                                    /* set new order item status */
                                    existingLogAirwayBillRetur.getVenReturItem().getVenReturStatus().setOrderStatusId(newOrderItemStatus);

                                    airwayBillReturHome.mergeLogAirwayBillRetur(existingLogAirwayBillRetur);
                                } catch (Exception e) {
                                    _log.error("mergeLogAirwayBillRetur Problem", e);
                                    failedRecord.put("Tr No : " + dailyReportRPX.getTrNo() + ", GDN Ref : " + dailyReportRPX.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                    continue;
                                }

                                /**
                                 * No data from MTA
                                 */
                            } else {

                                _log.debug("no data from mta");
                                newLogAirwayBillRetur.setActivityResultStatus(LogisticsConstants.RESULT_STATUS_NO_DATA_FROM_MTA);
                                newLogAirwayBillRetur.setMtaData(false);
                                newLogAirwayBillRetur.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());

                                /**
                                 * Mark when airway bill no from engine &
                                 * logistics are different
                                 */
                                if (!isDataBeforeAirwaybillAutomation && (!airwayBillNoFromEngine.equals(airwayBillNoFromLogistic))) {
                                    if ((!(existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_SETTLED) && newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)))) {
                                        // represent data from MTA
                                        existingLogAirwayBillRetur.setAirwayBillNumber(airwayBillNoFromEngine);
                                        // represent data from Logistics
                                        newLogAirwayBillRetur.setAirwayBillNumber(airwayBillNoFromLogistic);
                                    }
                                }

                                try {
                                    /* set new retur item status */
                                    newLogAirwayBillRetur.getVenReturItem().getVenReturStatus().setOrderStatusId(newOrderItemStatus);

                                    airwayBillReturHome.mergeLogAirwayBillRetur(newLogAirwayBillRetur);
                                } catch (Exception e) {
                                    _log.error("mergeLogAirwayBillRetur Problem", e);
                                    failedRecord.put("Tr No : " + dailyReportRPX.getTrNo() + ", GDN Ref : " + dailyReportRPX.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                    continue;
                                }
                            }
                        } else {
                            if (existingLogAirwayBill != null && existingLogAirwayBill.getMtaData()) {
                                AWBReconciliation awbReconciliation = new AWBReconciliation();
                                newLogAirwayBill.setAirwayBillId(existingLogAirwayBill.getAirwayBillId());
                                /**
                                 * If update status from CX to D no need to
                                 * reconcile again
                                 */
                                _log.debug("mta data true");


                                /**
                                 * Recon order before airway bill automation
                                 */
                                if (isDataBeforeAirwaybillAutomation) {
                                    if (existingAirwayBillStatus == null || (!(existingAirwayBillStatus.equals(VeniceConstants.LOG_LOGISTICS_STATUS_CODE_MDE) && newAirwayBillStatus.equals(VeniceConstants.LOG_LOGISTICS_STATUS_CODE_POD)))) {
                                        _log.debug("reconcile awb");
                                        try {
                                            newLogAirwayBill = awbReconciliation.performActivityReconciliation(conn, logActivityReportUpload, newLogAirwayBill, existingLogAirwayBill, wcsOrderItemId);
                                        } catch (Exception e) {
                                            _log.error("AWBReconciliation Problem", e);
                                            failedRecord.put("Tr No : " + dailyReportRPX.getTrNo() + ", GDN Ref : " + dailyReportRPX.getRefNo(), "Ops tell IT about this Reconciliation Problem : " + e.getMessage());
                                            continue;
                                        }
                                    }
                                } else {
                                    if ((!(existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_SETTLED) && newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)))) {
                                        _log.debug("reconcile awb");
                                        try {
                                            newLogAirwayBill = awbReconciliation.performActivityReconciliation(conn, logActivityReportUpload, newLogAirwayBill, existingLogAirwayBill, wcsOrderItemId);
                                        } catch (Exception e) {
                                            _log.error("AWBReconciliation Problem", e);
                                            failedRecord.put("Tr No : " + dailyReportRPX.getTrNo() + ", GDN Ref : " + dailyReportRPX.getRefNo(), "Ops tell IT about this Reconciliation Problem : " + e.getMessage());
                                            continue;
                                        }
                                    }
                                }

                                if ((existingLogAirwayBill.getAirwayBillNumber() == null || existingLogAirwayBill.getAirwayBillNumber().isEmpty()) && existingLogAirwayBill.getLogApprovalStatus2().getApprovalStatusId() != VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_APPROVED) {
                                    existingLogAirwayBill.setAirwayBillNumber(newLogAirwayBill.getAirwayBillNumber());

                                    airwayBillTransaction.setAirwayBillNo(newLogAirwayBill.getAirwayBillNumber());
                                }

                                //Set the activity result status and merge existing airway bill
                                existingLogAirwayBill.setActivityResultStatus(newLogAirwayBill.getActivityResultStatus());

                                //Set these few fields so that they show up in delivery status tracking
                                existingLogAirwayBill.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                                existingLogAirwayBill.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));

                                //Merge the existing airway bill
                                try {
                                    /* set new order item status */
                                    existingLogAirwayBill.getVenOrderItem().getVenOrderStatus().setOrderStatusId(newOrderItemStatus);

                                    airwayBillHome.mergeLogAirwayBill(existingLogAirwayBill);
                                } catch (Exception e) {
                                    _log.error("mergeLogAirwayBill Problem", e);
                                    failedRecord.put("Tr No : " + dailyReportRPX.getTrNo() + ", GDN Ref : " + dailyReportRPX.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                    continue;
                                }

                                /**
                                 * No data from MTA
                                 */
                            } else {

                                _log.debug("no data from mta");
                                newLogAirwayBill.setActivityResultStatus(LogisticsConstants.RESULT_STATUS_NO_DATA_FROM_MTA);
                                newLogAirwayBill.setMtaData(false);
                                newLogAirwayBill.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());

                                /**
                                 * Mark when airway bill no from engine &
                                 * logistics are different
                                 */
                                if (!isDataBeforeAirwaybillAutomation && (!airwayBillNoFromEngine.equals(airwayBillNoFromLogistic))) {
                                    if ((!(existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_SETTLED) && newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)))) {
                                        _log.debug("reconcile awb");
                                        try {
                                            AWBReconciliation awbReconciliation = new AWBReconciliation();
                                            // represent data from MTA
                                            existingLogAirwayBill.setAirwayBillNumber(airwayBillNoFromEngine);
                                            // represent data from Logistics
                                            newLogAirwayBill.setAirwayBillNumber(airwayBillNoFromLogistic);

                                            newLogAirwayBill = awbReconciliation.performActivityReconciliation(conn, logActivityReportUpload, newLogAirwayBill, existingLogAirwayBill, wcsOrderItemId);
                                        } catch (Exception e) {
                                            _log.error("AWBReconciliation Problem", e);
                                            failedRecord.put("Tr No : " + dailyReportRPX.getTrNo() + ", GDN Ref : " + dailyReportRPX.getRefNo(), "Ops tell IT about this Reconciliation Problem : " + e.getMessage());
                                            continue;
                                        }
                                    }
                                }

                                try {
                                    /* set new order item status */
                                    newLogAirwayBill.getVenOrderItem().getVenOrderStatus().setOrderStatusId(newOrderItemStatus);

                                    airwayBillHome.mergeLogAirwayBill(newLogAirwayBill);
                                } catch (Exception e) {
                                    _log.error("mergeLogAirwayBill Problem", e);
                                    failedRecord.put("Tr No : " + dailyReportRPX.getTrNo() + ", GDN Ref : " + dailyReportRPX.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                    continue;
                                }
                            }
                        }
                        /**
                         * Update the status of the order items and trigger the
                         * publish
                         */
                        try {

                            if (isDataBeforeAirwaybillAutomation) {
                                if (isRetur) {
                                    updateReturItemBeforeAirwayBillAutomationStatus(venReturItem, existingOrderItemStatus, newOrderItemStatus);
                                } else {
                                    updateOrderItemBeforeAirwayBillAutomationStatus(venOrderItem, existingOrderItemStatus, newOrderItemStatus);
                                }
                            } else {
                                if (isRetur) {
                                    updateReturItemAndAirwayBillTransactionStatus(fileUploadLog.getUploadUsername(),
                                            venReturItem,
                                            airwayBillTransaction,
                                            existingOrderItemStatus,
                                            newOrderItemStatus,
                                            existingAirwayBillTransactionStatus,
                                            newAirwayBillTransactionStatus,
                                            airwayBillTransactionLevel,
                                            airwayBillNoFromEngine,
                                            airwayBillNoFromLogistic,
                                            failedStatusUpdateList);
                                } else {
                                    updateOrderItemAndAirwayBillTransactionStatus(fileUploadLog.getUploadUsername(),
                                            venOrderItem,
                                            airwayBillTransaction,
                                            existingOrderItemStatus,
                                            newOrderItemStatus,
                                            existingAirwayBillTransactionStatus,
                                            newAirwayBillTransactionStatus,
                                            airwayBillTransactionLevel,
                                            airwayBillNoFromEngine,
                                            airwayBillNoFromLogistic,
                                            failedStatusUpdateList);
                                }
                            }
                        } catch (Exception e) {
                            _log.error("update airwaybill status Problem", e);
                            failedRecord.put("Tr No : " + dailyReportRPX.getTrNo() + ", GDN Ref : " + dailyReportRPX.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                            continue;
                        }

                        _log.debug("done update order item status and trigger the publish");

                    } else if (dailyReportRPX.getStatus().equalsIgnoreCase("RT")) {
                        if (isRetur) {
                            _log.debug("retur item status RT, only file name and location");
                            existingLogAirwayBillRetur.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());

                            //Merge the existing airway bill retur
                            try {
                                /* set new retur item status */
                                existingLogAirwayBillRetur.getVenReturItem().getVenReturStatus().setOrderStatusId(newOrderItemStatus);

                                airwayBillReturHome.mergeLogAirwayBillRetur(existingLogAirwayBillRetur);
                            } catch (Exception e) {
                                _log.error("mergeLogAirwayBillRetur Problem", e);
                                failedRecord.put("Tr No : " + dailyReportRPX.getTrNo() + ", GDN Ref : " + dailyReportRPX.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                continue;
                            }
                        } else {
                            _log.debug("order item status RT, only file name and location");
                            existingLogAirwayBill.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());

                            //Merge the existing airway bill
                            try {
                                /* set new order item status */
                                existingLogAirwayBill.getVenOrderItem().getVenOrderStatus().setOrderStatusId(newOrderItemStatus);

                                airwayBillHome.mergeLogAirwayBill(existingLogAirwayBill);
                            } catch (Exception e) {
                                _log.error("mergeLogAirwayBill Problem", e);
                                failedRecord.put("Tr No : " + dailyReportRPX.getTrNo() + ", GDN Ref : " + dailyReportRPX.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                continue;
                            }
                        }
                    } else {
                        if (isRetur) {
                            _log.debug("retur item status already D, only update recipient, relation, received date");

                            existingLogAirwayBillRetur.setReceived(newLogAirwayBill.getReceived());
                            existingLogAirwayBillRetur.setRecipient(newLogAirwayBill.getRecipient());
                            existingLogAirwayBillRetur.setRelation(newLogAirwayBill.getRelation());
                            existingLogAirwayBillRetur.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                            existingLogAirwayBillRetur.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));

                            //Merge the existing airway bill
                            try {
                                /* set new order item status */
                                existingLogAirwayBillRetur.getVenReturItem().getVenReturStatus().setOrderStatusId(newOrderItemStatus);

                                airwayBillReturHome.mergeLogAirwayBillRetur(existingLogAirwayBillRetur);
                            } catch (Exception e) {
                                _log.error("mergeLogAirwayBillRetur Problem", e);
                                failedRecord.put("Tr No : " + dailyReportRPX.getTrNo() + ", GDN Ref : " + dailyReportRPX.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                continue;
                            }
                        } else {
                            _log.debug("order item status already D, only update recipient, relation, received date");

                            existingLogAirwayBill.setReceived(newLogAirwayBill.getReceived());
                            existingLogAirwayBill.setRecipient(newLogAirwayBill.getRecipient());
                            existingLogAirwayBill.setRelation(newLogAirwayBill.getRelation());
                            existingLogAirwayBill.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                            existingLogAirwayBill.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));

                            //Merge the existing airway bill
                            try {
                                /* set new order item status */
                                existingLogAirwayBill.getVenOrderItem().getVenOrderStatus().setOrderStatusId(newOrderItemStatus);

                                airwayBillHome.mergeLogAirwayBill(existingLogAirwayBill);
                            } catch (Exception e) {
                                _log.error("mergeLogAirwayBill Problem", e);
                                failedRecord.put("Tr No : " + dailyReportRPX.getTrNo() + ", GDN Ref : " + dailyReportRPX.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                continue;
                            }
                        }

                        /**
                         * Update the status of the order items and trigger the
                         * publish
                         */
                        try {

                            if (isDataBeforeAirwaybillAutomation) {
                                if (isRetur) {
                                    updateReturItemBeforeAirwayBillAutomationStatus(venReturItem, existingOrderItemStatus, newOrderItemStatus);
                                } else {
                                    updateOrderItemBeforeAirwayBillAutomationStatus(venOrderItem, existingOrderItemStatus, newOrderItemStatus);
                                }
                            } else {
                                if (isRetur) {
                                    updateReturItemAndAirwayBillTransactionStatus(fileUploadLog.getUploadUsername(),
                                            venReturItem,
                                            airwayBillTransaction,
                                            existingOrderItemStatus,
                                            newOrderItemStatus,
                                            existingAirwayBillTransactionStatus,
                                            newAirwayBillTransactionStatus,
                                            airwayBillTransactionLevel,
                                            airwayBillNoFromEngine,
                                            airwayBillNoFromLogistic,
                                            failedStatusUpdateList);
                                } else {
                                    updateOrderItemAndAirwayBillTransactionStatus(fileUploadLog.getUploadUsername(),
                                            venOrderItem,
                                            airwayBillTransaction,
                                            existingOrderItemStatus,
                                            newOrderItemStatus,
                                            existingAirwayBillTransactionStatus,
                                            newAirwayBillTransactionStatus,
                                            airwayBillTransactionLevel,
                                            airwayBillNoFromEngine,
                                            airwayBillNoFromLogistic,
                                            failedStatusUpdateList);
                                }
                            }
                        } catch (Exception e) {
                            _log.error("update airwaybill status Problem", e);
                            failedRecord.put("Tr No : " + dailyReportRPX.getTrNo() + ", GDN Ref : " + dailyReportRPX.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                            continue;
                        }

                        _log.debug("done update order item status and trigger the publish");
                    }

                    //if activityResultStatus OK, then set to approved, this will trigger CX finance.
                    if (isRetur) {
                        if (newLogAirwayBillRetur.getActivityResultStatus().equals("OK")) {
                            _log.debug("activity result status OK, set approval status to approved to trigger CX finance");
                            LogApprovalStatus activityStatusApproved = new LogApprovalStatus();
                            activityStatusApproved.setApprovalStatusId(VeniceConstants.LOG_APPROVAL_STATUS_APPROVED);
                            existingLogAirwayBillRetur.setLogApprovalStatus2(activityStatusApproved);
                            existingLogAirwayBillRetur.setActivityApprovedByUserId("System");
                            airwayBillReturHome.mergeLogAirwayBillRetur(existingLogAirwayBillRetur);
                        }
                    } else {
                        if (newLogAirwayBill.getActivityResultStatus().equals("OK")) {
                            _log.debug("activity result status OK, set approval status to approved to trigger CX finance");
                            LogApprovalStatus activityStatusApproved = new LogApprovalStatus();
                            activityStatusApproved.setApprovalStatusId(VeniceConstants.LOG_APPROVAL_STATUS_APPROVED);
                            existingLogAirwayBill.setLogApprovalStatus2(activityStatusApproved);
                            existingLogAirwayBill.setActivityApprovedByUserId("System");
                            airwayBillHome.mergeLogAirwayBill(existingLogAirwayBill);
                        }
                    }
                }

                errorRowNumber++;
            }

            // close prepared statement
            if (psAirwayBillByGDNRef != null) {
                psAirwayBillByGDNRef.close();
                psAirwayBillByGDNRef = null;
            }

            if (psAirwayBillByItem != null) {
                psAirwayBillByItem.close();
                psAirwayBillByItem = null;
            }

            if (psItem != null) {
                psItem.close();
                psItem = null;
            }

            // close connection
            conn.close();

            if (!reportuploadEntryCreated) {
                String errMsg = "No airway bills were merged for report upload:" + fileUploadLog.getFileUploadName();
                _log.error(errMsg);
            }

            if (gdnRefNotFoundList.size() > 0 || failedRecord.size() > 0 || failedStatusUpdateList.size() > 0) {
                _log.info(gdnRefNotFoundList.size() + " row(s) was not uploaded, start export excel");
                _log.info(failedRecord.size() + " row(s) has problem when being processed");
                _log.info(failedStatusUpdateList.size() + " row(s) has fail status update");

                FileOutputStream fos = null;

                try {
                    String outputFileName = "ActivityReportFailedToUpload-" + fileDateTimeFormat.format(new Date()) + ".xls";
                    fos = new FileOutputStream(FILE_PATH + outputFileName);

                    HSSFWorkbook wb = new HSSFWorkbook();
                    HSSFSheet sheet = wb.createSheet("ActivityReportFailedToUpload");

                    ActivityInvoiceFailedToUploadExport activityInvoiceFailedToUploadExport = new ActivityInvoiceFailedToUploadExport(wb);
                    wb = activityInvoiceFailedToUploadExport.ExportExcel(gdnRefNotFoundList, failedRecord, failedStatusUpdateList, sheet, ACTIVITY_OR_INVOICE);
                    wb.write(fos);
                    _log.debug("done export excel");

                    fileUploadLog.setFailedFileUploadName(outputFileName);
                    fileUploadLog.setFailedFileUploadNameAndLoc(FILE_PATH + outputFileName);
                    fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_SUCCESS);
                    updateFileUploadLog(fileUploadLog);

                } catch (Exception e) {
                    _log.error("Exception in Excel ActivityReportImportRPXServlet", e);
                    throw new ServletException("Exception in Excel ActivityReportImportRPXServlet", e);
                } finally {
                    if (fos != null) {
                        fos.close();
                    }
                }

            } else {
                String successMsg = LogisticsConstants.UPLOAD_SUCCESS_MESSAGE;
                _log.info(successMsg);

                fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_SUCCESS);
                updateFileUploadLog(fileUploadLog);
            }

        } catch (Exception e) {
            String errMsg = LogisticsConstants.EXCEPTION_TEXT_GENERAL + e.getClass().getName() + ":" + e.getMessage() + ". Processing row number:" + errorRowNumber;
            _log.error(errMsg, e);
            e.printStackTrace();

            fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_FAIL);
            updateFileUploadLog(fileUploadLog);

        } finally {
            try {
                if (locator != null) {
                    locator.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void processMSGFormat(LogFileUploadLog fileUploadLog) {
        _log.info("Processing MSG Format");

        EntityManager em = emf.createEntityManager();
        @SuppressWarnings("deprecation")
        Connection conn = (Connection) ((EntityManagerImpl) em).getSession().connection();

        /**
         * Get the template file name
         */
        String templateFile = "";

        try {

            templateFile = getLogisticsTemplateFile(VeniceConstants.VEN_LOGISTICS_PROVIDER_MSG);

        } catch (Exception e) {

            _log.error("Error getting template", e);

            fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_FAIL);
            updateFileUploadLog(fileUploadLog);

            return;
        }

        /**
         * Parsing excel file to object
         */
        ExcelToPojo x = null;
        try {
            x = new ExcelToPojo(DailyReportMSG.class, System.getenv("VENICE_HOME") + LogisticsConstants.TEMPLATE_FOLDER + templateFile, fileUploadLog.getFileUploadNameAndLoc(), 0, 0);
            x = x.getPojo();
        } catch (Exception e) {
            String errMsg = LogisticsConstants.EXCEPTION_TEXT_FILE_PARSE + e.getMessage() + ". Processing row number " + (x != null && x.getErrorRowNumber() != null ? x.getErrorRowNumber() : "1" + "\n");
            _log.error(errMsg, e);
            e.printStackTrace();

            fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_FAIL);
            updateFileUploadLog(fileUploadLog);

            return;
        }

        /* Excel data */
        ArrayList<PojoInterface> result = x.getPojoResult();
        /* Store missing GDN Ref */
        HashMap<String, String> gdnRefNotFoundList = new HashMap<String, String>();
        /* Store data error during processing */
        HashMap<String, String> failedRecord = new HashMap<String, String>();
        /* Store fail status update during processing */
        List<FailedStatusUpdate> failedStatusUpdateList = new ArrayList<FailedStatusUpdate>();

        Locator<Object> locator = null;

        try {
            if (result.isEmpty()) {
                throw new Exception(LogisticsConstants.EXCEPTION_TEXT_ZERO_ROWS_TO_PROCESS);
            } else {
                _log.debug("result size: " + result.size());
            }

            locator = new Locator<Object>();

            LogAirwayBillSessionEJBLocal airwayBillHome = (LogAirwayBillSessionEJBLocal) locator
                    .lookupLocal(LogAirwayBillSessionEJBLocal.class, "LogAirwayBillSessionEJBBeanLocal");

            LogAirwayBillReturSessionEJBLocal airwayBillReturHome = (LogAirwayBillReturSessionEJBLocal) locator
                    .lookupLocal(LogAirwayBillReturSessionEJBLocal.class, "LogAirwayBillReturSessionEJBBeanLocal");

            LogActivityReportUploadSessionEJBLocal reportUploadHome = (LogActivityReportUploadSessionEJBLocal) locator
                    .lookupLocal(LogActivityReportUploadSessionEJBLocal.class, "LogActivityReportUploadSessionEJBBeanLocal");

            LogLogisticsProvider logisticsProvider = null;
            Boolean reportuploadEntryCreated = false;
            LogActivityReportUpload logActivityReportUpload = null;

            /*
             * Jika dalam report terdapat gdn reference with no matching order item id / gdn reference, maka di remove dari list, 
             * gdn ref nya dicatat dan ditampilkan di pop up window setelah upload. sedangkan data lain yang match tetap di proses.
             */
            filterDataMSG(result, gdnRefNotFoundList, failedRecord, conn);

            _log.debug("result size after validation: " + result.size());

            errorRowNumber = 1;

            /* Order Item Prepared Statement */
            PreparedStatement psItem = null;
            /* Airway Bill Prepared Statement */
            PreparedStatement psAirwayBillByGDNReff = null;
            PreparedStatement psAirwayBillByItem = null;

            logisticsProvider = getLogLogisticsProvider("MSG", conn);

            for (PojoInterface element : result) {
                DailyReportMSG dailyReportMSG = (DailyReportMSG) element;

                boolean isDataBeforeAirwaybillAutomation = true;

                long existingOrderItemStatus;
                long newOrderItemStatus;
                String existingAirwayBillTransactionStatus = "";
                String newAirwayBillTransactionStatus = "";
                String airwayBillTransactionLevel = "";

                String relation = "";
                String recipient = "";
                Timestamp received = null;

                String airwayBillNoFromLogistic = dailyReportMSG.getAwb();
                String airwayBillNoFromEngine = "";

                /* For Data before airway bill automation*/
                String newAirwayBillStatus = "";
                String existingAirwayBillStatus = "";

                // Get the order item id from the GDN reference
                StringTokenizer st = new StringTokenizer(dailyReportMSG.getRefNo());
                String orderOrRMA = st.nextToken("-");
                String wcsOrderId = st.nextToken("-");
                String wcsOrderItemId = st.nextToken();
                Integer sequence = new Integer(st.nextToken());

                Boolean isRetur = false;
                if (orderOrRMA.equals("R")) {
                    isRetur = true;
                }

                _log.info("Record No " + dailyReportMSG.getTrNo());
                _log.info("Tokenized string from report to get orderOrRMA:" + orderOrRMA + " OrderId:" + wcsOrderId + " OrderItemId:" + wcsOrderItemId + " Sequence:" + sequence);

                /**
                 * Get existing order item
                 */
                if (isRetur) {
                    psItem = conn.prepareStatement(SELECT_RETUR_ITEM_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                } else {
                    psItem = conn.prepareStatement(SELECT_ORDER_ITEM_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                }

                psItem.setString(1, wcsOrderItemId);
                ResultSet rsItem = psItem.executeQuery();

                VenOrderItem venOrderItem = new VenOrderItem();
                VenReturItem venReturItem = new VenReturItem();

                if (isRetur) {
                    venReturItem = returItemResultSetMapper(rsItem);

                    /**
                     * get existing retur item status
                     */
                    existingOrderItemStatus = venReturItem.getVenReturStatus().getOrderStatusId();
                } else {
                    venOrderItem = orderItemResultSetMapper(rsItem);

                    /**
                     * get existing order item status
                     */
                    existingOrderItemStatus = venOrderItem.getVenOrderStatus().getOrderStatusId();
                }

                rsItem.close();
                rsItem = null;

                /**
                 * Get existing airwaybill from AWB Engine
                 */
                AirwayBillTransaction airwayBillTransaction = new AirwayBillTransaction();
                try {

                    airwayBillTransaction = awbConn.getAirwayBillTransaction(dailyReportMSG.getRefNo());

                    /**
                     * get existing awb transaction status & level
                     */
                    existingAirwayBillTransactionStatus = airwayBillTransaction.getStatus();
                    airwayBillTransactionLevel = airwayBillTransaction.getLevel();

                } catch (Exception e) {
                    airwayBillTransaction = new AirwayBillTransaction();
                    _log.info("", e);
                }

                /**
                 * Get existing airwaybill from DB
                 */
                LogAirwayBill existingLogAirwayBill = null;
                LogAirwayBillRetur existingLogAirwayBillRetur = null;

                if (isRetur) {
                    psAirwayBillByGDNReff = conn.prepareStatement(SELECT_AIRWAY_BILL_RETUR_SQL_BY_GDN_REF, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    psAirwayBillByItem = conn.prepareStatement(SELECT_AIRWAY_BILL_RETUR_SQL_BY_RETUR_ITEM_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                } else {
                    psAirwayBillByGDNReff = conn.prepareStatement(SELECT_AIRWAY_BILL_SQL_BY_GDN_REF, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    psAirwayBillByItem = conn.prepareStatement(SELECT_AIRWAY_BILL_SQL_BY_ORDER_ITEM_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                }

                psAirwayBillByGDNReff.setString(1, dailyReportMSG.getRefNo());
                ResultSet rsAirwayBillByGDNReff = psAirwayBillByGDNReff.executeQuery();

                rsAirwayBillByGDNReff.last();
                int totalAirwayBill = rsAirwayBillByGDNReff.getRow();
                rsAirwayBillByGDNReff.beforeFirst();

                /**
                 * Order before airwaybill automation
                 */
                if (isRetur) {
                    /**
                     * Order before airwaybill automation
                     */
                    if (totalAirwayBill > 0) {
                        existingLogAirwayBillRetur = airwayBillReturResultSetMapper(rsAirwayBillByGDNReff, logisticsProvider, null, venReturItem);
                        isDataBeforeAirwaybillAutomation = true;

                        existingAirwayBillStatus = existingLogAirwayBillRetur.getStatus();
                        /**
                         * Order airwaybill automation
                         */
                    } else {

                        psAirwayBillByItem.setLong(1, venReturItem.getReturItemId());
                        ResultSet rsAirwayBillByReturItem = psAirwayBillByItem.executeQuery();

                        existingLogAirwayBillRetur = airwayBillReturResultSetMapper(rsAirwayBillByReturItem, logisticsProvider, airwayBillTransaction, venReturItem);
                        isDataBeforeAirwaybillAutomation = false;

                        rsAirwayBillByReturItem.close();

                        airwayBillNoFromEngine = airwayBillTransaction.getAirwayBillNo();
                    }
                } else {
                    if (totalAirwayBill > 0) {
                        existingLogAirwayBill = airwayBillResultSetMapper(rsAirwayBillByGDNReff, logisticsProvider, null, venOrderItem);
                        isDataBeforeAirwaybillAutomation = true;

                        existingAirwayBillStatus = existingLogAirwayBill.getStatus();
                        /**
                         * Order airwaybill automation
                         */
                    } else {

                        psAirwayBillByItem.setLong(1, venOrderItem.getOrderItemId());
                        ResultSet rsAirwayBillByOrderItem = psAirwayBillByItem.executeQuery();

                        existingLogAirwayBill = airwayBillResultSetMapper(rsAirwayBillByOrderItem, logisticsProvider, airwayBillTransaction, venOrderItem);
                        isDataBeforeAirwaybillAutomation = false;

                        rsAirwayBillByOrderItem.close();

                        airwayBillNoFromEngine = airwayBillTransaction.getAirwayBillNo();
                    }
                }
                rsAirwayBillByGDNReff.close();
                rsAirwayBillByGDNReff = null;

                /**
                 * Populate Upload data to airwaybill object
                 */
//				/* init airwaybill in DB */
//				if(existingLogAirwayBill.getAirwayBillId() == null){
//					existingLogAirwayBill = airwayBillHome.persistLogAirwayBill(existingLogAirwayBill);
//					
//				}					
				/* init new data with existing data */
                LogAirwayBill newLogAirwayBill = null;
                LogAirwayBillRetur newLogAirwayBillRetur = null;

                if (isRetur) {
                    newLogAirwayBillRetur = new LogAirwayBillRetur();
                    newLogAirwayBillRetur = existingLogAirwayBillRetur;
                } else {
                    newLogAirwayBill = new LogAirwayBill();
                    newLogAirwayBill = existingLogAirwayBill;
                }

                /*
                 * Status of the order item must be PU, PP, ES, CX, or D
                 */
                if (existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_PU
                        && existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_PP
                        && existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_ES
                        && existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_CX
                        //jika dalam record yg diupload ada yg statusnya sudah D, tetap bisa masuk tapi hanya update recipient, relation, received date
                        && existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_D) {

                    _log.debug("status not PU, PP, ES, CX or D, status order item: " + venOrderItem.getVenOrderStatus().getOrderStatusCode());
                    String errMsg = LogisticsConstants.EXCEPTION_TEXT_AWB_STATUS_NOT_PU_PP_ES_CX_D + wcsOrderItemId + ". AWB should not be in the report.";
                    _log.error(errMsg);

                } else {

                    _log.debug("status is PU, PP, ES, CX or D");

                    if (!reportuploadEntryCreated) {

                        _log.debug("report not created yet, creating new one");
                        reportuploadEntryCreated = true;
                        logActivityReportUpload = new LogActivityReportUpload();

                        logActivityReportUpload.setFileNameAndLocation(fileUploadLog.getFileUploadNameAndLoc());
                        logActivityReportUpload.setLogLogisticsProvider(logisticsProvider);

                        LogReportStatus logReportStatus = new LogReportStatus();
                        logReportStatus.setReportStatusId(new Long(0));
                        logActivityReportUpload.setLogReportStatus(logReportStatus);
                        LogReportTemplate logReportTemplate = new LogReportTemplate();
                        logReportTemplate.setTemplateId(new Long(4));

                        logActivityReportUpload.setLogReportTemplate(logReportTemplate);
                        logActivityReportUpload.setNumberOfRecords(new Long(result.size()));
                        logActivityReportUpload.setReportDesc(fileUploadLog.getFileUploadName() + ": Logistics activity report");
                        logActivityReportUpload.setReportTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));

                        // Persist the upload file record.
                        logActivityReportUpload = reportUploadHome.persistLogActivityReportUpload(logActivityReportUpload);
                        if (isRetur) {
                            if (existingLogAirwayBillRetur != null) {
                                existingLogAirwayBillRetur.setActivityFileNameAndLoc(logActivityReportUpload.getFileNameAndLocation());
                            }
                        } else {
                            if (existingLogAirwayBill != null) {
                                existingLogAirwayBill.setActivityFileNameAndLoc(logActivityReportUpload.getFileNameAndLocation());
                            }
                        }
                    }

                    //jika status DL dan received date & recipient tidak kosong, maka set status POD
                    //jika status DL dan received date / recipient kosong, maka set status MDE
                    //jika status BA maka set status DEX 14
                    //else set status IP

                    recipient = dailyReportMSG.getRecipient();

                    if (dailyReportMSG.getStatus().equalsIgnoreCase("OK")
                            && (!dailyReportMSG.getReceived().isEmpty() && dailyReportMSG.getReceived() != null)
                            && (!dailyReportMSG.getRecipient().isEmpty() && dailyReportMSG.getRecipient() != null)) { // this is D

                        _log.debug("Masuk OK");

                        /**
                         * set new order item and airwaybill status
                         */
                        newAirwayBillTransactionStatus = AirwayBillTransaction.STATUS_CLOSED;
                        newOrderItemStatus = VeniceConstants.VEN_ORDER_STATUS_D;
                        newAirwayBillStatus = VeniceConstants.LOG_LOGISTICS_STATUS_CODE_POD;

                        if ((dailyReportMSG.getConsignee().equalsIgnoreCase(dailyReportMSG.getRecipient())) || (dailyReportMSG.getConsignee().contains(dailyReportMSG.getRecipient()))) {

                            relation = "Yang bersangkutan";

                        } else if (!dailyReportMSG.getRelation().isEmpty()) {

                            relation = dailyReportMSG.getRelation();

                        } else {
                            relation = "Lain-lain";
                        }

                        if (dailyReportMSG.getReceived() != null && !dailyReportMSG.getReceived().isEmpty()) {
                            try {

                                received = new Timestamp(formatDate.parse(dailyReportMSG.getReceived()).getTime());

                            } catch (Exception e) {
                                _log.error("Received Date Problem", e);
                                failedRecord.put("Tr No : " + dailyReportMSG.getTrNo() + ", GDN Ref : " + dailyReportMSG.getRefNo(), "Ops please fix Received Date : " + e.getMessage());
                                continue;
                            }
                        }

                        /**
                         * status D but one of received date and recipient is
                         * empy will be considered CX
                         */
                    } else if (dailyReportMSG.getStatus().equalsIgnoreCase("OK") && (dailyReportMSG.getReceived().isEmpty() || dailyReportMSG.getRecipient().isEmpty())) {
                        _log.debug("Masuk OK tanpa received date atau recipient");

                        newAirwayBillTransactionStatus = AirwayBillTransaction.STATUS_SETTLED;
                        newOrderItemStatus = VeniceConstants.VEN_ORDER_STATUS_CX;
                        newAirwayBillStatus = VeniceConstants.LOG_LOGISTICS_STATUS_CODE_MDE;
                        /**
                         * CX
                         */
                    } else {
                        _log.debug("Masuk else IP");
                        newAirwayBillTransactionStatus = AirwayBillTransaction.STATUS_SETTLED;
                        newOrderItemStatus = VeniceConstants.VEN_ORDER_STATUS_CX;
                        newAirwayBillStatus = VeniceConstants.LOG_LOGISTICS_STATUS_CODE_MDE;
                    }

                    /**
                     * set awb status, relation, recipient & received for data
                     * before airwaybill automation
                     */
                    if (isRetur) {
                        if (isDataBeforeAirwaybillAutomation) {
                            newLogAirwayBillRetur.setStatus(newAirwayBillStatus);

                            try {
                                newLogAirwayBillRetur.setAirwayBillPickupDateTime(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportMSG.getPuDate())));
                                newLogAirwayBillRetur.setActualPickupDate(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportMSG.getPuDate())));
                            } catch (Exception e) {
                                _log.error("PUtime problem", e);
                                failedRecord.put("Tr No : " + dailyReportMSG.getTrNo() + ", GDN Ref : " + dailyReportMSG.getRefNo(), "Ops please fix PUtime Problem: " + e.getMessage());
                                continue;
                            }

                            try {
                                newLogAirwayBillRetur.setNumPackages(new Integer(new Double(dailyReportMSG.getPieces()).intValue()));
                            } catch (Exception e) {
                                _log.warn("Problem getting Pieces from GDN Ref " + dailyReportMSG.getRefNo() + ", value : " + dailyReportMSG.getPieces(), e);
                            }
                            try {
                                newLogAirwayBillRetur.setPackageWeight(new BigDecimal(new Double(dailyReportMSG.getWeight()).doubleValue()));
                            } catch (Exception e) {
                                _log.warn("Problem getting weight from GDN Ref " + dailyReportMSG.getRefNo() + ", value : " + dailyReportMSG.getWeight(), e);
                            }
                        }
                    } else {
                        if (isDataBeforeAirwaybillAutomation) {
                            newLogAirwayBill.setStatus(newAirwayBillStatus);

                            try {
                                newLogAirwayBill.setAirwayBillPickupDateTime(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportMSG.getPuDate())));
                                newLogAirwayBill.setActualPickupDate(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportMSG.getPuDate())));
                            } catch (Exception e) {
                                _log.error("PUtime problem", e);
                                failedRecord.put("Tr No : " + dailyReportMSG.getTrNo() + ", GDN Ref : " + dailyReportMSG.getRefNo(), "Ops please fix PUtime Problem: " + e.getMessage());
                                continue;
                            }

                            try {
                                newLogAirwayBill.setNumPackages(new Integer(new Double(dailyReportMSG.getPieces()).intValue()));
                            } catch (Exception e) {
                                _log.warn("Problem getting Pieces from GDN Ref " + dailyReportMSG.getRefNo() + ", value : " + dailyReportMSG.getPieces(), e);
                            }
                            try {
                                newLogAirwayBill.setPackageWeight(new BigDecimal(new Double(dailyReportMSG.getWeight()).doubleValue()));
                            } catch (Exception e) {
                                _log.warn("Problem getting weight from GDN Ref " + dailyReportMSG.getRefNo() + ", value : " + dailyReportMSG.getWeight(), e);
                            }
                        }
                    }

                    /**
                     * set new awb transaction status, relation, recipient &
                     * received
                     */
                    airwayBillTransaction.setStatus(newAirwayBillTransactionStatus);
                    airwayBillTransaction.setRelation(relation);
                    airwayBillTransaction.setRecipient(recipient);
                    airwayBillTransaction.setReceived(received);
                    airwayBillTransaction.setNamaPengirim(dailyReportMSG.getShipper());

                    try {
                        airwayBillTransaction.setTanggalActualPickup(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportMSG.getPuDate())));
                    } catch (Exception e) {
                        _log.error("PUtime problem", e);
                        failedRecord.put("Tr No : " + dailyReportMSG.getTrNo() + ", GDN Ref : " + dailyReportMSG.getRefNo(), "Ops please fix PUtime Problem: " + e.getMessage());
                        continue;
                    }

                    try {
                        airwayBillTransaction.setQtyProduk(new Integer(new Double(dailyReportMSG.getPieces()).intValue()));
                    } catch (Exception e) {
                        _log.warn("Problem getting Pieces from GDN Ref " + dailyReportMSG.getRefNo() + ", value : " + dailyReportMSG.getPieces(), e);
                    }

                    try {
                        airwayBillTransaction.setWeight(new Double(dailyReportMSG.getWeight()).doubleValue());
                    } catch (Exception e) {
                        _log.warn("Problem getting weight from GDN Ref " + dailyReportMSG.getRefNo() + ", value : " + dailyReportMSG.getWeight(), e);
                    }

                    //if undelivered date and note not empty, set to log airway bill table regardless of status
                    if (isRetur) {
                        if (dailyReportMSG.getUndelivered() != null && !dailyReportMSG.getUndelivered().isEmpty()) {
                            try {
                                newLogAirwayBillRetur.setUndelivered(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportMSG.getUndelivered())));
                            } catch (Exception e) {
                                _log.error("Undelivered Date Problem", e);
                                failedRecord.put("Tr No : " + dailyReportMSG.getTrNo() + ", GDN Ref : " + dailyReportMSG.getRefNo(), "Ops please fix Undelivered Date : " + e.getMessage());
                                continue;
                            }
                            newLogAirwayBillRetur.setNoteUndelivered(dailyReportMSG.getUndeliveredNote());
                        }
                    } else {
                        if (dailyReportMSG.getUndelivered() != null && !dailyReportMSG.getUndelivered().isEmpty()) {
                            try {
                                newLogAirwayBill.setUndelivered(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportMSG.getUndelivered())));
                            } catch (Exception e) {
                                _log.error("Undelivered Date Problem", e);
                                failedRecord.put("Tr No : " + dailyReportMSG.getTrNo() + ", GDN Ref : " + dailyReportMSG.getRefNo(), "Ops please fix Undelivered Date : " + e.getMessage());
                                continue;
                            }
                            newLogAirwayBill.setNoteUndelivered(dailyReportMSG.getUndeliveredNote());
                        }
                    }
                    if (isRetur) {
                        newLogAirwayBillRetur.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                        newLogAirwayBillRetur.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));
                        newLogAirwayBillRetur.setTrackingNumber(dailyReportMSG.getTrNo());
                        newLogAirwayBillRetur.setAddress(dailyReportMSG.getAddress());
                        newLogAirwayBillRetur.setDeliveryOrder(dailyReportMSG.getDoNumber());
                        newLogAirwayBillRetur.setConsignee(dailyReportMSG.getConsignee());
                        newLogAirwayBillRetur.setDestCode(dailyReportMSG.getDestCode());
                        newLogAirwayBillRetur.setDestination(dailyReportMSG.getDestination());
                        newLogAirwayBillRetur.setService(dailyReportMSG.getServices());
                        newLogAirwayBillRetur.setContent(dailyReportMSG.getContent());

                        newLogAirwayBillRetur.setRelation(relation);
                        newLogAirwayBillRetur.setRecipient(recipient);
                        newLogAirwayBillRetur.setReceived(received);

                        existingLogAirwayBillRetur.setReceived(newLogAirwayBill.getReceived());
                        existingLogAirwayBillRetur.setRecipient(newLogAirwayBill.getRecipient());
                        existingLogAirwayBillRetur.setRelation(newLogAirwayBill.getRelation());

                        //if status already approved don't update awb number
                        if (existingLogAirwayBillRetur.getLogApprovalStatus2().getApprovalStatusId() != VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_APPROVED) {
                            if (existingLogAirwayBillRetur.getAirwayBillNumber() == null || existingLogAirwayBillRetur.getAirwayBillNumber().isEmpty()) {
                                newLogAirwayBillRetur.setAirwayBillNumber(dailyReportMSG.getAwb());
                            } else {
                                if (!existingLogAirwayBillRetur.getAirwayBillNumber().equalsIgnoreCase(dailyReportMSG.getAwb())) {
                                    newLogAirwayBillRetur.setAirwayBillNumber(dailyReportMSG.getAwb());
                                }
                            }
                        }

                        newLogAirwayBillRetur.setLogLogisticsProvider(logisticsProvider);
                        newLogAirwayBillRetur.setVenReturItem(venReturItem);
                    } else {
                        newLogAirwayBill.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                        newLogAirwayBill.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));
                        newLogAirwayBill.setTrackingNumber(dailyReportMSG.getTrNo());
                        newLogAirwayBill.setAddress(dailyReportMSG.getAddress());
                        newLogAirwayBill.setDeliveryOrder(dailyReportMSG.getDoNumber());
                        newLogAirwayBill.setConsignee(dailyReportMSG.getConsignee());
                        newLogAirwayBill.setDestCode(dailyReportMSG.getDestCode());
                        newLogAirwayBill.setDestination(dailyReportMSG.getDestination());
                        newLogAirwayBill.setService(dailyReportMSG.getServices());
                        newLogAirwayBill.setContent(dailyReportMSG.getContent());

                        newLogAirwayBill.setRelation(relation);
                        newLogAirwayBill.setRecipient(recipient);
                        newLogAirwayBill.setReceived(received);

                        existingLogAirwayBill.setReceived(newLogAirwayBill.getReceived());
                        existingLogAirwayBill.setRecipient(newLogAirwayBill.getRecipient());
                        existingLogAirwayBill.setRelation(newLogAirwayBill.getRelation());

                        //if status already approved don't update awb number
                        if (existingLogAirwayBill.getLogApprovalStatus2().getApprovalStatusId() != VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_APPROVED) {
                            if (existingLogAirwayBill.getAirwayBillNumber() == null || existingLogAirwayBill.getAirwayBillNumber().isEmpty()) {
                                newLogAirwayBill.setAirwayBillNumber(dailyReportMSG.getAwb());
                            } else {
                                if (!existingLogAirwayBill.getAirwayBillNumber().equalsIgnoreCase(dailyReportMSG.getAwb())) {
                                    newLogAirwayBill.setAirwayBillNumber(dailyReportMSG.getAwb());
                                }
                            }
                        }

                        newLogAirwayBill.setLogLogisticsProvider(logisticsProvider);
                        newLogAirwayBill.setVenOrderItem(venOrderItem);
                    }

                    /*
                     * 2011-05-23 In accordance with JIRA VENICE-16
                     * Pickup Data Late Only reconcile the AWB data if
                     * the existing data has come from MTA or has been
                     * reconciled against data from MTA. Use the new
                     * MtaData flag in the AWB to determine this.
                     */

                    /* Reconcile and merge the airwaybill
                     * If there is no data from MTA then merge the new AWB
                     *     reconciliation will happen via integration later
                     * If there is existing MTA data then merge the existing AWB
                     *     this is only to store the AWB number 
                     */
                    if (existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_D && !dailyReportMSG.getStatus().equalsIgnoreCase("RT")) {
                        /**
                         * Data from MTA exist
                         */
                        if (isRetur) {
                            if (existingLogAirwayBillRetur != null && existingLogAirwayBillRetur.getMtaData()) {
                                newLogAirwayBillRetur.setAirwayBillReturId(existingLogAirwayBillRetur.getAirwayBillReturId());
                                /**
                                 * If update status from CX to D no need to
                                 * reconcile again
                                 */
                                _log.debug("mta data true");

                                if ((existingLogAirwayBillRetur.getAirwayBillNumber() == null || existingLogAirwayBillRetur.getAirwayBillNumber().isEmpty()) && existingLogAirwayBillRetur.getLogApprovalStatus2().getApprovalStatusId() != VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_APPROVED) {
                                    existingLogAirwayBillRetur.setAirwayBillNumber(newLogAirwayBillRetur.getAirwayBillNumber());

                                    airwayBillTransaction.setAirwayBillNo(newLogAirwayBillRetur.getAirwayBillNumber());
                                }

                                //Set the activity result status and merge existing airway bill
                                existingLogAirwayBillRetur.setActivityResultStatus(newLogAirwayBillRetur.getActivityResultStatus());

                                //Set these few fields so that they show up in delivery status tracking
                                existingLogAirwayBillRetur.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                                existingLogAirwayBillRetur.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));

                                //Merge the existing airway bill
                                try {
                                    /* set new order item status */
                                    existingLogAirwayBillRetur.getVenReturItem().getVenReturStatus().setOrderStatusId(newOrderItemStatus);

                                    airwayBillReturHome.mergeLogAirwayBillRetur(existingLogAirwayBillRetur);
                                } catch (Exception e) {
                                    _log.error("mergeLogAirwayBill Problem", e);
                                    failedRecord.put("Tr No : " + dailyReportMSG.getTrNo() + ", GDN Ref : " + dailyReportMSG.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                    continue;
                                }

                                /**
                                 * No data from MTA
                                 */
                            } else {

                                _log.debug("no data from mta");
                                newLogAirwayBillRetur.setActivityResultStatus(LogisticsConstants.RESULT_STATUS_NO_DATA_FROM_MTA);
                                newLogAirwayBillRetur.setMtaData(false);
                                newLogAirwayBillRetur.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());

                                /**
                                 * Mark when airway bill no from engine &
                                 * logistics are different
                                 */
                                if (!isDataBeforeAirwaybillAutomation && (!airwayBillNoFromEngine.equals(airwayBillNoFromLogistic))) {
                                    if ((!(existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_SETTLED) && newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)))) {
                                        // represent data from MTA
                                        existingLogAirwayBillRetur.setAirwayBillNumber(airwayBillNoFromEngine);
                                        // represent data from Logistics
                                        newLogAirwayBillRetur.setAirwayBillNumber(airwayBillNoFromLogistic);
                                    }
                                }

                                try {
                                    /* set new retur item status */
                                    newLogAirwayBillRetur.getVenReturItem().getVenReturStatus().setOrderStatusId(newOrderItemStatus);

                                    airwayBillReturHome.mergeLogAirwayBillRetur(newLogAirwayBillRetur);
                                } catch (Exception e) {
                                    _log.error("mergeLogAirwayBillRetur Problem", e);
                                    failedRecord.put("Tr No : " + dailyReportMSG.getTrNo() + ", GDN Ref : " + dailyReportMSG.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                    continue;
                                }
                            }
                        } else {
                            if (existingLogAirwayBill != null && existingLogAirwayBill.getMtaData()) {
                                AWBReconciliation awbReconciliation = new AWBReconciliation();
                                newLogAirwayBill.setAirwayBillId(existingLogAirwayBill.getAirwayBillId());
                                /**
                                 * If update status from CX to D no need to
                                 * reconcile again
                                 */
                                _log.debug("mta data true");

                                /**
                                 * Recon order before airway bill automation
                                 */
                                if (isDataBeforeAirwaybillAutomation) {
                                    if (existingAirwayBillStatus == null || (!(existingAirwayBillStatus.equals(VeniceConstants.LOG_LOGISTICS_STATUS_CODE_MDE) && newAirwayBillStatus.equals(VeniceConstants.LOG_LOGISTICS_STATUS_CODE_POD)))) {
                                        _log.debug("reconcile awb");
                                        try {
                                            newLogAirwayBill = awbReconciliation.performActivityReconciliation(conn, logActivityReportUpload, newLogAirwayBill, existingLogAirwayBill, wcsOrderItemId);
                                        } catch (Exception e) {
                                            _log.error("AWBReconciliation Problem", e);
                                            failedRecord.put("Tr No : " + dailyReportMSG.getTrNo() + ", GDN Ref : " + dailyReportMSG.getRefNo(), "Ops tell IT about this Reconciliation Problem : " + e.getMessage());
                                            continue;
                                        }
                                    }
                                } else {
                                    if ((!(existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_SETTLED) && newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)))) {
                                        _log.debug("reconcile awb");
                                        try {
                                            newLogAirwayBill = awbReconciliation.performActivityReconciliation(conn, logActivityReportUpload, newLogAirwayBill, existingLogAirwayBill, wcsOrderItemId);
                                        } catch (Exception e) {
                                            _log.error("AWBReconciliation Problem", e);
                                            failedRecord.put("Tr No : " + dailyReportMSG.getTrNo() + ", GDN Ref : " + dailyReportMSG.getRefNo(), "Ops tell IT about this Reconciliation Problem : " + e.getMessage());
                                            continue;
                                        }
                                    }
                                }

                                if ((existingLogAirwayBill.getAirwayBillNumber() == null || existingLogAirwayBill.getAirwayBillNumber().isEmpty()) && existingLogAirwayBill.getLogApprovalStatus2().getApprovalStatusId() != VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_APPROVED) {
                                    existingLogAirwayBill.setAirwayBillNumber(newLogAirwayBill.getAirwayBillNumber());

                                    airwayBillTransaction.setAirwayBillNo(newLogAirwayBill.getAirwayBillNumber());
                                }

                                //Set the activity result status and merge existing airway bill
                                existingLogAirwayBill.setActivityResultStatus(newLogAirwayBill.getActivityResultStatus());

                                //Set these few fields so that they show up in delivery status tracking
                                existingLogAirwayBill.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                                existingLogAirwayBill.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));

                                //Merge the existing airway bill
                                try {
                                    /* set new order item status */
                                    existingLogAirwayBill.getVenOrderItem().getVenOrderStatus().setOrderStatusId(newOrderItemStatus);

                                    airwayBillHome.mergeLogAirwayBill(existingLogAirwayBill);
                                } catch (Exception e) {
                                    _log.error("mergeLogAirwayBill Problem", e);
                                    failedRecord.put("Tr No : " + dailyReportMSG.getTrNo() + ", GDN Ref : " + dailyReportMSG.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                    continue;
                                }

                                /**
                                 * No data from MTA
                                 */
                            } else {

                                _log.debug("no data from mta");
                                newLogAirwayBill.setActivityResultStatus(LogisticsConstants.RESULT_STATUS_NO_DATA_FROM_MTA);
                                newLogAirwayBill.setMtaData(false);
                                newLogAirwayBill.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());

                                /**
                                 * Mark when airway bill no from engine &
                                 * logistics are different
                                 */
                                if (!isDataBeforeAirwaybillAutomation && (!airwayBillNoFromEngine.equals(airwayBillNoFromLogistic))) {
                                    if ((!(existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_SETTLED) && newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)))) {
                                        _log.debug("reconcile awb");
                                        try {
                                            AWBReconciliation awbReconciliation = new AWBReconciliation();
                                            // represent data from MTA
                                            existingLogAirwayBill.setAirwayBillNumber(airwayBillNoFromEngine);
                                            // represent data from Logistics
                                            newLogAirwayBill.setAirwayBillNumber(airwayBillNoFromLogistic);

                                            newLogAirwayBill = awbReconciliation.performActivityReconciliation(conn, logActivityReportUpload, newLogAirwayBill, existingLogAirwayBill, wcsOrderItemId);
                                        } catch (Exception e) {
                                            _log.error("AWBReconciliation Problem", e);
                                            failedRecord.put("Tr No : " + dailyReportMSG.getTrNo() + ", GDN Ref : " + dailyReportMSG.getRefNo(), "Ops tell IT about this Reconciliation Problem : " + e.getMessage());
                                            continue;
                                        }
                                    }
                                }

                                try {
                                    /* set new order item status */
                                    newLogAirwayBill.getVenOrderItem().getVenOrderStatus().setOrderStatusId(newOrderItemStatus);

                                    airwayBillHome.mergeLogAirwayBill(newLogAirwayBill);
                                } catch (Exception e) {
                                    _log.error("mergeLogAirwayBill Problem", e);
                                    failedRecord.put("Tr No : " + dailyReportMSG.getTrNo() + ", GDN Ref : " + dailyReportMSG.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                    continue;
                                }

                            }
                        }

                        /**
                         * Update the status of the order items and trigger the
                         * publish
                         */
                        try {

                            if (isDataBeforeAirwaybillAutomation) {
                                if (isRetur) {
                                    updateReturItemBeforeAirwayBillAutomationStatus(venReturItem, existingOrderItemStatus, newOrderItemStatus);
                                } else {
                                    updateOrderItemBeforeAirwayBillAutomationStatus(venOrderItem, existingOrderItemStatus, newOrderItemStatus);
                                }
                            } else {
                                if (isRetur) {
                                    updateReturItemAndAirwayBillTransactionStatus(fileUploadLog.getUploadUsername(),
                                            venReturItem,
                                            airwayBillTransaction,
                                            existingOrderItemStatus,
                                            newOrderItemStatus,
                                            existingAirwayBillTransactionStatus,
                                            newAirwayBillTransactionStatus,
                                            airwayBillTransactionLevel,
                                            airwayBillNoFromEngine,
                                            airwayBillNoFromLogistic,
                                            failedStatusUpdateList);
                                } else {
                                    updateOrderItemAndAirwayBillTransactionStatus(fileUploadLog.getUploadUsername(),
                                            venOrderItem,
                                            airwayBillTransaction,
                                            existingOrderItemStatus,
                                            newOrderItemStatus,
                                            existingAirwayBillTransactionStatus,
                                            newAirwayBillTransactionStatus,
                                            airwayBillTransactionLevel,
                                            airwayBillNoFromEngine,
                                            airwayBillNoFromLogistic,
                                            failedStatusUpdateList);
                                }
                            }
                        } catch (Exception e) {
                            _log.error("update airwaybill status Problem", e);
                            failedRecord.put("Tr No : " + dailyReportMSG.getTrNo() + ", GDN Ref : " + dailyReportMSG.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                            continue;
                        }

                        _log.debug("done update order item status and trigger the publish");

                    } else if (dailyReportMSG.getStatus().equalsIgnoreCase("RT")) {
                        if (isRetur) {
                            _log.debug("retur item status RT, only file name and location");
                            existingLogAirwayBillRetur.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());

                            //Merge the existing airway bill
                            try {
                                /* set new retur item status */
                                existingLogAirwayBillRetur.getVenReturItem().getVenReturStatus().setOrderStatusId(newOrderItemStatus);

                                airwayBillReturHome.mergeLogAirwayBillRetur(existingLogAirwayBillRetur);
                            } catch (Exception e) {
                                _log.error("mergeLogAirwayBillRetur Problem", e);
                                failedRecord.put("Tr No : " + dailyReportMSG.getTrNo() + ", GDN Ref : " + dailyReportMSG.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                continue;
                            }
                        } else {
                            _log.debug("order item status RT, only file name and location");
                            existingLogAirwayBill.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());

                            //Merge the existing airway bill
                            try {
                                /* set new order item status */
                                existingLogAirwayBill.getVenOrderItem().getVenOrderStatus().setOrderStatusId(newOrderItemStatus);

                                airwayBillHome.mergeLogAirwayBill(existingLogAirwayBill);
                            } catch (Exception e) {
                                _log.error("mergeLogAirwayBill Problem", e);
                                failedRecord.put("Tr No : " + dailyReportMSG.getTrNo() + ", GDN Ref : " + dailyReportMSG.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                continue;
                            }
                        }
                    } else {
                        if (isRetur) {
                            _log.debug("retur item status already D, only update recipient, relation, received date");

                            existingLogAirwayBillRetur.setReceived(newLogAirwayBill.getReceived());
                            existingLogAirwayBillRetur.setRecipient(newLogAirwayBill.getRecipient());
                            existingLogAirwayBillRetur.setRelation(newLogAirwayBill.getRelation());
                            existingLogAirwayBillRetur.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                            existingLogAirwayBillRetur.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));

                            //Merge the existing airway bill
                            try {
                                /* set new order item status */
                                existingLogAirwayBillRetur.getVenReturItem().getVenReturStatus().setOrderStatusId(newOrderItemStatus);

                                airwayBillReturHome.mergeLogAirwayBillRetur(existingLogAirwayBillRetur);
                            } catch (Exception e) {
                                _log.error("mergeLogAirwayBillRetur Problem", e);
                                failedRecord.put("Tr No : " + dailyReportMSG.getTrNo() + ", GDN Ref : " + dailyReportMSG.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                continue;
                            }
                        } else {
                            _log.debug("order item status already D, only update recipient, relation, received date");

                            existingLogAirwayBill.setReceived(newLogAirwayBill.getReceived());
                            existingLogAirwayBill.setRecipient(newLogAirwayBill.getRecipient());
                            existingLogAirwayBill.setRelation(newLogAirwayBill.getRelation());
                            existingLogAirwayBill.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                            existingLogAirwayBill.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));

                            //Merge the existing airway bill
                            try {
                                /* set new order item status */
                                existingLogAirwayBill.getVenOrderItem().getVenOrderStatus().setOrderStatusId(newOrderItemStatus);

                                airwayBillHome.mergeLogAirwayBill(existingLogAirwayBill);
                            } catch (Exception e) {
                                _log.error("mergeLogAirwayBill Problem", e);
                                failedRecord.put("Tr No : " + dailyReportMSG.getTrNo() + ", GDN Ref : " + dailyReportMSG.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                continue;
                            }
                        }

                        /**
                         * Update the status of the order items and trigger the
                         * publish
                         */
                        try {
                            if (isDataBeforeAirwaybillAutomation) {
                                if (isRetur) {
                                    updateReturItemBeforeAirwayBillAutomationStatus(venReturItem, existingOrderItemStatus, newOrderItemStatus);
                                } else {
                                    updateOrderItemBeforeAirwayBillAutomationStatus(venOrderItem, existingOrderItemStatus, newOrderItemStatus);
                                }
                            } else {
                                if (isRetur) {
                                    updateReturItemAndAirwayBillTransactionStatus(fileUploadLog.getUploadUsername(),
                                            venReturItem,
                                            airwayBillTransaction,
                                            existingOrderItemStatus,
                                            newOrderItemStatus,
                                            existingAirwayBillTransactionStatus,
                                            newAirwayBillTransactionStatus,
                                            airwayBillTransactionLevel,
                                            airwayBillNoFromEngine,
                                            airwayBillNoFromLogistic,
                                            failedStatusUpdateList);
                                } else {
                                    updateOrderItemAndAirwayBillTransactionStatus(fileUploadLog.getUploadUsername(),
                                            venOrderItem,
                                            airwayBillTransaction,
                                            existingOrderItemStatus,
                                            newOrderItemStatus,
                                            existingAirwayBillTransactionStatus,
                                            newAirwayBillTransactionStatus,
                                            airwayBillTransactionLevel,
                                            airwayBillNoFromEngine,
                                            airwayBillNoFromLogistic,
                                            failedStatusUpdateList);
                                }
                            }
                        } catch (Exception e) {
                            _log.error("update airwaybill status Problem", e);
                            failedRecord.put("Tr No : " + dailyReportMSG.getTrNo() + ", GDN Ref : " + dailyReportMSG.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                            continue;
                        }
                        _log.debug("done update order item status and trigger the publish");

                    }

                    //if activityResultStatus OK, then set to approved, this will trigger CX finance.
                    if (isRetur) {
                        if (newLogAirwayBillRetur.getActivityResultStatus().equals("OK")) {
                            _log.debug("activity result status OK, set approval status to approved to trigger CX finance");
                            LogApprovalStatus activityStatusApproved = new LogApprovalStatus();
                            activityStatusApproved.setApprovalStatusId(VeniceConstants.LOG_APPROVAL_STATUS_APPROVED);
                            existingLogAirwayBillRetur.setLogApprovalStatus2(activityStatusApproved);
                            existingLogAirwayBillRetur.setActivityApprovedByUserId("System");
                            airwayBillReturHome.mergeLogAirwayBillRetur(existingLogAirwayBillRetur);
                        }
                    } else {
                        if (newLogAirwayBill.getActivityResultStatus().equals("OK")) {
                            _log.debug("activity result status OK, set approval status to approved to trigger CX finance");
                            LogApprovalStatus activityStatusApproved = new LogApprovalStatus();
                            activityStatusApproved.setApprovalStatusId(VeniceConstants.LOG_APPROVAL_STATUS_APPROVED);
                            existingLogAirwayBill.setLogApprovalStatus2(activityStatusApproved);
                            existingLogAirwayBill.setActivityApprovedByUserId("System");
                            airwayBillHome.mergeLogAirwayBill(existingLogAirwayBill);
                        }
                    }
                }

                errorRowNumber++;

            }

            // close prepared statement
            if (psAirwayBillByGDNReff != null) {
                psAirwayBillByGDNReff.close();
                psAirwayBillByGDNReff = null;
            }

            if (psAirwayBillByItem != null) {
                psAirwayBillByItem.close();
                psAirwayBillByItem = null;
            }

            if (psItem != null) {
                psItem.close();
                psItem = null;
            }

            // close connection
            conn.close();

            if (!reportuploadEntryCreated) {
                String errMsg = "No airway bills were merged for report upload:" + fileUploadLog.getFileUploadName();
                _log.error(errMsg);
            }

            if (gdnRefNotFoundList.size() > 0 || failedRecord.size() > 0 || failedStatusUpdateList.size() > 0) {
                _log.info(gdnRefNotFoundList.size() + " row(s) was not uploaded, start export excel");
                _log.info(failedRecord.size() + " row(s) has problem when being processed");
                _log.info(failedStatusUpdateList.size() + " row(s) has fail status update");

                FileOutputStream fos = null;

                try {
                    String outputFileName = "ActivityReportFailedToUpload-" + fileDateTimeFormat.format(new Date()) + ".xls";
                    fos = new FileOutputStream(FILE_PATH + outputFileName);

                    HSSFWorkbook wb = new HSSFWorkbook();
                    HSSFSheet sheet = wb.createSheet("ActivityReportFailedToUpload");

                    ActivityInvoiceFailedToUploadExport activityInvoiceFailedToUploadExport = new ActivityInvoiceFailedToUploadExport(wb);
                    wb = activityInvoiceFailedToUploadExport.ExportExcel(gdnRefNotFoundList, failedRecord, failedStatusUpdateList, sheet, ACTIVITY_OR_INVOICE);
                    wb.write(fos);
                    _log.debug("done export excel");

                    fileUploadLog.setFailedFileUploadName(outputFileName);
                    fileUploadLog.setFailedFileUploadNameAndLoc(FILE_PATH + outputFileName);
                    fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_SUCCESS);
                    updateFileUploadLog(fileUploadLog);

                } catch (Exception e) {
                    _log.error("Exception in Excel ActivityReportImportMSGServlet", e);
                    throw new ServletException("Exception in Excel ActivityReportImportMSGServlet", e);
                } finally {
                    if (fos != null) {
                        fos.close();
                    }
                }

            } else {
                String successMsg = LogisticsConstants.UPLOAD_SUCCESS_MESSAGE;
                _log.info(successMsg);

                fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_SUCCESS);
                updateFileUploadLog(fileUploadLog);

            }

        } catch (Exception e) {
            String errMsg = LogisticsConstants.EXCEPTION_TEXT_GENERAL + e.getClass().getName() + ":" + e.getMessage() + ". Processing row number:" + errorRowNumber;
            _log.error(errMsg, e);
            e.printStackTrace();

            fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_FAIL);
            updateFileUploadLog(fileUploadLog);

        } finally {
            try {
                if (locator != null) {
                    locator.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void processNCSFormat(LogFileUploadLog fileUploadLog) {
        _log.info("Processing NCS Format");

        EntityManager em = emf.createEntityManager();
        @SuppressWarnings("deprecation")
        Connection conn = (Connection) ((EntityManagerImpl) em).getSession().connection();

        /**
         * Get the template file name
         */
        String templateFile = "";

        try {

            templateFile = getLogisticsTemplateFile(VeniceConstants.VEN_LOGISTICS_PROVIDER_NCS);

        } catch (Exception e) {

            _log.error("Error getting template", e);

            fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_FAIL);
            updateFileUploadLog(fileUploadLog);

            return;
        }

        /**
         * Parsing excel file to object
         */
        ExcelToPojo x = null;
        try {
            x = new ExcelToPojo(DailyReportNCS.class, System.getenv("VENICE_HOME") + LogisticsConstants.TEMPLATE_FOLDER + templateFile, fileUploadLog.getFileUploadNameAndLoc(), 0, 0);
            x = x.getPojo();
        } catch (Exception e) {
            String errMsg = LogisticsConstants.EXCEPTION_TEXT_FILE_PARSE + e.getMessage() + ". Processing row number " + (x != null && x.getErrorRowNumber() != null ? x.getErrorRowNumber() : "1" + "\n");
            _log.error(errMsg, e);
            e.printStackTrace();

            fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_FAIL);
            updateFileUploadLog(fileUploadLog);

            return;
        }

        /* Excel data */
        ArrayList<PojoInterface> result = x.getPojoResult();
        /* Store missing GDN Ref */
        HashMap<String, String> gdnRefNotFoundList = new HashMap<String, String>();
        /* Store data error during processing */
        HashMap<String, String> failedRecord = new HashMap<String, String>();
        /* Store fail status update during processing */
        List<FailedStatusUpdate> failedStatusUpdateList = new ArrayList<FailedStatusUpdate>();

        Locator<Object> locator = null;

        try {
            if (result.isEmpty()) {
                throw new Exception(LogisticsConstants.EXCEPTION_TEXT_ZERO_ROWS_TO_PROCESS);
            } else {
                _log.debug("result size: " + result.size());
            }

            locator = new Locator<Object>();

            LogAirwayBillSessionEJBLocal airwayBillHome = (LogAirwayBillSessionEJBLocal) locator
                    .lookupLocal(LogAirwayBillSessionEJBLocal.class, "LogAirwayBillSessionEJBBeanLocal");

            LogAirwayBillReturSessionEJBLocal airwayBillReturHome = (LogAirwayBillReturSessionEJBLocal) locator
                    .lookupLocal(LogAirwayBillReturSessionEJBLocal.class, "LogAirwayBillReturSessionEJBBeanLocal");

            LogActivityReportUploadSessionEJBLocal reportUploadHome = (LogActivityReportUploadSessionEJBLocal) locator
                    .lookupLocal(LogActivityReportUploadSessionEJBLocal.class, "LogActivityReportUploadSessionEJBBeanLocal");

            LogLogisticsProvider logisticsProvider = null;
            Boolean reportuploadEntryCreated = false;
            LogActivityReportUpload logActivityReportUpload = null;

            /*
             * Jika dalam report terdapat gdn reference with no matching order item id / gdn reference, maka di remove dari list, 
             * gdn ref nya dicatat dan ditampilkan di pop up window setelah upload. sedangkan data lain yang match tetap di proses.
             */
            filterDataNCS(result, gdnRefNotFoundList, failedRecord, conn);

            _log.debug("result size after validation: " + result.size());

            errorRowNumber = 1;

            /* Order Item Prepared Statement */
            PreparedStatement psItem = null;
            /* Airway Bill Prepared Statement */
            PreparedStatement psAirwayBillByGDNReff = null;
            PreparedStatement psAirwayBillByItem = null;

            logisticsProvider = getLogLogisticsProvider("NCS", conn);

            for (PojoInterface element : result) {
                DailyReportNCS dailyReportNCS = (DailyReportNCS) element;

                boolean isDataBeforeAirwaybillAutomation = true;

                long existingOrderItemStatus;
                long newOrderItemStatus;
                String existingAirwayBillTransactionStatus = "";
                String newAirwayBillTransactionStatus = "";
                String airwayBillTransactionLevel = "";

                String relation = "";
                String recipient = "";
                Timestamp received = null;

                String airwayBillNoFromLogistic = dailyReportNCS.getAwb();
                String airwayBillNoFromEngine = "";

                /* For Data before airway bill automation*/
                String newAirwayBillStatus = "";
                String existingAirwayBillStatus = "";

                // Get the order item id from the GDN reference
                StringTokenizer st = new StringTokenizer(dailyReportNCS.getRefNo());
                String orderOrRMA = st.nextToken("-");
                String wcsOrderId = st.nextToken("-");
                String wcsOrderItemId = st.nextToken();
                Integer sequence = new Integer(st.nextToken());

                Boolean isRetur = false;
                if (orderOrRMA.equals("R")) {
                    isRetur = true;
                }

                _log.info("Record No " + dailyReportNCS.getTrNo());
                _log.info("Tokenized string from report to get orderOrRMA:" + orderOrRMA + " OrderId:" + wcsOrderId + " OrderItemId:" + wcsOrderItemId + " Sequence:" + sequence);

                /**
                 * Get existing order item
                 */
                if (isRetur) {
                    psItem = conn.prepareStatement(SELECT_RETUR_ITEM_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                } else {
                    psItem = conn.prepareStatement(SELECT_ORDER_ITEM_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                }

                psItem.setString(1, wcsOrderItemId);
                ResultSet rsItem = psItem.executeQuery();

                VenOrderItem venOrderItem = new VenOrderItem();
                VenReturItem venReturItem = new VenReturItem();

                if (isRetur) {
                    venReturItem = returItemResultSetMapper(rsItem);

                    /**
                     * get existing retur item status
                     */
                    existingOrderItemStatus = venReturItem.getVenReturStatus().getOrderStatusId();
                } else {
                    venOrderItem = orderItemResultSetMapper(rsItem);

                    /**
                     * get existing order item status
                     */
                    existingOrderItemStatus = venOrderItem.getVenOrderStatus().getOrderStatusId();
                }

                rsItem.close();
                rsItem = null;

                /**
                 * Get existing airwaybill from AWB Engine
                 */
                AirwayBillTransaction airwayBillTransaction = new AirwayBillTransaction();
                try {

                    airwayBillTransaction = awbConn.getAirwayBillTransaction(dailyReportNCS.getRefNo());

                    /**
                     * get existing awb transaction status & level
                     */
                    existingAirwayBillTransactionStatus = airwayBillTransaction.getStatus();
                    airwayBillTransactionLevel = airwayBillTransaction.getLevel();

                } catch (Exception e) {
                    airwayBillTransaction = new AirwayBillTransaction();
                    _log.info("", e);
                }

                /**
                 * Get existing airwaybill from DB
                 */
                LogAirwayBill existingLogAirwayBill = null;
                LogAirwayBillRetur existingLogAirwayBillRetur = null;

                if (isRetur) {
                    psAirwayBillByGDNReff = conn.prepareStatement(SELECT_AIRWAY_BILL_RETUR_SQL_BY_GDN_REF, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    psAirwayBillByItem = conn.prepareStatement(SELECT_AIRWAY_BILL_RETUR_SQL_BY_RETUR_ITEM_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                } else {
                    psAirwayBillByGDNReff = conn.prepareStatement(SELECT_AIRWAY_BILL_SQL_BY_GDN_REF, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    psAirwayBillByItem = conn.prepareStatement(SELECT_AIRWAY_BILL_SQL_BY_ORDER_ITEM_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                }

                psAirwayBillByGDNReff.setString(1, dailyReportNCS.getRefNo());
                ResultSet rsAirwayBillByGDNReff = psAirwayBillByGDNReff.executeQuery();

                rsAirwayBillByGDNReff.last();
                int totalAirwayBill = rsAirwayBillByGDNReff.getRow();
                rsAirwayBillByGDNReff.beforeFirst();

                /**
                 * Order before airwaybill automation
                 */
                if (isRetur) {
                    /**
                     * Order before airwaybill automation
                     */
                    if (totalAirwayBill > 0) {
                        existingLogAirwayBillRetur = airwayBillReturResultSetMapper(rsAirwayBillByGDNReff, logisticsProvider, null, venReturItem);
                        isDataBeforeAirwaybillAutomation = true;

                        existingAirwayBillStatus = existingLogAirwayBillRetur.getStatus();
                        /**
                         * Order airwaybill automation
                         */
                    } else {

                        psAirwayBillByItem.setLong(1, venReturItem.getReturItemId());
                        ResultSet rsAirwayBillByReturItem = psAirwayBillByItem.executeQuery();

                        existingLogAirwayBillRetur = airwayBillReturResultSetMapper(rsAirwayBillByReturItem, logisticsProvider, airwayBillTransaction, venReturItem);
                        isDataBeforeAirwaybillAutomation = false;

                        rsAirwayBillByReturItem.close();

                        airwayBillNoFromEngine = airwayBillTransaction.getAirwayBillNo();
                    }
                } else {
                    if (totalAirwayBill > 0) {
                        existingLogAirwayBill = airwayBillResultSetMapper(rsAirwayBillByGDNReff, logisticsProvider, null, venOrderItem);
                        isDataBeforeAirwaybillAutomation = true;

                        existingAirwayBillStatus = existingLogAirwayBill.getStatus();
                        /**
                         * Order airwaybill automation
                         */
                    } else {

                        psAirwayBillByItem.setLong(1, venOrderItem.getOrderItemId());
                        ResultSet rsAirwayBillByOrderItem = psAirwayBillByItem.executeQuery();

                        existingLogAirwayBill = airwayBillResultSetMapper(rsAirwayBillByOrderItem, logisticsProvider, airwayBillTransaction, venOrderItem);
                        isDataBeforeAirwaybillAutomation = false;

                        rsAirwayBillByOrderItem.close();

                        airwayBillNoFromEngine = airwayBillTransaction.getAirwayBillNo();
                    }
                }
                rsAirwayBillByGDNReff.close();
                rsAirwayBillByGDNReff = null;

                /**
                 * Populate Upload data to airwaybill object
                 */
//				/* init airwaybill in DB */
//				if(existingLogAirwayBill.getAirwayBillId() == null){
//					existingLogAirwayBill = airwayBillHome.persistLogAirwayBill(existingLogAirwayBill);
//					
//				}					
				/* init new data with existing data */
                LogAirwayBill newLogAirwayBill = null;
                LogAirwayBillRetur newLogAirwayBillRetur = null;

                if (isRetur) {
                    newLogAirwayBillRetur = new LogAirwayBillRetur();
                    newLogAirwayBillRetur = existingLogAirwayBillRetur;
                } else {
                    newLogAirwayBill = new LogAirwayBill();
                    newLogAirwayBill = existingLogAirwayBill;
                }

                /*
                 * Status of the order item must be PU, PP, ES, CX, or D
                 */
                if (existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_PU
                        && existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_PP
                        && existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_ES
                        && existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_CX
                        //jika dalam record yg diupload ada yg statusnya sudah D, tetap bisa masuk tapi hanya update recipient, relation, received date
                        && existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_D) {

                    _log.debug("status not PU, PP, ES, CX or D, status order item: " + venOrderItem.getVenOrderStatus().getOrderStatusCode());
                    String errMsg = LogisticsConstants.EXCEPTION_TEXT_AWB_STATUS_NOT_PU_PP_ES_CX_D + wcsOrderItemId + ". AWB should not be in the report.";
                    _log.error(errMsg);

                } else {

                    _log.debug("status is PU, PP, ES, CX or D");

                    if (!reportuploadEntryCreated) {

                        _log.debug("report not created yet, creating new one");
                        reportuploadEntryCreated = true;
                        logActivityReportUpload = new LogActivityReportUpload();

                        logActivityReportUpload.setFileNameAndLocation(fileUploadLog.getFileUploadNameAndLoc());
                        logActivityReportUpload.setLogLogisticsProvider(logisticsProvider);

                        LogReportStatus logReportStatus = new LogReportStatus();
                        logReportStatus.setReportStatusId(new Long(0));
                        logActivityReportUpload.setLogReportStatus(logReportStatus);
                        LogReportTemplate logReportTemplate = new LogReportTemplate();
                        logReportTemplate.setTemplateId(new Long(4));

                        logActivityReportUpload.setLogReportTemplate(logReportTemplate);
                        logActivityReportUpload.setNumberOfRecords(new Long(result.size()));
                        logActivityReportUpload.setReportDesc(fileUploadLog.getFileUploadName() + ": Logistics activity report");
                        logActivityReportUpload.setReportTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));

                        // Persist the upload file record.
                        logActivityReportUpload = reportUploadHome.persistLogActivityReportUpload(logActivityReportUpload);
                        if (isRetur) {
                            if (existingLogAirwayBillRetur != null) {
                                existingLogAirwayBillRetur.setActivityFileNameAndLoc(logActivityReportUpload.getFileNameAndLocation());
                            }
                        } else {
                            if (existingLogAirwayBill != null) {
                                existingLogAirwayBill.setActivityFileNameAndLoc(logActivityReportUpload.getFileNameAndLocation());
                            }
                        }
                    }

                    //jika status DL dan received date & recipient tidak kosong, maka set status POD
                    //jika status DL dan received date / recipient kosong, maka set status MDE
                    //jika status BA maka set status DEX 14
                    //else set status IP

                    recipient = dailyReportNCS.getRecipient();

                    if (dailyReportNCS.getStatus().equalsIgnoreCase("OK")
                            && (!dailyReportNCS.getReceived().isEmpty() && dailyReportNCS.getReceived() != null)
                            && (!dailyReportNCS.getRecipient().isEmpty() && dailyReportNCS.getRecipient() != null)) { // this is D

                        _log.debug("Masuk OK");

                        /**
                         * set new order item and airwaybill status
                         */
                        newAirwayBillTransactionStatus = AirwayBillTransaction.STATUS_CLOSED;
                        newOrderItemStatus = VeniceConstants.VEN_ORDER_STATUS_D;
                        newAirwayBillStatus = VeniceConstants.LOG_LOGISTICS_STATUS_CODE_POD;

                        if ((dailyReportNCS.getConsignee().equalsIgnoreCase(dailyReportNCS.getRecipient())) || (dailyReportNCS.getConsignee().contains(dailyReportNCS.getRecipient()))) {

                            relation = "Yang bersangkutan";

                        } else if (!dailyReportNCS.getRelation().isEmpty()) {

                            relation = dailyReportNCS.getRelation();

                        } else {
                            relation = "Lain-lain";
                        }

                        if (dailyReportNCS.getReceived() != null && !dailyReportNCS.getReceived().isEmpty()) {
                            try {

                                received = new Timestamp(formatDate.parse(dailyReportNCS.getReceived()).getTime());

                            } catch (Exception e) {
                                _log.error("Received Date Problem", e);
                                failedRecord.put("Tr No : " + dailyReportNCS.getTrNo() + ", GDN Ref : " + dailyReportNCS.getRefNo(), "Ops please fix Received Date : " + e.getMessage());
                                continue;
                            }
                        }

                        /**
                         * status D but one of received date and recipient is
                         * empy will be considered CX
                         */
                    } else if (dailyReportNCS.getStatus().equalsIgnoreCase("OK") && (dailyReportNCS.getReceived().isEmpty() || dailyReportNCS.getRecipient().isEmpty())) {
                        _log.debug("Masuk OK tanpa received date atau recipient");

                        newAirwayBillTransactionStatus = AirwayBillTransaction.STATUS_SETTLED;
                        newOrderItemStatus = VeniceConstants.VEN_ORDER_STATUS_CX;
                        newAirwayBillStatus = VeniceConstants.LOG_LOGISTICS_STATUS_CODE_MDE;
                        /**
                         * CX
                         */
                    } else {
                        _log.debug("Masuk else IP");
                        newAirwayBillTransactionStatus = AirwayBillTransaction.STATUS_SETTLED;
                        newOrderItemStatus = VeniceConstants.VEN_ORDER_STATUS_CX;
                        newAirwayBillStatus = VeniceConstants.LOG_LOGISTICS_STATUS_CODE_MDE;
                    }

                    /**
                     * set awb status, relation, recipient & received for data
                     * before airwaybill automation
                     */
                    if (isRetur) {
                        if (isDataBeforeAirwaybillAutomation) {
                            newLogAirwayBillRetur.setStatus(newAirwayBillStatus);

                            try {
                                newLogAirwayBillRetur.setAirwayBillPickupDateTime(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportNCS.getPuDate())));
                                newLogAirwayBillRetur.setActualPickupDate(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportNCS.getPuDate())));
                            } catch (Exception e) {
                                _log.error("PUtime problem", e);
                                failedRecord.put("Tr No : " + dailyReportNCS.getTrNo() + ", GDN Ref : " + dailyReportNCS.getRefNo(), "Ops please fix PUtime Problem: " + e.getMessage());
                                continue;
                            }

                            try {
                                newLogAirwayBillRetur.setNumPackages(new Integer(new Double(dailyReportNCS.getPieces()).intValue()));
                            } catch (Exception e) {
                                _log.warn("Problem getting Pieces from GDN Ref " + dailyReportNCS.getRefNo() + ", value : " + dailyReportNCS.getPieces(), e);
                            }
                            try {
                                newLogAirwayBillRetur.setPackageWeight(new BigDecimal(new Double(dailyReportNCS.getWeight()).doubleValue()));
                            } catch (Exception e) {
                                _log.warn("Problem getting weight from GDN Ref " + dailyReportNCS.getRefNo() + ", value : " + dailyReportNCS.getWeight(), e);
                            }
                        }
                    } else {
                        if (isDataBeforeAirwaybillAutomation) {
                            newLogAirwayBill.setStatus(newAirwayBillStatus);

                            try {
                                newLogAirwayBill.setAirwayBillPickupDateTime(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportNCS.getPuDate())));
                                newLogAirwayBill.setActualPickupDate(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportNCS.getPuDate())));
                            } catch (Exception e) {
                                _log.error("PUtime problem", e);
                                failedRecord.put("Tr No : " + dailyReportNCS.getTrNo() + ", GDN Ref : " + dailyReportNCS.getRefNo(), "Ops please fix PUtime Problem: " + e.getMessage());
                                continue;
                            }

                            try {
                                newLogAirwayBill.setNumPackages(new Integer(new Double(dailyReportNCS.getPieces()).intValue()));
                            } catch (Exception e) {
                                _log.warn("Problem getting Pieces from GDN Ref " + dailyReportNCS.getRefNo() + ", value : " + dailyReportNCS.getPieces(), e);
                            }
                            try {
                                newLogAirwayBill.setPackageWeight(new BigDecimal(new Double(dailyReportNCS.getWeight()).doubleValue()));
                            } catch (Exception e) {
                                _log.warn("Problem getting weight from GDN Ref " + dailyReportNCS.getRefNo() + ", value : " + dailyReportNCS.getWeight(), e);
                            }
                        }
                    }

                    /**
                     * set new awb transaction status, relation, recipient &
                     * received
                     */
                    airwayBillTransaction.setStatus(newAirwayBillTransactionStatus);
                    airwayBillTransaction.setRelation(relation);
                    airwayBillTransaction.setRecipient(recipient);
                    airwayBillTransaction.setReceived(received);
                    airwayBillTransaction.setNamaPengirim(dailyReportNCS.getShipper());

                    try {
                        airwayBillTransaction.setTanggalActualPickup(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportNCS.getPuDate())));
                    } catch (Exception e) {
                        _log.error("PUtime problem", e);
                        failedRecord.put("Tr No : " + dailyReportNCS.getTrNo() + ", GDN Ref : " + dailyReportNCS.getRefNo(), "Ops please fix PUtime Problem: " + e.getMessage());
                        continue;
                    }

                    try {
                        airwayBillTransaction.setQtyProduk(new Integer(new Double(dailyReportNCS.getPieces()).intValue()));
                    } catch (Exception e) {
                        _log.warn("Problem getting Pieces from GDN Ref " + dailyReportNCS.getRefNo() + ", value : " + dailyReportNCS.getPieces(), e);
                    }

                    try {
                        airwayBillTransaction.setWeight(new Double(dailyReportNCS.getWeight()).doubleValue());
                    } catch (Exception e) {
                        _log.warn("Problem getting weight from GDN Ref " + dailyReportNCS.getRefNo() + ", value : " + dailyReportNCS.getWeight(), e);
                    }

                    //if undelivered date and note not empty, set to log airway bill table regardless of status
                    if (isRetur) {
                        if (dailyReportNCS.getUndelivered() != null && !dailyReportNCS.getUndelivered().isEmpty()) {
                            try {
                                newLogAirwayBillRetur.setUndelivered(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportNCS.getUndelivered())));
                            } catch (Exception e) {
                                _log.error("Undelivered Date Problem", e);
                                failedRecord.put("Tr No : " + dailyReportNCS.getTrNo() + ", GDN Ref : " + dailyReportNCS.getRefNo(), "Ops please fix Undelivered Date : " + e.getMessage());
                                continue;
                            }
                            newLogAirwayBillRetur.setNoteUndelivered(dailyReportNCS.getUndeliveredNote());
                        }
                    } else {
                        if (dailyReportNCS.getUndelivered() != null && !dailyReportNCS.getUndelivered().isEmpty()) {
                            try {
                                newLogAirwayBill.setUndelivered(SQLDateUtility.utilDateToSqlTimestamp(formatDate.parse(dailyReportNCS.getUndelivered())));
                            } catch (Exception e) {
                                _log.error("Undelivered Date Problem", e);
                                failedRecord.put("Tr No : " + dailyReportNCS.getTrNo() + ", GDN Ref : " + dailyReportNCS.getRefNo(), "Ops please fix Undelivered Date : " + e.getMessage());
                                continue;
                            }
                            newLogAirwayBill.setNoteUndelivered(dailyReportNCS.getUndeliveredNote());
                        }
                    }
                    if (isRetur) {
                        newLogAirwayBillRetur.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                        newLogAirwayBillRetur.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));
                        newLogAirwayBillRetur.setTrackingNumber(dailyReportNCS.getTrNo());
                        newLogAirwayBillRetur.setAddress(dailyReportNCS.getAddress());
                        newLogAirwayBillRetur.setDeliveryOrder(dailyReportNCS.getDoNumber());
                        newLogAirwayBillRetur.setConsignee(dailyReportNCS.getConsignee());
                        newLogAirwayBillRetur.setDestCode(dailyReportNCS.getDestCode());
                        newLogAirwayBillRetur.setDestination(dailyReportNCS.getDestination());
                        newLogAirwayBillRetur.setService(dailyReportNCS.getServices());
                        newLogAirwayBillRetur.setContent(dailyReportNCS.getContent());

                        newLogAirwayBillRetur.setRelation(relation);
                        newLogAirwayBillRetur.setRecipient(recipient);
                        newLogAirwayBillRetur.setReceived(received);

                        existingLogAirwayBillRetur.setReceived(newLogAirwayBill.getReceived());
                        existingLogAirwayBillRetur.setRecipient(newLogAirwayBill.getRecipient());
                        existingLogAirwayBillRetur.setRelation(newLogAirwayBill.getRelation());

                        //if status already approved don't update awb number
                        if (existingLogAirwayBillRetur.getLogApprovalStatus2().getApprovalStatusId() != VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_APPROVED) {
                            if (existingLogAirwayBillRetur.getAirwayBillNumber() == null || existingLogAirwayBillRetur.getAirwayBillNumber().isEmpty()) {
                                newLogAirwayBillRetur.setAirwayBillNumber(dailyReportNCS.getAwb());
                            } else {
                                if (!existingLogAirwayBillRetur.getAirwayBillNumber().equalsIgnoreCase(dailyReportNCS.getAwb())) {
                                    newLogAirwayBillRetur.setAirwayBillNumber(dailyReportNCS.getAwb());
                                }
                            }
                        }

                        newLogAirwayBillRetur.setLogLogisticsProvider(logisticsProvider);
                        newLogAirwayBillRetur.setVenReturItem(venReturItem);
                    } else {
                        newLogAirwayBill.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                        newLogAirwayBill.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));
                        newLogAirwayBill.setTrackingNumber(dailyReportNCS.getTrNo());
                        newLogAirwayBill.setAddress(dailyReportNCS.getAddress());
                        newLogAirwayBill.setDeliveryOrder(dailyReportNCS.getDoNumber());
                        newLogAirwayBill.setConsignee(dailyReportNCS.getConsignee());
                        newLogAirwayBill.setDestCode(dailyReportNCS.getDestCode());
                        newLogAirwayBill.setDestination(dailyReportNCS.getDestination());
                        newLogAirwayBill.setService(dailyReportNCS.getServices());
                        newLogAirwayBill.setContent(dailyReportNCS.getContent());

                        newLogAirwayBill.setRelation(relation);
                        newLogAirwayBill.setRecipient(recipient);
                        newLogAirwayBill.setReceived(received);

                        existingLogAirwayBill.setReceived(newLogAirwayBill.getReceived());
                        existingLogAirwayBill.setRecipient(newLogAirwayBill.getRecipient());
                        existingLogAirwayBill.setRelation(newLogAirwayBill.getRelation());

                        //if status already approved don't update awb number
                        if (existingLogAirwayBill.getLogApprovalStatus2().getApprovalStatusId() != VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_APPROVED) {
                            if (existingLogAirwayBill.getAirwayBillNumber() == null || existingLogAirwayBill.getAirwayBillNumber().isEmpty()) {
                                newLogAirwayBill.setAirwayBillNumber(dailyReportNCS.getAwb());
                            } else {
                                if (!existingLogAirwayBill.getAirwayBillNumber().equalsIgnoreCase(dailyReportNCS.getAwb())) {
                                    newLogAirwayBill.setAirwayBillNumber(dailyReportNCS.getAwb());
                                }
                            }
                        }

                        newLogAirwayBill.setLogLogisticsProvider(logisticsProvider);
                        newLogAirwayBill.setVenOrderItem(venOrderItem);
                    }

                    /*
                     * 2011-05-23 In accordance with JIRA VENICE-16
                     * Pickup Data Late Only reconcile the AWB data if
                     * the existing data has come from MTA or has been
                     * reconciled against data from MTA. Use the new
                     * MtaData flag in the AWB to determine this.
                     */

                    /* Reconcile and merge the airwaybill
                     * If there is no data from MTA then merge the new AWB
                     *     reconciliation will happen via integration later
                     * If there is existing MTA data then merge the existing AWB
                     *     this is only to store the AWB number 
                     */
                    if (existingOrderItemStatus != VeniceConstants.VEN_ORDER_STATUS_D && !dailyReportNCS.getStatus().equalsIgnoreCase("RT")) {
                        /**
                         * Data from MTA exist
                         */
                        if (isRetur) {
                            if (existingLogAirwayBillRetur != null && existingLogAirwayBillRetur.getMtaData()) {
                                newLogAirwayBillRetur.setAirwayBillReturId(existingLogAirwayBillRetur.getAirwayBillReturId());
                                /**
                                 * If update status from CX to D no need to
                                 * reconcile again
                                 */
                                _log.debug("mta data true");

                                if ((existingLogAirwayBillRetur.getAirwayBillNumber() == null || existingLogAirwayBillRetur.getAirwayBillNumber().isEmpty()) && existingLogAirwayBillRetur.getLogApprovalStatus2().getApprovalStatusId() != VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_APPROVED) {
                                    existingLogAirwayBillRetur.setAirwayBillNumber(newLogAirwayBillRetur.getAirwayBillNumber());

                                    airwayBillTransaction.setAirwayBillNo(newLogAirwayBillRetur.getAirwayBillNumber());
                                }

                                //Set the activity result status and merge existing airway bill
                                existingLogAirwayBillRetur.setActivityResultStatus(newLogAirwayBillRetur.getActivityResultStatus());

                                //Set these few fields so that they show up in delivery status tracking
                                existingLogAirwayBillRetur.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                                existingLogAirwayBillRetur.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));

                                //Merge the existing airway bill
                                try {
                                    /* set new order item status */
                                    existingLogAirwayBillRetur.getVenReturItem().getVenReturStatus().setOrderStatusId(newOrderItemStatus);

                                    airwayBillReturHome.mergeLogAirwayBillRetur(existingLogAirwayBillRetur);
                                } catch (Exception e) {
                                    _log.error("mergeLogAirwayBill Problem", e);
                                    failedRecord.put("Tr No : " + dailyReportNCS.getTrNo() + ", GDN Ref : " + dailyReportNCS.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                    continue;
                                }

                                /**
                                 * No data from MTA
                                 */
                            } else {

                                _log.debug("no data from mta");
                                newLogAirwayBillRetur.setActivityResultStatus(LogisticsConstants.RESULT_STATUS_NO_DATA_FROM_MTA);
                                newLogAirwayBillRetur.setMtaData(false);
                                newLogAirwayBillRetur.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());

                                /**
                                 * Mark when airway bill no from engine &
                                 * logistics are different
                                 */
                                if (!isDataBeforeAirwaybillAutomation && (!airwayBillNoFromEngine.equals(airwayBillNoFromLogistic))) {
                                    if ((!(existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_SETTLED) && newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)))) {
                                        // represent data from MTA
                                        existingLogAirwayBillRetur.setAirwayBillNumber(airwayBillNoFromEngine);
                                        // represent data from Logistics
                                        newLogAirwayBillRetur.setAirwayBillNumber(airwayBillNoFromLogistic);
                                    }
                                }

                                try {
                                    /* set new retur item status */
                                    newLogAirwayBillRetur.getVenReturItem().getVenReturStatus().setOrderStatusId(newOrderItemStatus);

                                    airwayBillReturHome.mergeLogAirwayBillRetur(newLogAirwayBillRetur);
                                } catch (Exception e) {
                                    _log.error("mergeLogAirwayBillRetur Problem", e);
                                    failedRecord.put("Tr No : " + dailyReportNCS.getTrNo() + ", GDN Ref : " + dailyReportNCS.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                    continue;
                                }
                            }
                        } else {
                            if (existingLogAirwayBill != null && existingLogAirwayBill.getMtaData()) {
                                AWBReconciliation awbReconciliation = new AWBReconciliation();
                                newLogAirwayBill.setAirwayBillId(existingLogAirwayBill.getAirwayBillId());
                                /**
                                 * If update status from CX to D no need to
                                 * reconcile again
                                 */
                                _log.debug("mta data true");

                                /**
                                 * Recon order before airway bill automation
                                 */
                                if (isDataBeforeAirwaybillAutomation) {
                                    if (existingAirwayBillStatus == null || (!(existingAirwayBillStatus.equals(VeniceConstants.LOG_LOGISTICS_STATUS_CODE_MDE) && newAirwayBillStatus.equals(VeniceConstants.LOG_LOGISTICS_STATUS_CODE_POD)))) {
                                        _log.debug("reconcile awb");
                                        try {
                                            newLogAirwayBill = awbReconciliation.performActivityReconciliation(conn, logActivityReportUpload, newLogAirwayBill, existingLogAirwayBill, wcsOrderItemId);
                                        } catch (Exception e) {
                                            _log.error("AWBReconciliation Problem", e);
                                            failedRecord.put("Tr No : " + dailyReportNCS.getTrNo() + ", GDN Ref : " + dailyReportNCS.getRefNo(), "Ops tell IT about this Reconciliation Problem : " + e.getMessage());
                                            continue;
                                        }
                                    }
                                } else {
                                    if ((!(existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_SETTLED) && newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)))) {
                                        _log.debug("reconcile awb");
                                        try {
                                            newLogAirwayBill = awbReconciliation.performActivityReconciliation(conn, logActivityReportUpload, newLogAirwayBill, existingLogAirwayBill, wcsOrderItemId);
                                        } catch (Exception e) {
                                            _log.error("AWBReconciliation Problem", e);
                                            failedRecord.put("Tr No : " + dailyReportNCS.getTrNo() + ", GDN Ref : " + dailyReportNCS.getRefNo(), "Ops tell IT about this Reconciliation Problem : " + e.getMessage());
                                            continue;
                                        }
                                    }
                                }

                                if ((existingLogAirwayBill.getAirwayBillNumber() == null || existingLogAirwayBill.getAirwayBillNumber().isEmpty()) && existingLogAirwayBill.getLogApprovalStatus2().getApprovalStatusId() != VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_APPROVED) {
                                    existingLogAirwayBill.setAirwayBillNumber(newLogAirwayBill.getAirwayBillNumber());

                                    airwayBillTransaction.setAirwayBillNo(newLogAirwayBill.getAirwayBillNumber());
                                }

                                //Set the activity result status and merge existing airway bill
                                existingLogAirwayBill.setActivityResultStatus(newLogAirwayBill.getActivityResultStatus());

                                //Set these few fields so that they show up in delivery status tracking
                                existingLogAirwayBill.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                                existingLogAirwayBill.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));

                                //Merge the existing airway bill
                                try {
                                    /* set new order item status */
                                    existingLogAirwayBill.getVenOrderItem().getVenOrderStatus().setOrderStatusId(newOrderItemStatus);

                                    airwayBillHome.mergeLogAirwayBill(existingLogAirwayBill);
                                } catch (Exception e) {
                                    _log.error("mergeLogAirwayBill Problem", e);
                                    failedRecord.put("Tr No : " + dailyReportNCS.getTrNo() + ", GDN Ref : " + dailyReportNCS.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                    continue;
                                }

                                /**
                                 * No data from MTA
                                 */
                            } else {

                                _log.debug("no data from mta");
                                newLogAirwayBill.setActivityResultStatus(LogisticsConstants.RESULT_STATUS_NO_DATA_FROM_MTA);
                                newLogAirwayBill.setMtaData(false);
                                newLogAirwayBill.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());

                                /**
                                 * Mark when airway bill no from engine &
                                 * logistics are different
                                 */
                                if (!isDataBeforeAirwaybillAutomation && (!airwayBillNoFromEngine.equals(airwayBillNoFromLogistic))) {
                                    if ((!(existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_SETTLED) && newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)))) {
                                        _log.debug("reconcile awb");
                                        try {
                                            AWBReconciliation awbReconciliation = new AWBReconciliation();
                                            // represent data from MTA
                                            existingLogAirwayBill.setAirwayBillNumber(airwayBillNoFromEngine);
                                            // represent data from Logistics
                                            newLogAirwayBill.setAirwayBillNumber(airwayBillNoFromLogistic);

                                            newLogAirwayBill = awbReconciliation.performActivityReconciliation(conn, logActivityReportUpload, newLogAirwayBill, existingLogAirwayBill, wcsOrderItemId);
                                        } catch (Exception e) {
                                            _log.error("AWBReconciliation Problem", e);
                                            failedRecord.put("Tr No : " + dailyReportNCS.getTrNo() + ", GDN Ref : " + dailyReportNCS.getRefNo(), "Ops tell IT about this Reconciliation Problem : " + e.getMessage());
                                            continue;
                                        }
                                    }
                                }

                                try {
                                    /* set new order item status */
                                    newLogAirwayBill.getVenOrderItem().getVenOrderStatus().setOrderStatusId(newOrderItemStatus);

                                    airwayBillHome.mergeLogAirwayBill(newLogAirwayBill);
                                } catch (Exception e) {
                                    _log.error("mergeLogAirwayBill Problem", e);
                                    failedRecord.put("Tr No : " + dailyReportNCS.getTrNo() + ", GDN Ref : " + dailyReportNCS.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                    continue;
                                }

                            }
                        }

                        /**
                         * Update the status of the order items and trigger the
                         * publish
                         */
                        try {

                            if (isDataBeforeAirwaybillAutomation) {
                                if (isRetur) {
                                    updateReturItemBeforeAirwayBillAutomationStatus(venReturItem, existingOrderItemStatus, newOrderItemStatus);
                                } else {
                                    updateOrderItemBeforeAirwayBillAutomationStatus(venOrderItem, existingOrderItemStatus, newOrderItemStatus);
                                }
                            } else {
                                if (isRetur) {
                                    updateReturItemAndAirwayBillTransactionStatus(fileUploadLog.getUploadUsername(),
                                            venReturItem,
                                            airwayBillTransaction,
                                            existingOrderItemStatus,
                                            newOrderItemStatus,
                                            existingAirwayBillTransactionStatus,
                                            newAirwayBillTransactionStatus,
                                            airwayBillTransactionLevel,
                                            airwayBillNoFromEngine,
                                            airwayBillNoFromLogistic,
                                            failedStatusUpdateList);
                                } else {
                                    updateOrderItemAndAirwayBillTransactionStatus(fileUploadLog.getUploadUsername(),
                                            venOrderItem,
                                            airwayBillTransaction,
                                            existingOrderItemStatus,
                                            newOrderItemStatus,
                                            existingAirwayBillTransactionStatus,
                                            newAirwayBillTransactionStatus,
                                            airwayBillTransactionLevel,
                                            airwayBillNoFromEngine,
                                            airwayBillNoFromLogistic,
                                            failedStatusUpdateList);
                                }
                            }
                        } catch (Exception e) {
                            _log.error("update airwaybill status Problem", e);
                            failedRecord.put("Tr No : " + dailyReportNCS.getTrNo() + ", GDN Ref : " + dailyReportNCS.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                            continue;
                        }

                        _log.debug("done update order item status and trigger the publish");

                    } else if (dailyReportNCS.getStatus().equalsIgnoreCase("RT")) {
                        if (isRetur) {
                            _log.debug("retur item status RT, only file name and location");
                            existingLogAirwayBillRetur.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());

                            //Merge the existing airway bill
                            try {
                                /* set new retur item status */
                                existingLogAirwayBillRetur.getVenReturItem().getVenReturStatus().setOrderStatusId(newOrderItemStatus);

                                airwayBillReturHome.mergeLogAirwayBillRetur(existingLogAirwayBillRetur);
                            } catch (Exception e) {
                                _log.error("mergeLogAirwayBillRetur Problem", e);
                                failedRecord.put("Tr No : " + dailyReportNCS.getTrNo() + ", GDN Ref : " + dailyReportNCS.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                continue;
                            }
                        } else {
                            _log.debug("order item status RT, only file name and location");
                            existingLogAirwayBill.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());

                            //Merge the existing airway bill
                            try {
                                /* set new order item status */
                                existingLogAirwayBill.getVenOrderItem().getVenOrderStatus().setOrderStatusId(newOrderItemStatus);

                                airwayBillHome.mergeLogAirwayBill(existingLogAirwayBill);
                            } catch (Exception e) {
                                _log.error("mergeLogAirwayBill Problem", e);
                                failedRecord.put("Tr No : " + dailyReportNCS.getTrNo() + ", GDN Ref : " + dailyReportNCS.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                continue;
                            }
                        }
                    } else {
                        if (isRetur) {
                            _log.debug("retur item status already D, only update recipient, relation, received date");

                            existingLogAirwayBillRetur.setReceived(newLogAirwayBill.getReceived());
                            existingLogAirwayBillRetur.setRecipient(newLogAirwayBill.getRecipient());
                            existingLogAirwayBillRetur.setRelation(newLogAirwayBill.getRelation());
                            existingLogAirwayBillRetur.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                            existingLogAirwayBillRetur.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));

                            //Merge the existing airway bill
                            try {
                                /* set new order item status */
                                existingLogAirwayBillRetur.getVenReturItem().getVenReturStatus().setOrderStatusId(newOrderItemStatus);

                                airwayBillReturHome.mergeLogAirwayBillRetur(existingLogAirwayBillRetur);
                            } catch (Exception e) {
                                _log.error("mergeLogAirwayBillRetur Problem", e);
                                failedRecord.put("Tr No : " + dailyReportNCS.getTrNo() + ", GDN Ref : " + dailyReportNCS.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                continue;
                            }
                        } else {
                            _log.debug("order item status already D, only update recipient, relation, received date");

                            existingLogAirwayBill.setReceived(newLogAirwayBill.getReceived());
                            existingLogAirwayBill.setRecipient(newLogAirwayBill.getRecipient());
                            existingLogAirwayBill.setRelation(newLogAirwayBill.getRelation());
                            existingLogAirwayBill.setActivityFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
                            existingLogAirwayBill.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));

                            //Merge the existing airway bill
                            try {
                                /* set new order item status */
                                existingLogAirwayBill.getVenOrderItem().getVenOrderStatus().setOrderStatusId(newOrderItemStatus);

                                airwayBillHome.mergeLogAirwayBill(existingLogAirwayBill);
                            } catch (Exception e) {
                                _log.error("mergeLogAirwayBill Problem", e);
                                failedRecord.put("Tr No : " + dailyReportNCS.getTrNo() + ", GDN Ref : " + dailyReportNCS.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                                continue;
                            }
                        }

                        /**
                         * Update the status of the order items and trigger the
                         * publish
                         */
                        try {
                            if (isDataBeforeAirwaybillAutomation) {
                                if (isRetur) {
                                    updateReturItemBeforeAirwayBillAutomationStatus(venReturItem, existingOrderItemStatus, newOrderItemStatus);
                                } else {
                                    updateOrderItemBeforeAirwayBillAutomationStatus(venOrderItem, existingOrderItemStatus, newOrderItemStatus);
                                }
                            } else {
                                if (isRetur) {
                                    updateReturItemAndAirwayBillTransactionStatus(fileUploadLog.getUploadUsername(),
                                            venReturItem,
                                            airwayBillTransaction,
                                            existingOrderItemStatus,
                                            newOrderItemStatus,
                                            existingAirwayBillTransactionStatus,
                                            newAirwayBillTransactionStatus,
                                            airwayBillTransactionLevel,
                                            airwayBillNoFromEngine,
                                            airwayBillNoFromLogistic,
                                            failedStatusUpdateList);
                                } else {
                                    updateOrderItemAndAirwayBillTransactionStatus(fileUploadLog.getUploadUsername(),
                                            venOrderItem,
                                            airwayBillTransaction,
                                            existingOrderItemStatus,
                                            newOrderItemStatus,
                                            existingAirwayBillTransactionStatus,
                                            newAirwayBillTransactionStatus,
                                            airwayBillTransactionLevel,
                                            airwayBillNoFromEngine,
                                            airwayBillNoFromLogistic,
                                            failedStatusUpdateList);
                                }
                            }
                        } catch (Exception e) {
                            _log.error("update airwaybill status Problem", e);
                            failedRecord.put("Tr No : " + dailyReportNCS.getTrNo() + ", GDN Ref : " + dailyReportNCS.getRefNo(), "Ops tell IT about this Data Problem : " + e.getMessage());
                            continue;
                        }
                        _log.debug("done update order item status and trigger the publish");

                    }

                    //if activityResultStatus OK, then set to approved, this will trigger CX finance.
                    if (isRetur) {
                        if (newLogAirwayBillRetur.getActivityResultStatus().equals("OK")) {
                            _log.debug("activity result status OK, set approval status to approved to trigger CX finance");
                            LogApprovalStatus activityStatusApproved = new LogApprovalStatus();
                            activityStatusApproved.setApprovalStatusId(VeniceConstants.LOG_APPROVAL_STATUS_APPROVED);
                            existingLogAirwayBillRetur.setLogApprovalStatus2(activityStatusApproved);
                            existingLogAirwayBillRetur.setActivityApprovedByUserId("System");
                            airwayBillReturHome.mergeLogAirwayBillRetur(existingLogAirwayBillRetur);
                        }
                    } else {
                        if (newLogAirwayBill.getActivityResultStatus().equals("OK")) {
                            _log.debug("activity result status OK, set approval status to approved to trigger CX finance");
                            LogApprovalStatus activityStatusApproved = new LogApprovalStatus();
                            activityStatusApproved.setApprovalStatusId(VeniceConstants.LOG_APPROVAL_STATUS_APPROVED);
                            existingLogAirwayBill.setLogApprovalStatus2(activityStatusApproved);
                            existingLogAirwayBill.setActivityApprovedByUserId("System");
                            airwayBillHome.mergeLogAirwayBill(existingLogAirwayBill);
                        }
                    }
                }

                errorRowNumber++;

            }

            // close prepared statement
            psAirwayBillByGDNReff.close();
            psAirwayBillByGDNReff = null;

            psAirwayBillByItem.close();
            psAirwayBillByItem = null;

            psItem.close();
            psItem = null;

            // close connection
            conn.close();

            if (!reportuploadEntryCreated) {
                String errMsg = "No airway bills were merged for report upload:" + fileUploadLog.getFileUploadName();
                _log.error(errMsg);
            }

            if (gdnRefNotFoundList.size() > 0 || failedRecord.size() > 0 || failedStatusUpdateList.size() > 0) {
                _log.info(gdnRefNotFoundList.size() + " row(s) was not uploaded, start export excel");
                _log.info(failedRecord.size() + " row(s) has problem when being processed");
                _log.info(failedStatusUpdateList.size() + " row(s) has fail status update");

                FileOutputStream fos = null;

                try {
                    String outputFileName = "ActivityReportFailedToUpload-" + fileDateTimeFormat.format(new Date()) + ".xls";
                    fos = new FileOutputStream(FILE_PATH + outputFileName);

                    HSSFWorkbook wb = new HSSFWorkbook();
                    HSSFSheet sheet = wb.createSheet("ActivityReportFailedToUpload");

                    ActivityInvoiceFailedToUploadExport activityInvoiceFailedToUploadExport = new ActivityInvoiceFailedToUploadExport(wb);
                    wb = activityInvoiceFailedToUploadExport.ExportExcel(gdnRefNotFoundList, failedRecord, failedStatusUpdateList, sheet, ACTIVITY_OR_INVOICE);
                    wb.write(fos);
                    _log.debug("done export excel");

                    fileUploadLog.setFailedFileUploadName(outputFileName);
                    fileUploadLog.setFailedFileUploadNameAndLoc(FILE_PATH + outputFileName);
                    fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_SUCCESS);
                    updateFileUploadLog(fileUploadLog);

                } catch (Exception e) {
                    _log.error("Exception in Excel ActivityReportImportNCSServlet", e);
                    throw new ServletException("Exception in Excel ActivityReportImportNCSServlet", e);
                } finally {
                    if (fos != null) {
                        fos.close();
                    }
                }

            } else {
                String successMsg = LogisticsConstants.UPLOAD_SUCCESS_MESSAGE;
                _log.info(successMsg);

                fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_SUCCESS);
                updateFileUploadLog(fileUploadLog);

            }

        } catch (Exception e) {
            String errMsg = LogisticsConstants.EXCEPTION_TEXT_GENERAL + e.getClass().getName() + ":" + e.getMessage() + ". Processing row number:" + errorRowNumber;
            _log.error(errMsg, e);
            e.printStackTrace();

            fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_FAIL);
            updateFileUploadLog(fileUploadLog);

        } finally {
            try {
                if (locator != null) {
                    locator.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Boolean updateOrderItemStatus(VenOrderItem venOrderItem, long newStatus) {

        Locator<Object> locator = null;
        try {
            locator = new Locator<Object>();

            VenOrderItemSessionEJBLocal venOrderItemHome = (VenOrderItemSessionEJBLocal) locator
                    .lookupLocal(VenOrderItemSessionEJBLocal.class, "VenOrderItemSessionEJBBeanLocal");

            VenOrderStatus venOrderStatus = new VenOrderStatus();
            venOrderStatus.setOrderStatusId(newStatus);
            venOrderItem.setVenOrderStatus(venOrderStatus);

            if (newStatus == VeniceConstants.VEN_ORDER_STATUS_CX) {
                venOrderItem.setSalesBatchStatus(VeniceConstants.FIN_SALES_BATCH_STATUS_READY);
            }

            venOrderItemHome.mergeVenOrderItem(venOrderItem);

        } catch (Exception e) {
            String errMsg = "An exception occured when performing update of the order item status based on the activity report upload:" + venOrderItem.getWcsOrderItemId();
            _log.error(errMsg, e);

            return false;

        } finally {
            try {
                if (locator != null) {
                    locator.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private Boolean updateReturItemStatus(VenReturItem venReturItem, long newStatus) {

        Locator<Object> locator = null;
        try {
            locator = new Locator<Object>();

            VenReturItemSessionEJBLocal venReturItemHome = (VenReturItemSessionEJBLocal) locator
                    .lookupLocal(VenReturItemSessionEJBLocal.class, "VenReturItemSessionEJBBeanLocal");

            VenOrderStatus venReturStatus = new VenOrderStatus();
            venReturStatus.setOrderStatusId(newStatus);
            venReturItem.setVenReturStatus(venReturStatus);

            venReturItemHome.mergeVenReturItem(venReturItem);

        } catch (Exception e) {
            String errMsg = "An exception occured when performing update of the retur item status based on the activity report upload:" + venReturItem.getWcsReturItemId();
            _log.error(errMsg, e);

            return false;

        } finally {
            try {
                if (locator != null) {
                    locator.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private Boolean updateOrderItemBeforeAirwayBillAutomationStatus(VenOrderItem venOrderItem,
            long existingOrderItemStatus,
            long newOrderItemStatus) throws Exception {

        _log.debug("Order Item : " + venOrderItem.getWcsOrderItemId());
        _log.debug("Existing Order Item Status : " + existingOrderItemStatus);
        _log.debug("New Order Item Status : " + newOrderItemStatus);

        /**
         * Order Item is PU
         */
        if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_PU) {
            /**
             * PU -> CX will put ES in the middle : PU -> ES -> CX
             */
            if (newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_CX) {
                updateOrderItemStatus(venOrderItem, VeniceConstants.VEN_ORDER_STATUS_ES);

                updateOrderItemStatus(venOrderItem, VeniceConstants.VEN_ORDER_STATUS_CX);
            }
        }

        /**
         * Order Item is ES
         */
        if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_ES) {

            /**
             * ES -> CX
             */
            if (newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_CX) {

                updateOrderItemStatus(venOrderItem, VeniceConstants.VEN_ORDER_STATUS_CX);
            }

            /**
             * ES -> D will put CX in the middle : ES -> CX -> D
             */
            if (newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_D) {
                updateOrderItemStatus(venOrderItem, VeniceConstants.VEN_ORDER_STATUS_CX);

                updateOrderItemStatus(venOrderItem, VeniceConstants.VEN_ORDER_STATUS_D);
            }

        }

        /**
         * Airway bill is CX
         */
        if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_CX) {
            /**
             * CX -> D
             */
            if (newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_D) {
                updateOrderItemStatus(venOrderItem, VeniceConstants.VEN_ORDER_STATUS_D);
            }

        }


        /**
         * Airway bill is D
         */
        if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_D) {

            /**
             * D -> D will only update receiver, relation, and received
             */
            if (newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_D) {
            }

        }

        return Boolean.TRUE;
    }

    private Boolean updateReturItemBeforeAirwayBillAutomationStatus(VenReturItem venReturItem,
            long existingReturItemStatus,
            long newReturItemStatus) throws Exception {

        _log.debug("Retur Item : " + venReturItem.getWcsReturItemId());
        _log.debug("Existing Retur Item Status : " + existingReturItemStatus);
        _log.debug("New Retur Item Status : " + newReturItemStatus);

        /**
         * Retur Item is PU
         */
        if (existingReturItemStatus == VeniceConstants.VEN_ORDER_STATUS_PU) {
            /**
             * PU -> CX will put ES in the middle : PU -> ES -> CX
             */
            if (newReturItemStatus == VeniceConstants.VEN_ORDER_STATUS_CX) {
                updateReturItemStatus(venReturItem, VeniceConstants.VEN_ORDER_STATUS_ES);

                updateReturItemStatus(venReturItem, VeniceConstants.VEN_ORDER_STATUS_CX);
            }
        }

        /**
         * Order Item is ES
         */
        if (existingReturItemStatus == VeniceConstants.VEN_ORDER_STATUS_ES) {

            /**
             * ES -> CX
             */
            if (newReturItemStatus == VeniceConstants.VEN_ORDER_STATUS_CX) {

                updateReturItemStatus(venReturItem, VeniceConstants.VEN_ORDER_STATUS_CX);
            }

            /**
             * ES -> D will put CX in the middle : ES -> CX -> D
             */
            if (newReturItemStatus == VeniceConstants.VEN_ORDER_STATUS_D) {
                updateReturItemStatus(venReturItem, VeniceConstants.VEN_ORDER_STATUS_CX);

                updateReturItemStatus(venReturItem, VeniceConstants.VEN_ORDER_STATUS_D);
            }

        }

        /**
         * Airway bill is CX
         */
        if (existingReturItemStatus == VeniceConstants.VEN_ORDER_STATUS_CX) {
            /**
             * CX -> D
             */
            if (newReturItemStatus == VeniceConstants.VEN_ORDER_STATUS_D) {
                updateReturItemStatus(venReturItem, VeniceConstants.VEN_ORDER_STATUS_D);
            }

        }


        /**
         * Airway bill is D
         */
        if (existingReturItemStatus == VeniceConstants.VEN_ORDER_STATUS_D) {
            /**
             * D -> D will only update receiver, relation, and received
             */
            if (newReturItemStatus == VeniceConstants.VEN_ORDER_STATUS_D) {
            }
        }

        return Boolean.TRUE;
    }

    /**
     *
     * @param gdnRefNo
     * @param newAirwayBillNo
     * @param uploadUsername
     * @return overriding AWB no is allowed or not
     */
    private Boolean overrideAirwayBillNumber(String gdnRefNo, String newAirwayBillNo, String uploadUsername) {

        _log.debug("GDN Ref No : " + gdnRefNo);
        _log.debug("New Airway Bill No : " + newAirwayBillNo);

        return awbConn.overrideAirwayBillNumber(gdnRefNo, newAirwayBillNo, uploadUsername);

    }

    private Boolean updateOrderItemAndAirwayBillTransactionStatus(String uploadUsername,
            VenOrderItem venOrderItem,
            AirwayBillTransaction airwayBillTransaction,
            long existingOrderItemStatus,
            long newOrderItemStatus,
            String existingAirwayBillTransactionStatus,
            String newAirwayBillTransactionStatus,
            String airwayBillTransactionLevel,
            String airwayBillNoFromEngine,
            String airwayBillNoFromLogistic,
            List<FailedStatusUpdate> failedStatusUpdateList) {

        boolean isProcessSuccess = false;

        /**
         * When airway bill number from engine & logistics are the same, update
         * airway bill status
         */
        if (airwayBillNoFromEngine.equals(airwayBillNoFromLogistic)) {
            _log.debug("Airway Bill Engine & Logistic are matched");
            try {
                isProcessSuccess = updateOrderItemAndAirwayBillTransactionStatus(uploadUsername, venOrderItem, airwayBillTransaction, existingOrderItemStatus, newOrderItemStatus, existingAirwayBillTransactionStatus, newAirwayBillTransactionStatus, airwayBillTransactionLevel);
            } catch (Exception e) {
                _log.error("Problem updateOrderItemAndAirwayBillTransactionStatus ", e);
                isProcessSuccess = false;
            }
            /**
             * When airwaybill status update fail, add to fail report
             */
            /**
             * Airway bill number from engine & logistics are different
             */
        } else {
            _log.debug("Airway Bill Engine & Logistic are NOT matched");
            _log.debug("Airway Bill Engine : " + airwayBillNoFromEngine);
            _log.debug("Airway Bill Logistics : " + airwayBillNoFromLogistic);
            /**
             * When existing airway bill status is CX & D, airway bill number
             * override is not allowed. Add to fail report.
             */
            if (existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_SETTLED)
                    || existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)) {

                _log.debug("Airway Bill from engine " + airwayBillNoFromEngine + " status is CX or D, not allowed to override");

                isProcessSuccess = false;

            } else {

                /**
                 * Ask engine to override airway bill number
                 */
                isProcessSuccess = overrideAirwayBillNumber(airwayBillTransaction.getGdnRef(), airwayBillNoFromLogistic, uploadUsername);

                _log.debug("Airway Bill override result from engine " + isProcessSuccess);

                /**
                 * Update airwaybill status when override succesful
                 */
                if (isProcessSuccess) {
                    airwayBillTransaction.setAirwayBillNo(airwayBillNoFromLogistic);

                    try {
                        isProcessSuccess = updateOrderItemAndAirwayBillTransactionStatus(uploadUsername, venOrderItem, airwayBillTransaction, existingOrderItemStatus, newOrderItemStatus, existingAirwayBillTransactionStatus, newAirwayBillTransactionStatus, airwayBillTransactionLevel);
                    } catch (Exception e) {
                        _log.error("Problem updateOrderItemAndAirwayBillTransactionStatus ", e);
                        isProcessSuccess = false;
                    }
                    /**
                     * When airwaybill status update fail, add to fail report
                     */
                    /**
                     * When airwaybill status override fail, add to fail report
                     */
                } else {
                    isProcessSuccess = false;
                }

            }

        }

        if (!isProcessSuccess) {
            FailedStatusUpdate failedStatusUpdate = new FailedStatusUpdate();

            failedStatusUpdate.setGdnRef(airwayBillTransaction.getGdnRef());
            failedStatusUpdate.setAirwayBillNoLogistic(airwayBillNoFromLogistic);
            failedStatusUpdate.setAirwayBillNoMTA(airwayBillNoFromEngine);

            if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_CX) {
                failedStatusUpdate.setOrderItemStatus("CX");
            }

            if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_D) {
                failedStatusUpdate.setOrderItemStatus("D");
            }

            if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_ES) {
                failedStatusUpdate.setOrderItemStatus("ES");
            }

            if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_PU) {
                failedStatusUpdate.setOrderItemStatus("PU");
            }

            failedStatusUpdate.setAirwayBillStatus(existingAirwayBillTransactionStatus);

            failedStatusUpdateList.add(failedStatusUpdate);

            return false;
        } else {
            return true;
        }
    }

    private Boolean updateOrderItemAndAirwayBillTransactionStatus(String uploadUsername, VenOrderItem venOrderItem, AirwayBillTransaction airwayBillTransaction, long existingOrderItemStatus, long newOrderItemStatus, String existingAirwayBillTransactionStatus, String newAirwayBillTransactionStatus, String airwayBillTransactionLevel) throws Exception {

        _log.debug("Order Item : " + venOrderItem.getWcsOrderItemId());
        _log.debug("Existing Order Item Status : " + existingOrderItemStatus);
        _log.debug("New Order Item Status : " + newOrderItemStatus);
        _log.debug("Airway Bill Number : " + airwayBillTransaction.getAirwayBillNo());
        _log.debug("Existing Airway Bill Transaction Status : " + existingAirwayBillTransactionStatus);
        _log.debug("New Airway Bill Transaction Status : " + newAirwayBillTransactionStatus);
        _log.debug("Airway Bill Transaction Level : " + airwayBillTransactionLevel);

        boolean isUpdateSuccessful = false;

//    	SalesRecordGenerator salesRecordGenerator = new SalesRecordGenerator();

        /**
         * Airway bill is PU
         */
        if (existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_PICK_UP)) {
            /**
             * PU -> CX will put ES in the middle : PU -> ES -> CX
             */
            if (newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_SETTLED)) {

                isUpdateSuccessful = awbConn.updateAirwayBillToES(airwayBillTransaction.getAirwayBillNo(), airwayBillTransaction.getKodeLogistik(), uploadUsername);

                if (isUpdateSuccessful) {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to ES successful");
                    /* Modify order item status when AWB Level is main */
                    if (airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_ORDER_MAIN)
                            || airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_RETUR_MAIN)) {

                        updateOrderItemStatus(venOrderItem, VeniceConstants.VEN_ORDER_STATUS_ES);
                    }
                } else {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to ES fail");
                    return false;
                }

                isUpdateSuccessful = awbConn.updateAirwayBillToCX(airwayBillTransaction.getAirwayBillNo(), airwayBillTransaction.getKodeLogistik(), uploadUsername, airwayBillTransaction.getTanggalActualPickup(), airwayBillTransaction.getRecipient(), airwayBillTransaction.getRelation(), airwayBillTransaction.getReceived());

                if (isUpdateSuccessful) {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to CX successful");
                    /* Modify order item status when AWB Level is main */
                    if (airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_ORDER_MAIN)
                            || airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_RETUR_MAIN)) {

                        if (updateOrderItemStatus(venOrderItem, VeniceConstants.VEN_ORDER_STATUS_CX)) {
//							salesRecordGenerator.createOrUpdateSalesRecord(venOrderItem);
                        }
                    }

                } else {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to CX fail");
                    return false;
                }

            }

        }

        /**
         * Airway bill is ES
         */
        if (existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_EMAIL_SENT)) {

            /**
             * ES -> CX
             */
            if (newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_SETTLED)) {

                isUpdateSuccessful = awbConn.updateAirwayBillToCX(airwayBillTransaction.getAirwayBillNo(), airwayBillTransaction.getKodeLogistik(), uploadUsername, airwayBillTransaction.getTanggalActualPickup(), airwayBillTransaction.getRecipient(), airwayBillTransaction.getRelation(), airwayBillTransaction.getReceived());

                if (isUpdateSuccessful) {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to CX successful");
                    /* Modify order item status when AWB Level is main */
                    if (airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_ORDER_MAIN)
                            || airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_RETUR_MAIN)) {

                        if (updateOrderItemStatus(venOrderItem, VeniceConstants.VEN_ORDER_STATUS_CX)) {
//							salesRecordGenerator.createOrUpdateSalesRecord(venOrderItem);
                        }
                    }

                } else {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to CX fail");
                    return false;
                }

            }

            /**
             * ES -> D will put CX in the middle : ES -> CX -> D
             */
            if (newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)) {

                isUpdateSuccessful = awbConn.updateAirwayBillToCX(airwayBillTransaction.getAirwayBillNo(), airwayBillTransaction.getKodeLogistik(), uploadUsername, airwayBillTransaction.getTanggalActualPickup(), airwayBillTransaction.getRecipient(), airwayBillTransaction.getRelation(), airwayBillTransaction.getReceived());

                if (isUpdateSuccessful) {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to CX successful");
                    /* Modify order item status when AWB Level is main */
                    if (airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_ORDER_MAIN)
                            || airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_RETUR_MAIN)) {

                        if (updateOrderItemStatus(venOrderItem, VeniceConstants.VEN_ORDER_STATUS_CX)) {
//							salesRecordGenerator.createOrUpdateSalesRecord(venOrderItem);
                        }
                    }
                } else {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to CX fail");
                    return false;
                }

                isUpdateSuccessful = awbConn.updateAirwayBillToD(airwayBillTransaction.getAirwayBillNo(), airwayBillTransaction.getKodeLogistik(), uploadUsername, airwayBillTransaction.getTanggalActualPickup(), airwayBillTransaction.getRecipient(), airwayBillTransaction.getRelation(), airwayBillTransaction.getReceived());

                if (isUpdateSuccessful) {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to D successful");
                    /* Modify order item status when AWB Level is main */
                    if (airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_ORDER_MAIN)
                            || airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_RETUR_MAIN)) {

                        updateOrderItemStatus(venOrderItem, VeniceConstants.VEN_ORDER_STATUS_D);
                    }
                } else {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to D fail");
                    return false;
                }

            }

        }

        /**
         * Airway bill is CX
         */
        if (existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_SETTLED)) {
            /**
             * CX -> D
             */
            if (newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)) {
                isUpdateSuccessful = awbConn.updateAirwayBillToD(airwayBillTransaction.getAirwayBillNo(), airwayBillTransaction.getKodeLogistik(), uploadUsername, airwayBillTransaction.getTanggalActualPickup(), airwayBillTransaction.getRecipient(), airwayBillTransaction.getRelation(), airwayBillTransaction.getReceived());

                if (isUpdateSuccessful) {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to D successful");
                    /* Modify order item status when AWB Level is main */
                    if (airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_ORDER_MAIN)
                            || airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_RETUR_MAIN)) {

                        updateOrderItemStatus(venOrderItem, VeniceConstants.VEN_ORDER_STATUS_D);

                    }
                } else {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to D fail");
                    return false;
                }


            }

            /**
             * Airway bill is CX but order item is ES, update order item to CX
             */
            if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_ES) {

                /* Modify order item status when AWB Level is main */
                if (airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_ORDER_MAIN)
                        || airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_RETUR_MAIN)) {

                    if (updateOrderItemStatus(venOrderItem, VeniceConstants.VEN_ORDER_STATUS_CX)) {
//						salesRecordGenerator.createOrUpdateSalesRecord(venOrderItem);
                    }

                }

            }


            /**
             * Airway bill is CX but order item is PU, update order item to ES
             * then CX
             */
            if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_PU) {

                /* Modify order item status when AWB Level is main */
                if (airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_ORDER_MAIN)
                        || airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_RETUR_MAIN)) {

                    updateOrderItemStatus(venOrderItem, VeniceConstants.VEN_ORDER_STATUS_ES);
                    if (updateOrderItemStatus(venOrderItem, VeniceConstants.VEN_ORDER_STATUS_CX)) {
//						salesRecordGenerator.createOrUpdateSalesRecord(venOrderItem);
                    }

                }

            }

        }


        /**
         * Airway bill is D
         */
        if (existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)) {

            /**
             * D -> D will only update receiver, relation, and received
             */
            if (newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)) {
            }


            /**
             * Airway bill is D but order item is CX, update order item to D
             */
            if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_CX) {

                /* Modify order item status when AWB Level is main */
                if (airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_ORDER_MAIN)
                        || airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_RETUR_MAIN)) {

                    updateOrderItemStatus(venOrderItem, VeniceConstants.VEN_ORDER_STATUS_D);

                }

            }


            /**
             * Airway bill is D but order item is ES, update order item to CX
             * then D
             */
            if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_ES) {

                /* Modify order item status when AWB Level is main */
                if (airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_ORDER_MAIN)
                        || airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_RETUR_MAIN)) {

                    if (updateOrderItemStatus(venOrderItem, VeniceConstants.VEN_ORDER_STATUS_CX)) {
//						salesRecordGenerator.createOrUpdateSalesRecord(venOrderItem);
                    }
                    updateOrderItemStatus(venOrderItem, VeniceConstants.VEN_ORDER_STATUS_D);

                }

            }

        }

        return Boolean.TRUE;
    }

    private Boolean updateReturItemAndAirwayBillTransactionStatus(String uploadUsername,
            VenReturItem venReturItem,
            AirwayBillTransaction airwayBillTransaction,
            long existingOrderItemStatus,
            long newOrderItemStatus,
            String existingAirwayBillTransactionStatus,
            String newAirwayBillTransactionStatus,
            String airwayBillTransactionLevel,
            String airwayBillNoFromEngine,
            String airwayBillNoFromLogistic,
            List<FailedStatusUpdate> failedStatusUpdateList) {

        boolean isProcessSuccess = false;

        /**
         * When airway bill number from engine & logistics are the same, update
         * airway bill status
         */
        if (airwayBillNoFromEngine.equals(airwayBillNoFromLogistic)) {
            _log.debug("Airway Bill Engine & Logistic are matched");
            try {
                isProcessSuccess = updateReturItemAndAirwayBillTransactionStatus(uploadUsername, venReturItem, airwayBillTransaction, existingOrderItemStatus, newOrderItemStatus, existingAirwayBillTransactionStatus, newAirwayBillTransactionStatus, airwayBillTransactionLevel);
            } catch (Exception e) {
                _log.error("Problem updateOrderItemAndAirwayBillTransactionStatus ", e);
                isProcessSuccess = false;
            }
            /**
             * When airwaybill status update fail, add to fail report
             */
            /**
             * Airway bill number from engine & logistics are different
             */
        } else {
            _log.debug("Airway Bill Engine & Logistic are NOT matched");
            _log.debug("Airway Bill Engine : " + airwayBillNoFromEngine);
            _log.debug("Airway Bill Logistics : " + airwayBillNoFromLogistic);
            /**
             * When existing airway bill status is CX & D, airway bill number
             * override is not allowed. Add to fail report.
             */
            if (existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_SETTLED)
                    || existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)) {

                _log.debug("Airway Bill from engine " + airwayBillNoFromEngine + " status is CX or D, not allowed to override");

                isProcessSuccess = false;

            } else {

                /**
                 * Ask engine to override airway bill number
                 */
                isProcessSuccess = overrideAirwayBillNumber(airwayBillTransaction.getGdnRef(), airwayBillNoFromLogistic, uploadUsername);

                _log.debug("Airway Bill override result from engine " + isProcessSuccess);

                /**
                 * Update airwaybill status when override succesful
                 */
                if (isProcessSuccess) {
                    airwayBillTransaction.setAirwayBillNo(airwayBillNoFromLogistic);

                    try {
                        isProcessSuccess = updateReturItemAndAirwayBillTransactionStatus(uploadUsername, venReturItem, airwayBillTransaction, existingOrderItemStatus, newOrderItemStatus, existingAirwayBillTransactionStatus, newAirwayBillTransactionStatus, airwayBillTransactionLevel);
                    } catch (Exception e) {
                        _log.error("Problem updateReturItemAndAirwayBillTransactionStatus ", e);
                        isProcessSuccess = false;
                    }
                    /**
                     * When airwaybill status update fail, add to fail report
                     */
                    /**
                     * When airwaybill status override fail, add to fail report
                     */
                } else {
                    isProcessSuccess = false;
                }
            }
        }

        if (!isProcessSuccess) {
            FailedStatusUpdate failedStatusUpdate = new FailedStatusUpdate();

            failedStatusUpdate.setGdnRef(airwayBillTransaction.getGdnRef());
            failedStatusUpdate.setAirwayBillNoLogistic(airwayBillNoFromLogistic);
            failedStatusUpdate.setAirwayBillNoMTA(airwayBillNoFromEngine);

            if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_CX) {
                failedStatusUpdate.setOrderItemStatus("CX");
            }

            if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_D) {
                failedStatusUpdate.setOrderItemStatus("D");
            }

            if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_ES) {
                failedStatusUpdate.setOrderItemStatus("ES");
            }

            if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_PU) {
                failedStatusUpdate.setOrderItemStatus("PU");
            }

            failedStatusUpdate.setAirwayBillStatus(existingAirwayBillTransactionStatus);

            failedStatusUpdateList.add(failedStatusUpdate);

            return false;
        } else {
            return true;
        }
    }

    private Boolean updateReturItemAndAirwayBillTransactionStatus(String uploadUsername, VenReturItem venReturItem, AirwayBillTransaction airwayBillTransaction, long existingOrderItemStatus, long newOrderItemStatus, String existingAirwayBillTransactionStatus, String newAirwayBillTransactionStatus, String airwayBillTransactionLevel) throws Exception {

        _log.debug("Retur Item : " + venReturItem.getWcsReturItemId());
        _log.debug("Existing Retur Item Status : " + existingOrderItemStatus);
        _log.debug("New Retur Item Status : " + newOrderItemStatus);
        _log.debug("Airway Bill Number : " + airwayBillTransaction.getAirwayBillNo());
        _log.debug("Existing Airway Bill Transaction Status : " + existingAirwayBillTransactionStatus);
        _log.debug("New Airway Bill Transaction Status : " + newAirwayBillTransactionStatus);
        _log.debug("Airway Bill Transaction Level : " + airwayBillTransactionLevel);

        boolean isUpdateSuccessful = false;

        /**
         * Airway bill is PU
         */
        if (existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_PICK_UP)) {
            /**
             * PU -> CX will put ES in the middle : PU -> ES -> CX
             */
            if (newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_SETTLED)) {

                isUpdateSuccessful = awbConn.updateAirwayBillToES(airwayBillTransaction.getAirwayBillNo(), airwayBillTransaction.getKodeLogistik(), uploadUsername);

                if (isUpdateSuccessful) {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to ES successful");
                    /* Modify retur item status when AWB Level is main */
                    if (airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_ORDER_MAIN)
                            || airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_RETUR_MAIN)) {

                        updateReturItemStatus(venReturItem, VeniceConstants.VEN_ORDER_STATUS_ES);
                    }
                } else {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to ES fail");
                    return false;
                }

                isUpdateSuccessful = awbConn.updateAirwayBillToCX(airwayBillTransaction.getAirwayBillNo(), airwayBillTransaction.getKodeLogistik(), uploadUsername, airwayBillTransaction.getTanggalActualPickup(), airwayBillTransaction.getRecipient(), airwayBillTransaction.getRelation(), airwayBillTransaction.getReceived());

                if (isUpdateSuccessful) {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to CX successful");
                    /* Modify retur item status when AWB Level is main */
                    if (airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_ORDER_MAIN)
                            || airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_RETUR_MAIN)) {

                        updateReturItemStatus(venReturItem, VeniceConstants.VEN_ORDER_STATUS_CX);
                    }

                } else {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to CX fail");
                    return false;
                }
            }
        }

        /**
         * Airway bill is ES
         */
        if (existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_EMAIL_SENT)) {

            /**
             * ES -> CX
             */
            if (newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_SETTLED)) {

                isUpdateSuccessful = awbConn.updateAirwayBillToCX(airwayBillTransaction.getAirwayBillNo(), airwayBillTransaction.getKodeLogistik(), uploadUsername, airwayBillTransaction.getTanggalActualPickup(), airwayBillTransaction.getRecipient(), airwayBillTransaction.getRelation(), airwayBillTransaction.getReceived());

                if (isUpdateSuccessful) {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to CX successful");
                    /* Modify retur item status when AWB Level is main */
                    if (airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_ORDER_MAIN)
                            || airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_RETUR_MAIN)) {

                        updateReturItemStatus(venReturItem, VeniceConstants.VEN_ORDER_STATUS_CX);
                    }
                } else {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to CX fail");
                    return false;
                }
            }

            /**
             * ES -> D will put CX in the middle : ES -> CX -> D
             */
            if (newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)) {

                isUpdateSuccessful = awbConn.updateAirwayBillToCX(airwayBillTransaction.getAirwayBillNo(), airwayBillTransaction.getKodeLogistik(), uploadUsername, airwayBillTransaction.getTanggalActualPickup(), airwayBillTransaction.getRecipient(), airwayBillTransaction.getRelation(), airwayBillTransaction.getReceived());

                if (isUpdateSuccessful) {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to CX successful");
                    /* Modify retur item status when AWB Level is main */
                    if (airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_ORDER_MAIN)
                            || airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_RETUR_MAIN)) {

                        updateReturItemStatus(venReturItem, VeniceConstants.VEN_ORDER_STATUS_CX);
                    }
                } else {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to CX fail");
                    return false;
                }

                isUpdateSuccessful = awbConn.updateAirwayBillToD(airwayBillTransaction.getAirwayBillNo(), airwayBillTransaction.getKodeLogistik(), uploadUsername, airwayBillTransaction.getTanggalActualPickup(), airwayBillTransaction.getRecipient(), airwayBillTransaction.getRelation(), airwayBillTransaction.getReceived());

                if (isUpdateSuccessful) {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to D successful");
                    /* Modify retur item status when AWB Level is main */
                    if (airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_ORDER_MAIN)
                            || airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_RETUR_MAIN)) {

                        updateReturItemStatus(venReturItem, VeniceConstants.VEN_ORDER_STATUS_D);
                    }
                } else {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to D fail");
                    return false;
                }
            }
        }

        /**
         * Airway bill is CX
         */
        if (existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_SETTLED)) {
            /**
             * CX -> D
             */
            if (newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)) {
                isUpdateSuccessful = awbConn.updateAirwayBillToD(airwayBillTransaction.getAirwayBillNo(), airwayBillTransaction.getKodeLogistik(), uploadUsername, airwayBillTransaction.getTanggalActualPickup(), airwayBillTransaction.getRecipient(), airwayBillTransaction.getRelation(), airwayBillTransaction.getReceived());

                if (isUpdateSuccessful) {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to D successful");
                    /* Modify retur item status when AWB Level is main */
                    if (airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_ORDER_MAIN)
                            || airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_RETUR_MAIN)) {

                        updateReturItemStatus(venReturItem, VeniceConstants.VEN_ORDER_STATUS_D);
                    }
                } else {
                    _log.info("Update AWB " + airwayBillTransaction.getAirwayBillNo() + " to D fail");
                    return false;
                }
            }

            /**
             * Airway bill is CX but retur item is ES, update retur item to CX
             */
            if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_ES) {

                /* Modify retur item status when AWB Level is main */
                if (airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_ORDER_MAIN)
                        || airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_RETUR_MAIN)) {

                    updateReturItemStatus(venReturItem, VeniceConstants.VEN_ORDER_STATUS_CX);
                }
            }

            /**
             * Airway bill is CX but retur item is PU, update retur item to ES
             * then CX
             */
            if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_PU) {

                /* Modify retur item status when AWB Level is main */
                if (airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_ORDER_MAIN)
                        || airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_RETUR_MAIN)) {

                    updateReturItemStatus(venReturItem, VeniceConstants.VEN_ORDER_STATUS_ES);
                    updateReturItemStatus(venReturItem, VeniceConstants.VEN_ORDER_STATUS_CX);
                }
            }
        }

        /**
         * Airway bill is D
         */
        if (existingAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)) {

            /**
             * D -> D will only update receiver, relation, and received
             */
            if (newAirwayBillTransactionStatus.equals(AirwayBillTransaction.STATUS_CLOSED)) {
            }


            /**
             * Airway bill is D but retur item is CX, update retur item to D
             */
            if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_CX) {

                /* Modify retur item status when AWB Level is main */
                if (airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_ORDER_MAIN)
                        || airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_RETUR_MAIN)) {

                    updateReturItemStatus(venReturItem, VeniceConstants.VEN_ORDER_STATUS_D);
                }
            }

            /**
             * Airway bill is D but retur item is ES, update returretur item to
             * CX then D
             */
            if (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_ES) {

                /* Modify retur item status when AWB Level is main */
                if (airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_ORDER_MAIN)
                        || airwayBillTransactionLevel.equals(AirwayBillTransaction.LEVEL_RETUR_MAIN)) {

                    updateReturItemStatus(venReturItem, VeniceConstants.VEN_ORDER_STATUS_CX);
                    updateReturItemStatus(venReturItem, VeniceConstants.VEN_ORDER_STATUS_D);

                }
            }
        }

        return Boolean.TRUE;
    }
//    private Boolean updateOrderItemStatus(String wcsOrderItemId, Connection conn) throws EJBException {
//		if(wcsOrderItemId == null || wcsOrderItemId.isEmpty()){
//			return Boolean.TRUE;
//		}
//		
//		Locator<Object> locator = null;
//		try {
//			locator = new Locator<Object>();
//		
//			VenOrderItemSessionEJBLocal venOrderItemHome = (VenOrderItemSessionEJBLocal) locator
//					.lookupLocal(VenOrderItemSessionEJBLocal.class, "VenOrderItemSessionEJBBeanLocal");
//			
//			String orderItemSQL = "select * from ven_order_item where wcs_order_item_id = ?";
//			PreparedStatement psOrderItem = conn.prepareStatement(orderItemSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//			psOrderItem.setString(1, wcsOrderItemId);
//			
//			ResultSet rsOrderItem = psOrderItem.executeQuery();
//			
//			rsOrderItem.last();
//			int totalOrderItem = rsOrderItem.getRow();
//			rsOrderItem.beforeFirst();
//			
//			if(totalOrderItem == 0){
//				return Boolean.TRUE;
//			}
//		
//			rsOrderItem.next();
//			
//			VenOrderItem venOrderItem = new VenOrderItem();
//			
//			venOrderItem.setEtd(rsOrderItem.getInt("etd"));
//			venOrderItem.setGiftCardFlag(rsOrderItem.getBoolean("gift_card_flag"));
//			venOrderItem.setGiftCardNote(rsOrderItem.getString("gift_card_note"));
//			venOrderItem.setGiftWrapPrice(rsOrderItem.getBigDecimal("gift_wrap_price"));
//			venOrderItem.setGiftWrapFlag(rsOrderItem.getBoolean("gift_wrap_flag"));
//			venOrderItem.setInsuranceCost(rsOrderItem.getBigDecimal("insurance_cost"));
//			venOrderItem.setMaxEstDate(rsOrderItem.getTimestamp("max_est_date"));
//			venOrderItem.setMerchantSettlementFlag(rsOrderItem.getBoolean("merchant_settlement_flag"));
//			venOrderItem.setMinEstDate(rsOrderItem.getTimestamp("min_est_date"));
//			venOrderItem.setOrderItemId(rsOrderItem.getLong("order_item_id"));
//			venOrderItem.setPackageCount(rsOrderItem.getInt("package_count"));
//			venOrderItem.setPrice(rsOrderItem.getBigDecimal("price"));
//			venOrderItem.setQuantity(rsOrderItem.getInt("quantity"));
//			venOrderItem.setSaltCode(rsOrderItem.getString("salt_code"));
//			venOrderItem.setShippingCost(rsOrderItem.getBigDecimal("shipping_cost"));
//			venOrderItem.setShippingWeight(rsOrderItem.getBigDecimal("shipping_weight"));
//			venOrderItem.setSpecialHandlingInstructions(rsOrderItem.getString("special_handling_instructions"));
//			venOrderItem.setTotal(rsOrderItem.getBigDecimal("total"));
//			venOrderItem.setWcsOrderItemId(rsOrderItem.getString("wcs_order_item_id"));
//			venOrderItem.setLogisticsPricePerKg(rsOrderItem.getBigDecimal("logistics_price_per_kg"));
//			
//			VenAddress shippingAddress = new VenAddress();
//			shippingAddress.setAddressId(rsOrderItem.getLong("shipping_address_id"));
//			venOrderItem.setVenAddress(shippingAddress);
//			
//			VenMerchantProduct product = new VenMerchantProduct();
//			product.setProductId(rsOrderItem.getLong("product_id"));
//			venOrderItem.setVenMerchantProduct(product);
//			
//			VenOrder order = new VenOrder();
//			order.setOrderId(rsOrderItem.getLong("order_id"));
//			venOrderItem.setVenOrder(order);
//			
//			VenOrderStatus orderStatus = new VenOrderStatus();
//			orderStatus.setOrderStatusId(rsOrderItem.getLong("order_status_id"));
//			venOrderItem.setVenOrderStatus(orderStatus);
//			
//			VenRecipient venRecipient = new VenRecipient();
//			venRecipient.setRecipientId(rsOrderItem.getLong("recipient_id"));
//			venOrderItem.setVenRecipient(venRecipient);
//			
//			LogLogisticService logisticService = new LogLogisticService();
//			logisticService.setLogisticsServiceId(rsOrderItem.getLong("logistics_service_id"));
//			venOrderItem.setLogLogisticService(logisticService);
//			
//			rsOrderItem.close();
//			psOrderItem.close();
//		
//			String airwayBillSQL = "select status from log_airway_bill where distribution_cart_id = ?";
//			PreparedStatement psAirwayBill = conn.prepareStatement(airwayBillSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//			
//			String distributionCartSQL = "select distribution_cart_id from ven_distribution_cart where order_item_id = ?";
//			PreparedStatement psDistributionCart = conn.prepareStatement(distributionCartSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//			psDistributionCart.setLong(1, venOrderItem.getOrderItemId());
//			
//			ResultSet rsDistributionCart = psDistributionCart.executeQuery();
//			
//			while (rsDistributionCart.next()) {
//				
//				psAirwayBill.setLong(1, rsDistributionCart.getLong("distribution_cart_id"));
//				ResultSet rsAirwayBill = psAirwayBill.executeQuery();
//				
//				rsAirwayBill.last();
//				int totalAirwayBill = rsAirwayBill.getRow();
//				rsAirwayBill.beforeFirst();
//				
//				Boolean allD = true;
//				Boolean allCX = true;
//				Boolean allRT = true;
//				
//				while (rsAirwayBill.next()) {
//					if (rsAirwayBill.getString("status") == null || !rsAirwayBill.getString("status").equalsIgnoreCase(VeniceConstants.LOG_LOGISTICS_STATUS_CODE_POD)) {
//						allD = false;
//					}
//					if (rsAirwayBill.getString("status") == null || !rsAirwayBill.getString("status").equalsIgnoreCase(VeniceConstants.LOG_LOGISTICS_STATUS_CODE_MDE)) {
//						allCX = false;
//					}
//					if (rsAirwayBill.getString("status") == null || !rsAirwayBill.getString("status").equalsIgnoreCase(VeniceConstants.LOG_LOGISTICS_STATUS_CODE_DEX_14)) {
//						allRT = false;
//					}		
//				}
//				
//				rsAirwayBill.close();
//				rsAirwayBill = null;
//				
//				// Make sure that there are actually airway bills then update the status accordingly
//				if (totalAirwayBill > 0) {
//					if (allD) {
//						// Set the status to D and merge
//						VenOrderStatus venOrderStatus = new VenOrderStatus();
//						venOrderStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_D);
//						venOrderItem.setVenOrderStatus(venOrderStatus);
//						venOrderItemHome.mergeVenOrderItem(venOrderItem);
//					}
//					if (allCX) {
//						// Set the status to CX and merge
//						VenOrderStatus venOrderStatus = new VenOrderStatus();
//						venOrderStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_CX);
//						venOrderItem.setVenOrderStatus(venOrderStatus);
//						venOrderItemHome.mergeVenOrderItem(venOrderItem);
//					}
//					if (allRT) {
//						// Set the status to RT and merge
//						VenOrderStatus venOrderStatus = new VenOrderStatus();
//						venOrderStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_RT);
//						venOrderItem.setVenOrderStatus(venOrderStatus);
//						venOrderItemHome.mergeVenOrderItem(venOrderItem);
//					}
//				}
//				
//			}
//			
//			rsDistributionCart.close();
//			psDistributionCart.close();
//			
//		} catch (Exception e) {
//			String errMsg = "An exception occured when performing update of the order item status based on the activity report upload:" + wcsOrderItemId;
//			_log.error(errMsg + e.getMessage());
//			e.printStackTrace();
//			throw new EJBException(errMsg);
//		} finally{
//			try{
//				if(locator!=null){
//					locator.close();
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//		return Boolean.TRUE;
//	}
}
