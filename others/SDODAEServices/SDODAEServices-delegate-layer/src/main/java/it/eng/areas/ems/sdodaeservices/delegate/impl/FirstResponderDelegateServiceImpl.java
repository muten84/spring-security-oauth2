package it.eng.areas.ems.sdodaeservices.delegate.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.hash.Hashing;

import it.eng.areas.ems.common.sdo.dto.CompoundDTORule;
import it.eng.areas.ems.common.sdo.dto.DTOService;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderActivationRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderBareRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderImageRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.QuestionarioDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.UtenteDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.EventDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.FirstResponderDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.MailDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.PushNotificationService;
import it.eng.areas.ems.sdodaeservices.delegate.model.Dispositivo;
import it.eng.areas.ems.sdodaeservices.delegate.model.EntityType;
import it.eng.areas.ems.sdodaeservices.delegate.model.Event;
import it.eng.areas.ems.sdodaeservices.delegate.model.FRLocation;
import it.eng.areas.ems.sdodaeservices.delegate.model.FRPositionToCO;
import it.eng.areas.ems.sdodaeservices.delegate.model.FRSortComparator;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponderImageUpload;
import it.eng.areas.ems.sdodaeservices.delegate.model.GPSLocation;
import it.eng.areas.ems.sdodaeservices.delegate.model.GenericException;
import it.eng.areas.ems.sdodaeservices.delegate.model.Intervento;
import it.eng.areas.ems.sdodaeservices.delegate.model.MailTemplateEnum;
import it.eng.areas.ems.sdodaeservices.delegate.model.Questionario;
import it.eng.areas.ems.sdodaeservices.delegate.model.RuoliEnum;
import it.eng.areas.ems.sdodaeservices.delegate.model.Strade;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.FirstResponderFilter;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.UtenteFilter;
import it.eng.areas.ems.sdodaeservices.entity.ComuneDO;
import it.eng.areas.ems.sdodaeservices.entity.DispositiviDO;
import it.eng.areas.ems.sdodaeservices.entity.FRPositionToCODO;
import it.eng.areas.ems.sdodaeservices.entity.FRStatoProfiloEnum;
import it.eng.areas.ems.sdodaeservices.entity.FirstResponderDO;
import it.eng.areas.ems.sdodaeservices.entity.GPSLocationDO;
import it.eng.areas.ems.sdodaeservices.entity.ImageDO;
import it.eng.areas.ems.sdodaeservices.entity.InterventoCoordDO;
import it.eng.areas.ems.sdodaeservices.entity.InterventoCoordDO.Type;
import it.eng.areas.ems.sdodaeservices.entity.InterventoDO;
import it.eng.areas.ems.sdodaeservices.entity.LocationDO;
import it.eng.areas.ems.sdodaeservices.entity.ParametersEnum;
import it.eng.areas.ems.sdodaeservices.entity.QuestionarioDO;
import it.eng.areas.ems.sdodaeservices.entity.ResponsabileProvinciaDO;
import it.eng.areas.ems.sdodaeservices.entity.RuoloDO;
import it.eng.areas.ems.sdodaeservices.entity.UtenteDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.FirstResponderFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.UtenteFilterDO;
import it.eng.areas.ems.sdodaeservices.service.AnagraficheTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.EventTransactionalService;
//import it.eng.areas.ems.sdodaeservices.entity.gis.VCTFirstResponderDO;
import it.eng.areas.ems.sdodaeservices.service.FirstResponderTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.UserTransactionalService;
import it.eng.areas.ems.sdodaeservices.utils.FRUtils;

@Component
public class FirstResponderDelegateServiceImpl implements FirstResponderDelegateService {

	private Logger logger = LoggerFactory.getLogger(FirstResponderDelegateServiceImpl.class);

	@Autowired
	private FirstResponderTransactionalService firstResponderService;

	@Autowired
	private DTOService dtoService;

	@Autowired
	private MailDelegateService mailService;

