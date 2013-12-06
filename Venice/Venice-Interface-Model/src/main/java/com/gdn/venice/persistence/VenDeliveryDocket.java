package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;


/**
 * The persistent class for the ven_delivery_docket database table.
 * 
 */
@Entity
@Table(name="ven_delivery_docket")
public class VenDeliveryDocket implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_delivery_docket")  
	@TableGenerator(name="ven_delivery_docket", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="delivery_docket_id", unique=true, nullable=false)
	private Long deliveryDocketId;

	@Column(name="delivery_date", nullable=false)
	private Timestamp deliveryDate;

	@Column(name="receiver_name", nullable=false, length=100)
	private String receiverName;

	@Column(name="receiver_occupation", nullable=false, length=100)
	private String receiverOccupation;

	//bi-directional many-to-one association to VenOrderItem
    @ManyToOne
	@JoinColumn(name="order_item_id", nullable=false)
	private VenOrderItem venOrderItem;

    public VenDeliveryDocket() {
    }

	public Long getDeliveryDocketId() {
		return this.deliveryDocketId;
	}

	public void setDeliveryDocketId(Long deliveryDocketId) {
		this.deliveryDocketId = deliveryDocketId;
	}

	public Timestamp getDeliveryDate() {
		return this.deliveryDate;
	}

	public void setDeliveryDate(Timestamp deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getReceiverName() {
		return this.receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverOccupation() {
		return this.receiverOccupation;
	}

	public void setReceiverOccupation(String receiverOccupation) {
		this.receiverOccupation = receiverOccupation;
	}

	public VenOrderItem getVenOrderItem() {
		return this.venOrderItem;
	}

	public void setVenOrderItem(VenOrderItem venOrderItem) {
		this.venOrderItem = venOrderItem;
	}
	
}