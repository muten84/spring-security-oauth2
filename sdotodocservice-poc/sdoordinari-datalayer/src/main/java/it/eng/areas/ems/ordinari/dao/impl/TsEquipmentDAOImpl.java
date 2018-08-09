package it.eng.areas.ems.ordinari.dao.impl;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.ordinari.dao.TsEquipmentDAO;
import it.eng.areas.ems.ordinari.dao.rule.BookingDetailRule;
import it.eng.areas.ems.ordinari.entity.TsEquipmentDO;
import it.esel.parsley.lang.StringUtils;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class TsEquipmentDAOImpl extends EntityDAOImpl<TsEquipmentDO,String> implements TsEquipmentDAO {

	public Class<TsEquipmentDO> getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		if (name == null) {
			name = "DEEP";
		}
		if (name.equals(BookingDetailRule.NAME)) {
			return new BookingDetailRule();
		}
		return null;
	}

	@Override
	public TsEquipmentDO getTsEquipmentByDescription(String description,String fetchRule) {
		
		
		Criteria criteria = createCriteria();
		if (!StringUtils.isEmpty(fetchRule)) {
			criteria.setFetchMode(fetchRule, FetchMode.JOIN);
		}

		criteria.add(Restrictions.eq("description",	description));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (TsEquipmentDO) criteria.list().get(0);
		
	}

}