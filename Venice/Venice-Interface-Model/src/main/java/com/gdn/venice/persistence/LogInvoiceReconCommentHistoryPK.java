package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the log_invoice_recon_comment_history database table.
 * 
 */
@Embeddable
public class LogInvoiceReconCommentHistoryPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="invoice_recon_record_id", unique=true, nullable=false)
	private Long invoiceReconRecordId;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="history_timestamp", unique=true, nullable=false)
	private java.util.Date historyTimestamp;

    public LogInvoiceReconCommentHistoryPK() {
    }
	public Long getInvoiceReconRecordId() {
		return this.invoiceReconRecordId;
	}
	public void setInvoiceReconRecordId(Long invoiceReconRecordId) {
		this.invoiceReconRecordId = invoiceReconRecordId;
	}
	public java.util.Date getHistoryTimestamp() {
		return this.historyTimestamp;
	}
	public void setHistoryTimestamp(java.util.Date historyTimestamp) {
		this.historyTimestamp = historyTimestamp;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof LogInvoiceReconCommentHistoryPK)) {
			return false;
		}
		LogInvoiceReconCommentHistoryPK castOther = (LogInvoiceReconCommentHistoryPK)other;
		return 
			this.invoiceReconRecordId.equals(castOther.invoiceReconRecordId)
			&& this.historyTimestamp.equals(castOther.historyTimestamp);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.invoiceReconRecordId.hashCode();
		hash = hash * prime + this.historyTimestamp.hashCode();
		
		return hash;
    }
}