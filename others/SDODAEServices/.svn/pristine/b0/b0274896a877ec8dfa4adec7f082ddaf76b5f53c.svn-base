package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.MailTraceDAO;
import it.eng.areas.ems.sdodaeservices.entity.MailTraceDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.MailTraceFilterDO;
import it.esel.parsley.lang.StringUtils;

@Repository
public class MailTraceDAOImpl extends EntityDAOImpl<MailTraceDO, String> implements MailTraceDAO {

	@Override
	public Class<MailTraceDO> getEntityClass() {
		return MailTraceDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String arg0) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MailTraceDO> searchMailTraceByFilter(MailTraceFilterDO filter) {
		Criteria c = createCriteria();

		if (!StringUtils.isEmpty(filter.getDestinatario())) {
			c.add(Restrictions.like("destinatario", "%" + filter.getDestinatario() + "%"));
		}

		if (filter.getDataA() != null) {
			c.add(Restrictions.le("dataInvio", filter.getDataA()));
		}
		if (filter.getDataDa() != null) {
			c.add(Restrictions.ge("dataInvio", filter.getDataDa()));
		}
		c.setFetchMode("mailTemplate", FetchMode.JOIN);
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		c.addOrder(Order.desc("dataInvio"));
		return c.list();
	}

}
