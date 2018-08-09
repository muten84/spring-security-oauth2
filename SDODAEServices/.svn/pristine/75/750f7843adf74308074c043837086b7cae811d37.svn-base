package it.eng.areas.ems.sdodaeservices.delegate;

import java.util.List;

import it.eng.areas.ems.sdodaeservices.delegate.model.Dae;
import it.eng.areas.ems.sdodaeservices.delegate.model.DaeFault;
import it.eng.areas.ems.sdodaeservices.delegate.model.DaeImageUpload;
import it.eng.areas.ems.sdodaeservices.delegate.model.Image;
import it.eng.areas.ems.sdodaeservices.delegate.model.ProgrammaManutenzioneHistory;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.eng.areas.ems.sdodaeservices.delegate.model.VctDAE;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.DaeFilter;
import it.eng.areas.ems.sdodaeservices.entity.gis.VctDaeDODistanceBean;
import it.eng.areas.ems.sdodaeservices.exception.DaeDuplicateException;

public interface DaeDelegateService {

	public Dae getDaeById(String id);

	public boolean deleteDaeById(String id);

	public long countAll();

	public List<VctDAE> getAllVctDae();

	public List<Dae> searchDaeByFilter(DaeFilter daeFilter) throws Exception;

	public Dae insertDae(Dae dae, Utente user, boolean fromapp) throws DaeDuplicateException;

	public VctDAE saveVctDae(VctDAE daeDTO);

	public Dae uploadDaeImage(DaeImageUpload upload, Utente utente) throws Exception;

	public boolean deleteLogicallyDae(Dae dae, Utente utente);

	public Dae getDaeWithImageById(String id);

	public boolean synchGisDATA();

	public Image getImageByID(String id);

	public boolean deleteAllVctDae();

	/**
	 * Usare searchDaeByFilter
	 * 
	 * @param fetchRule
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public List<Dae> getAllDAE(String fetchRule) throws Exception;

	public List<VctDaeDODistanceBean> getPagedVctDAEOrderByDistance(Integer pageSize, Integer start, Double latitudine,
			Double longitudine, Integer SRID);

	public List<VctDaeDODistanceBean> getPagedVctDAEOrderByDistance(DaeFilter daeFilter);

	public DaeFault saveDaeFault(DaeFault fault);

	public void updateImageDae(String id, String encode);

	public List<ProgrammaManutenzioneHistory> listProgrammaManutenzioneHistory(String id);

}
