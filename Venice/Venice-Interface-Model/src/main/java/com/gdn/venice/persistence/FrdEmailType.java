package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_email_type database table.
 * 
 */
@Entity
@Table(name="frd_email_type")
public class FrdEmailType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_email_type")  
	@TableGenerator(name="frd_email_type", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;

	@Column(name="email_server_pattern", length=100, nullable=false)
	private String emailServerPattern;

	@Column(name="email_type", length=100, nullable=false)
	private String emailType;

    public FrdEmailType() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmailServerPattern() {
		return this.emailServerPattern;
	}

	public void setEmailServerPattern(String emailServerPattern) {
		this.emailServerPattern = emailServerPattern;
	}

	public String getEmailType() {
		return this.emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

}