package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;


/**
 * The persistent class for the raf_task_instance database table.
 * 
 */
@Entity
@Table(name="raf_task_instance")
public class RafTaskInstance implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="raf_task_instance")  
	@TableGenerator(name="raf_task_instance", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="task_instance_id", unique=true, nullable=false)
	private Long taskInstanceId;

	@Column(name="bpm_created_date", nullable=false)
	private Timestamp bpmCreatedDate;

	@Column(name="bpm_due_date", nullable=false)
	private Timestamp bpmDueDate;

	@Column(name="bpm_task_instance_id", nullable=false, length=1000)
	private String bpmTaskInstanceId;

	@Column(name="bpm_task_status", nullable=false, length=1000)
	private String bpmTaskStatus;

	//bi-directional many-to-one association to RafProcessInstance
    @ManyToOne
	@JoinColumn(name="process_instance_id", nullable=false)
	private RafProcessInstance rafProcessInstance;

	//bi-directional many-to-one association to RafRole
    @ManyToOne
	@JoinColumn(name="role_id")
	private RafRole rafRole;

	//bi-directional many-to-one association to RafUser
    @ManyToOne
	@JoinColumn(name="user_id")
	private RafUser rafUser;

    public RafTaskInstance() {
    }

	public Long getTaskInstanceId() {
		return this.taskInstanceId;
	}

	public void setTaskInstanceId(Long taskInstanceId) {
		this.taskInstanceId = taskInstanceId;
	}

	public Timestamp getBpmCreatedDate() {
		return this.bpmCreatedDate;
	}

	public void setBpmCreatedDate(Timestamp bpmCreatedDate) {
		this.bpmCreatedDate = bpmCreatedDate;
	}

	public Timestamp getBpmDueDate() {
		return this.bpmDueDate;
	}

	public void setBpmDueDate(Timestamp bpmDueDate) {
		this.bpmDueDate = bpmDueDate;
	}

	public String getBpmTaskInstanceId() {
		return this.bpmTaskInstanceId;
	}

	public void setBpmTaskInstanceId(String bpmTaskInstanceId) {
		this.bpmTaskInstanceId = bpmTaskInstanceId;
	}

	public String getBpmTaskStatus() {
		return this.bpmTaskStatus;
	}

	public void setBpmTaskStatus(String bpmTaskStatus) {
		this.bpmTaskStatus = bpmTaskStatus;
	}

	public RafProcessInstance getRafProcessInstance() {
		return this.rafProcessInstance;
	}

	public void setRafProcessInstance(RafProcessInstance rafProcessInstance) {
		this.rafProcessInstance = rafProcessInstance;
	}
	
	public RafRole getRafRole() {
		return this.rafRole;
	}

	public void setRafRole(RafRole rafRole) {
		this.rafRole = rafRole;
	}
	
	public RafUser getRafUser() {
		return this.rafUser;
	}

	public void setRafUser(RafUser rafUser) {
		this.rafUser = rafUser;
	}
	
}