	@Autowired
	private EventDelegateService eventDelegateService;

	@Autowired
	private PushNotificationService notificationService;

	@Autowired
	private AnagraficheTransactionalService anaService;

	@Autowired
	private AnagraficheDelegateServiceImpl anaDeleService;

	@Autowired
	private UserTransactionalService userService;

	@Autowired
	private EventTransactionalService evenTransactionalService;

	@Override
	public long countAll() {
		// TODO Auto-generated method stub

		return firstResponderService.countAll();
	}

	@Override
	public List<FirstResponder> getAllFirstResponder() {
		try {
			List<FirstResponderDO> firstResponderDoList = firstResponderService.getAllFirstResponder();
			List<FirstResponder> firstResponderList = (List<FirstResponder>) dtoService.convertCollection(
					firstResponderDoList, FirstResponder.class, new CompoundDTORule(FirstResponderDO.class,
							FirstResponder.class, FirstResponderDeepDepthRule.NAME));
			return firstResponderList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllFirstResponder", e);
			throw e;
		}
	}

	@Override
	public FirstResponder getFirstResponderById(String fetchRule, String id) {
		try {
			FirstResponderDO firstResponderDo = null;

			firstResponderDo = firstResponderService.getFirstResponderById(fetchRule, id);

			FirstResponder firstResponder = (FirstResponder) dtoService.convertObject(firstResponderDo,
					FirstResponder.class, new CompoundDTORule(FirstResponderDO.class, FirstResponder.class, fetchRule));

			return firstResponder;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getFirstResponderById", e);
			throw e;
		}

	}

	@Override
	public FirstResponder updateImmagineProfilo(String frID, FirstResponderImageUpload foto) {
		try {
			FirstResponderDO frDO = firstResponderService.getFirstResponderById(FirstResponderDeepDepthRule.NAME, frID);
			ImageDO img = frDO.getUtente().getImmagine();
			if (img == null) {
				img = new ImageDO();
			}
			img.setData(foto.getBase64Image());
			img = userService.saveImage(img);
			frDO.getUtente().setImmagine(img);
			frDO = firstResponderService.saveFirstResponder(frDO);
			FirstResponder frDTO = getFirstResponderById(FirstResponderDeepDepthRule.NAME, frDO.getId());
			return frDTO;

		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING updateImmagineProfilo", e);
			throw e;
		}
	}

	@Override
	public FirstResponder updateImmagineCertificato(String frID, FirstResponderImageUpload foto) {
		try {
			FirstResponderDO frDO = firstResponderService.getFirstResponderById(FirstResponderDeepDepthRule.NAME, frID);
			ImageDO img = frDO.getUtente().getImmagine();
			if (img == null) {
				img = new ImageDO();
			}
			img.setData(foto.getBase64Image());
			img = userService.saveImage(img);
			frDO.getCertificatoFr().setImmagine(img);
			frDO = firstResponderService.saveFirstResponder(frDO);

			FirstResponder frDTO = getFirstResponderById(FirstResponderDeepDepthRule.NAME, frDO.getId());
			return frDTO;

		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING updateImmagineProfilo", e);
			throw e;
		}
	}

	@Override
	public FirstResponder updateDeviceInfo(String frID, Dispositivo dispositivo) {
		try {
			FirstResponderDO firstResponderDo = null;

			firstResponderDo = firstResponderService.getFirstResponderById(FirstResponderDeepDepthRule.NAME, frID);
			DispositiviDO dispDO = new DispositiviDO();
			if (firstResponderDo.getDispositivo() != null) {
				dispDO.setId(firstResponderDo.getDispositivo().getId());
			}
			dispDO.setFirstResponder(firstResponderDo);
			dispDO.setPushToken(dispositivo.getPushToken());
			dispDO.setMarca(dispositivo.getMarca());
			dispDO.setModello(dispositivo.getModello());
			dispDO.setOs(dispositivo.getOs());
			dispDO.setUltimoAccesso(Calendar.getInstance());
			firstResponderDo.setDispositivo(dispDO);
			firstResponderService.updateFirstResponder(firstResponderDo);
			FirstResponder firstResponder = (FirstResponder) dtoService.convertObject(firstResponderDo,
					FirstResponder.class, new CompoundDTORule(FirstResponderDO.class, FirstResponder.class,
							FirstResponderDeepDepthRule.NAME));

			return firstResponder;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING updateDeviceInfo", e);
			throw e;
		}
	}

