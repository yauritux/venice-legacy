package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Time;
import java.util.List;


/**
 * The persistent class for the log_pickup_schedules database table.
 * 
 */
@Entity
@Table(name="log_pickup_schedules")
public class LogPickupSchedule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="log_pickup_schedules")  
	@TableGenerator(name="log_pickup_schedules", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="pickup_schedules_id", unique=true, nullable=false)
	private Long pickupSchedulesId;

	@Column(name="from_time", nullable=false)
	private Time fromTime;

	@Column(name="include_public_holidays", nullable=false)
	private Boolean includePublicHolidays;

	@Column(name="pickup_schedule_desc", nullable=false, length=100)
	private String pickupScheduleDesc;

	@Column(name="to_time", nullable=false)
	private Time toTime;

	//bi-directional many-to-one association to LogLogisticsProvider
    @ManyToOne
	@JoinColumn(name="logistics_provider_id", nullable=false)
	private LogLogisticsProvider logLogisticsProvider;

	//bi-directional many-to-many association to LogScheduleDayOfWeek
	@ManyToMany(mappedBy="logPickupSchedules")//, fetch=FetchType.EAGER)
	private List<LogScheduleDayOfWeek> logScheduleDayOfWeeks;

    public LogPickupSchedule() {
    }

	public Long getPickupSchedulesId() {
		return this.pickupSchedulesId;
	}

	public void setPickupSchedulesId(Long pickupSchedulesId) {
		this.pickupSchedulesId = pickupSchedulesId;
	}

	public Time getFromTime() {
		return this.fromTime;
	}

	public void setFromTime(Time fromTime) {
		this.fromTime = fromTime;
	}

	public Boolean getIncludePublicHolidays() {
		return this.includePublicHolidays;
	}

	public void setIncludePublicHolidays(Boolean includePublicHolidays) {
		this.includePublicHolidays = includePublicHolidays;
	}

	public String getPickupScheduleDesc() {
		return this.pickupScheduleDesc;
	}

	public void setPickupScheduleDesc(String pickupScheduleDesc) {
		this.pickupScheduleDesc = pickupScheduleDesc;
	}

	public Time getToTime() {
		return this.toTime;
	}

	public void setToTime(Time toTime) {
		this.toTime = toTime;
	}

	public LogLogisticsProvider getLogLogisticsProvider() {
		return this.logLogisticsProvider;
	}

	public void setLogLogisticsProvider(LogLogisticsProvider logLogisticsProvider) {
		this.logLogisticsProvider = logLogisticsProvider;
	}
	
	public List<LogScheduleDayOfWeek> getLogScheduleDayOfWeeks() {
		return this.logScheduleDayOfWeeks;
	}

	public void setLogScheduleDayOfWeeks(List<LogScheduleDayOfWeek> logScheduleDayOfWeeks) {
		this.logScheduleDayOfWeeks = logScheduleDayOfWeeks;
	}
	
}