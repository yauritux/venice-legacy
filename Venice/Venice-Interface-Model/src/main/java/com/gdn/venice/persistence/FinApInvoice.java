package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the fin_ap_invoice database table.
 * 
 */
@Entity
@Table(name="fin_ap_invoice")
public class FinApInvoice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_ap_invoice")  
	@TableGenerator(name="fin_ap_invoice", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="ap_invoice_id", unique=true, nullable=false)
	private Long apInvoiceId;

	@Column(name="ar_amount", precision=20, scale=2)
	private BigDecimal arAmount;

	@Column(name="invoice_amount", nullable=false, precision=20, scale=2)
	private BigDecimal invoiceAmount;

    @Temporal( TemporalType.DATE)
	@Column(name="invoice_date", nullable=false)
	private Date invoiceDate;

    @Temporal( TemporalType.DATE)
	@Column(name="invoice_due_date", nullable=false)
	private Date invoiceDueDate;

	@Column(name="invoice_tax_amount", nullable=false, precision=20, scale=2)
	private BigDecimal invoiceTaxAmount;

	@Column(name="invoice_timestamp", nullable=false)
	private Timestamp invoiceTimestamp;

	//bi-directional many-to-one association to FinApPayment
    @ManyToOne
	@JoinColumn(name="ap_payment_id")
	private FinApPayment finApPayment;

	//bi-directional many-to-one association to VenParty
    @ManyToOne
	@JoinColumn(name="party_id", nullable=false)
	private VenParty venParty;

	//bi-directional many-to-many association to FinJournalTransaction
	@ManyToMany(mappedBy="finApInvoices")//, fetch=FetchType.EAGER)
	private List<FinJournalTransaction> finJournalTransactions;

	//bi-directional many-to-one association to LogInvoiceReportUpload
	@OneToMany(mappedBy="finApInvoice")
	private List<LogInvoiceReportUpload> logInvoiceReportUploads;

    public FinApInvoice() {
    }

	public Long getApInvoiceId() {
		return this.apInvoiceId;
	}

	public void setApInvoiceId(Long apInvoiceId) {
		this.apInvoiceId = apInvoiceId;
	}

	public BigDecimal getArAmount() {
		return this.arAmount;
	}

	public void setArAmount(BigDecimal arAmount) {
		this.arAmount = arAmount;
	}

	public BigDecimal getInvoiceAmount() {
		return this.invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public Date getInvoiceDate() {
		return this.invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Date getInvoiceDueDate() {
		return this.invoiceDueDate;
	}

	public void setInvoiceDueDate(Date invoiceDueDate) {
		this.invoiceDueDate = invoiceDueDate;
	}

	public BigDecimal getInvoiceTaxAmount() {
		return this.invoiceTaxAmount;
	}

	public void setInvoiceTaxAmount(BigDecimal invoiceTaxAmount) {
		this.invoiceTaxAmount = invoiceTaxAmount;
	}

	public Timestamp getInvoiceTimestamp() {
		return this.invoiceTimestamp;
	}

	public void setInvoiceTimestamp(Timestamp invoiceTimestamp) {
		this.invoiceTimestamp = invoiceTimestamp;
	}

	public FinApPayment getFinApPayment() {
		return this.finApPayment;
	}

	public void setFinApPayment(FinApPayment finApPayment) {
		this.finApPayment = finApPayment;
	}
	
	public VenParty getVenParty() {
		return this.venParty;
	}

	public void setVenParty(VenParty venParty) {
		this.venParty = venParty;
	}
	
	public List<FinJournalTransaction> getFinJournalTransactions() {
		return this.finJournalTransactions;
	}

	public void setFinJournalTransactions(List<FinJournalTransaction> finJournalTransactions) {
		this.finJournalTransactions = finJournalTransactions;
	}
	
	public List<LogInvoiceReportUpload> getLogInvoiceReportUploads() {
		return this.logInvoiceReportUploads;
	}

	public void setLogInvoiceReportUploads(List<LogInvoiceReportUpload> logInvoiceReportUploads) {
		this.logInvoiceReportUploads = logInvoiceReportUploads;
	}
	
}