	public List<FirstResponder> searchFirstResponderForEmergency(Event emergency) {
		return null;

	}

	@Override
	public List<FirstResponder> getFirstResponderToBeNotified(int catFRPriority) {

		List<FirstResponderDO> firstResponderDoListDO = firstResponderService
				.getFirstResponderToBeNotified(catFRPriority);

		List<FirstResponder> firstResponderList = (List<FirstResponder>) dtoService.convertCollection(
				firstResponderDoListDO, FirstResponder.class,
				new CompoundDTORule(FirstResponderDO.class, FirstResponder.class, FirstResponderActivationRule.NAME));

		Collections.sort(firstResponderList, new FRSortComparator());

		return firstResponderList;

	}

	@Override
	public List<FirstResponder> getFRToBeNotifiedModifyOrClosure(String event, int catFRPriority) {

		List<FirstResponderDO> firstResponderDoListDO = firstResponderService.getFRToBeNotifiedModifyOrClosure(event,
				catFRPriority);

		List<FirstResponder> firstResponderList = (List<FirstResponder>) dtoService.convertCollection(
				firstResponderDoListDO, FirstResponder.class,
				new CompoundDTORule(FirstResponderDO.class, FirstResponder.class, FirstResponderActivationRule.NAME));

		Collections.sort(firstResponderList, new FRSortComparator());

		return firstResponderList;
	}

	@Override
	public List<FirstResponder> searchFirstResponderByFilter(FirstResponderFilter firstResponderFilter) {

		try {

			List<FirstResponderDO> firstResponderDoList = null;

			FirstResponderFilterDO firstResponderFilterDO = null;

			if (StringUtils.isEmpty(firstResponderFilter.getFetchRule())) {
				firstResponderFilter.setFetchRule(FirstResponderDeepDepthRule.NAME);
			}

			firstResponderFilterDO = dtoService.convertObject(firstResponderFilter, FirstResponderFilterDO.class);

			firstResponderDoList = firstResponderService.searchFirstResponderByFilter(firstResponderFilterDO);

			List<FirstResponder> firstResponderList = (List<FirstResponder>) dtoService.convertCollection(
					firstResponderDoList, FirstResponder.class, new CompoundDTORule(FirstResponderDO.class,
							FirstResponder.class, firstResponderFilter.getFetchRule()));

			Collections.sort(firstResponderList, new FRSortComparator());

			return firstResponderList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getFirstResponderByFilter", e);
			throw e;
		}

	}

	private boolean validateFirstResponder(FirstResponder firstResponder) throws GenericException {
		if (StringUtils.isEmpty(firstResponder.getEmail())) {
			throw new GenericException("Il campo email è obbligatorio");
		}
		if (StringUtils.isEmpty(firstResponder.getLogon()) && StringUtils.isEmpty(firstResponder.getEmail())) {
			throw new GenericException("Il campo email è obbligatorio");
		}

		if (StringUtils.isEmpty(firstResponder.getCodiceFiscale())) {
			throw new GenericException("Il campo Codice Fiscale è obbligatorio");
		}

		String cfRegex = anaService.getParametro(ParametersEnum.REGEX_CODICE_FISCALE.name()).getValore().trim();
		if (!StringUtils.isEmpty(cfRegex)) {
			String CF = firstResponder.getCodiceFiscale().toUpperCase();
			logger.debug("CHECK CODICE FISCALE: " + CF);
			boolean matches = CF.matches(cfRegex);
			if (!matches) {
				throw new GenericException("Il Codice Fiscale non è corretto");
			}
		}

		if (StringUtils.isEmpty(firstResponder.getId())) {
			FirstResponderFilterDO flt = new FirstResponderFilterDO();
			flt.setLogon(firstResponder.getLogon());
			flt.setFetchRule(FirstResponderBareRule.NAME);
			List<FirstResponderDO> fltDOS = firstResponderService.searchFirstResponderByFilter(flt);
			if (fltDOS.size() > 0) {
				throw new GenericException("Indirizzo email già in uso");
			}
		}

		return true;
	}

