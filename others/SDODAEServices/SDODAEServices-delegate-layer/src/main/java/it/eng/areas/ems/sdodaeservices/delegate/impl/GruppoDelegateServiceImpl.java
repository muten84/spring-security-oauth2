package it.eng.areas.ems.sdodaeservices.delegate.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.eng.areas.ems.common.sdo.dto.CompoundDTORule;
import it.eng.areas.ems.common.sdo.dto.DTOService;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.GruppoDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.GruppoDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.Gruppo;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.GruppoFilter;
import it.eng.areas.ems.sdodaeservices.entity.GruppoDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.GruppoFilterDO;
//import it.eng.areas.ems.sdodaeservices.entity.gis.VCTGruppoDO;
import it.eng.areas.ems.sdodaeservices.service.GruppoTransactionalService;

@Component
public class GruppoDelegateServiceImpl implements GruppoDelegateService {

	private Logger logger = LoggerFactory.getLogger(GruppoDelegateServiceImpl.class);

	@Autowired
	private GruppoTransactionalService gruppoService;

	@Autowired
	private DTOService dtoService;

	@Override
	public long countAll() {
		// TODO Auto-generated method stub

		return gruppoService.countAll();
	}

	@Override
	public List<Gruppo> getAllGruppo() {
		try {
			List<GruppoDO> gruppoDoList = gruppoService.getAllGruppo();
			List<Gruppo> gruppoList = (List<Gruppo>) dtoService.convertCollection(gruppoDoList, Gruppo.class,
					new CompoundDTORule(GruppoDO.class, Gruppo.class, GruppoDeepDepthRule.NAME));
			return gruppoList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllGruppo", e);
			return null;
		}

	}

	@Override
	public Gruppo getGruppoById(String id) {
		try {
			GruppoDO gruppoDo = null;

			gruppoDo = gruppoService.getGruppoById(id);

			Gruppo gruppo = (Gruppo) dtoService.convertObject(gruppoDo, Gruppo.class,
					new CompoundDTORule(GruppoDO.class, Gruppo.class, GruppoDeepDepthRule.NAME));
			return gruppo;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getGruppoById", e);
			return null;
		}

	}

	@Override
	public List<Gruppo> getGruppoByFilter(GruppoFilter gruppoFilter) {
		try {
			List<GruppoDO> gruppoDoList = null;

			GruppoFilterDO gruppoFilterDO = null;

			gruppoFilterDO = dtoService.convertObject(gruppoFilter, GruppoFilterDO.class,
					new CompoundDTORule(GruppoFilter.class, GruppoFilterDO.class, GruppoDeepDepthRule.NAME));

			gruppoDoList = gruppoService.getGruppoByFilter(gruppoFilterDO);

			List<Gruppo> gruppoList = (List<Gruppo>) dtoService.convertCollection(gruppoDoList, Gruppo.class,
					new CompoundDTORule(GruppoDO.class, Gruppo.class, GruppoDeepDepthRule.NAME));

			return gruppoList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getGruppoByFilter", e);
			return null;
		}
	}

	@Override
	public Gruppo insertGruppo(Gruppo gruppo) {

		try {
			GruppoDO gruppoDO = (GruppoDO) dtoService.convertObject(gruppo, GruppoDO.class,
					new CompoundDTORule(Gruppo.class, GruppoDO.class, GruppoDeepDepthRule.NAME));

			GruppoDO returnedGruppoDo = gruppoService.insertGruppo(gruppoDO);
			returnedGruppoDo = gruppoService.getGruppoById(returnedGruppoDo.getId());

			Gruppo returnedGruppo = (Gruppo) dtoService.convertObject(returnedGruppoDo, Gruppo.class,
					new CompoundDTORule(GruppoDO.class, Gruppo.class, GruppoDeepDepthRule.NAME));

			return returnedGruppo;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING insertGruppo", e);
			return null;
		}
	}

	/*
	 * @Override public List<VCTGruppoDO> getAllVctGruppo() { try {
	 * ContextHolder.setDataSourceType(DataSourceType.GIS); return
	 * gruppoService.getAllVctGruppo(); } finally {
	 * ContextHolder.clearDataSourceType(); } }
	 */

	@Override
	public boolean deleteGruppoById(String id) {
		try {
			return gruppoService.deleteGruppoById(id);
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllVctDae", e);
			return false;
		}
	}

}
