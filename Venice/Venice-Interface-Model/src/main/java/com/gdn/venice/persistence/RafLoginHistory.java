package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;


/**
 * The persistent class for the raf_login_history database table.
 * 
 */
@Entity
@Table(name="raf_login_history")
public class RafLoginHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="raf_login_history")  
	@TableGenerator(name="raf_login_history", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="login_history_id", unique=true, nullable=false)
	private Long loginHistoryId;

	@Column(name="login_history_timestamp", nullable=false)
	private Timestamp loginHistoryTimestamp;

	//bi-directional many-to-one association to RafUser
    @ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private RafUser rafUser;

    public RafLoginHistory() {
    }

	public Long getLoginHistoryId() {
		return this.loginHistoryId;
	}

	public void setLoginHistoryId(Long loginHistoryId) {
		this.loginHistoryId = loginHistoryId;
	}

	public Timestamp getLoginHistoryTimestamp() {
		return this.loginHistoryTimestamp;
	}

	public void setLoginHistoryTimestamp(Timestamp loginHistoryTimestamp) {
		this.loginHistoryTimestamp = loginHistoryTimestamp;
	}

	public RafUser getRafUser() {
		return this.rafUser;
	}

	public void setRafUser(RafUser rafUser) {
		this.rafUser = rafUser;
	}
	
}