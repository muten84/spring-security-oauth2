package it.eng.areas.ems.sdodaeservices.entity.filter;

import java.util.List;

import it.eng.area118.sdocommon.dao.filter.EntityFilter;
import it.eng.area118.sdocommon.dao.filter.OrderBy;
import it.eng.area118.sdocommon.dao.filter.Paging;

/**
 * Classe per distinguere i filtri che implicano una restrizione territoriale
 * 
 * @author Miranda Mauro
 *
 */
public class TerritorialFilterDO extends EntityFilter {

	private List<String> provinces;

	private List<String> municipalities;

	public TerritorialFilterDO() {
		super();
	}

	public TerritorialFilterDO(OrderBy orderBy) {
		super(orderBy);
	}

	public TerritorialFilterDO(Paging paging, String fetchRule, OrderBy orderBy) {
		super(paging, fetchRule, orderBy);
	}

	public TerritorialFilterDO(Paging paging, String fetchRule) {
		super(paging, fetchRule);
	}

	public TerritorialFilterDO(Paging paging) {
		super(paging);
	}

	public TerritorialFilterDO(String fetchRule) {
		super(fetchRule);
	}

	public List<String> getProvinces() {
		return provinces;
	}

	public void setProvinces(List<String> provinces) {
		this.provinces = provinces;
	}

	public List<String> getMunicipalities() {
		return municipalities;
	}

	public void setMunicipalities(List<String> municipalities) {
		this.municipalities = municipalities;
	}

}
