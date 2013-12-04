package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_parameter_rule_30 database table.
 * 
 */
@Entity
@Table(name="frd_parameter_rule_30")
public class FrdParameterRule30 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_parameter_rule_30")  
	@TableGenerator(name="frd_parameter_rule_30", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;

	@Column(name="kode_kota", length=20)
	private String kodeKota;

	@Column(name="nama_kota", length=100)
	private String namaKota;

    public FrdParameterRule30() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKodeKota() {
		return this.kodeKota;
	}

	public void setKodeKota(String kodeKota) {
		this.kodeKota = kodeKota;
	}

	public String getNamaKota() {
		return this.namaKota;
	}

	public void setNamaKota(String namaKota) {
		this.namaKota = namaKota;
	}

}