package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_product_type database table.
 * 
 */
@Entity
@Table(name="ven_product_type")
public class VenProductType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_product_type")  
	@TableGenerator(name="ven_product_type", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="product_type_id", unique=true, nullable=false)
	private Long productTypeId;

	@Column(name="product_type_code", nullable=false, length=100)
	private String productTypeCode;

	@Column(name="product_type_desc", nullable=false, length=100)
	private String productTypeDesc;

	//bi-directional many-to-one association to VenMerchantProduct
	@OneToMany(mappedBy="venProductType")
	private List<VenMerchantProduct> venMerchantProducts;

    public VenProductType() {
    }

	public Long getProductTypeId() {
		return this.productTypeId;
	}

	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getProductTypeCode() {
		return this.productTypeCode;
	}

	public void setProductTypeCode(String productTypeCode) {
		this.productTypeCode = productTypeCode;
	}

	public String getProductTypeDesc() {
		return this.productTypeDesc;
	}

	public void setProductTypeDesc(String productTypeDesc) {
		this.productTypeDesc = productTypeDesc;
	}

	public List<VenMerchantProduct> getVenMerchantProducts() {
		return this.venMerchantProducts;
	}

	public void setVenMerchantProducts(List<VenMerchantProduct> venMerchantProducts) {
		this.venMerchantProducts = venMerchantProducts;
	}
	
}