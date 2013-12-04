package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the ven_holiday database table.
 * 
 */
@Entity
@Table(name="ven_holiday")
public class VenHoliday implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_holiday")  
	@TableGenerator(name="ven_holiday", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="holiday_id", unique=true, nullable=false)
	private Long holidayId;

    @Temporal( TemporalType.DATE)
	@Column(name="holiday_date", nullable=false)
	private Date holidayDate;

    public VenHoliday() {
    }

	public Long getHolidayId() {
		return this.holidayId;
	}

	public void setHolidayId(Long holidayId) {
		this.holidayId = holidayId;
	}

	public Date getHolidayDate() {
		return this.holidayDate;
	}

	public void setHolidayDate(Date holidayDate) {
		this.holidayDate = holidayDate;
	}

}