package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the fin_ar_funds_in_recon_comments database table.
 * 
 */
@Entity
@Table(name="fin_ar_funds_in_recon_comments")
public class FinArFundsInReconComment implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FinArFundsInReconCommentPK id;

	@Column(nullable=false, length=1000)
	private String comment;

	@Column(name="user_logon_name", nullable=false, length=100)
	private String userLogonName;

	//bi-directional many-to-one association to FinArFundsInReconRecord
    @ManyToOne
	@JoinColumn(name="reconciliation_record_id", nullable=false, insertable=false, updatable=false)
	private FinArFundsInReconRecord finArFundsInReconRecord;

    public FinArFundsInReconComment() {
    }

	public FinArFundsInReconCommentPK getId() {
		return this.id;
	}

	public void setId(FinArFundsInReconCommentPK id) {
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

	public FinArFundsInReconRecord getFinArFundsInReconRecord() {
		return this.finArFundsInReconRecord;
	}

	public void setFinArFundsInReconRecord(FinArFundsInReconRecord finArFundsInReconRecord) {
		this.finArFundsInReconRecord = finArFundsInReconRecord;
	}
	
}