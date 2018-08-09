package it.eng.areas.ems.sdodaeservices.dao.impl;


import java.util.Iterator;

import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.QuestionarioDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.QuestionarioDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.DomandaQuestionarioDO;
import it.eng.areas.ems.sdodaeservices.entity.QuestionarioDO;

@Repository public class  QuestionarioDAOImpl extends EntityDAOImpl<QuestionarioDO, String> implements QuestionarioDAO{
	
	public QuestionarioDAOImpl() {
	}

	@Override
	public Class<QuestionarioDO> getEntityClass() {
		return QuestionarioDO.class;
	}
	
	@Override
	protected FetchRule getFetchRule(String name) {
		if (name.equals(QuestionarioDeepDepthRule.NAME)) {

			return new QuestionarioDeepDepthRule();
		}

		return null;
	}

	
	@Override
	public QuestionarioDO insertQuestionario(QuestionarioDO questionarioDO) {

		Iterator<DomandaQuestionarioDO> dqi = questionarioDO.getDomandaQuestionario().iterator();  

		while (dqi.hasNext()){
		      dqi.next().setQuestionario(questionarioDO);
		    }
		
		return this.save(questionarioDO);
	}

	
	
}
