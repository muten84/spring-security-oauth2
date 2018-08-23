package it.eng.areas.ems.sdodaeservices.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.eng.area118.sdocommon.dao.filter.EntityFilter;
import it.eng.areas.ems.sdodaeservices.dao.DispositiviDAO;
import it.eng.areas.ems.sdodaeservices.dao.FRPositionToCODAO;
import it.eng.areas.ems.sdodaeservices.dao.FirstResponderDAO;
import it.eng.areas.ems.sdodaeservices.dao.InterventoCoordDAO;
import it.eng.areas.ems.sdodaeservices.dao.MobileTokenDAO;
import it.eng.areas.ems.sdodaeservices.dao.QuestionarioDAO;
import it.eng.areas.ems.sdodaeservices.dao.UtenteDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderImageRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.QuestionarioDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.UtenteDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.DaeMobileTokenDO;
import it.eng.areas.ems.sdodaeservices.entity.DispositiviDO;
import it.eng.areas.ems.sdodaeservices.entity.FRPositionToCODO;
import it.eng.areas.ems.sdodaeservices.entity.FRStatoProfiloEnum;
import it.eng.areas.ems.sdodaeservices.entity.FirstResponderDO;
import it.eng.areas.ems.sdodaeservices.entity.InterventoCoordDO;
import it.eng.areas.ems.sdodaeservices.entity.PasswordHistoryDO;
import it.eng.areas.ems.sdodaeservices.entity.QuestionarioDO;
import it.eng.areas.ems.sdodaeservices.entity.UtenteDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.FirstResponderFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.UtenteFilterDO;
import it.eng.areas.ems.sdodaeservices.service.FirstResponderTransactionalService;
import it.eng.areas.ems.sdodaeservices.utils.FRUtils;
import it.esel.parsley.lang.StringUtils;

