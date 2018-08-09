package it.eng.areas.to.sdoto.docservice.dao.impl;


import it.eng.areas.to.sdoto.docservice.entity.*;
import it.eng.areas.to.sdoto.docservice.dao.*;
import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class ExampleDAOImpl extends EntityDAOImpl<ExampleDO, String> implements ExampleDAO{
	
	public ExampleDAOImpl() {
	}

	@Override
	public Class<ExampleDO> getEntityClass() {
		return ExampleDO.class;
	}
	
	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}
	
}
