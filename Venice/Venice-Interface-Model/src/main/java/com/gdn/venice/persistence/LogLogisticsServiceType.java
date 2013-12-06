package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the log_logistics_service_type database table.
 * 
 */
@Entity
@Table(name="log_logistics_service_type")
public class LogLogisticsServiceType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="log_logistics_service_type")  
	@TableGenerator(name="log_logistics_service_type", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="logistics_service_type_id", unique=true, nullable=false)
	private Long logisticsServiceTypeId;

	@Column(name="express_flag", nullable=false)
	private Boolean expressFlag;

	@Column(name="logistics_service_type_desc", nullable=false, length=100)
	private String logisticsServiceTypeDesc;

	//bi-directional many-to-one association to LogLogisticService
	@OneToMany(mappedBy="logLogisticsServiceType")
	private List<LogLogisticService> logLogisticServices;

    public LogLogisticsServiceType() {
    }

	public Long getLogisticsServiceTypeId() {
		return this.logisticsServiceTypeId;
	}

	public void setLogisticsServiceTypeId(Long logisticsServiceTypeId) {
		this.logisticsServiceTypeId = logisticsServiceTypeId;
	}

	public Boolean getExpressFlag() {
		return this.expressFlag;
	}

	public void setExpressFlag(Boolean expressFlag) {
		this.expressFlag = expressFlag;
	}

	public String getLogisticsServiceTypeDesc() {
		return this.logisticsServiceTypeDesc;
	}

	public void setLogisticsServiceTypeDesc(String logisticsServiceTypeDesc) {
		this.logisticsServiceTypeDesc = logisticsServiceTypeDesc;
	}

	public List<LogLogisticService> getLogLogisticServices() {
		return this.logLogisticServices;
	}

	public void setLogLogisticServices(List<LogLogisticService> logLogisticServices) {
		this.logLogisticServices = logLogisticServices;
	}
	
}