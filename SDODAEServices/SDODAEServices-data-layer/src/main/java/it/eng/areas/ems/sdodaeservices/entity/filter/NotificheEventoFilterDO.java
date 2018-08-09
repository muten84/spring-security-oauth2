package it.eng.areas.ems.sdodaeservices.entity.filter;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import it.eng.area118.sdocommon.dao.filter.EntityFilter;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class NotificheEventoFilterDO extends EntityFilter implements Serializable {

	/**
	 * 
	 */

	protected String id;

	protected String event;

	public NotificheEventoFilterDO() {
	}

	public NotificheEventoFilterDO(String fetchRule) {
		super(fetchRule);
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

}
