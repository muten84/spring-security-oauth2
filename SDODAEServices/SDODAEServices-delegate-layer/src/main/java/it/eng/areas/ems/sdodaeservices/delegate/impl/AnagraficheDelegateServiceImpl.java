package it.eng.areas.ems.sdodaeservices.delegate.impl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.eng.areas.ems.common.sdo.dto.CompoundDTORule;
import it.eng.areas.ems.common.sdo.dto.DTOService;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.ComuniProvinceDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.RuoloDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.StradeDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.TipoStrutturaDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.CategoriaFr;
import it.eng.areas.ems.sdodaeservices.delegate.model.Comune;
import it.eng.areas.ems.sdodaeservices.delegate.model.Config;
import it.eng.areas.ems.sdodaeservices.delegate.model.DashboardPosition;
import it.eng.areas.ems.sdodaeservices.delegate.model.EnteCertificatore;
import it.eng.areas.ems.sdodaeservices.delegate.model.Localita;
import it.eng.areas.ems.sdodaeservices.delegate.model.Professione;
import it.eng.areas.ems.sdodaeservices.delegate.model.ProfessioneFilter;
import it.eng.areas.ems.sdodaeservices.delegate.model.Provincia;
import it.eng.areas.ems.sdodaeservices.delegate.model.Ruolo;
import it.eng.areas.ems.sdodaeservices.delegate.model.StaticData;
import it.eng.areas.ems.sdodaeservices.delegate.model.Stato;
import it.eng.areas.ems.sdodaeservices.delegate.model.StatoDAE;
import it.eng.areas.ems.sdodaeservices.delegate.model.Strade;
import it.eng.areas.ems.sdodaeservices.delegate.model.TipoStruttura;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.ComuneFilter;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.LocalitaFilter;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.StradeFilter;
import it.eng.areas.ems.sdodaeservices.entity.CategoriaFrDO;
import it.eng.areas.ems.sdodaeservices.entity.ComuneDO;
import it.eng.areas.ems.sdodaeservices.entity.ConfigDO;
import it.eng.areas.ems.sdodaeservices.entity.DashboardPositionDO;
import it.eng.areas.ems.sdodaeservices.entity.EnteCertificatoreDO;
import it.eng.areas.ems.sdodaeservices.entity.LocalitaDO;
import it.eng.areas.ems.sdodaeservices.entity.ProfessioneDO;
import it.eng.areas.ems.sdodaeservices.entity.ProvinciaDO;
import it.eng.areas.ems.sdodaeservices.entity.RuoloDO;
import it.eng.areas.ems.sdodaeservices.entity.StaticDataDO;
import it.eng.areas.ems.sdodaeservices.entity.StatoDAEDO;
import it.eng.areas.ems.sdodaeservices.entity.StatoDO;
import it.eng.areas.ems.sdodaeservices.entity.StradeDO;
import it.eng.areas.ems.sdodaeservices.entity.TipoStrutturaDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.ComuneFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.LocalitaFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.ProfessioneFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.StradeFilterDO;
//import it.eng.areas.ems.sdodaeservices.entity.gis.VCTComuniProvinceDO;
import it.eng.areas.ems.sdodaeservices.service.AnagraficheTransactionalService;

@Component
public class AnagraficheDelegateServiceImpl implements AnagraficheDelegateService {

	private Logger logger = LoggerFactory.getLogger(AnagraficheDelegateServiceImpl.class);

	@Autowired
	private AnagraficheTransactionalService anagraficheTransactionalService;

	@Autowired
	private DTOService dtoService;

	@Override
	public List<Comune> getComuniByFilter(ComuneFilter comuneFilter) {
		try {
			List<ComuneDO> comuniDoList = null;

			ComuneFilterDO comuneFilterDO = null;

			comuneFilterDO = dtoService.convertObject(comuneFilter, ComuneFilterDO.class,
					new CompoundDTORule(ComuneFilter.class, ComuneFilterDO.class, ComuniProvinceDeepDepthRule.NAME));

			comuniDoList = anagraficheTransactionalService.searchComuniByFilter(comuneFilterDO);

			List<Comune> comuniList = (List<Comune>) dtoService.convertCollection(comuniDoList, Comune.class,
					new CompoundDTORule(ComuneDO.class, Comune.class, ComuniProvinceDeepDepthRule.NAME));

			return comuniList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING searchComuniByFilter", e);
			return null;
		}

	}

	@Override
	public List<Provincia> getAllProvince() {
		try {
			List<ProvinciaDO> provinceDoList = anagraficheTransactionalService.getAllProvince();
			List<Provincia> provinceList = (List<Provincia>) dtoService.convertCollection(provinceDoList,
					Provincia.class,
					new CompoundDTORule(ProvinciaDO.class, Provincia.class, ComuniProvinceDeepDepthRule.NAME));
			return provinceList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllProvince", e);
			return null;
		}

	}

