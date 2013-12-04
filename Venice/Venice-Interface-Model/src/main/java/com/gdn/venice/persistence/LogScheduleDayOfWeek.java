package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the log_schedule_day_of_week database table.
 * 
 */
@Entity
@Table(name="log_schedule_day_of_week")
public class LogScheduleDayOfWeek implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="log_schedule_day_of_week")  
	@TableGenerator(name="log_schedule_day_of_week", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="log_schedule_day_of_week_seq")
	//@SequenceGenerator(name="log_schedule_day_of_week_seq", sequenceName="log_schedule_day_of_week_seq")//,allocationSize=1)
@Column(name="schedule_day_id", unique=true, nullable=false)
	private Long scheduleDayId;

	@Column(name="schedule_day_desc", nullable=false, length=100)
	private String scheduleDayDesc;

	//bi-directional many-to-many association to LogPickupSchedule
    @ManyToMany//(fetch=FetchType.EAGER)
	@JoinTable(
		name="log_schedule_days"
		, joinColumns={
			@JoinColumn(name="schedule_day_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="pickup_schedules_id", nullable=false)
			}
		)
	private List<LogPickupSchedule> logPickupSchedules;

    public LogScheduleDayOfWeek() {
    }

	public Long getScheduleDayId() {
		return this.scheduleDayId;
	}

	public void setScheduleDayId(Long scheduleDayId) {
		this.scheduleDayId = scheduleDayId;
	}

	public String getScheduleDayDesc() {
		return this.scheduleDayDesc;
	}

	public void setScheduleDayDesc(String scheduleDayDesc) {
		this.scheduleDayDesc = scheduleDayDesc;
	}

	public List<LogPickupSchedule> getLogPickupSchedules() {
		return this.logPickupSchedules;
	}

	public void setLogPickupSchedules(List<LogPickupSchedule> logPickupSchedules) {
		this.logPickupSchedules = logPickupSchedules;
	}
	
}