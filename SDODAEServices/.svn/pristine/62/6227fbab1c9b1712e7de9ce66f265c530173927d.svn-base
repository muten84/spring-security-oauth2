package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.MobileTokenDAO;
import it.eng.areas.ems.sdodaeservices.entity.DaeMobileTokenDO;

@Repository
public class MobileTokenDAOImpl extends EntityDAOImpl<DaeMobileTokenDO, String> implements MobileTokenDAO {

	@Override
	public Class<DaeMobileTokenDO> getEntityClass() {
		// TODO Auto-generated method stub
		return DaeMobileTokenDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DaeMobileTokenDO> getExpiredTokens(Integer timeOutMinutes) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.isNull("expiredTimeStamp"));
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -timeOutMinutes);
		criteria.add(
				Restrictions.or(Restrictions.le("renewedTimeStamp", cal), Restrictions.isNull("renewedTimeStamp")));
		criteria.add(Restrictions.le("creationTimeStamp", cal));
		List<DaeMobileTokenDO> list = criteria.list();
		// carico tutti i token con la data di scadenza inserita
		criteria = createCriteria();
		criteria.add(Restrictions.isNotNull("expiredTimeStamp"));
		list.addAll(criteria.list());
		//
		return list;
	}

	@Override
	public List<DaeMobileTokenDO> getTokensNotExpiredByUserId(String userId) {
		Criteria criteria = createCriteria();
		// carico solo i token non scaduti di quel utente
		criteria.add(Restrictions.eq("utenteId", userId));
		criteria.add(Restrictions.isNull("expiredTimeStamp"));

		return criteria.list();
	}

	@Override
	public List<DaeMobileTokenDO> getTokensByUserId(String userId) {
		Criteria criteria = createCriteria();
		// carico solo i token non scaduti di quel utente
		criteria.add(Restrictions.eq("utenteId", userId));

		return criteria.list();
	}

}
