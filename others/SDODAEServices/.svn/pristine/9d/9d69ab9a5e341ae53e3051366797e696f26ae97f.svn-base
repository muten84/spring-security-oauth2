package it.eng.areas.ems.sdodaeservices.delegate;

import java.util.List;

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

public interface AnagraficheDelegateService {

	public List<Comune> getComuniByFilter(ComuneFilter comuniFilter);

	public List<Provincia> getAllProvince();

	public List<TipoStruttura> getAllTipoStruttura();

	public List<Strade> searchStradeByFilter(StradeFilter comuneFilter);

	public List<Strade> getAllStrade();

	public Ruolo insertRuolo(Ruolo ruolo);

	public List<Ruolo> getAllRuolo();

	public Ruolo getRuoloById(String id);

	public boolean deleteRuoloById(String id);

	public List<CategoriaFr> getAllCategoriaFR();

	public List<Professione> getAllProfessione();

	public List<EnteCertificatore> getAllEnteCertificatore();

	public List<StatoDAE> getAllStatoDae();

	public List<Stato> getAllStato();

	public String getParameter(String parameter);

	public int getParameter(String parameter, int defaultValue);

	public boolean getParameter(String parameter, boolean defaultValue);

	public List<Professione> searchProfessioneByFilter(ProfessioneFilter filter);

	public List<EnteCertificatore> getAllEnteCertificatoreForMedico();

	public String getParameter(String parameter, String dflt);

	public List<Config> getAllConfiguration();

	public Config saveConfiguration(Config config);

	public Strade saveStrada(Strade strada);

	public List<CategoriaFr> getAllCategoriaFRForApp();

	public CategoriaFr getCategoriaById(String id);

	public List<Provincia> getProvinceByArea(String[] area);

	public List<StaticData> searchStaticDataByType(String type);

	public List<Localita> searchLocalitaByFilter(LocalitaFilter filter);

	public DashboardPosition[] saveDashboardPosition(Utente utente, DashboardPosition[] positions);

	public DashboardPosition[] getDashboardPosition(String id);
}
