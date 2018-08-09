package it.eng.areas.ems.sdodaeservices.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.cache.LoadingCache;

import it.eng.area118.sdocommon.dao.filter.EntityFilter;
import it.eng.area118.sdocommon.dao.filter.OrderBy;
import it.eng.areas.ems.sdodaeservices.dao.CategoriaFrDAO;
import it.eng.areas.ems.sdodaeservices.dao.ComuneDAO;
import it.eng.areas.ems.sdodaeservices.dao.ConfigDAO;
import it.eng.areas.ems.sdodaeservices.dao.DashboardPositionDAO;
import it.eng.areas.ems.sdodaeservices.dao.EnteCertificatoreDAO;
import it.eng.areas.ems.sdodaeservices.dao.LocalitaDAO;
import it.eng.areas.ems.sdodaeservices.dao.ProfessioneDAO;
import it.eng.areas.ems.sdodaeservices.dao.ProvinciaDAO;
import it.eng.areas.ems.sdodaeservices.dao.ResponsabileProvinciaDAO;
import it.eng.areas.ems.sdodaeservices.dao.RuoloDAO;
import it.eng.areas.ems.sdodaeservices.dao.StaticDataDAO;
import it.eng.areas.ems.sdodaeservices.dao.StatoDAEDAO;
import it.eng.areas.ems.sdodaeservices.dao.StatoDAO;
import it.eng.areas.ems.sdodaeservices.dao.StradeDAO;
import it.eng.areas.ems.sdodaeservices.dao.TipoStrutturaDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.ComuniProvinceDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.RuoloDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.CategoriaFrDO;
import it.eng.areas.ems.sdodaeservices.entity.ComuneDO;
import it.eng.areas.ems.sdodaeservices.entity.ConfigDO;
import it.eng.areas.ems.sdodaeservices.entity.DashboardPositionDO;
import it.eng.areas.ems.sdodaeservices.entity.EnteCertificatoreDO;
import it.eng.areas.ems.sdodaeservices.entity.LocalitaDO;
import it.eng.areas.ems.sdodaeservices.entity.ProfessioneDO;
import it.eng.areas.ems.sdodaeservices.entity.ProvinciaDO;
import it.eng.areas.ems.sdodaeservices.entity.ResponsabileProvinciaDO;
import it.eng.areas.ems.sdodaeservices.entity.RuoloDO;
import it.eng.areas.ems.sdodaeservices.entity.StaticDataDO;
import it.eng.areas.ems.sdodaeservices.entity.StatoDAEDO;
import it.eng.areas.ems.sdodaeservices.entity.StatoDO;
import it.eng.areas.ems.sdodaeservices.entity.StradeDO;
import it.eng.areas.ems.sdodaeservices.entity.TipoStrutturaDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.CategoriaFrFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.ComuneFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.EnteCertificatoreFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.LocalitaFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.ProfessioneFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.StradeFilterDO;
import it.eng.areas.ems.sdodaeservices.service.AnagraficheTransactionalService;

