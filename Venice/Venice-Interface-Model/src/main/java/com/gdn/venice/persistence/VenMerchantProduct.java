package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the ven_merchant_product database table.
 * 
 */
@Entity
@Table(name="ven_merchant_product")
public class VenMerchantProduct implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_merchant_product")  
	@TableGenerator(name="ven_merchant_product", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="product_id", unique=true, nullable=false)
	private Long productId;

	@Column(name="maximum_number_ever_ordered")
	private Long maximumNumberEverOrdered;

	@Column(name="shipping_weight", precision=20, scale=2)
	private BigDecimal shippingWeight;

	@Column(name="wcs_product_name", nullable=false, length=200)
	private String wcsProductName;

	@Column(name="wcs_product_sku", nullable=false, length=200)
	private String wcsProductSku;

	//bi-directional many-to-one association to VenMerchant
    @ManyToOne
	@JoinColumn(name="merchant_id")
	private VenMerchant venMerchant;

	//bi-directional many-to-one association to VenProductType
    @ManyToOne
	@JoinColumn(name="product_type_id")
	private VenProductType venProductType;

	//bi-directional many-to-one association to VenOrderItem
	@OneToMany(mappedBy="venMerchantProduct")
	private List<VenOrderItem> venOrderItems;

	//bi-directional many-to-many association to VenProductCategory
	@ManyToMany(mappedBy="venMerchantProducts")//, fetch=FetchType.EAGER)
	private List<VenProductCategory> venProductCategories;
	
	@Column(name="cost_of_goods_sold", precision=20, scale=2)
	private BigDecimal costOfGoodsSold;

    public VenMerchantProduct() {
    }

	public Long getProductId() {
		return this.productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getMaximumNumberEverOrdered() {
		return this.maximumNumberEverOrdered;
	}

	public void setMaximumNumberEverOrdered(Long maximumNumberEverOrdered) {
		this.maximumNumberEverOrdered = maximumNumberEverOrdered;
	}

	public BigDecimal getShippingWeight() {
		return this.shippingWeight;
	}

	public void setShippingWeight(BigDecimal shippingWeight) {
		this.shippingWeight = shippingWeight;
	}

	public String getWcsProductName() {
		return this.wcsProductName;
	}

	public void setWcsProductName(String wcsProductName) {
		this.wcsProductName = wcsProductName;
	}

	public String getWcsProductSku() {
		return this.wcsProductSku;
	}

	public void setWcsProductSku(String wcsProductSku) {
		this.wcsProductSku = wcsProductSku;
	}

	public VenMerchant getVenMerchant() {
		return this.venMerchant;
	}

	public void setVenMerchant(VenMerchant venMerchant) {
		this.venMerchant = venMerchant;
	}
	
	public VenProductType getVenProductType() {
		return this.venProductType;
	}

	public void setVenProductType(VenProductType venProductType) {
		this.venProductType = venProductType;
	}
	
	public List<VenOrderItem> getVenOrderItems() {
		return this.venOrderItems;
	}

	public void setVenOrderItems(List<VenOrderItem> venOrderItems) {
		this.venOrderItems = venOrderItems;
	}
	
	public List<VenProductCategory> getVenProductCategories() {
		return this.venProductCategories;
	}

	public void setVenProductCategories(List<VenProductCategory> venProductCategories) {
		this.venProductCategories = venProductCategories;
	}

	public BigDecimal getCostOfGoodsSold() {
		return costOfGoodsSold;
	}

	public void setCostOfGoodsSold(BigDecimal costOfGoodsSold) {
		this.costOfGoodsSold = costOfGoodsSold;
	}
	
}