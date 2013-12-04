package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the log_logistic_service database table.
 * 
 */
@Entity
@Table(name="log_logistic_service")
public class LogLogisticService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="log_logistic_service")  
	@TableGenerator(name="log_logistic_service", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="logistics_service_id", unique=true, nullable=false)
	private Long logisticsServiceId;

	@Column(name="logistics_service_desc", nullable=false, length=100)
	private String logisticsServiceDesc;

	@Column(name="service_code", nullable=false, length=100)
	private String serviceCode;

	//bi-directional many-to-one association to LogLogisticsProvider
    @ManyToOne
	@JoinColumn(name="logistics_provider_id", nullable=false)
	private LogLogisticsProvider logLogisticsProvider;

	//bi-directional many-to-one association to LogLogisticsServiceType
    @ManyToOne
	@JoinColumn(name="logistics_service_type_id", nullable=false)
	private LogLogisticsServiceType logLogisticsServiceType;

	//bi-directional many-to-one association to VenOrderItem
	@OneToMany(mappedBy="logLogisticService")
	private List<VenOrderItem> venOrderItems;

    public LogLogisticService() {
    }

	public Long getLogisticsServiceId() {
		return this.logisticsServiceId;
	}

	public void setLogisticsServiceId(Long logisticsServiceId) {
		this.logisticsServiceId = logisticsServiceId;
	}

	public String getLogisticsServiceDesc() {
		return this.logisticsServiceDesc;
	}

	public void setLogisticsServiceDesc(String logisticsServiceDesc) {
		this.logisticsServiceDesc = logisticsServiceDesc;
	}

	public String getServiceCode() {
		return this.serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public LogLogisticsProvider getLogLogisticsProvider() {
		return this.logLogisticsProvider;
	}

	public void setLogLogisticsProvider(LogLogisticsProvider logLogisticsProvider) {
		this.logLogisticsProvider = logLogisticsProvider;
	}
	
	public LogLogisticsServiceType getLogLogisticsServiceType() {
		return this.logLogisticsServiceType;
	}

	public void setLogLogisticsServiceType(LogLogisticsServiceType logLogisticsServiceType) {
		this.logLogisticsServiceType = logLogisticsServiceType;
	}
	
	public List<VenOrderItem> getVenOrderItems() {
		return this.venOrderItems;
	}

	public void setVenOrderItems(List<VenOrderItem> venOrderItems) {
		this.venOrderItems = venOrderItems;
	}
	
}