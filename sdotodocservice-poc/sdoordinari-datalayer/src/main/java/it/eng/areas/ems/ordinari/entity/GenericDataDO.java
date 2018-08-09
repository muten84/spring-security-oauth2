package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the GENERIC_DATA database table.
 * 
 */
@Entity
@Table(name="GENERIC_DATA")
public class GenericDataDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Column(name="ITEM_SECTION")
	private String itemSection;

	@Column(name="ITEM_TYPE")
	private String itemType;

	@Column(name="ITEM_VALUE")
	private String itemValue;

	public GenericDataDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getItemSection() {
		return this.itemSection;
	}

	public void setItemSection(String itemSection) {
		this.itemSection = itemSection;
	}

	public String getItemType() {
		return this.itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItemValue() {
		return this.itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

}