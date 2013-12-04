package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the raf_user database table.
 * 
 */
@Entity
@Table(name="raf_user")
public class RafUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="raf_user")  
	@TableGenerator(name="raf_user", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="user_id")
	private Long userId;

	@Column(name="encrypted_password", length=100)
	private String encryptedPassword;

	@Column(name="login_name", nullable=false, length=100)
	private String loginName;

	//bi-directional many-to-one association to VenParty
    @ManyToOne
	@JoinColumn(name="party_id")
	private VenParty venParty;

	//bi-directional many-to-one association to RafUserGroupMembership
	@OneToMany(mappedBy="rafUser")
	private List<RafUserGroupMembership> rafUserGroupMemberships;

    public RafUser() {
    }

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEncryptedPassword() {
		return this.encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public VenParty getVenParty() {
		return this.venParty;
	}

	public void setVenParty(VenParty venParty) {
		this.venParty = venParty;
	}
	
	public List<RafUserGroupMembership> getRafUserGroupMemberships() {
		return this.rafUserGroupMemberships;
	}

	public void setRafUserGroupMemberships(List<RafUserGroupMembership> rafUserGroupMemberships) {
		this.rafUserGroupMemberships = rafUserGroupMemberships;
	}
	
}