package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the raf_process_instance database table.
 * 
 */
@Entity
@Table(name="raf_process_instance")
public class RafProcessInstance implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="raf_process_instance")  
	@TableGenerator(name="raf_process_instance", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="process_instance_id", unique=true, nullable=false)
	private Long processInstanceId;

	@Column(name="bpm_process_instance_id", nullable=false, length=1000)
	private String bpmProcessInstanceId;

	@Column(name="bpm_process_instance_name", nullable=false, length=1000)
	private String bpmProcessInstanceName;

	//bi-directional many-to-one association to RafProcess
    @ManyToOne
	@JoinColumn(name="process_id", nullable=false)
	private RafProcess rafProcess;

	//bi-directional many-to-one association to RafTaskInstance
	@OneToMany(mappedBy="rafProcessInstance")
	private List<RafTaskInstance> rafTaskInstances;

    public RafProcessInstance() {
    }

	public Long getProcessInstanceId() {
		return this.processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getBpmProcessInstanceId() {
		return this.bpmProcessInstanceId;
	}

	public void setBpmProcessInstanceId(String bpmProcessInstanceId) {
		this.bpmProcessInstanceId = bpmProcessInstanceId;
	}

	public String getBpmProcessInstanceName() {
		return this.bpmProcessInstanceName;
	}

	public void setBpmProcessInstanceName(String bpmProcessInstanceName) {
		this.bpmProcessInstanceName = bpmProcessInstanceName;
	}

	public RafProcess getRafProcess() {
		return this.rafProcess;
	}

	public void setRafProcess(RafProcess rafProcess) {
		this.rafProcess = rafProcess;
	}
	
	public List<RafTaskInstance> getRafTaskInstances() {
		return this.rafTaskInstances;
	}

	public void setRafTaskInstances(List<RafTaskInstance> rafTaskInstances) {
		this.rafTaskInstances = rafTaskInstances;
	}
	
}