package it.eng.areas.ems.sdodaeservices.dao.impl;


import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.QuestionarioFirstResponderDAO;
import it.eng.areas.ems.sdodaeservices.entity.QuestionarioFirstResponderDO;

@Repository public class  QuestionarioFirstResponderDAOImpl extends EntityDAOImpl<QuestionarioFirstResponderDO, String> implements QuestionarioFirstResponderDAO{
	
	public QuestionarioFirstResponderDAOImpl() {
	}

	@Override
	public Class<QuestionarioFirstResponderDO> getEntityClass() {
		return QuestionarioFirstResponderDO.class;
	}
	
	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}
	
}
