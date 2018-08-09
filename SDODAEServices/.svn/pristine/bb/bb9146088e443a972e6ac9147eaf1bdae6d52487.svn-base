/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.service;

import java.util.List;

import it.eng.areas.ems.sdodaeservices.entity.FRPositionToCODO;
import it.eng.areas.ems.sdodaeservices.entity.FirstResponderDO;
import it.eng.areas.ems.sdodaeservices.entity.InterventoCoordDO;
import it.eng.areas.ems.sdodaeservices.entity.QuestionarioDO;
import it.eng.areas.ems.sdodaeservices.entity.UtenteDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.FirstResponderFilterDO;
//import it.eng.areas.ems.sdodaeservices.entity.gis.VCTFirstResponderDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.UtenteFilterDO;

public interface FirstResponderTransactionalService {

	/**
	 * 
	 * Get an entity of type ExampleDO by its Id
	 * 
	 * @param id
	 * @return
	 */
	public List<FirstResponderDO> getAllFirstResponder();

	public FirstResponderDO getFirstResponderById(String fetchRule, String id);

	public boolean deleteFirstResponderById(String id);

	long countAll();

	public List<FirstResponderDO> searchFirstResponderByFilter(FirstResponderFilterDO daeFilter);

	public FirstResponderDO saveFirstResponder(FirstResponderDO daeDO);

	public QuestionarioDO insertQuestionario(QuestionarioDO questionarioDO);

	public QuestionarioDO getQuestionarioById(String id);

	public boolean deleteQuestionarioById(String id);

	public FirstResponderDO updateFirstResponder(FirstResponderDO firstResponderDO);

	public List<UtenteDO> searchUtentiByFilter(UtenteFilterDO filter);

	public UtenteDO saveUtenteDO(UtenteDO utente);

	public UtenteDO getUtenteByID(String utenteId);

	public boolean deleteUtenteById(String utenteID);

	public FirstResponderDO getFirstResponderByUserId(String fetchRule, String userId);

	public boolean acceptPrivacyAgreement(String frID);

	public InterventoCoordDO saveInterventoCoord(InterventoCoordDO coordDO);

	public List<FirstResponderDO> getFirstResponderToBeNotified(int catFR);

	public List<FirstResponderDO> getFRToBeNotifiedModifyOrClosure(String eventId, int catFR);

	public FirstResponderDO updateAvailability(String frID, boolean availability);

	public FirstResponderDO updateFRNumIntervEseguiti(String frID, Integer numIntEseguiti);

	public void cleanDispositivo(String id);

	public boolean setDoNotDisturb(String frID, String from, String to);

	public boolean removeDoNotDisturb(String frID);

	public void updateDoNotDisturbEnd(String hour);

	public void updateDoNotDisturbStart(String hour);

	public void saveFRPositionToCO(FRPositionToCODO pos);

	public List<FRPositionToCODO> getLastFRPositionToCO(Integer maxResult);

	public FirstResponderDO updateFRProfile(FirstResponderDO frDO);

	public boolean setSilent(String frID, String from, String to);

	public boolean removeSilent(String frID);

	public void updateSilentEnd(String hour);

	public void deleteLogicallyFR(String id, UtenteDO convertObject);

	public FirstResponderDO activateProfile(String frID);

}
