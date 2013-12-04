package com.gdn.venice.client.app.task;

public class ProcessNameTokens {
	
	public static final String AIRWAYBILLID = "airwayBillId";
	public static final String INVOICERENUMBER = "invoiceNumber";
	public static final String FUNDINRECONRECORDID = "fundInReconRecordId";
	public static final String APPAYMENTID = "apPaymentId";
	public static final String JOURNALGROUPID = "journalGroupId";
	
	
	public static final String SUBMITTEDBY = "submittedBy";
	
	public static final String ORDERID = "orderId";
	public static final String WCSORDERID = "wcsOrderId";
		
	public static final String LOGISTICSACTIVITYREPORTAPPROVAL = "Logistics Activity Report Approval";
	public static final String LOGISTICSACTIVITYREPORTAPPROVAL_APPROVALACTIVITYNAME = "Review and Approve Activity Report";
	public static final String LOGISTICSACTIVITYREPORTAPPROVAL_NOTIFICATIONACTIVITYNAME = "Notified of Activity Report Approval Result";
	
	public static final String LOGISTICSINVOICEAPPROVAL = "Logistics Invoice Approval";
	public static final String LOGISTICSINVOICEREPORTAPPROVAL_APPROVALACTIVITYNAME = "Review and Approve Invoice";
	public static final String LOGISTICSINVOICEREPORTAPPROVAL_NOTIFICATIONACTIVITYNAME = "Notified of Invoice Approval Result";
	
	public static final String LOGISTICSPICKUPPROBLEMINVESTIGATION = "Logistics Pickup Problem Investigation";
	
	public static final String LOGISTICSMTADATAACTIVITYRECONCILIATION = "Logistics MTA Data Activity Reconciliation";	
	public static final String LOGISTICSMTADATAINVOICERECONCILIATION = "Logistics MTA Data Invoice Reconciliation";
	
	public static final String FINANCEFUNDINRECONCILIATIONRESULTAPPROVAL = "Finance Fund In Reconciliation Result Approval";
	public static final String FINANCEFUNDINRECONCILIATIONRESULTAPPROVAL_APPROVALACTIVITYNAME = "Review and Approve Fund In Reconciliation Result";
	public static final String FINANCEFUNDINRECONCILIATIONRESULTAPPROVAL_ACKNOWLEDGEMENTACTIVITYNAME = "Acknowledge Fund In Reconciliation Approval Result";
	public static final String FINANCEORDERITEMCANCELLEDNOTIFICATION = "Finance Order Item Cancelled Notification";
	
	public static final String FRAUDMANAGEMENTPROCESS = "Fraud Management Process";
	public static final String FRAUDCASEID = "fraudCaseId";
	public static final String FRAUDSTATUSID = "fraudStatusId";
	
	public static final String FINANCEAPPAYMENTRESULTAPPROVAL = "Finance AP Payment Approval Process";
	public static final String FINANCEAPPAYMENTAPPROVAL_APPROVALACTIVITYNAME = "Review and Approve/Reject AP Payments";
	public static final String FINANCEAPPAYMENTAPPROVAL_ACKNOWLEDGEMENTACTIVITYNAME = "Acknowledge AP Payment Approval Result";
	
	public static final String FINANCEJOURNALRESULTAPPROVAL = "Finance Journal Approval Process";
	public static final String FINANCEJOURNALAPPROVAL_APPROVALACTIVITYNAME = "Approve or Reject Journal";
	public static final String FINANCEJOURNALAPPROVAL_REVISEJOURNALACTIVITYNAME = "Revise Journal with Manual Journal";

	public static final String FINANCECASHRECEIVEANDSALESJOURNALRECONCILIATION = "Finance Cash Receive and Sales Journal Reconciliation";
	
	public static final Long TASK_TYPE_UNKNOWN = new Long(0);
	public static final Long TASK_TYPE_LOGISTICS = new Long(1);
	public static final Long TASK_TYPE_FINANCE = new Long(2);
	public static final Long TASK_TYPE_KPI = new Long(3);
	public static final Long TASK_TYPE_FRAUD = new Long(4);
}
