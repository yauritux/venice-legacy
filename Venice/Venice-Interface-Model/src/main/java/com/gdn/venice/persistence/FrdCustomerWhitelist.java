package com.gdn.venice.persistence;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * The persistent class for the frd_customer_whitelist database table.
 * 
 */
@Entity
@Table(name="frd_customer_whitelist")
public class FrdCustomerWhitelist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_customer_whitelist")  
	@TableGenerator(name="frd_customer_whitelist", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="id", unique=true, nullable=false)
	private Long id;

	@Column(name="creditcardnumber", length=100)
	private String creditcardnumber;

	@Column(name="customername", length=100)
	private String customername;

	@Column(name="eci", length=50)
	private String eci;

	@Column(name="email", length=100)
	private String email;

	@Column(name="expireddate", length=100)
	private String expireddate;

	@Column(name="genuinedate")
	private Timestamp genuinedate;

	@Column(name="issuerbank", length=50)
	private String issuerbank;

	@Column(name="orderid", length=100)
	private String orderid;

	@Column(name="ordertimestamp")
	private Timestamp ordertimestamp;

	@Column(name="remark", length=1000)
	private String remark;

	@Column(name="shippingaddress", length=1000)
	private String shippingaddress;
	
	@Column(name="created")
	private Timestamp created;
	
	@Column(name="createdby", length=100)
	private String createdby;

   	public FrdCustomerWhitelist() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreditcardnumber() {
		return this.creditcardnumber;
	}

	public void setCreditcardnumber(String creditcardnumber) {
		this.creditcardnumber = creditcardnumber;
	}

	public String getCustomername() {
		return this.customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getEci() {
		return this.eci;
	}

	public void setEci(String eci) {
		this.eci = eci;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getExpireddate() {
		return this.expireddate;
	}

	public void setExpireddate(String expireddate) {
		this.expireddate = expireddate;
	}

	public Timestamp getGenuinedate() {
		return this.genuinedate;
	}

	public void setGenuinedate(Timestamp genuinedate) {
		this.genuinedate = genuinedate;
	}

	public String getIssuerbank() {
		return this.issuerbank;
	}

	public void setIssuerbank(String issuerbank) {
		this.issuerbank = issuerbank;
	}

	public String getOrderid() {
		return this.orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public Timestamp getOrdertimestamp() {
		return this.ordertimestamp;
	}

	public void setOrdertimestamp(Timestamp ordertimestamp) {
		this.ordertimestamp = ordertimestamp;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getShippingaddress() {
		return this.shippingaddress;
	}

	public void setShippingaddress(String shippingaddress) {
		this.shippingaddress = shippingaddress;
	}
	
	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}
	
	 public String getCreatedby() {
			return createdby;
		}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

}