package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.ResponsabileProvinciaDAO;
import it.eng.areas.ems.sdodaeservices.entity.ResponsabileProvinciaDO;

@Repository
public class ResponsabileProvinciaDAOImpl extends EntityDAOImpl<ResponsabileProvinciaDO, String>
		implements ResponsabileProvinciaDAO {

	public ResponsabileProvinciaDAOImpl() {
	}

	@Override
	public Class<ResponsabileProvinciaDO> getEntityClass() {
		return ResponsabileProvinciaDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}

	@Override
	public List<ResponsabileProvinciaDO> searchResponsabileByProvince(String province) {
		Criteria c = createCriteria();
		c.createAlias("provincia", "provincia");

		c.add(Restrictions.eq("provincia.nomeProvincia", province));
		return c.list();

	}

}
