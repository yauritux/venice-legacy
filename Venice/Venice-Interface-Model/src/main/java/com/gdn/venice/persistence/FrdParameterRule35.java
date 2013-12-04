package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the frd_parameter_rule_35 database table.
 * 
 */
@Entity
@Table(name="frd_parameter_rule_35")
public class FrdParameterRule35 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_parameter_rule_35")  
	@TableGenerator(name="frd_parameter_rule_35", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;

	@Column(name="cc_number", length=100, nullable=false)
	private String ccNumber;

	@Column(name="customer_name", length=100, nullable=false)
	private String customerName;

	@Column(name="email", length=100, nullable=false)
	private String email;

	@Column(name="no_surat", length=100)
	private String noSurat;

	@Column(name="order_date", length=100, nullable=false)
	private Timestamp orderDate;

	@Column(name="order_id", length=100, nullable=false)
	private String orderId;

	@Column(name="remarks", length=1000)
	private String remarks;

    public FrdParameterRule35() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCcNumber() {
		return this.ccNumber;
	}

	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNoSurat() {
		return this.noSurat;
	}

	public void setNoSurat(String noSurat) {
		this.noSurat = noSurat;
	}

	public Timestamp getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}