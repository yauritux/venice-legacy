package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_order_blocking_source database table.
 * 
 */
@Entity
@Table(name="ven_order_blocking_source")
public class VenOrderBlockingSource implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_order_blocking_source")  
	@TableGenerator(name="ven_order_blocking_source", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="blocking_source_id", unique=true, nullable=false)
	private Long blockingSourceId;

	@Column(name="blocking_source_desc", nullable=false, length=1000)
	private String blockingSourceDesc;

	//bi-directional many-to-one association to VenOrder
	@OneToMany(mappedBy="venOrderBlockingSource")
	private List<VenOrder> venOrders;

    public VenOrderBlockingSource() {
    }

	public Long getBlockingSourceId() {
		return this.blockingSourceId;
	}

	public void setBlockingSourceId(Long blockingSourceId) {
		this.blockingSourceId = blockingSourceId;
	}

	public String getBlockingSourceDesc() {
		return this.blockingSourceDesc;
	}

	public void setBlockingSourceDesc(String blockingSourceDesc) {
		this.blockingSourceDesc = blockingSourceDesc;
	}

	public List<VenOrder> getVenOrders() {
		return this.venOrders;
	}

	public void setVenOrders(List<VenOrder> venOrders) {
		this.venOrders = venOrders;
	}
	
}