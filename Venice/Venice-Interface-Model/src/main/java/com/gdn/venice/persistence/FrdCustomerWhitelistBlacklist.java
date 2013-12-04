package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;


/**
 * The persistent class for the frd_customer_whitelist_blacklist database table.
 * 
 */
@Entity
@Table(name="frd_customer_whitelist_blacklist")
public class FrdCustomerWhitelistBlacklist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_customer_whitelist_blacklist")  
	@TableGenerator(name="frd_customer_whitelist_blacklist", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;

	@Column(name="customer_full_name", length=100)
	private String customerFullName;
	
	@Column(name="address", length=1000)
	private String address;
	
	@Column(name="phone_number", length=50)
	private String phoneNumber;

	@Column(name="email", length=100)
	private String email;
	
	@Column(name="description", length=1000)
	private String description;

	@Column(name="created_by", length=100)
	private String createdBy;
	
	@Column(name="timestamp")
	private Timestamp timestamp;
	
	@Column(name="handphone_number", length=50)
	private String handphoneNumber;	
	
	@Column(name="shipping_phone_number", length=50)
	private String shippingPhoneNumber;
	
	@Column(name="shipping_handphone_number", length=50)
	private String shippingHandphoneNumber;
	
	@Column(name="shipping_address", length=2000)
	private String shippingAddress;
	
	@Column(name="cc_number", length=50)
	private String ccNumber;
	
	@Column(name="order_timestamp")
	private Timestamp orderTimestamp;
	
    public FrdCustomerWhitelistBlacklist() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCustomerFullName() {
		return this.customerFullName;
	}

	public void setCustomerFullName(String customerFullName) {
		this.customerFullName = customerFullName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Timestamp getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getHandphoneNumber() {
		return handphoneNumber;
	}

	public void setHandphoneNumber(String handphoneNumber) {
		this.handphoneNumber = handphoneNumber;
	}

	public String getShippingPhoneNumber() {
		return shippingPhoneNumber;
	}

	public void setShippingPhoneNumber(String shippingPhoneNumber) {
		this.shippingPhoneNumber = shippingPhoneNumber;
	}

	public String getShippingHandphoneNumber() {
		return shippingHandphoneNumber;
	}

	public void setShippingHandphoneNumber(String shippingHandphoneNumber) {
		this.shippingHandphoneNumber = shippingHandphoneNumber;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getCcNumber() {
		return ccNumber;
	}

	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}

	public Timestamp getOrderTimestamp() {
		return orderTimestamp;
	}

	public void setOrderTimestamp(Timestamp orderTimestamp) {
		this.orderTimestamp = orderTimestamp;
	}

}