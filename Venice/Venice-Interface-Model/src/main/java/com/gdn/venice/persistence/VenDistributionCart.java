package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the ven_distribution_cart database table.
 * 
 */
@Entity
@Table(name="ven_distribution_cart")
public class VenDistributionCart implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_distribution_cart")  
	@TableGenerator(name="ven_distribution_cart", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="distribution_cart_id", unique=true, nullable=false)
	private Long distributionCartId;

	@Column(name="dc_sequence", nullable=false)
	private Integer dcSequence;

	@Column(name="package_weight", nullable=false, precision=20, scale=2)
	private BigDecimal packageWeight;

	@Column(nullable=false)
	private Integer quantity;

	//bi-directional many-to-one association to LogAirwayBill
	@OneToMany(mappedBy="venDistributionCart")
	private List<LogAirwayBill> logAirwayBills;

	//bi-directional many-to-one association to VenOrderItem
    @ManyToOne
	@JoinColumn(name="order_item_id", nullable=false)
	private VenOrderItem venOrderItem;

    public VenDistributionCart() {
    }

	public Long getDistributionCartId() {
		return this.distributionCartId;
	}

	public void setDistributionCartId(Long distributionCartId) {
		this.distributionCartId = distributionCartId;
	}

	public Integer getDcSequence() {
		return this.dcSequence;
	}

	public void setDcSequence(Integer dcSequence) {
		this.dcSequence = dcSequence;
	}

	public BigDecimal getPackageWeight() {
		return this.packageWeight;
	}

	public void setPackageWeight(BigDecimal packageWeight) {
		this.packageWeight = packageWeight;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public List<LogAirwayBill> getLogAirwayBills() {
		return this.logAirwayBills;
	}

	public void setLogAirwayBills(List<LogAirwayBill> logAirwayBills) {
		this.logAirwayBills = logAirwayBills;
	}
	
	public VenOrderItem getVenOrderItem() {
		return this.venOrderItem;
	}

	public void setVenOrderItem(VenOrderItem venOrderItem) {
		this.venOrderItem = venOrderItem;
	}
	
}