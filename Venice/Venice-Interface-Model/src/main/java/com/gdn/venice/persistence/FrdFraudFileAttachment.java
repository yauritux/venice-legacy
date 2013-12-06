package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_fraud_file_attachment database table.
 * 
 */
@Entity
@Table(name="frd_fraud_file_attachment")
public class FrdFraudFileAttachment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_fraud_file_attachment")  
	@TableGenerator(name="frd_fraud_file_attachment", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="fraud_file_attachment_id", unique=true, nullable=false)
	private Long fraudFileAttachmentId;

	@Column(name="created_by", nullable=false, length=50)
	private String createdBy;

	@Column(name="file_location", nullable=false, length=1000)
	private String fileLocation;

	@Column(name="file_name", nullable=false, length=1000)
	private String fileName;

	@Column(name="fraud_file_attachment_description", length=1000)
	private String fraudFileAttachmentDescription;

	//bi-directional many-to-one association to FrdFraudSuspicionCase
    @ManyToOne
	@JoinColumn(name="fraud_suspicion_case_id", nullable=false)
	private FrdFraudSuspicionCase frdFraudSuspicionCase;

    public FrdFraudFileAttachment() {
    }

	public Long getFraudFileAttachmentId() {
		return this.fraudFileAttachmentId;
	}

	public void setFraudFileAttachmentId(Long fraudFileAttachmentId) {
		this.fraudFileAttachmentId = fraudFileAttachmentId;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getFileLocation() {
		return this.fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFraudFileAttachmentDescription() {
		return this.fraudFileAttachmentDescription;
	}

	public void setFraudFileAttachmentDescription(String fraudFileAttachmentDescription) {
		this.fraudFileAttachmentDescription = fraudFileAttachmentDescription;
	}

	public FrdFraudSuspicionCase getFrdFraudSuspicionCase() {
		return this.frdFraudSuspicionCase;
	}

	public void setFrdFraudSuspicionCase(FrdFraudSuspicionCase frdFraudSuspicionCase) {
		this.frdFraudSuspicionCase = frdFraudSuspicionCase;
	}
	
}