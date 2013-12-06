package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the log_activity_recon_comment_history database table.
 * 
 */
@Entity
@Table(name="log_activity_recon_comment_history")
public class LogActivityReconCommentHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private LogActivityReconCommentHistoryPK id;

	@Column(nullable=false, length=1000)
	private String comment;

	@Column(name="user_logon_name", nullable=false, length=100)
	private String userLogonName;

	//bi-directional many-to-one association to LogActivityReconRecord
    @ManyToOne
	@JoinColumn(name="activity_recon_record_id", nullable=false, insertable=false, updatable=false)
	private LogActivityReconRecord logActivityReconRecord;

    public LogActivityReconCommentHistory() {
    }

	public LogActivityReconCommentHistoryPK getId() {
		return this.id;
	}

	public void setId(LogActivityReconCommentHistoryPK id) {
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

	public LogActivityReconRecord getLogActivityReconRecord() {
		return this.logActivityReconRecord;
	}

	public void setLogActivityReconRecord(LogActivityReconRecord logActivityReconRecord) {
		this.logActivityReconRecord = logActivityReconRecord;
	}
	
}