/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.service;

import java.util.List;

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
import it.eng.areas.ems.sdodaeservices.entity.filter.ComuneFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.LocalitaFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.ProfessioneFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.StradeFilterDO;

public interface AnagraficheTransactionalService {

	/**
	 * 
	 * Get an entity of type ExampleDO by its Id
	 * 
	 * @param id
	 * @return
	 */
	public List<ComuneDO> searchComuniByFilter(ComuneFilterDO comuniFilter);

	public List<ProvinciaDO> getAllProvince();

	public List<TipoStrutturaDO> getAllTipoStruttura();

	public List<StradeDO> searchStradeByFilter(StradeFilterDO comuneFilterDO);

	public List<StradeDO> getAllStrade();

	public List<CategoriaFrDO> getAllCategoriaFR();

	public RuoloDO insertRuolo(RuoloDO ruoloDO);

	public RuoloDO getRuoloById(String id);

	public boolean deleteRuoloById(String id);

	public List<RuoloDO> getAllRuolo();

	public List<ProfessioneDO> getAllProfessione();

	public List<ProfessioneDO> searchProfessioneByFilter(ProfessioneFilterDO filter);

	public List<ConfigDO> getAllConfiguration();

	public List<EnteCertificatoreDO> getAllEnteCertificatore();

	public List<StatoDAEDO> getAllStatoDae();

	public List<StatoDO> getAllStato();

	public ConfigDO getParametro(String parametro);

	public List<EnteCertificatoreDO> getAllEnteCertificatoreForMedico();

	public ConfigDO saveConfig(ConfigDO toSave);

	public StradeDO saveStrada(StradeDO toSave);

	public StradeDO getStradaById(String id, String fetchRule);

	public ComuneDO getComuneByID(String id);

	public ComuneDO getComuneByIstat(String istat);

	public List<CategoriaFrDO> getAllCategoriaFRForApp();

	public String getParametro(String parametro, String defValue);

	public int getParametro(String parametro, int defValue);

	public CategoriaFrDO getCategoriaById(String id);

	public List<StaticDataDO> searchStaticDataByType(String type);

	public List<ResponsabileProvinciaDO> searchResponsabileByProvince(String province);

	public List<LocalitaDO> searchLocalitaByFilter(LocalitaFilterDO localitaFilterDO);

	public List<DashboardPositionDO> saveDashboardPositions(List<DashboardPositionDO> toSave);

	public List<DashboardPositionDO> getDashboardPositionsByUserId(String id);

}
