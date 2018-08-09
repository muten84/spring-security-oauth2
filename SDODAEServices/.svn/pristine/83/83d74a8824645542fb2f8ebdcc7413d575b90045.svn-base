package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.bean.DaeSubscription;
import it.eng.areas.ems.sdodaeservices.dao.DaeHistoryDAO;
import it.eng.areas.ems.sdodaeservices.entity.DaeHistoryDO;
import it.eng.areas.ems.sdodaeservices.entity.enums.Operation;

@Repository
public class DaeHistoryDAOImpl extends EntityDAOImpl<DaeHistoryDO, String> implements DaeHistoryDAO {

	@Override
	public Class<DaeHistoryDO> getEntityClass() {
		return DaeHistoryDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}

	@Override
	public List<DaeSubscription> listDAEValidation(Calendar from, Calendar to) {
		Criteria criteria = createCriteria();
		ProjectionList projL = Projections.projectionList();
		projL.add(Projections.sqlGroupProjection(
				"count(*) as numDAE, to_char(this_.operation_date, 'yyyy-MM-dd') as aaaammgg",
				"to_char(this_.operation_date, 'yyyy-MM-dd') order by aaaammgg asc",
				new String[] { "numDAE", "aaaammgg" }, new Type[] { IntegerType.INSTANCE, StringType.INSTANCE }));
		criteria.setProjection(projL);
		criteria.setResultTransformer(Transformers.aliasToBean(DaeSubscription.class));

		criteria.add(Restrictions.ge("operationDate", from.getTime()));
		criteria.add(Restrictions.le("operationDate", to.getTime()));

		criteria.add(Restrictions.eq("operation", Operation.VALIDATE));

		return criteria.list();
	}

}