@Component
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class FirstResponderTransactionalServiceImpl implements FirstResponderTransactionalService {

	@Autowired
	private FirstResponderDAO firstResponderDAO;

	@Autowired
	private UtenteDAO utenteDAO;

	@Autowired
	private InterventoCoordDAO coordDAO;

	@Autowired
	private QuestionarioDAO questionarioDAO;

	@Autowired
	private DispositiviDAO dispositiviDAO;

	@Autowired
	private FRPositionToCODAO frPosition;

	@Autowired
	private MobileTokenDAO tokenDAO;

	private static Logger logger = LoggerFactory.getLogger(FirstResponderTransactionalServiceImpl.class);

	public FirstResponderTransactionalServiceImpl() {

	}

	@PostConstruct
	public void init() {
		// TODO: all dependendecies injected, init your stateless service here
		// TODO: tutte le dipendenze sono state inettate inizializza qui il tuo
		// stateless service
	}

	@Override
	public FirstResponderDO getFirstResponderById(String fetchRule, String id) {

		return firstResponderDAO.get(fetchRule, id);

		// if (example == null) {
		// throw new NullPointerException("The entity does not exist");
		// }

	}

	@Override
	public List<UtenteDO> searchUtentiByFilter(UtenteFilterDO filter) {
		return utenteDAO.searchUtenteByFilter(filter);
	}

	@Override
	public UtenteDO getUtenteByID(String utenteId) {
		return utenteDAO.get(UtenteDeepDepthRule.NAME, utenteId);
	}

	@Override
	public FirstResponderDO updateFRNumIntervEseguiti(String frID, Integer numIntEseguiti) {
		FirstResponderDO frDO = firstResponderDAO.get(frID);
		frDO.setInterventiEseguiti(numIntEseguiti);
		frDO = firstResponderDAO.save(frDO);
		return frDO;
	}

	public long countAll() {
		long num = firstResponderDAO.countAll();

		// if (example == null) {
		// throw new NullPointerException("The entity does not exist");
		// }
		return num;
	}

	@Override
	public List<FirstResponderDO> getAllFirstResponder() {
		// TODO Auto-generated method stub
		EntityFilter filter = new EntityFilter();
		filter.setFetchRule(FirstResponderDeepDepthRule.NAME);

		return firstResponderDAO.getAll(filter);
	}

	@Override
	public List<FirstResponderDO> searchFirstResponderByFilter(FirstResponderFilterDO firstResponderFilter) {
		if (StringUtils.isEmpty(firstResponderFilter.getFetchRule())) {
			firstResponderFilter.setFetchRule(FirstResponderDeepDepthRule.NAME);
		}
		return firstResponderDAO.searchFirstResponderByFilter(firstResponderFilter);

	}

	@Override
	public List<FirstResponderDO> getFirstResponderToBeNotified(int catFR) {
		return firstResponderDAO.getFirstResponderToBeNotified(catFR);
	}

	@Override
	public List<FirstResponderDO> getFRToBeNotifiedModifyOrClosure(String eventId, int catFR) {
		return firstResponderDAO.getFRToBeNotifiedModifyOrClosure(eventId, catFR);
	}

	@Override
	public UtenteDO saveUtenteDO(UtenteDO utente) {
		UtenteDO ut = utenteDAO.save(utente);
		ut = getUtenteByID(ut.getId());
		return ut;
	}

	@Override
	public FirstResponderDO saveFirstResponder(FirstResponderDO firstResponderDO) {
		if (StringUtils.isEmpty(firstResponderDO.getUtente().getId())) {
			// aggiungo l'history della password
			PasswordHistoryDO pHistory = new PasswordHistoryDO();
			pHistory.setUtente(firstResponderDO.getUtente());
			pHistory.setCreationDate(new Date());
			pHistory.setTemporany(false);
			pHistory.setPasswordHash(firstResponderDO.getUtente().getPasswordHash());

			firstResponderDO.getUtente().getPasswordHistories().add(pHistory);
		}

		return firstResponderDAO.insertFirstResponder(firstResponderDO);
	}

	// @Override
	// public List<VCTFirstResponderDO> getAllVctFirstResponder() {
	// return vctFirstResponderDAO.getAllVCTDAE();
	// }

	@Override
	public boolean deleteFirstResponderById(String id) {
		FirstResponderDO firstResponderDO = getFirstResponderById(FirstResponderDeepDepthRule.NAME, id);
		if (firstResponderDO != null) {
			firstResponderDAO.delete(firstResponderDO);
			return true;
		}
		return false;
	}

	@Override
	public QuestionarioDO insertQuestionario(QuestionarioDO questionarioDO) {
		// TODO Auto-generated method stub
		return questionarioDAO.insertQuestionario(questionarioDO);
	}

	@Override
	public QuestionarioDO getQuestionarioById(String id) {

		return questionarioDAO.get(QuestionarioDeepDepthRule.NAME, id);
	}

	@Override
	public boolean deleteQuestionarioById(String id) {
		QuestionarioDO questionarioDO = getQuestionarioById(id);
		if (questionarioDO != null) {
			questionarioDAO.delete(questionarioDO);
			return true;
		}
		return false;
	}

	@Override
	public FirstResponderDO updateFirstResponder(FirstResponderDO firstResponderDO) {

		return firstResponderDAO.save(firstResponderDO);
	}

	@Override
	public FirstResponderDO updateAvailability(String frID, boolean availability) {
		FirstResponderDO frDO = firstResponderDAO.get(frID);
		frDO.setDisponibile(availability);
		frDO = firstResponderDAO.save(frDO);
		return frDO;
	}

	@Override
	public boolean deleteUtenteById(String utenteID) {
		return utenteDAO.delete(utenteID);
	}

	@Override
	public FirstResponderDO getFirstResponderByUserId(String fetchRule, String userId) {
		return firstResponderDAO.getFirstResponderByUserId(fetchRule, userId);
	}

	@Override
	public boolean acceptPrivacyAgreement(String frID) {
		FirstResponderDO frDO = firstResponderDAO.get(frID);
		frDO.setPrivacyAcceptedDate(Calendar.getInstance().getTime());
		firstResponderDAO.save(frDO);
		return true;
	}

	@Override
	public InterventoCoordDO saveInterventoCoord(InterventoCoordDO coordDO) {
		return coordDAO.save(coordDO);
	}

	@Override
	public void cleanDispositivo(String id) {
		DispositiviDO dispositivo = dispositiviDAO.get(id);

		dispositivo.setPushToken(null);
		dispositivo.setMarca(null);
		dispositivo.setOs(null);
		dispositivo.setModello(null);
	}

	@Override
	public boolean setDoNotDisturb(String frID, String from, String to) {
		FirstResponderDO fr = firstResponderDAO.get(frID);

		fr.setDoNotDisturb(true);
		fr.setDoNotDisturbFrom(from);
		fr.setDoNotDisturbTo(to);

		return false;
	}

	@Override
	public boolean removeDoNotDisturb(String frID) {
		FirstResponderDO fr = firstResponderDAO.get(frID);

		fr.setDoNotDisturb(false);
		// forzo il disponibile, nel caso sia stato disabilitato
		fr.setDisponibile(true);

		return true;
	}

	@Override
	public void updateDoNotDisturbEnd(String hour) {
		// Carica tutti i fr con orario di non disturbare finito
		List<FirstResponderDO> responders = firstResponderDAO.loadFRDoNotDisturbEnd(hour);

		responders.forEach(f -> {
			// abilito il FR
			logger.info("Do not Disturb: " + f.getId() + " is enabled");
			f.setDisponibile(FRUtils.isDisponibile(f));

		});

	}

	@Override
	public void updateDoNotDisturbStart(String hour) {
		// Carica tutti i fr con orario di non disturbare iniziato
		List<FirstResponderDO> responders = firstResponderDAO.loadFRDoNotDisturbStart(hour);
		responders.forEach(f -> {
			// disabilito il FR
			logger.info("Do not Disturb: " + f.getId() + " is disabled");
			f.setDisponibile(false);

		});
	}

	@Override
	public boolean setSilent(String frID, String from, String to) {
		FirstResponderDO fr = firstResponderDAO.get(frID);

		fr.setSilent(true);
		fr.setSilentFrom(from);
		fr.setSilentTo(to);

		fr.setDisponibile(false);

		return false;
	}

	@Override
	public boolean removeSilent(String frID) {
		FirstResponderDO fr = firstResponderDAO.get(frID);

		fr.setSilent(false);
		fr.setSilentFrom(null);
		fr.setSilentTo(null);
		// forzo il disponibile, nel caso sia stato disabilitato
		fr.setDisponibile(FRUtils.isDisponibile(fr));

		return true;
	}

	@Override
	public void updateSilentEnd(String hour) {
		// Carica tutti i fr con orario di non disturbare finito
		List<FirstResponderDO> responders = firstResponderDAO.loadFRSilentEnd(hour);

		responders.forEach(f -> {
			// abilito il FR
			logger.info("Silent : " + f.getId() + " is enabled");
			f.setSilent(false);
			f.setDisponibile(FRUtils.isDisponibile(f));
		});

	}

	@Override
	public void saveFRPositionToCO(FRPositionToCODO pos) {
		frPosition.save(pos);
	}

	@Override
	public List<FRPositionToCODO> getLastFRPositionToCO(Integer maxResult) {
		return frPosition.getLastFRPositionToCO(maxResult);
	}

	@Override
	public FirstResponderDO updateFRProfile(FirstResponderDO frDO) {
		FirstResponderDO actual = firstResponderDAO.get("BASIC", frDO.getId());

		actual.setNumCellulare(frDO.getNumCellulare());
		// if (frDO.getProfessione() != null) {
		// actual.setProfessione(frDO.getProfessione());
		// }

		if (!StringUtils.isEmpty(frDO.getUtente().getNome())) {
			actual.getUtente().setNome(frDO.getUtente().getNome());
		}
		if (!StringUtils.isEmpty(frDO.getUtente().getCognome())) {
			actual.getUtente().setCognome(frDO.getUtente().getCognome());
		}
		// actual.getUtente().setEmail(frDO.getUtente().getEmail());

		if (!StringUtils.isEmpty(frDO.getUtente().getCodiceFiscale())) {
			actual.getUtente().setCodiceFiscale(frDO.getUtente().getCodiceFiscale());
		}
		actual.getUtente().setDataNascita(frDO.getUtente().getDataNascita());

		actual.getUtente().setCivico(frDO.getUtente().getCivico());
		actual.getUtente().setIndirizzo(frDO.getUtente().getIndirizzo());
		actual.getUtente().setComuneResidenza(frDO.getUtente().getComuneResidenza());
		actual.getUtente().setLanguage(frDO.getUtente().getLanguage());

		if (frDO.getCertificatoFr().getDataConseguimento() != null) {
			actual.getCertificatoFr().setDataConseguimento(frDO.getCertificatoFr().getDataConseguimento());
		}

		if (!StringUtils.isEmpty(frDO.getCertificatoFr().getRilasciatoDa())) {
			actual.getCertificatoFr().setRilasciatoDa(frDO.getCertificatoFr().getRilasciatoDa());
		}

		// se mi è arrivata anche l'immagine la sostituisco a quella già
		// presente

		if (frDO.getUtente().getImmagine() != null && !StringUtils.isEmpty(frDO.getUtente().getImmagine().getData())) {
			if (actual.getUtente().getImmagine() != null) {
				actual.getUtente().getImmagine().setData(frDO.getUtente().getImmagine().getData());
			} else {
				actual.getUtente().setImmagine(frDO.getUtente().getImmagine());
			}
		}

		actual.setComuniCompetenza(frDO.getComuniCompetenza());

		return actual;
	}

	@Override
	public void deleteLogicallyFR(String id, UtenteDO convertObject) {
		FirstResponderDO fr = firstResponderDAO.get(id);
		fr.getUtente().setDeleted(true);
		fr.setDisponibile(false);
		// cancello i token salvati per quell'utente
		List<DaeMobileTokenDO> tokens = tokenDAO.getTokensByUserId(fr.getUtente().getId());
		if (tokens != null) {
			tokens.forEach(tokenDAO::delete);
		}
	}

	@Override
	public FirstResponderDO activateProfile(String frID) {
		FirstResponderDO fr = firstResponderDAO.get(FirstResponderImageRule.NAME, frID);
		// se il FR non è stato cancellato e non è attivo procedo
		// all'attivazione
		if ((fr.getUtente().getDeleted() == null || !fr.getUtente().getDeleted())
				&& fr.getStatoProfilo() == FRStatoProfiloEnum.IN_ATTESA_DI_ATTIVAZIONE) {
			fr.setStatoProfilo(FRStatoProfiloEnum.ATTIVO);
			fr.setDisponibile(true);

			fr.setDataValidazione(new Date());
			fr.setUtenteValidazione(fr.getUtente().getEmail());
			// restituisco il fr appena attivato
			return fr;
		}
		// altrimenti non faccio nulla
		return null;
	}

}
