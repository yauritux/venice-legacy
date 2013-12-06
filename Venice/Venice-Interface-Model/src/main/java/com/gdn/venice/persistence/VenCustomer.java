package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the ven_customer database table.
 * 
 */
@Entity
@Table(name="ven_customer")
public class VenCustomer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_customer")  
	@TableGenerator(name="ven_customer", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="customer_id", unique=true, nullable=false)
	private Long customerId;

	@Column(name="customer_user_name", nullable=false, length=100)
	private String customerUserName;
	
	@Column(name="user_type", nullable=false, length=100)
	private String userType;

    @Temporal( TemporalType.DATE)
	@Column(name="date_of_birth")
	private Date dateOfBirth;

	@Column(name="first_time_transaction_flag", nullable=false)
	private Boolean firstTimeTransactionFlag;

	@Column(name="wcs_customer_id", nullable=false, length=100)
	private String wcsCustomerId;

	//bi-directional many-to-one association to VenParty
    @ManyToOne
	@JoinColumn(name="party_id", nullable=false)
	private VenParty venParty;

	//bi-directional many-to-one association to VenOrder
	@OneToMany(mappedBy="venCustomer")
	private List<VenOrder> venOrders;

    public VenCustomer() {
    }

	public Long getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getCustomerUserName() {
		return this.customerUserName;
	}

	public void setCustomerUserName(String customerUserName) {
		this.customerUserName = customerUserName;
	}

	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Boolean getFirstTimeTransactionFlag() {
		return this.firstTimeTransactionFlag;
	}

	public void setFirstTimeTransactionFlag(Boolean firstTimeTransactionFlag) {
		this.firstTimeTransactionFlag = firstTimeTransactionFlag;
	}

	public String getWcsCustomerId() {
		return this.wcsCustomerId;
	}

	public void setWcsCustomerId(String wcsCustomerId) {
		this.wcsCustomerId = wcsCustomerId;
	}

	public VenParty getVenParty() {
		return this.venParty;
	}

	public void setVenParty(VenParty venParty) {
		this.venParty = venParty;
	}
	
	public List<VenOrder> getVenOrders() {
		return this.venOrders;
	}

	public void setVenOrders(List<VenOrder> venOrders) {
		this.venOrders = venOrders;
	}
	
}