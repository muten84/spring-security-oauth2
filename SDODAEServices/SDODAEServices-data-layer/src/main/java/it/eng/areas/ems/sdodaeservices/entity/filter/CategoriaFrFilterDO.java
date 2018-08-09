package it.eng.areas.ems.sdodaeservices.entity.filter;


import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import it.eng.area118.sdocommon.dao.filter.EntityFilter;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoriaFrFilterDO extends EntityFilter implements Serializable {

	private String id;
	
	
	private Boolean forApp;
	
	
	public CategoriaFrFilterDO() {
		// TODO Auto-generated constructor stub
	}
	
		
	public CategoriaFrFilterDO(String  fetchrule) {
		super(fetchrule);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getForApp() {
		return forApp;
	}

	public void setForApp(Boolean forApp) {
		this.forApp = forApp;
	}
	
	
	



}