	@Override
	public List<Provincia> getProvinceByArea(String[] area) {

		List<String> list = Arrays.asList(area);

		try {
			List<ProvinciaDO> provinceDoList = anagraficheTransactionalService.getAllProvince();
			provinceDoList = provinceDoList.stream().filter((p) -> {
				return list.contains(p.getSigla());
			}).collect(Collectors.toList());

			List<Provincia> provinceList = (List<Provincia>) dtoService.convertCollection(provinceDoList,
					Provincia.class,
					new CompoundDTORule(ProvinciaDO.class, Provincia.class, ComuniProvinceDeepDepthRule.NAME));
			return provinceList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getProvinceByArea", e);
			return null;
		}

	}

	@Override
	public List<TipoStruttura> getAllTipoStruttura() {
		try {
			List<TipoStrutturaDO> tipoStrutturaDoList = anagraficheTransactionalService.getAllTipoStruttura();
			List<TipoStruttura> tipoStrutturaList = (List<TipoStruttura>) dtoService.convertCollection(
					tipoStrutturaDoList, TipoStruttura.class,
					new CompoundDTORule(TipoStrutturaDO.class, TipoStruttura.class, TipoStrutturaDeepDepthRule.NAME));
			return tipoStrutturaList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllTipoStruttura", e);
			return null;
		}

	}

	@Override
	public List<Strade> searchStradeByFilter(StradeFilter stradaFilter) {
		try {
			List<StradeDO> stradeDoList = null;

			StradeFilterDO stradaFilterDO = null;

			stradaFilterDO = dtoService.convertObject(stradaFilter, StradeFilterDO.class,
					new CompoundDTORule(StradeFilter.class, StradeFilterDO.class, StradeDeepDepthRule.NAME));

			stradeDoList = anagraficheTransactionalService.searchStradeByFilter(stradaFilterDO);

			List<Strade> stradeList = (List<Strade>) dtoService.convertCollection(stradeDoList, Strade.class,
					new CompoundDTORule(StradeDO.class, Strade.class, stradaFilter.getFetchRule()));

			return stradeList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getStradeByFilter", e);
			return null;
		}
	}

	@Override
	public List<Strade> getAllStrade() {
		try {
			List<StradeDO> stradeDoList = anagraficheTransactionalService.getAllStrade();
			List<Strade> stradeList = (List<Strade>) dtoService.convertCollection(stradeDoList, Strade.class,
					new CompoundDTORule(StradeDO.class, Strade.class, StradeDeepDepthRule.NAME));
			return stradeList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllStrade", e);
			return null;
		}

	}

	@Override
	public List<Ruolo> getAllRuolo() {
		try {
			List<RuoloDO> ruoloDoList = anagraficheTransactionalService.getAllRuolo();
			List<Ruolo> ruoloList = (List<Ruolo>) dtoService.convertCollection(ruoloDoList, Ruolo.class,
					new CompoundDTORule(RuoloDO.class, Ruolo.class, RuoloDeepDepthRule.NAME));
			return ruoloList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllRuolo", e);
			return null;
		}

	}

	@Override
	public Ruolo insertRuolo(Ruolo ruolo) {

		try {

			RuoloDO ruoloDO = (RuoloDO) dtoService.convertObject(ruolo, RuoloDO.class,
					new CompoundDTORule(Ruolo.class, RuoloDO.class, RuoloDeepDepthRule.NAME));

			RuoloDO returnedRuoloDo = anagraficheTransactionalService.insertRuolo(ruoloDO);
			returnedRuoloDo = anagraficheTransactionalService.getRuoloById(returnedRuoloDo.getId());

			Ruolo returnedRuolo = (Ruolo) dtoService.convertObject(returnedRuoloDo, Ruolo.class,
					new CompoundDTORule(RuoloDO.class, Ruolo.class, RuoloDeepDepthRule.NAME));

			return returnedRuolo;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING insertRuolo", e);
			throw e;
		}

	}

	@Override
	public Ruolo getRuoloById(String id) {

		try {
			RuoloDO ruoloDo = null;
			ruoloDo = anagraficheTransactionalService.getRuoloById(id);
			Ruolo ruolo = (Ruolo) dtoService.convertObject(ruoloDo, Ruolo.class,
					new CompoundDTORule(RuoloDO.class, Ruolo.class, RuoloDeepDepthRule.NAME));
			return ruolo;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getRuoloById", e);
			return null;
		}

	}

	@Override
	public boolean deleteRuoloById(String id) {
		try {
			return anagraficheTransactionalService.deleteRuoloById(id);
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING deleteRuoloById", e);
			return false;
		}

	}

