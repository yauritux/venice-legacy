package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_rule_config_treshold database table.
 * 
 */
@Entity
@Table(name="frd_rule_config_treshold")
public class FrdRuleConfigTreshold implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_rule_config_treshold")  
//	@TableGenerator(name="frd_rule_config_treshold", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(length=100)
	private String key;

	@Column(length=200, nullable=false)
	private String value;

    public FrdRuleConfigTreshold() {
    }

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}