	@Override
	public FirstResponder updateFRNumIntervEseguiti(String frID, Integer numIntEseguiti) {
		FirstResponderDO frDO = firstResponderService.updateFRNumIntervEseguiti(frID, numIntEseguiti);
		FirstResponder firstResponderDTO = (FirstResponder) dtoService.convertObject(frDO, FirstResponder.class,
				new CompoundDTORule(FirstResponderDO.class, FirstResponder.class, FirstResponderBareRule.NAME));
		return firstResponderDTO;

	}

	@Override
	public FirstResponder saveFirstResponder(FirstResponder firstResponder, Utente user) throws Exception {
		boolean sendNotificationPush = false;
		try {

			if (firstResponder.getIndirizzo() != null && StringUtils.isEmpty(firstResponder.getIndirizzo().getId())) {
				logger.info("NEW STREET DETECTED I'VE TO PERSIST...." + firstResponder.getIndirizzo().toString()
						+ " COMUNE: " + firstResponder.getComuneResidenza());
				Strade newStreet = firstResponder.getIndirizzo();
				newStreet.setName(newStreet.getName().toUpperCase());
				newStreet.setComune(firstResponder.getComuneResidenza());
				newStreet = anaDeleService.saveStrada(newStreet);
				firstResponder.setIndirizzo(newStreet);
				firstResponder.setComuneResidenza(newStreet.getComune());
			}

			FirstResponderDO firstResponderDO = (FirstResponderDO) dtoService.convertObject(firstResponder,
					FirstResponderDO.class, new CompoundDTORule(FirstResponder.class, FirstResponderDO.class,
							FirstResponderDeepDepthRule.NAME));

			if (!StringUtils.isEmpty(firstResponderDO.getId())) {
				FirstResponderDO frDO = firstResponderService.getFirstResponderById(FirstResponderDeepDepthRule.NAME,
						firstResponderDO.getId());
				firstResponderDO.getUtente().setId(frDO.getUtente().getId());
				if (firstResponder.getStatoProfilo() != frDO.getStatoProfilo()) {
					sendNotificationPush = true;

					firstResponderDO.setDisponibile(FRUtils.isDisponibile(firstResponderDO));

					if (firstResponder.getStatoProfilo() == FRStatoProfiloEnum.ATTIVO) {
						firstResponder.setDataValidazione(new Date());
						firstResponder.setUtenteValidazione(user != null ? user.getEmail() : null);
					}
				}
			}

			validateFirstResponder(firstResponder);
			// aggiungo il ruolo al FR

			if (StringUtils.isEmpty(firstResponderDO.getUtente().getId())) {
				// se l'utente non ha l'id quindi è un inserimento
				if (firstResponderDO.getUtente().getRuoli() == null) {
					firstResponderDO.getUtente().setRuoli(new HashSet<>());
				}
				if (firstResponderDO.getUtente().getRuoli().isEmpty()) {
					RuoloDO ruolo = userService.getRuoloByName(RuoliEnum.FIRST_RESPONDER.name());
					firstResponderDO.getUtente().getRuoli().add(ruolo);
				}
			}

			//
			if (StringUtils.isEmpty(firstResponder.getLogon())) {
				firstResponder.setLogon(firstResponder.getEmail());
			}

			if (!StringUtils.isEmpty(firstResponder.getPassword())) {
				String hashed = Hashing.sha256().hashString(firstResponder.getPassword(), StandardCharsets.UTF_8)
						.toString();
				firstResponderDO.getUtente().setPasswordHash(hashed);
			}

			if (firstResponder.getStatoProfilo() == null) {
				firstResponderDO.setStatoProfilo(FRStatoProfiloEnum.IN_ATTESA_DI_ATTIVAZIONE);
			}

			if (firstResponder.getDataIscrizione() == null) {
				firstResponderDO.setDataIscrizione(new Date());
			}
			// ricarico l'immagine se non mi è arrivata nel save
			if ((!StringUtils.isEmpty(firstResponderDO.getId())) && firstResponderDO.getUtente().getImmagine() != null
					&& StringUtils.isEmpty(firstResponderDO.getUtente().getImmagine().getData())) {
				FirstResponderDO actualFR = firstResponderService.getFirstResponderById(FirstResponderImageRule.NAME,
						firstResponderDO.getId());
				firstResponderDO.getUtente().setImmagine(actualFR.getUtente().getImmagine());
			}
			// ricarico l'immagine se non mi è arrivata nel save
			if ((firstResponderDO.getCertificatoFr() != null) && (!StringUtils.isEmpty(firstResponderDO.getId()))
					&& firstResponderDO.getCertificatoFr().getImmagine() != null
					&& StringUtils.isEmpty(firstResponderDO.getCertificatoFr().getImmagine().getData())) {
				FirstResponderDO actualFR = firstResponderService
						.getFirstResponderById(FirstResponderDeepDepthRule.NAME, firstResponderDO.getId());
				firstResponderDO.getCertificatoFr().setImmagine(actualFR.getCertificatoFr().getImmagine());
			}

			FirstResponderDO returnedFirstResponderDo = firstResponderService.saveFirstResponder(firstResponderDO);

			FirstResponder returnedFirstResponder = getFirstResponderById(FirstResponderDeepDepthRule.NAME,
					returnedFirstResponderDo.getId());

			boolean enabledMail = Boolean
					.valueOf(anaService.getParametro(ParametersEnum.ENABLE_MAIL.name()).getValore());
			try {
				if (firstResponder.getId() == null && enabledMail) {
					// genero l'url di conferma dell'account
					String address = anaService.getParametro(ParametersEnum.PUBLIC_ADDRESS.name()).getValore();
					String confirmURL = address + "/frservice/activateProfile/" + returnedFirstResponder.getId();
					returnedFirstResponder.setConfirmURL(confirmURL);

					// passo l'oggetto salvato perchè ha l'id valorizzato
					sendMail(returnedFirstResponder);

					// (Mauro ) commentato dopo il TS di febbraio 2018
					// sendMailToAdministrator(returnedFirstResponder);
				}

				if (sendNotificationPush) {

					logger.info("I HAVE TO NOTIFY PUSH MESSAGE - PROFILE UPDATED. FR ID: " + firstResponder.getId());
					List<FirstResponder> distlist = new ArrayList<FirstResponder>();
					distlist.add(firstResponder);
					notificationService.notifyMessageToFirstResponderList("Aggiornamento del tuo stato profilo",
							distlist);
					if (enabledMail) {
						// mando pure la mail di avvenuta attiazione
						mailService.sendMail(firstResponder.getEmail(), firstResponder,
								MailTemplateEnum.ENABLE_FR_MAIL_TEMPLATE, EntityType.USER, firstResponder.getId());
					}
				}
			} catch (Exception e) {
				logger.error("ERROR WHILE MANAGING NOTIFICATION", e);
			}

			return returnedFirstResponder;

		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING insertFirstResponder", e);
			throw e;
		}
	}

