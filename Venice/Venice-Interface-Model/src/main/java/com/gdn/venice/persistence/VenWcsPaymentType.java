package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_wcs_payment_type database table.
 * 
 */
@Entity
@Table(name="ven_wcs_payment_type")
public class VenWcsPaymentType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_wcs_payment_type")  
	@TableGenerator(name="ven_wcs_payment_type", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="wcs_payment_type_id", unique=true, nullable=false)
	private Long wcsPaymentTypeId;

	@Column(name="wcs_payment_type_code", nullable=false, length=100)
	private String wcsPaymentTypeCode;

	@Column(name="wcs_payment_type_desc", nullable=false, length=100)
	private String wcsPaymentTypeDesc;

	//bi-directional many-to-one association to VenOrderPayment
	@OneToMany(mappedBy="venWcsPaymentType")
	private List<VenOrderPayment> venOrderPayments;

    public VenWcsPaymentType() {
    }

	public Long getWcsPaymentTypeId() {
		return this.wcsPaymentTypeId;
	}

	public void setWcsPaymentTypeId(Long wcsPaymentTypeId) {
		this.wcsPaymentTypeId = wcsPaymentTypeId;
	}

	public String getWcsPaymentTypeCode() {
		return this.wcsPaymentTypeCode;
	}

	public void setWcsPaymentTypeCode(String wcsPaymentTypeCode) {
		this.wcsPaymentTypeCode = wcsPaymentTypeCode;
	}

	public String getWcsPaymentTypeDesc() {
		return this.wcsPaymentTypeDesc;
	}

	public void setWcsPaymentTypeDesc(String wcsPaymentTypeDesc) {
		this.wcsPaymentTypeDesc = wcsPaymentTypeDesc;
	}

	public List<VenOrderPayment> getVenOrderPayments() {
		return this.venOrderPayments;
	}

	public void setVenOrderPayments(List<VenOrderPayment> venOrderPayments) {
		this.venOrderPayments = venOrderPayments;
	}
	
}