package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the log_pickup_report_cell database table.
 * 
 */
@Embeddable
public class LogPickupReportCellPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="provider_instructions_report_id", unique=true, nullable=false)
	private Long providerInstructionsReportId;

	@Column(name="pickup_report_column_id", unique=true, nullable=false)
	private Long pickupReportColumnId;

    public LogPickupReportCellPK() {
    }
	public Long getProviderInstructionsReportId() {
		return this.providerInstructionsReportId;
	}
	public void setProviderInstructionsReportId(Long providerInstructionsReportId) {
		this.providerInstructionsReportId = providerInstructionsReportId;
	}
	public Long getPickupReportColumnId() {
		return this.pickupReportColumnId;
	}
	public void setPickupReportColumnId(Long pickupReportColumnId) {
		this.pickupReportColumnId = pickupReportColumnId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof LogPickupReportCellPK)) {
			return false;
		}
		LogPickupReportCellPK castOther = (LogPickupReportCellPK)other;
		return 
			this.providerInstructionsReportId.equals(castOther.providerInstructionsReportId)
			&& this.pickupReportColumnId.equals(castOther.pickupReportColumnId);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.providerInstructionsReportId.hashCode();
		hash = hash * prime + this.pickupReportColumnId.hashCode();
		
		return hash;
    }
}