@Component
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class AnagraficheTransactionalServiceImpl implements AnagraficheTransactionalService {

	@Autowired
	private ComuneDAO comuneDAO;

	@Autowired
	private StradeDAO stradeDAO;

	@Autowired
	private ProvinciaDAO provinciaDAO;

	@Autowired
	private CategoriaFrDAO categoriaDAO;

	@Autowired
	private TipoStrutturaDAO tipoStrutturaDAO;

	@Autowired
	private RuoloDAO ruoloDAO;

	@Autowired
	private ConfigDAO configDAO;

	@Autowired
	private ProfessioneDAO professioneDAO;

	@Autowired
	private EnteCertificatoreDAO enteCertificatoreDAO;

	@Autowired
	private StatoDAEDAO statoDAEDAO;

	@Autowired
	private StatoDAO statoDAO;

	@Autowired
	private LocalitaDAO localitaDAO;

	@Autowired
	private StaticDataDAO staticDataDAO;

	@Autowired
	private ResponsabileProvinciaDAO responsabileProvinciaDAO;

	@Autowired
	private DashboardPositionDAO dashboardPositionDAO;

	private LoadingCache<String, ConfigDO> paramCache;

	public AnagraficheTransactionalServiceImpl() {

	}

	@PostConstruct
	public void init() {
		// TODO: all dependendecies injected, init your stateless service here
		// TODO: tutte le dipendenze sono state inettate inizializza qui il tuo
		// stateless service
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public List<ComuneDO> searchComuniByFilter(ComuneFilterDO comuneFilter) {
		comuneFilter.setFetchRule(ComuniProvinceDeepDepthRule.NAME);
		return comuneDAO.searchComuniByFilter(comuneFilter);
	}

	@Override
	public List<ProvinciaDO> getAllProvince() {

		return provinciaDAO.getAllSorted();

	}

	@Override
	public List<TipoStrutturaDO> getAllTipoStruttura() {

		return tipoStrutturaDAO.getAllSorted();

	}

	@Override
	public List<StradeDO> searchStradeByFilter(StradeFilterDO stradeFilterDO) {

		return stradeDAO.searchStradeByFilter(stradeFilterDO);

	}

	@Override
	public List<StradeDO> getAllStrade() {
		return stradeDAO.getAll();
	}

	@Override
	public List<RuoloDO> getAllRuolo() {
		return ruoloDAO.getAll(new EntityFilter(new OrderBy("id")));
	}

	@Override
	public RuoloDO insertRuolo(RuoloDO ruoloDO) {
		return ruoloDAO.insertRuolo(ruoloDO);
	}

	@Override
	public RuoloDO getRuoloById(String id) {
		return ruoloDAO.get(RuoloDeepDepthRule.NAME, id);

	}

	@Override
	public boolean deleteRuoloById(String id) {
		RuoloDO ruoloDO = getRuoloById(id);
		if (ruoloDO != null) {
			ruoloDAO.delete(ruoloDO);
			return true;
		}
		return false;

	}

	@Override
	public List<CategoriaFrDO> getAllCategoriaFR() {
		return categoriaDAO.getAll();
	}

	@Override
	public List<CategoriaFrDO> getAllCategoriaFRForApp() {
		CategoriaFrFilterDO filter = new CategoriaFrFilterDO();
		filter.setForApp(true);
		return categoriaDAO.searchCategoriaFrByFilter(filter);
	}

	@Override
	public List<ProfessioneDO> getAllProfessione() {

		return professioneDAO.getAll();
	}

	@Override
	public List<EnteCertificatoreDO> getAllEnteCertificatore() {

		EntityFilter filter = new EntityFilter();
		filter.setOrderBy(new OrderBy("descrizione", true));
		return enteCertificatoreDAO.getAll(filter);
	}

	@Override
	public List<EnteCertificatoreDO> getAllEnteCertificatoreForMedico() {

		EnteCertificatoreFilterDO filt = new EnteCertificatoreFilterDO("");
		filt.setOnlyForMed(true);
		return enteCertificatoreDAO.searchEnteCertificatoreByFilter(filt);
	}

	@Override
	public List<StatoDAEDO> getAllStatoDae() {
		// TODO Auto-generated method stub
		return statoDAEDAO.getAll();

	}

	@Override
	public List<StatoDO> getAllStato() {
		// TODO Auto-generated method stub
		return statoDAO.getAll();
	}

	@Override
	public ConfigDO getParametro(String parametro) {
		return configDAO.getConfigParameter(parametro);
	}

	@Override
	public String getParametro(String parametro, String defValue) {
		ConfigDO ret = configDAO.getConfigParameter(parametro);
		if (ret == null) {
			return defValue;
		} else {
			return ret.getValore();
		}
	}

	@Override
	public int getParametro(String parametro, int defValue) {
		ConfigDO ret = configDAO.getConfigParameter(parametro);
		if (ret == null) {
			return defValue;
		} else {
			return Integer.parseInt(ret.getValore());
		}
	}

	@Override
	public List<ProfessioneDO> searchProfessioneByFilter(ProfessioneFilterDO filter) {

		return professioneDAO.searchProfessioneByFilter(filter);
	}

	@Override
	public List<ConfigDO> getAllConfiguration() {
		return configDAO.getAll();
	}

	@Override
	public ConfigDO saveConfig(ConfigDO toSave) {
		return configDAO.save(toSave);
	}

	@Override
	public StradeDO saveStrada(StradeDO toSave) {
		return stradeDAO.save(toSave);
	}

	@Override
	public ComuneDO getComuneByID(String id) {
		return comuneDAO.get(id);
	}

	@Override
	public StradeDO getStradaById(String id, String fetchRule) {
		return stradeDAO.get(fetchRule, id);
	}

	@Override
	public ComuneDO getComuneByIstat(String istat) {
		return comuneDAO.getComuneByIstat(istat);
	}

	@Override
	public CategoriaFrDO getCategoriaById(String id) {
		return categoriaDAO.get(id);
	}

	@Override
	public List<StaticDataDO> searchStaticDataByType(String type) {
		return staticDataDAO.searchStaticDataByType(type);
	}

	@Override
	public List<ResponsabileProvinciaDO> searchResponsabileByProvince(String province) {
		return responsabileProvinciaDAO.searchResponsabileByProvince(province);
	}

	@Override
	public List<LocalitaDO> searchLocalitaByFilter(LocalitaFilterDO localitaFilterDO) {
		return localitaDAO.searchLocalitaByFilter(localitaFilterDO);
	}

	@Override
	public List<DashboardPositionDO> saveDashboardPositions(List<DashboardPositionDO> toSave) {
		List<DashboardPositionDO> toRet = new ArrayList<>();
		toSave.forEach(d -> {
			d = dashboardPositionDAO.save(d);
			toRet.add(d);
		});

		return toRet;
	}

	@Override
	public List<DashboardPositionDO> getDashboardPositionsByUserId(String utenteId) {
		return dashboardPositionDAO.getDashboardPositionsByUserId(utenteId);
	}

}
