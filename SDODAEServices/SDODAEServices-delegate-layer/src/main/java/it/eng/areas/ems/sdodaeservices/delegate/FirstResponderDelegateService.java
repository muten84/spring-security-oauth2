package it.eng.areas.ems.sdodaeservices.delegate;

import java.util.List;

import it.eng.areas.ems.sdodaeservices.delegate.model.Dispositivo;
import it.eng.areas.ems.sdodaeservices.delegate.model.FRLocation;
import it.eng.areas.ems.sdodaeservices.delegate.model.FRPositionToCO;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponderImageUpload;
import it.eng.areas.ems.sdodaeservices.delegate.model.GPSLocation;
import it.eng.areas.ems.sdodaeservices.delegate.model.GenericException;
import it.eng.areas.ems.sdodaeservices.delegate.model.Questionario;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.FirstResponderFilter;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.UtenteFilter;

public interface FirstResponderDelegateService {

	public Utente getUtenteById(String id);

	public boolean deleteFirstResponderById(String id);

	public long countAll();

	// public List<VCTFirstResponderDO> getAllVctFirstResponder();

	public List<FirstResponder> searchFirstResponderByFilter(FirstResponderFilter firstResponderFilter);

	public FirstResponder saveFirstResponder(FirstResponder firstResponder, Utente user)
			throws GenericException, Exception;

	public List<FirstResponder> getAllFirstResponder();

	public Questionario insertQuestionario(Questionario questionario);

	public Questionario getQuestionarioById(String id);

	boolean deleteQuestionarioById(String id);

	public FirstResponder updateAvailability(String frID, boolean availability);

	public FirstResponder updatePositionFirstResponderById(String frID, FRLocation gpsLocation);

	public FirstResponder updateDeviceInfo(String frID, Dispositivo dispositivo);

	public FirstResponder updateImmagineProfilo(String frID, FirstResponderImageUpload foto);

	public List<Utente> searchUtenteByFilter(UtenteFilter filter);

	public FirstResponder updateImmagineCertificato(String frID, FirstResponderImageUpload foto);

	public Utente saveUtente(Utente ut);

	public boolean deleteUtente(Utente ut);

	public FirstResponder getFirstResponderById(String fetchRule, String id);

	public FirstResponder getFirstResponderByUserId(String nAME, String userId);

	public List<FirstResponder> getFirstResponderToBeNotified(int catFRPriority);

	public FirstResponder updateFRNumIntervEseguiti(String frID, Integer numIntEseguiti);

	public void cleanDispositivo(String id);

	public List<FirstResponder> getFRToBeNotifiedModifyOrClosure(String event, int catFRPriority);

	public boolean setDoNotDisturb(String frid, String from, String to);

	public boolean removeDoNotDisturb(String frid);

	public void saveFRPositionToCO(FirstResponder fr, GPSLocation request);

	public List<FRPositionToCO> getLastFRPositionToCO(Integer maxResult);

	public FirstResponder updateFRProfile(FirstResponder fr);

	public boolean setSilent(String id, String from, String to);

	public boolean removeSilent(String id);

	public boolean deleteLogicallyFR(FirstResponder fr, Utente utente);

	public FirstResponder activateProfile(String frID);

}
