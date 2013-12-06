package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the log_merchant_pickup_instructions database table.
 * 
 */
@Entity
@Table(name="log_merchant_pickup_instructions")
public class LogMerchantPickupInstruction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="log_merchant_pickup_instructions")  
	@TableGenerator(name="log_merchant_pickup_instructions", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="merchant_pickup_details_id", unique=true, nullable=false)
	private Long merchantPickupDetailsId;

	@Column(name="merchant_pic", nullable=false, length=100)
	private String merchantPic;

	@Column(name="merchant_pic_phone", nullable=false, length=100)
	private String merchantPicPhone;

	@Column(name="pickup_date_time", nullable=false)
	private Timestamp pickupDateTime;

	@Column(name="special_handling_instructions", length=1000)
	private String specialHandlingInstructions;

	//bi-directional many-to-one association to VenAddress
    @ManyToOne
	@JoinColumn(name="pickup_address_id", nullable=false)
	private VenAddress venAddress;

	//bi-directional many-to-one association to VenMerchant
    @ManyToOne
	@JoinColumn(name="merchant_id", nullable=false)
	private VenMerchant venMerchant;

	//bi-directional many-to-one association to VenOrderItem
    @ManyToOne
	@JoinColumn(name="order_item_id")
	private VenOrderItem venOrderItem;
    
	//bi-directional many-to-one association to VenReturItem
    @ManyToOne
	@JoinColumn(name="retur_item_id")
	private VenReturItem venReturItem;

    public LogMerchantPickupInstruction() {
    }

	public Long getMerchantPickupDetailsId() {
		return this.merchantPickupDetailsId;
	}

	public void setMerchantPickupDetailsId(Long merchantPickupDetailsId) {
		this.merchantPickupDetailsId = merchantPickupDetailsId;
	}

	public String getMerchantPic() {
		return this.merchantPic;
	}

	public void setMerchantPic(String merchantPic) {
		this.merchantPic = merchantPic;
	}

	public String getMerchantPicPhone() {
		return this.merchantPicPhone;
	}

	public void setMerchantPicPhone(String merchantPicPhone) {
		this.merchantPicPhone = merchantPicPhone;
	}

	public Timestamp getPickupDateTime() {
		return this.pickupDateTime;
	}

	public void setPickupDateTime(Timestamp pickupDateTime) {
		this.pickupDateTime = pickupDateTime;
	}

	public String getSpecialHandlingInstructions() {
		return this.specialHandlingInstructions;
	}

	public void setSpecialHandlingInstructions(String specialHandlingInstructions) {
		this.specialHandlingInstructions = specialHandlingInstructions;
	}

	public VenAddress getVenAddress() {
		return this.venAddress;
	}

	public void setVenAddress(VenAddress venAddress) {
		this.venAddress = venAddress;
	}
	
	public VenMerchant getVenMerchant() {
		return this.venMerchant;
	}

	public void setVenMerchant(VenMerchant venMerchant) {
		this.venMerchant = venMerchant;
	}
	
	public VenOrderItem getVenOrderItem() {
		return this.venOrderItem;
	}

	public void setVenOrderItem(VenOrderItem venOrderItem) {
		this.venOrderItem = venOrderItem;
	}

	public VenReturItem getVenReturItem() {
		return venReturItem;
	}

	public void setVenReturItem(VenReturItem venReturItem) {
		this.venReturItem = venReturItem;
	}
	
}