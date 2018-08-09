package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.ProgrammaManutenzioneHistoryDAO;
import it.eng.areas.ems.sdodaeservices.entity.ProgrammaManutenzioneHistoryDO;

@Repository
public class ProgrammaManutenzioneHistoryDAOImpl extends EntityDAOImpl<ProgrammaManutenzioneHistoryDO, String>
		implements ProgrammaManutenzioneHistoryDAO {

	public ProgrammaManutenzioneHistoryDAOImpl() {
	}

	@Override
	public Class<ProgrammaManutenzioneHistoryDO> getEntityClass() {
		return ProgrammaManutenzioneHistoryDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}

	@Override
	public List<ProgrammaManutenzioneHistoryDO> listByProgrammaId(String id) {
		Criteria c = createCriteria();

		c.setFetchMode("utente", FetchMode.JOIN);

		c.add(Restrictions.eq("programmaManutenzione.id", id));

		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return c.list();
	}

}
