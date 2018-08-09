package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.RuoloDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.RuoloDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.RuoloDO;

@Repository
public class RuoloDAOImpl extends EntityDAOImpl<RuoloDO, String> implements RuoloDAO {

	public RuoloDAOImpl() {
	}

	@Override
	public Class<RuoloDO> getEntityClass() {
		return RuoloDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		if (name.equals(RuoloDeepDepthRule.NAME)) {
			return new RuoloDeepDepthRule();
		}
		return null;
	}

	@Override
	public RuoloDO insertRuolo(RuoloDO ruoloDO) {

		return this.save(ruoloDO);
	}

	@Override
	public RuoloDO getRuoloByName(String name) {
		Criteria cri = createCriteria();
		cri.add(Restrictions.eq("nome", name));
		return (RuoloDO) cri.uniqueResult();
	}

	@Override
	public List<RuoloDO> getAllRuoli() {
		Criteria cri = createCriteria();
		cri.add(Restrictions.eq("visible", true));
		return cri.list();
	}
}