	@Override
	public List<CategoriaFr> getAllCategoriaFR() {
		try {
			List<CategoriaFrDO> catDOList = anagraficheTransactionalService.getAllCategoriaFR();
			List<CategoriaFr> catList = (List<CategoriaFr>) dtoService.convertCollection(catDOList, CategoriaFr.class,
					new CompoundDTORule(CategoriaFrDO.class, CategoriaFr.class, "BARE"));
			return catList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllCategoriaFR", e);
			throw e;
		}
	}

	@Override
	public List<CategoriaFr> getAllCategoriaFRForApp() {
		try {
			List<CategoriaFrDO> catDOList = anagraficheTransactionalService.getAllCategoriaFRForApp();
			List<CategoriaFr> catList = (List<CategoriaFr>) dtoService.convertCollection(catDOList, CategoriaFr.class,
					new CompoundDTORule(CategoriaFrDO.class, CategoriaFr.class, "BARE"));
			return catList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllCategoriaFR", e);
			throw e;
		}
	}

	@Override
	public List<Professione> getAllProfessione() {
		try {
			List<ProfessioneDO> professioneDOList = anagraficheTransactionalService
					.searchProfessioneByFilter(new ProfessioneFilterDO());
			List<Professione> professioneList = (List<Professione>) dtoService.convertCollection(professioneDOList,
					Professione.class);
			return professioneList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllProfessione", e);
			throw e;
		}
	}

	@Override
	public List<Professione> searchProfessioneByFilter(ProfessioneFilter filter) {
		try {
			ProfessioneFilterDO fltDO = (ProfessioneFilterDO) dtoService.convertObject(filter,
					ProfessioneFilterDO.class);
			List<ProfessioneDO> professioneDOList = anagraficheTransactionalService.searchProfessioneByFilter(fltDO);
			List<Professione> professioneList = (List<Professione>) dtoService.convertCollection(professioneDOList,
					Professione.class);
			return professioneList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING searchProfessioneByFilter", e);
			throw e;
		}
	}

	@Override
	public List<EnteCertificatore> getAllEnteCertificatore() {
		try {
			List<EnteCertificatoreDO> enteCertificatoreDOList = anagraficheTransactionalService
					.getAllEnteCertificatore();
			List<EnteCertificatore> enteCertificatoreList = (List<EnteCertificatore>) dtoService
					.convertCollection(enteCertificatoreDOList, EnteCertificatore.class);
			return enteCertificatoreList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllEnteCertificatore", e);
			throw e;
		}
	}

	@Override
	public List<EnteCertificatore> getAllEnteCertificatoreForMedico() {
		try {
			List<EnteCertificatoreDO> enteCertificatoreDOList = anagraficheTransactionalService
					.getAllEnteCertificatoreForMedico();
			List<EnteCertificatore> enteCertificatoreList = (List<EnteCertificatore>) dtoService
					.convertCollection(enteCertificatoreDOList, EnteCertificatore.class);
			return enteCertificatoreList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllEnteCertificatore", e);
			throw e;
		}
	}

	@Override
	public List<StatoDAE> getAllStatoDae() {
		try {
			List<StatoDAEDO> statoDaeDOList = anagraficheTransactionalService.getAllStatoDae();
			List<StatoDAE> statoDaeList = (List<StatoDAE>) dtoService.convertCollection(statoDaeDOList, StatoDAE.class);
			return statoDaeList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllStatoDae", e);
			throw e;
		}
	}

	@Override
	public List<Stato> getAllStato() {
		try {
			List<StatoDO> statoDOList = anagraficheTransactionalService.getAllStato();
			List<Stato> statoList = (List<Stato>) dtoService.convertCollection(statoDOList, Stato.class);
			return statoList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllStato", e);
			throw e;
		}
	}

	@Override
	public String getParameter(String parameter) {
		ConfigDO config = anagraficheTransactionalService.getParametro(parameter);
		if (config != null) {
			return config.getValore();
		}
		return null;
	}

	@Override
	public String getParameter(String parameter, String dflt) {
		ConfigDO config = anagraficheTransactionalService.getParametro(parameter);
		if (config != null) {
			return config.getValore();
		}
		return dflt;
	}

	@Override
	public int getParameter(String parameter, int defaultValue) {
		String val = getParameter(parameter);
		if (val != null) {
			try {
				return Integer.parseInt(val);
			} catch (Exception e) {
			}
		}
		return defaultValue;
	}

	@Override
	public boolean getParameter(String parameter, boolean defaultValue) {
		String val = getParameter(parameter);
		if (val != null) {
			try {
				return Boolean.getBoolean(val.trim().toLowerCase());
			} catch (Exception e) {
				logger.error("Error on converting " + val);
			}
		}
		return defaultValue;
	}

