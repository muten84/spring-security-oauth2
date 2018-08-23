package it.eng.areas.ems.sdodaeservices.dao.impl;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.StatoDAEDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.StatoDAEDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.StatoDAEDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.StatoDAEFilterDO;

@Repository public class  StatoDAEDAOImpl extends EntityDAOImpl<StatoDAEDO, String> implements StatoDAEDAO{
	
	public StatoDAEDAOImpl() {
	}

	
	@Override
	public Class<StatoDAEDO> getEntityClass() {
		return StatoDAEDO.class;
	}
	


		
	protected FetchRule getFetchRule(String name) {
		if (name.equals(StatoDAEDeepDepthRule.NAME)  ){
				
				return new StatoDAEDeepDepthRule();
			}
				
			return null;	}
	
		
		public List<StatoDAEDO> searchStatoDAEByFilter(StatoDAEFilterDO filter) {
			return searchStatoDAEByFilter(filter, null);
			}
			public List<StatoDAEDO> searchStatoDAEByFilter(StatoDAEFilterDO filter, String fetchRule) {
				Criteria criteria = createCriteria(filter);
				
				if (!fetchRule.equals(null)){
				criteria.setFetchMode(fetchRule, FetchMode.JOIN);
				}
				
				
				criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
				return criteria.list();
			}

			@Override
			public StatoDAEDO insertStatoDAE(StatoDAEDO statoDaeDO) {
				
			
				return this.save(statoDaeDO);
			}


	
}


