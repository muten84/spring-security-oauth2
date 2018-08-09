package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.NotificheEventoDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.NotificheEventoDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.NotificheEventoEseguitoDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.NotificheEventoDO;
import it.eng.areas.ems.sdodaeservices.entity.NotificheTypeEnum;
import it.eng.areas.ems.sdodaeservices.entity.filter.NotificheEventoFilterDO;

@Repository
public class NotificheEventoDAOImpl extends EntityDAOImpl<NotificheEventoDO, String> implements NotificheEventoDAO {

	public NotificheEventoDAOImpl() {
	}

	@Override
	public Class<NotificheEventoDO> getEntityClass() {
		return NotificheEventoDO.class;
	}

	protected FetchRule getFetchRule(String name) {
		if (name.equals(NotificheEventoDeepDepthRule.NAME)) {
			return new NotificheEventoDeepDepthRule();
		} else if (name.equals(NotificheEventoEseguitoDepthRule.NAME)) {
			return new NotificheEventoEseguitoDepthRule();
		}

		return null;
	}

	@Override
	public List<NotificheEventoDO> searchNotificheEventoByFilter(NotificheEventoFilterDO filter) {
		Criteria criteria = createCriteria(filter);

		if (!StringUtils.isEmpty(filter.getEvent())) {
			criteria.createAlias("event", "evt", JoinType.LEFT_OUTER_JOIN);
			criteria.add(Restrictions.eq("evt.id", filter.getEvent()));
		}

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public NotificheEventoDO insertNotificheEvento(NotificheEventoDO notificheEventoDO) {

		return this.save(notificheEventoDO);
	}

	@Override
	public long countNotified(String id) {
		Criteria criteria = createCriteria();

		criteria.add(Restrictions.eq("event.id", id));
		criteria.add(Restrictions.eq("tipoNotifica", NotificheTypeEnum.NUOVA_EMERGENZA));

		ProjectionList proj = Projections.projectionList();
		proj.add(Projections.count("firstResponder.id"));

		criteria.setProjection(proj);

		Long res = (Long) criteria.uniqueResult();

		return (long) (res != null ? res : 0);
	}

}