	protected void sendMail(Utente firstResponder) {
		mailService.sendMail(firstResponder.getEmail(), firstResponder, MailTemplateEnum.NEW_USER_MAIL_TEMPLATE,
				EntityType.USER, firstResponder.getId());
	}

	protected void sendMailToAdministrator(FirstResponder firstResponder) {
		logger.info("Send mail to administrator of provinvce "
				+ firstResponder.getComuneResidenza().getProvincia().getNomeProvincia());
		// carico l'amministratore della provincia
		List<ResponsabileProvinciaDO> responsabili = anaService
				.searchResponsabileByProvince(firstResponder.getComuneResidenza().getProvincia().getNomeProvincia());

		responsabili.forEach(r -> {
			mailService.sendMail(r.getEmail(), firstResponder, MailTemplateEnum.ADMINISTRATOR_NEW_USER_MAIL_TEMPLATE,
					EntityType.USER, firstResponder.getId());
		});

	}

	/*
	 * @Override public List<VCTFirstResponderDO> getAllVctFirstResponder() {
	 * try { ContextHolder.setDataSourceType(DataSourceType.GIS); return
	 * firstResponderService.getAllVctFirstResponder(); } finally {
	 * ContextHolder.clearDataSourceType(); } }
	 */

	@Override
	public boolean deleteFirstResponderById(String id) {
		try {
			return firstResponderService.deleteFirstResponderById(id);
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllVctDae", e);
			return false;
		}
	}

