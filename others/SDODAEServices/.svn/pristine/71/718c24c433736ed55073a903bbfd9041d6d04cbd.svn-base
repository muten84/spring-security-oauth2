package it.eng.areas.ems.sdodaeservices.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.ResetPasswordDAO;
import it.eng.areas.ems.sdodaeservices.entity.ResetPasswordDO;
import it.eng.areas.ems.sdodaeservices.entity.ResetPasswordDO.Stato;

@Repository
public class ResetPasswordDAOImpl extends EntityDAOImpl<ResetPasswordDO, String> implements ResetPasswordDAO {

	@Override
	public Class<ResetPasswordDO> getEntityClass() {
		return ResetPasswordDO.class;
	}

	@Override
	public ResetPasswordDO searchByTokenAndStato(String token, Stato stato) {
		Criteria cri = createCriteria();

		cri.add(Restrictions.eq("token", token));
		cri.add(Restrictions.eq("stato", stato));

		cri.setFetchMode("utente", FetchMode.JOIN);
		cri.setFetchMode("utente.passwordHistories", FetchMode.JOIN);

		return (ResetPasswordDO) cri.uniqueResult();
	}

	@Override
	protected FetchRule getFetchRule(String arg0) {
		return null;
	}

}
