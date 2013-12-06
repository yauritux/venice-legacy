package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_product_category database table.
 * 
 */
@Entity
@Table(name="ven_product_category")
public class VenProductCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_product_category")  
	@TableGenerator(name="ven_product_category", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="product_category_id", unique=true, nullable=false)
	private Long productCategoryId;

	@Column(nullable=false)
	private Integer level;

	@Column(name="product_category", nullable=false, length=100)
	private String productCategory;

	//bi-directional many-to-many association to VenMerchantProduct
    @ManyToMany//(fetch=FetchType.EAGER)
	@JoinTable(
		name="ven_product_categories"
		, joinColumns={
			@JoinColumn(name="product_category_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="product_id", nullable=false)
			}
		)
	private List<VenMerchantProduct> venMerchantProducts;

    public VenProductCategory() {
    }

	public Long getProductCategoryId() {
		return this.productCategoryId;
	}

	public void setProductCategoryId(Long productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getProductCategory() {
		return this.productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public List<VenMerchantProduct> getVenMerchantProducts() {
		return this.venMerchantProducts;
	}

	public void setVenMerchantProducts(List<VenMerchantProduct> venMerchantProducts) {
		this.venMerchantProducts = venMerchantProducts;
	}
	
}