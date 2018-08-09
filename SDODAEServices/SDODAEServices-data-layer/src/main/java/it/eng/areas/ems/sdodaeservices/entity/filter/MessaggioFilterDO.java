package it.eng.areas.ems.sdodaeservices.entity.filter;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import it.eng.area118.sdocommon.dao.filter.EntityFilter;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class MessaggioFilterDO extends EntityFilter implements Serializable {

	/**
	 * 
	 */

	protected String id;

	private Date da;

	private Date a;

	public MessaggioFilterDO() {
		super();
	}

	public MessaggioFilterDO(String fetchrule) {
		super(fetchrule);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDa() {
		return da;
	}

	public void setDa(Date da) {
		this.da = da;
	}

	public Date getA() {
		return a;
	}

	public void setA(Date a) {
		this.a = a;
	}

}
