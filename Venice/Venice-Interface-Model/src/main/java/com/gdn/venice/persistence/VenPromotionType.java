package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ven_promotion_type database table.
 * 
 */
@Entity
@Table(name="ven_promotion_type")
public class VenPromotionType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_promotion_type")  
	@TableGenerator(name="ven_promotion_type", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="promotion_type", unique=true, nullable=false)
	private Long promotionType;

	@Column(name="promotion_type_desc", nullable=false, length=100)
	private String promotionTypeDesc;			

    public VenPromotionType() {
    }

	public Long getPromotionType() {
		return this.promotionType;
	}

	public void setPromotionType(Long promotionType) {
		this.promotionType = promotionType;
	}

	public String getPromotionTypeDesc() {
		return this.promotionTypeDesc;
	}

	public void setPromotionTypeDesc(String promotionTypeDesc) {
		this.promotionTypeDesc = promotionTypeDesc;
	}	
}