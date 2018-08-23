package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the DDT database table.
 * 
 */
@Entity
@Table(name="GUIDA")
public class GuidSequenceDO implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String seq_id;

	public String getSeq_id() {
		return seq_id;
	}

	public void setSeq_id(String seq_id) {
		this.seq_id = seq_id;
	}

}