	@Override
	public List<Config> getAllConfiguration() {
		List<ConfigDO> configs = anagraficheTransactionalService.getAllConfiguration();
		return (List<Config>) dtoService.convertCollection(configs, Config.class);
	}

	@Override
	public Config saveConfiguration(Config config) {
		ConfigDO toSave = dtoService.convertObject(config, ConfigDO.class);
		toSave = anagraficheTransactionalService.saveConfig(toSave);
		return dtoService.convertObject(toSave, Config.class);
	}

	@Override
	public Strade saveStrada(Strade strada) {
		logger.info("SAVING NEW STRADA");
		StradeDO toSave = dtoService.convertObject(strada, StradeDO.class);
		toSave.setNote("INSERITA DA PORTALE DAE");
		if (strada.getComune() != null && !StringUtils.isEmpty(strada.getComune().getId())) {
			ComuneDO comDO = anagraficheTransactionalService.getComuneByID(strada.getComune().getId());
			toSave.setComune(comDO);
			logger.info("COMUNE SAVED BY ID..." + comDO.getId());
		}
		if (strada.getComune() != null && !StringUtils.isEmpty(strada.getComune().getCodiceIstat())) {
			ComuneDO comDO = anagraficheTransactionalService.getComuneByIstat(strada.getComune().getCodiceIstat());
			toSave.setComune(comDO);
			logger.info("COMUNE SAVED BY ISTAT CODE..." + comDO.getId());
		}

		toSave = anagraficheTransactionalService.saveStrada(toSave);
		toSave = anagraficheTransactionalService.getStradaById(toSave.getId(), StradeDeepDepthRule.NAME);
		return dtoService.convertObject(toSave, Strade.class,
				new CompoundDTORule(StradeDO.class, Strade.class, StradeDeepDepthRule.NAME));
	}

	@Override
	public CategoriaFr getCategoriaById(String id) {
		try {
			CategoriaFrDO catDO = anagraficheTransactionalService.getCategoriaById(id);
			CategoriaFr toRet = dtoService.convertObject(catDO, CategoriaFr.class,
					new CompoundDTORule(CategoriaFrDO.class, CategoriaFr.class, "BARE"));
			return toRet;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getCategoriaById", e);
			throw e;
		}
	}

	@Override
	public List<StaticData> searchStaticDataByType(String type) {
		List<StaticDataDO> staticDataDOs = anagraficheTransactionalService.searchStaticDataByType(type);
		return (List<StaticData>) dtoService.convertCollection(staticDataDOs, StaticData.class);
	}

	@Override
	public List<Localita> searchLocalitaByFilter(LocalitaFilter filter) {
		try {
			List<LocalitaDO> localitaDoList = null;

			LocalitaFilterDO localitaFilterDO = null;

			localitaFilterDO = dtoService.convertObject(filter, LocalitaFilterDO.class);

			localitaDoList = anagraficheTransactionalService.searchLocalitaByFilter(localitaFilterDO);

			List<Localita> stradeList = (List<Localita>) dtoService.convertCollection(localitaDoList, Localita.class,
					new CompoundDTORule(LocalitaDO.class, Localita.class, filter.getFetchRule()));

			return stradeList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING searchLocalitaByFilter", e);
			return null;
		}
	}

	@Override
	public DashboardPosition[] saveDashboardPosition(final Utente utente, DashboardPosition[] positions) {
		AtomicInteger order = new AtomicInteger(0);

		List<DashboardPositionDO> toSave = Arrays.asList(positions).stream().map(p -> {
			DashboardPositionDO pos = new DashboardPositionDO();
			pos.setId(utente.getId() + "_" + p.getId());
			pos.setElementId(p.getId());
			pos.setX(p.getX());
			pos.setUtenteId(utente.getId());

			pos.setOrdering(order.getAndIncrement());
			return pos;
		}).collect(Collectors.toList());

		toSave = anagraficheTransactionalService.saveDashboardPositions(toSave);

		return toSave.stream().map(this::convertDashboardPosition).collect(Collectors.toList())
				.toArray(new DashboardPosition[toSave.size()]);
	}

	@Override
	public DashboardPosition[] getDashboardPosition(String id) {
		List<DashboardPositionDO> saved = anagraficheTransactionalService.getDashboardPositionsByUserId(id);
		return saved.stream().map(this::convertDashboardPosition).collect(Collectors.toList())
				.toArray(new DashboardPosition[saved.size()]);
	}

	protected DashboardPosition convertDashboardPosition(DashboardPositionDO pos) {
		DashboardPosition toRet = new DashboardPosition();

		toRet.setId(pos.getElementId());
		toRet.setX(pos.getX());

		return toRet;
	}

}
