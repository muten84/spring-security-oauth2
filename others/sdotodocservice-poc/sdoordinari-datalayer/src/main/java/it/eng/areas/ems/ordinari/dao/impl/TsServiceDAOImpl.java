package it.eng.areas.ems.ordinari.dao.impl;

import it.eng.area118.sdocommon.dao.DataAccessException;
import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;



import it.eng.areas.ems.ordinari.dao.DdtDAO;
import it.eng.areas.ems.ordinari.dao.TsServiceDAO;
import it.eng.areas.ems.ordinari.entity.DdtDO;
import it.eng.areas.ems.ordinari.entity.TsServiceDO;

import java.util.List;

import javax.persistence.Id;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class TsServiceDAOImpl extends EntityDAOImpl<TsServiceDO,String> implements TsServiceDAO {

	public Class<TsServiceDO> getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected FetchRule getFetchRule(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