	@Override
	public Questionario insertQuestionario(Questionario questionario) {
		try {
			QuestionarioDO questionarioDO = (QuestionarioDO) dtoService.convertObject(questionario,
					QuestionarioDO.class,
					new CompoundDTORule(Questionario.class, QuestionarioDO.class, QuestionarioDeepDepthRule.NAME));

			QuestionarioDO returnedQuestionarioDo = firstResponderService.insertQuestionario(questionarioDO);
			returnedQuestionarioDo = firstResponderService.getQuestionarioById(returnedQuestionarioDo.getId());

			Questionario returnedQuestionario = (Questionario) dtoService.convertObject(returnedQuestionarioDo,
					Questionario.class,
					new CompoundDTORule(QuestionarioDO.class, Questionario.class, QuestionarioDeepDepthRule.NAME));

			return returnedQuestionario;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING insertQuestionario", e);
			throw e;
		}
	}

	@Override
	public Questionario getQuestionarioById(String id) {
		try {
			QuestionarioDO questionarioDo = null;

			questionarioDo = firstResponderService.getQuestionarioById(id);

			Questionario questionario = (Questionario) dtoService.convertObject(questionarioDo, Questionario.class,
					new CompoundDTORule(QuestionarioDO.class, Questionario.class, QuestionarioDeepDepthRule.NAME));

			return questionario;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getQuestionarioById", e);
			throw e;
		}

	}

	@Override
	public boolean deleteQuestionarioById(String id) {
		try {
			return firstResponderService.deleteQuestionarioById(id);
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING deleteQuestionarioById", e);
			return false;
		}
	}

