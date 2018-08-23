package it.eng.areas.ems.ordinari.dao.impl;

import java.util.List;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.ordinari.dao.WebBookingEquipmentDAO;
import it.eng.areas.ems.ordinari.dao.rule.WebBookingDeepDepthRule;
import it.eng.areas.ems.ordinari.entity.WebBookingDO;
import it.eng.areas.ems.ordinari.entity.WebBookingEquipmentDO;
import it.esel.parsley.lang.StringUtils;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class WebBookingEquipmentDAOImpl extends EntityDAOImpl<WebBookingEquipmentDO,String> implements WebBookingEquipmentDAO {


	public Class<WebBookingEquipmentDO> getEntityClass() {
		return WebBookingEquipmentDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		if (name.equals(WebBookingDeepDepthRule.NAME)) {

			return new WebBookingDeepDepthRule();
		}

		return null;
		
	}

	@Override
	public WebBookingEquipmentDO searchWebBookingEquipmentByDescription(
			String description) {
	
		Criteria criteria = createCriteria(WebBookingEquipmentDO.class);
		

		criteria.add(Restrictions.eq("equipmentCompact",	description));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (WebBookingEquipmentDO) criteria.uniqueResult();


	}
	

}
