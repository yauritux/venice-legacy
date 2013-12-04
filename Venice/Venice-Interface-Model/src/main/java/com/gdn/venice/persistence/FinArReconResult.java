package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the fin_ar_recon_result database table.
 * 
 */
@Entity
@Table(name="fin_ar_recon_result")
public class FinArReconResult implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_ar_recon_result")  
	@TableGenerator(name="fin_ar_recon_result", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="recon_result_id", unique=true, nullable=false)
	private Long reconResultId;

	@Column(name="recon_result_desc", nullable=false, length=100)
	private String reconResultDesc;

	//bi-directional many-to-one association to FinArFundsInReconRecord
	@OneToMany(mappedBy="finArReconResult")
	private List<FinArFundsInReconRecord> finArFundsInReconRecords;

    public FinArReconResult() {
    }

	public Long getReconResultId() {
		return this.reconResultId;
	}

	public void setReconResultId(Long reconResultId) {
		this.reconResultId = reconResultId;
	}

	public String getReconResultDesc() {
		return this.reconResultDesc;
	}

	public void setReconResultDesc(String reconResultDesc) {
		this.reconResultDesc = reconResultDesc;
	}

	public List<FinArFundsInReconRecord> getFinArFundsInReconRecords() {
		return this.finArFundsInReconRecords;
	}

	public void setFinArFundsInReconRecords(List<FinArFundsInReconRecord> finArFundsInReconRecords) {
		this.finArFundsInReconRecords = finArFundsInReconRecords;
	}
	
}