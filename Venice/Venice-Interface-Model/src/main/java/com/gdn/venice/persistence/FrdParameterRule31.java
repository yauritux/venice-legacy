package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_parameter_rule_31 database table.
 * 
 */
@Entity
@Table(name="frd_parameter_rule_31")
public class FrdParameterRule31 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_parameter_rule_31")  
	@TableGenerator(name="frd_parameter_rule_31", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;

	@Column(name="email", length=100, nullable=false)
	private String email;

	@Column(name="no_cc", length=100, nullable=false)
	private String noCc;

    public FrdParameterRule31() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNoCc() {
		return this.noCc;
	}

	public void setNoCc(String noCc) {
		this.noCc = noCc;
	}

}