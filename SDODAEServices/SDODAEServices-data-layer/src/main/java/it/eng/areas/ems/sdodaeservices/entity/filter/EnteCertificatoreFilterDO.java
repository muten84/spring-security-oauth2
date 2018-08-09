package it.eng.areas.ems.sdodaeservices.entity.filter;


import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import it.eng.area118.sdocommon.dao.filter.EntityFilter;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class EnteCertificatoreFilterDO extends EntityFilter implements Serializable {

	/**
	 * 
	 */
	
	protected String id;
	
	private Boolean onlyForMed;
	
	
		
	public EnteCertificatoreFilterDO(String  fetchrule) {
		super(fetchrule);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getOnlyForMed() {
		return onlyForMed;
	}

	public void setOnlyForMed(Boolean onlyForMed) {
		this.onlyForMed = onlyForMed;
	}

	
	


}
