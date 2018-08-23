package it.eng.areas.ems.sdodaeservices.delegate.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.eng.areas.ems.common.sdo.dto.CompoundDTORule;
import it.eng.areas.ems.common.sdo.dto.DTOService;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.CategoriaFrDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.CategoriaFrDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.CategoriaFr;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.CategoriaFrFilter;
import it.eng.areas.ems.sdodaeservices.entity.CategoriaFrDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.CategoriaFrFilterDO;
//import it.eng.areas.ems.sdodaeservices.entity.gis.VCTCategoriaFrDO;
import it.eng.areas.ems.sdodaeservices.service.CategoriaFrTransactionalService;

@Component
public class CategoriaFrDelegateServiceImpl implements CategoriaFrDelegateService {

	private Logger logger = LoggerFactory.getLogger(DaeDelegateServiceImpl.class);

	@Autowired
	private CategoriaFrTransactionalService categoriaFrService;

	@Autowired
	private DTOService dtoService;

	@Override
	public long countAll() {
		try {
			return categoriaFrService.countAll();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING countAll", e);
			return 0l;
		}
	}

	@Override
	public List<CategoriaFr> getAllCategoriaFr() {
		try {

			List<CategoriaFrDO> categoriaFrDoList = categoriaFrService.getAllCategoriaFr();

			List<CategoriaFr> categoriaFrList = (List<CategoriaFr>) dtoService.convertCollection(categoriaFrDoList,
					CategoriaFr.class,
					new CompoundDTORule(CategoriaFrDO.class, CategoriaFr.class, CategoriaFrDeepDepthRule.NAME));

			return categoriaFrList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllCategoriaFr", e);
			return null;
		}

	}

	@Override
	public CategoriaFr getCategoriaFrById(String id) {
		try {

			CategoriaFrDO categoriaFrDo = null;

			categoriaFrDo = categoriaFrService.getCategoriaFrById(id);

			CategoriaFr categoriaFr = (CategoriaFr) dtoService.convertObject(categoriaFrDo, CategoriaFr.class,
					new CompoundDTORule(CategoriaFrDO.class, CategoriaFr.class, CategoriaFrDeepDepthRule.NAME));

			return categoriaFr;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getCategoriaFrById", e);
			return null;
		}
	}

	@Override
	public List<CategoriaFr> getCategoriaFrByFilter(CategoriaFrFilter categoriaFrFilter) {
		try {
			List<CategoriaFrDO> categoriaFrDoList = null;

			CategoriaFrFilterDO categoriaFrFilterDO = null;

			categoriaFrFilterDO = dtoService.convertObject(categoriaFrFilter, CategoriaFrFilterDO.class,
					new CompoundDTORule(CategoriaFrFilter.class, CategoriaFrFilterDO.class,
							CategoriaFrDeepDepthRule.NAME));

			categoriaFrDoList = categoriaFrService.getCategoriaFrByFilter(categoriaFrFilterDO);

			List<CategoriaFr> categoriaFrList = (List<CategoriaFr>) dtoService.convertCollection(categoriaFrDoList,
					CategoriaFr.class,
					new CompoundDTORule(CategoriaFrDO.class, CategoriaFr.class, CategoriaFrDeepDepthRule.NAME));

			return categoriaFrList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getCategoriaFrByFilter", e);
			return null;
		}
	}

	@Override
	public CategoriaFr insertCategoriaFr(CategoriaFr categoriaFr) {

		try {
			CategoriaFrDO categoriaFrDO = (CategoriaFrDO) dtoService.convertObject(categoriaFr, CategoriaFrDO.class,
					new CompoundDTORule(CategoriaFr.class, CategoriaFrDO.class, CategoriaFrDeepDepthRule.NAME));

			CategoriaFrDO returnedCategoriaFrDo = categoriaFrService.insertCategoriaFr(categoriaFrDO);
			returnedCategoriaFrDo = categoriaFrService.getCategoriaFrById(returnedCategoriaFrDo.getId());

			CategoriaFr returnedCategoriaFr = (CategoriaFr) dtoService.convertObject(returnedCategoriaFrDo,
					CategoriaFr.class,
					new CompoundDTORule(CategoriaFrDO.class, CategoriaFr.class, CategoriaFrDeepDepthRule.NAME));

			return returnedCategoriaFr;

		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING insertCategoriaFr", e);
			return null;
		}
	}

	/*
	 * @Override public List<VCTCategoriaFrDO> getAllVctCategoriaFr() { try {
	 * ContextHolder.setDataSourceType(DataSourceType.GIS); return
	 * categoriaFrService.getAllVctCategoriaFr(); } finally {
	 * ContextHolder.clearDataSourceType(); } }
	 */

	@Override
	public boolean deleteCategoriaFrById(String id) {
		try {
			return categoriaFrService.deleteCategoriaFrById(id);
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING deleteCategoriaFrById", e);
			return false;
		}
	}

}
