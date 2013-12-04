package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the ven_order_item_adjustment database table.
 * 
 */
@Entity
@Table(name="ven_order_item_adjustment")
public class VenOrderItemAdjustment implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VenOrderItemAdjustmentPK id;

	@Column(name="admin_desc", nullable=false, length=1000)
	private String adminDesc;

	@Column(nullable=false, precision=20, scale=2)
	private BigDecimal amount;

	//bi-directional many-to-one association to VenOrderItem
    @ManyToOne
	@JoinColumn(name="order_item_id", nullable=false, insertable=false, updatable=false)
	private VenOrderItem venOrderItem;

	//bi-directional many-to-one association to VenPromotion
    @ManyToOne
	@JoinColumn(name="promotion_id", nullable=false, insertable=false, updatable=false)
	private VenPromotion venPromotion;
    
	@Column(name="voucher_code", length=100)
	private String voucherCode;

    public VenOrderItemAdjustment() {
    }

	public VenOrderItemAdjustmentPK getId() {
		return this.id;
	}

	public void setId(VenOrderItemAdjustmentPK id) {
		this.id = id;
	}
	
	public String getAdminDesc() {
		return this.adminDesc;
	}

	public void setAdminDesc(String adminDesc) {
		this.adminDesc = adminDesc;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public VenOrderItem getVenOrderItem() {
		return this.venOrderItem;
	}

	public void setVenOrderItem(VenOrderItem venOrderItem) {
		this.venOrderItem = venOrderItem;
	}
	
	public VenPromotion getVenPromotion() {
		return this.venPromotion;
	}

	public void setVenPromotion(VenPromotion venPromotion) {
		this.venPromotion = venPromotion;
	}

	public String getVoucherCode() {
		return voucherCode;
	}

	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}
	
}