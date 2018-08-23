/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import it.eng.areas.ems.sdodaeservices.entity.DaeDO;
import it.eng.areas.ems.sdodaeservices.entity.DaeFaultDO;
import it.eng.areas.ems.sdodaeservices.entity.ImageDO;
import it.eng.areas.ems.sdodaeservices.entity.ProgrammaManutenzioneHistoryDO;
import it.eng.areas.ems.sdodaeservices.entity.UtenteDO;
import it.eng.areas.ems.sdodaeservices.entity.enums.Operation;
import it.eng.areas.ems.sdodaeservices.entity.filter.DaeFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.gis.VCTDaeDO;
import it.eng.areas.ems.sdodaeservices.entity.gis.VctDaeDODistanceBean;
import it.eng.areas.ems.sdodaeservices.exception.DaeDuplicateException;

public interface DaeTransactionalService {

	/**
	 * 
	 * Get an entity of type ExampleDO by its Id
	 * 
	 * @param id
	 * @return
	 */

	public boolean deleteDaeById(String id);

	long countAll();

	public List<DaeDO> searchDaeByFilter(DaeFilterDO daeFilter) throws ExecutionException;

	public DaeDO insertDae(DaeDO daeDO, UtenteDO userDO, Operation operation) throws DaeDuplicateException;

	public List<VCTDaeDO> getAllVctDae();

	public DaeDO cleanDisponibilita(DaeDO daeDO);

	public VCTDaeDO saveVctDae(VCTDaeDO daeDO);

	public DaeDO getDaeById(String fetchRule, String id);

	public Integer deleteAllVctDAE();

	public ImageDO getImageByDaeID(String ID);

	public List<VctDaeDODistanceBean> getPagedVctDAEOrderByDistance(Integer pageSize, Integer start, Double latitudine,
			Double longitudine, Integer SRID);

	public List<VctDaeDODistanceBean> getPagedVctDAEOrderByDistance(DaeFilterDO daeFilter);

	public DaeFaultDO saveDaeFault(DaeFaultDO faultDO);

	public ImageDO saveImage(ImageDO img);

	public void updateImageDae(String id, String encode);

	public ImageDO getImageByID(String daeId);

	public void deleteVctDaeById(String id);

	public DaeDO deleteLogicallyDae(String id, UtenteDO user);

	public List<ProgrammaManutenzioneHistoryDO> listProgrammaManutenzioneHistory(String id);

}
