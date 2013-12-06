package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the log_invoice_recon_comment_history database table.
 * 
 */
@Entity
@Table(name="log_invoice_recon_comment_history")
public class LogInvoiceReconCommentHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private LogInvoiceReconCommentHistoryPK id;

	@Column(nullable=false, length=1000)
	private String comment;

	@Column(name="user_logon_name", nullable=false, length=100)
	private String userLogonName;

	//bi-directional many-to-one association to LogInvoiceReconRecord
    @ManyToOne
	@JoinColumn(name="invoice_recon_record_id", nullable=false, insertable=false, updatable=false)
	private LogInvoiceReconRecord logInvoiceReconRecord;

    public LogInvoiceReconCommentHistory() {
    }

	public LogInvoiceReconCommentHistoryPK getId() {
		return this.id;
	}

	public void setId(LogInvoiceReconCommentHistoryPK id) {
		this.id = id;
	}
	
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUserLogonName() {
		return this.userLogonName;
	}

	public void setUserLogonName(String userLogonName) {
		this.userLogonName = userLogonName;
	}

	public LogInvoiceReconRecord getLogInvoiceReconRecord() {
		return this.logInvoiceReconRecord;
	}

	public void setLogInvoiceReconRecord(LogInvoiceReconRecord logInvoiceReconRecord) {
		this.logInvoiceReconRecord = logInvoiceReconRecord;
	}
	
}