	@Override
	public FirstResponder updatePositionFirstResponderById(String frID, FRLocation gpsLocation) {

		try {

			FirstResponderDO returnedFirstResponderDo = firstResponderService
					.getFirstResponderById(FirstResponderDeepDepthRule.NAME, frID);
			if (gpsLocation != null) {
				GPSLocationDO gpsLocDO = dtoService.convertObject(gpsLocation, GPSLocationDO.class);
				if (gpsLocDO.getTimeStamp() == null) {
					gpsLocDO.setTimeStamp(new Date());
				}
				LocationDO locDO = dtoService.convertObject(gpsLocation, LocationDO.class);

				if (returnedFirstResponderDo.getLastPosition() != null) {
					locDO.setId(returnedFirstResponderDo.getLastPosition().getId());
				}
				if (returnedFirstResponderDo.getLastPosition() != null
						&& returnedFirstResponderDo.getLastPosition().getGpsLocation() != null) {
					gpsLocDO.setId(returnedFirstResponderDo.getLastPosition().getGpsLocation().getId());
				}
				if (gpsLocation.getComune() != null) {
					ComuneDO comDO = dtoService.convertObject(gpsLocation.getComune(), ComuneDO.class);
					locDO.setComune(comDO);
				}
				locDO.setGpsLocation(gpsLocDO);
				returnedFirstResponderDo.setLastPosition(locDO);
			}

			Boolean saveIntCoord = Boolean
					.valueOf(anaDeleService.getParameter(ParametersEnum.SAVE_INTERVENTION_COORD.name(), "false"));

			returnedFirstResponderDo = firstResponderService.updateFirstResponder(returnedFirstResponderDo);

			if (saveIntCoord) {

				logger.info("CHECK IF " + returnedFirstResponderDo.getUtente().getNome() + " "
						+ returnedFirstResponderDo.getUtente().getCognome() + " IS IN INTERVENTION");
				Intervento inter = eventDelegateService
						.getInterventoAttivoByFirstResponder(returnedFirstResponderDo.getId());
				if (inter != null) {
					logger.info("YES!" + returnedFirstResponderDo.getUtente().getNome() + " "
							+ returnedFirstResponderDo.getUtente().getCognome()
							+ " IS IN INTERVENTION. I HAVE TO STORE COORDINATES");

					InterventoDO intDO = evenTransactionalService.getInterventoById(inter.getId());
					InterventoCoordDO coordDO = new InterventoCoordDO();
					coordDO.setIntervento(intDO);
					coordDO.setType(Type.U);
					coordDO.setLatitudine(Double.valueOf(gpsLocation.getLatitudine()));
					coordDO.setLongitudine(Double.valueOf(gpsLocation.getLongitudine()));
					coordDO.setDistanza(gpsLocation.getDistanza());
					coordDO.setDataCreazione(Calendar.getInstance().getTime());
					firstResponderService.saveInterventoCoord(coordDO);

				}
			}
			FirstResponder returnedFirstResponder = getFirstResponderById(FirstResponderBareRule.NAME, frID);

			return returnedFirstResponder;

		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING updatePositionFirstResponderById", e);
			throw e;
		}
	}

	@Override
	public FirstResponder updateAvailability(String frID, boolean availability) {

		try {

			FirstResponderDO returnedFirstResponderDo = firstResponderService.updateAvailability(frID, availability);

			FirstResponder returnedFirstResponder = getFirstResponderById(FirstResponderDeepDepthRule.NAME, frID);

			return returnedFirstResponder;

		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING updateAvailability", e);
			throw e;
		}
	}

	@Override
	public Utente getUtenteById(String id) {
		UtenteDO ut = firstResponderService.getUtenteByID(id);
		Utente utDTO = dtoService.convertObject(ut, Utente.class,
				new CompoundDTORule(UtenteDO.class, Utente.class, UtenteDeepDepthRule.NAME));
		return utDTO;
	}

	@Override
	public Utente saveUtente(Utente ut) {
		UtenteDO utDO = dtoService.convertObject(ut, UtenteDO.class,
				new CompoundDTORule(Utente.class, UtenteDO.class, UtenteDeepDepthRule.NAME));
		utDO = firstResponderService.saveUtenteDO(utDO);
		Utente utDTO = dtoService.convertObject(utDO, Utente.class,
				new CompoundDTORule(UtenteDO.class, Utente.class, UtenteDeepDepthRule.NAME));
		return utDTO;
	}

	@Override
	public List<Utente> searchUtenteByFilter(UtenteFilter filter) {
		UtenteFilterDO utFilter = dtoService.convertObject(filter, UtenteFilterDO.class);
		List<UtenteDO> utList = firstResponderService.searchUtentiByFilter(utFilter);
		List<Utente> utDTO = (List<Utente>) dtoService.convertCollection(utList, Utente.class,
				new CompoundDTORule(UtenteDO.class, Utente.class, UtenteDeepDepthRule.NAME));
		return utDTO;

	}

	@Override
	public boolean deleteUtente(Utente ut) {
		if (!StringUtils.isEmpty(ut.getId())) {
			return firstResponderService.deleteUtenteById(ut.getId());
		} else {
			return false;
		}
	}

	@Override
	public FirstResponder getFirstResponderByUserId(String depth, String userId) {

		FirstResponderDO returnedFirstResponderDo = firstResponderService.getFirstResponderByUserId(depth, userId);

		FirstResponder returnedFirstResponder = dtoService.convertObject(returnedFirstResponderDo, FirstResponder.class,
				new CompoundDTORule(FirstResponderDO.class, FirstResponder.class, depth));
		return returnedFirstResponder;

	}

	@Override
	public void cleanDispositivo(String id) {
		firstResponderService.cleanDispositivo(id);
	}

	@Override
	public boolean setDoNotDisturb(String frID, String from, String to) {
		return firstResponderService.setDoNotDisturb(frID, from, to);
	}

	@Override
	public boolean removeDoNotDisturb(String frID) {
		return firstResponderService.removeDoNotDisturb(frID);
	}

	@Override
	public boolean setSilent(String frID, String from, String to) {
		return firstResponderService.setSilent(frID, from, to);
	}

	@Override
	public boolean removeSilent(String frID) {
		return firstResponderService.removeSilent(frID);
	}

	@Override
	public void saveFRPositionToCO(FirstResponder fr, GPSLocation request) {
		// TODO Auto-generated method stub
		FRPositionToCODO pos = new FRPositionToCODO();

		pos.setFirstResponder(dtoService.convertObject(fr, FirstResponderDO.class));
		pos.setTimeStamp(Calendar.getInstance());
		pos.setLatitudine(request.getLatitudine());
		pos.setLongitudine(request.getLongitudine());
		pos.setAccuratezza(request.getAccuratezza());

		firstResponderService.saveFRPositionToCO(pos);
	}

	@Override
	public List<FRPositionToCO> getLastFRPositionToCO(Integer maxResult) {

		List<FRPositionToCODO> res = firstResponderService.getLastFRPositionToCO(maxResult);

		return (List<FRPositionToCO>) dtoService.convertCollection(res, FRPositionToCO.class,
				new CompoundDTORule(FRPositionToCODO.class, FRPositionToCO.class, "BASIC_NUMBER"));
	}

	@Override
	public FirstResponder updateFRProfile(FirstResponder fr) {
		final FirstResponderDO frDO = dtoService.convertObject(fr, FirstResponderDO.class,
				new CompoundDTORule(FirstResponder.class, FirstResponderDO.class, FirstResponderDeepDepthRule.NAME));

		FirstResponderDO toRet = firstResponderService.updateFRProfile(frDO);
		return dtoService.convertObject(toRet, FirstResponder.class,
				new CompoundDTORule(FirstResponderDO.class, FirstResponder.class, "BASIC"));
	}

	@Override
	public boolean deleteLogicallyFR(FirstResponder fr, Utente utente) {
		firstResponderService.deleteLogicallyFR(fr.getId(), dtoService.convertObject(utente, UtenteDO.class));
		return true;
	}

	@Override
	public FirstResponder activateProfile(String frID) {
		FirstResponderDO frDO = firstResponderService.activateProfile(frID);
		FirstResponder toRet = dtoService.convertObject(frDO, FirstResponder.class,
				new CompoundDTORule(FirstResponderDO.class, FirstResponder.class, FirstResponderImageRule.NAME));

		boolean enabledMail = Boolean.valueOf(anaService.getParametro(ParametersEnum.ENABLE_MAIL.name()).getValore());
		if (enabledMail) {
			// mando pure la mail di avvenuta attiazione
			mailService.sendMail(toRet.getEmail(), toRet, MailTemplateEnum.ENABLE_FR_MAIL_TEMPLATE, EntityType.USER,
					toRet.getId());
		}
		return toRet;
	}

}
