package it.eng.areas.ems.sdodaeservices.delegate.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.eng.areas.ems.common.sdo.dto.CompoundDTORule;
import it.eng.areas.ems.common.sdo.dto.DTOService;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.EventDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.EventDetailDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.InterventoBareDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.InterventoDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.InterventoForEventDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.NotificheEventoDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.NotificheEventoEseguitoDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.EventDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.FirstResponderDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.MailDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.PushNotificationService;
import it.eng.areas.ems.sdodaeservices.delegate.model.CheckInterventionBean;
import it.eng.areas.ems.sdodaeservices.delegate.model.CheckInterventionResponse;
import it.eng.areas.ems.sdodaeservices.delegate.model.Comune;
import it.eng.areas.ems.sdodaeservices.delegate.model.EntityType;
import it.eng.areas.ems.sdodaeservices.delegate.model.Event;
import it.eng.areas.ems.sdodaeservices.delegate.model.EventInfoRequest;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.GPSLocation;
import it.eng.areas.ems.sdodaeservices.delegate.model.GenericException;
import it.eng.areas.ems.sdodaeservices.delegate.model.GenericResponse;
import it.eng.areas.ems.sdodaeservices.delegate.model.Intervento;
import it.eng.areas.ems.sdodaeservices.delegate.model.MailTemplateEnum;
import it.eng.areas.ems.sdodaeservices.delegate.model.NotificheEvento;
import it.eng.areas.ems.sdodaeservices.delegate.model.NotificheEvento.Type;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.EventFilter;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.InterventoFilter;
import it.eng.areas.ems.sdodaeservices.entity.CategoriaFrDO;
import it.eng.areas.ems.sdodaeservices.entity.EventDO;
import it.eng.areas.ems.sdodaeservices.entity.FirstResponderDO;
import it.eng.areas.ems.sdodaeservices.entity.InterventoCoordDO;
import it.eng.areas.ems.sdodaeservices.entity.InterventoDO;
import it.eng.areas.ems.sdodaeservices.entity.NotificheEventoDO;
import it.eng.areas.ems.sdodaeservices.entity.NotificheTypeEnum;
import it.eng.areas.ems.sdodaeservices.entity.ParametersEnum;
import it.eng.areas.ems.sdodaeservices.entity.filter.EventFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.InterventoFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.NotificheEventoFilterDO;
import it.eng.areas.ems.sdodaeservices.geo.utility.DistanceCalculator;
import it.eng.areas.ems.sdodaeservices.service.EventTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.FirstResponderTransactionalService;

@Component
public class EventDelegateServiceImpl implements EventDelegateService {

	private Logger logger = LoggerFactory.getLogger(EventDelegateServiceImpl.class);

	@Autowired
	private EventTransactionalService eventService;

	@Autowired
	private FirstResponderTransactionalService frTransactionalService;

	@Autowired
	private FirstResponderDelegateService frService;

	@Autowired
	private PushNotificationService pushNotification;

	@Autowired
	private AnagraficheDelegateService anagraficheService;

	@Autowired
	private MailDelegateService mailService;

	@Autowired
	private DTOService dtoService;

	private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(15);

	@Override
	public long countAll() {
		// TODO Auto-generated method stub

		return eventService.countAll();
	}

	@Override
	public List<Event> getAllEvent() {
		try {
			List<EventDO> eventDoList = eventService.getAllEvent();
			List<Event> eventList = (List<Event>) dtoService.convertCollection(eventDoList, Event.class,
					new CompoundDTORule(EventDO.class, Event.class, EventDeepDepthRule.NAME));
			return eventList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllEvent", e);
			throw e;
		}

	}

	@Override
	public List<Event> getAvailableEventsForFirstResponder(FirstResponder firstResponder) {
		try {

			Integer distanceThreshold = Integer
					.valueOf(anagraficheService.getParameter(ParametersEnum.DISTANCE_THRESHOLD.name(), "10"));
			logger.info("DISTANCE THRESHOLD CONFIGURED:" + distanceThreshold);

			long expirationGPS = anagraficheService.getParameter(ParametersEnum.GPS_COORD_MINUTES_EXPIRATION.name(),
					10);
			logger.info("EXPIRATION GPS CONFIGURED:" + expirationGPS);

			String enableNotificationAlgorithm = anagraficheService
					.getParameter(ParametersEnum.ENABLE_NOTIFICATION_ALGORITHM.name(), "TRUE");

			List<EventDO> evtToreturn = null;

			if (enableNotificationAlgorithm.equals("TRUE")) {
				evtToreturn = eventService.getNotifiedEvents(firstResponder.getId());
			} else {
				// mauro Cambiato algoritmo del get my event

				long actualMillis = System.currentTimeMillis();
				List<EventDO> eventDoList = eventService.getAllActiveEvents(firstResponder.getId());
				evtToreturn = new ArrayList<>();

				for (EventDO event : eventDoList) {
					boolean toAdd = false;

					if (firstResponder.getCategoriaFr().getId().equals("SUPER_USER")) {
						// caso 1 : utente super user
						toAdd = true;
					} else {
						// caso 2 : coordinate non valide: controllo il comune

						boolean invalid = false;
						if (firstResponder.getLastPosition() != null
								&& firstResponder.getLastPosition().getTimeStamp() != null) {
							long diff = actualMillis - firstResponder.getLastPosition().getTimeStamp().getTime();
							long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
							if (minutes > expirationGPS) {
								// se l'ultima posizione ha superato il tempo
								// massimo
								invalid = true;
							}
						} else {
							// se l'ultima posizione non è presente
							invalid = true;
						}
						if (invalid) {
							for (Comune c : firstResponder.getComuniCompetenza()) {
								logger.info("Comune : " + c.getNomeComune());
								if (event.getComune().toLowerCase().contains(c.getNomeComune().toLowerCase())) {
									logger.info(
											"Municipality " + event.getComune() + " is present " + c.getNomeComune());
									toAdd = true;
								}
							}
						} else if (firstResponder.getLastPosition() != null
								&& firstResponder.getLastPosition().getLatitudine() != null
								&& firstResponder.getLastPosition().getLongitudine() != null) {

							double dst = DistanceCalculator.distance(event.getCoordinate().getLatitudine(),
									event.getCoordinate().getLongitudine(),
									firstResponder.getLastPosition().getLatitudine(),
									firstResponder.getLastPosition().getLongitudine(), "K");

							logger.info("DISTANCE BETWEEN EVENT AND FR " + firstResponder.getNome() + " "
									+ firstResponder.getCognome() + " IS " + dst + " KM");
							if (dst <= distanceThreshold) {
								toAdd = true;
							}
						}
					}
					// se l'evento è da aggiungere
					if (toAdd) {
						evtToreturn.add(event);
					}
				}
			}
			List<Event> eventList = (List<Event>) dtoService.convertCollection(evtToreturn, Event.class,
					new CompoundDTORule(EventDO.class, Event.class, EventDeepDepthRule.NAME));
			return eventList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAvailableEventsForFirstResponder", e);
			throw e;
		}

	}

	@Override
	public Event getEventById(String id) {
		try {
			EventDO eventDo = null;

			eventDo = eventService.getEventById(id);

			Event event = (Event) dtoService.convertObject(eventDo, Event.class,
					new CompoundDTORule(EventDO.class, Event.class, EventDeepDepthRule.NAME));
			return event;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getEventById", e);
			throw e;
		}

	}

	@Override
	public Event getEventByCartellinoAndCO(String cartellino, String coRiferimento) {
		try {
			List<EventDO> eventDo = null;
			EventFilterDO filt = new EventFilterDO();
			filt.setCartellinoEvento(cartellino);
			filt.setCoRiferimento(coRiferimento);
			filt.setFetchRule(EventDeepDepthRule.NAME);
			eventDo = eventService.getEventByFilter(filt);
			if (eventDo.size() > 0) {
				EventDO eventDO = eventDo.get(0);
				Event event = (Event) dtoService.convertObject(eventDO, Event.class,
						new CompoundDTORule(EventDO.class, Event.class, EventDeepDepthRule.NAME));
				return event;
			}
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getEventByCartellinoAndCO", e);
			throw e;
		}
		return null;

	}

	@Override
	public CheckInterventionResponse checkIntervention(CheckInterventionBean checkIntervention, FirstResponder fr)
			throws Exception {
		CheckInterventionResponse check = new CheckInterventionResponse();
		Intervento inter = new Intervento();
		inter.setEvent(checkIntervention.getEvent());
		inter.setEseguitoDa(fr);
		inter.setDataCreazione(Calendar.getInstance().getTime());
		InterventoFilter filter = new InterventoFilter();
		filter.setEventId(checkIntervention.getEvent().getId());
		filter.setFetchRule(InterventoBareDepthRule.NAME);
		filter.setAccepted(true);
		Integer nInterEse = 0;
		if (fr.getInterventiEseguiti() != null) {
			nInterEse = fr.getInterventiEseguiti();
		}
		List<Intervento> interAct = searchInterventoByFilter(filter);
		String numMaxFirstResponder = anagraficheService.getParameter(ParametersEnum.MAX_ACTIVED_FIRST_RESPONDER.name(),
				"5");
		Integer maxFR = Integer.valueOf(numMaxFirstResponder);
		EventDO evtDO = eventService.getEventById(checkIntervention.getEvent().getId());
		if (evtDO.getAbortDate() != null || evtDO.getCloseDate() != null) {
			check.setCanManageIntervention(false);
			check.setNote("Emergenza non disponibile");
			inter.setRifiutatoDa("SYSTEM");
			inter.setDataRifiuto(Calendar.getInstance().getTime());
		} else if (interAct.size() > maxFR - 1) {
			inter.setRifiutatoDa("SYSTEM");
			inter.setDataRifiuto(Calendar.getInstance().getTime());
			check.setCanManageIntervention(false);
			check.setNote(maxFR + " Interventi già attivi sull'emergenza");
		} else {
			inter.setDataAccettazione(Calendar.getInstance().getTime());
			inter.setAccettatoDa("SYSTEM");
			check.setCanManageIntervention(true);
			nInterEse++;
		}

		// update evento con numero fr che hanno accettato
		int numAccepted = interAct.size();
		if (check.isCanManageIntervention()) {
			numAccepted++;
		}
		eventService.updateNumAccepted(evtDO.getId(), numAccepted);
		/* fine update evento con numero che hanno accettato */

		inter = insertIntervento(inter);

		if (inter.getAccettatoDa() != null) {
			// (Mauro) invio la mail a chi ha accettato
			sendMailToFR(inter.getEseguitoDa(), inter, MailTemplateEnum.FR_ACCEPTED_EVENT);

			if (checkIntervention.getLocation() != null && checkIntervention.getLocation().getLatitudine() != null
					&& checkIntervention.getLocation().getLongitudine() != null) {
				// aggiungo la coordinata sull'accettazione
				InterventoCoordDO coordDO = new InterventoCoordDO();

				coordDO.setType(it.eng.areas.ems.sdodaeservices.entity.InterventoCoordDO.Type.A);
				coordDO.setIntervento(new InterventoDO(inter.getId()));
				coordDO.setLatitudine(checkIntervention.getLocation().getLatitudine().doubleValue());
				coordDO.setLongitudine(checkIntervention.getLocation().getLongitudine().doubleValue());
				coordDO.setDataCreazione(new Date());

				frTransactionalService.saveInterventoCoord(coordDO);
			}
		}
		frService.updateFRNumIntervEseguiti(fr.getId(), nInterEse);

		return check;

	}

	private void sendMailToFR(FirstResponder fr, Intervento inter, MailTemplateEnum frAcceptedEvent) {

		mailService.sendMail(fr.getEmail(), inter, frAcceptedEvent, EntityType.USER, fr.getId());

	}

	@Override
	public List<Event> getEventByFilter(EventFilter eventFilter) {
		try {
			List<EventDO> eventDoList = null;

			EventFilterDO eventFilterDO = null;

			eventFilterDO = dtoService.convertObject(eventFilter, EventFilterDO.class,
					new CompoundDTORule(EventFilter.class, EventFilterDO.class, EventDeepDepthRule.NAME));

			eventDoList = eventService.getEventByFilter(eventFilterDO);

			List<Event> eventList = (List<Event>) dtoService.convertCollection(eventDoList, Event.class,
					new CompoundDTORule(EventDO.class, Event.class, EventDeepDepthRule.NAME));

			return eventList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getEventByFilter", e);
			throw e;
		}
	}

	@Override
	public int closeOrAbortInterventionByEvent(EventInfoRequest eventInfo, boolean abort) {

		int total = 0;

		if (!it.esel.parsley.lang.StringUtils.isEmpty(eventInfo.getCartellino())) {
			logger.info("SEARCHING ACTIVE INTERVENTION FOR EVENT: " + eventInfo.getCartellino() + " OP CENTRAL: "
					+ eventInfo.getCoRiferimento());

			StringBuffer message = new StringBuffer();
			if (!abort) {
				message.append(anagraficheService.getParameter(ParametersEnum.CLOSE_EVENT_MESSAGE_TEXT.name(),
						"Chiusura intervento"));
			} else {
				message.append(anagraficheService.getParameter(ParametersEnum.ABORT_EVENT_MESSAGE_TEXT.name(),
						"Annullamento intervento"));
			}

			InterventoFilterDO fltDO = new InterventoFilterDO();
			fltDO.setCartellinoEvento(eventInfo.getCartellino());
			fltDO.setCoRiferimento(eventInfo.getCoRiferimento());
			fltDO.setFetchRule(InterventoDeepDepthRule.NAME);
			fltDO.setAccepted(true);
			// invio le notifiche a tutti gli utneti compresi quelli arrivati
			// sul posto
			// fltDO.setNotClosed(true);
			List<InterventoDO> intDO = eventService.searchInterventionByFilter(fltDO);

			EventFilterDO filt = new EventFilterDO();
			filt.setCartellinoEvento(eventInfo.getCartellino());
			filt.setCoRiferimento(eventInfo.getCoRiferimento());
			filt.setFetchRule(EventDeepDepthRule.NAME);
			List<EventDO> eventsDo = eventService.getEventByFilter(filt);

			if (eventsDo.size() > 0) {
				for (EventDO eventDO : eventsDo) {
					if (abort) {
						eventDO.setAbortDate(Calendar.getInstance());
					} else {
						eventDO.setCloseDate(Calendar.getInstance());
					}
					eventDO.setClosed(true);
					eventService.saveEvent(eventDO);

					final EventDO ev = eventService.getEventById(EventDeepDepthRule.NAME, eventDO.getId());

					// (Mauro ) invio le notifiche a tutti i FR
					List<FirstResponderDO> frsToNotified = frTransactionalService
							.getFRToBeNotifiedModifyOrClosure(ev.getId(), ev.getCategoriaFr().getPriority());

					total += frsToNotified.size();

					frsToNotified.forEach(firstResponder -> {
						executor.submit(() -> {
							try {
								// mauro - tolto controlli sulla distanza
								String esitoPush = pushNotification.notifyAbortToFirstResponder(message.toString(),
										ev.getId(), firstResponder.getDispositivo().getPushToken());
								logger.info("CLOSING/ABORT INTERVENTION PUSH SENT TO FR: " + firstResponder.getId()
										+ " STATUS: " + esitoPush);
								NotificheEventoDO not = new NotificheEventoDO();
								EventDO eDO = eventService.getEventById(ev.getId());
								not.setEvent(eDO);
								not.setTimestamp(Calendar.getInstance().getTime());
								not.setFirstResponder(firstResponder);
								not.setTipoNotifica(NotificheTypeEnum.ABORT);
								not.setEsito(esitoPush);
								eventService.insertNotificheEvento(not);

							} catch (Exception e) {
								logger.error(
										"CANNOT NOTIFY TO FR:" + firstResponder.getUtente().getNome() + " "
												+ firstResponder.getUtente().getCognome() + " CAUSE LOCATION EXCEPTION",
										e);
							}
						});
					});
				}
			}

			if (intDO.size() > 0) {
				logger.info(
						intDO.size() + " INTERVENTION FOUND FIRST FOR CLOSING/ABORT ON DATABASE (abort:" + abort + ")");
				for (InterventoDO interventoDO : intDO) {
					if (interventoDO.getEseguitoDa() != null && interventoDO.getEseguitoDa().getDispositivo() != null) {
						try {
							// (Mauro) sia chiusura che annullamento riempiono
							// la data di annullamento.
							// La data di chiusura viene riempita solo dall'app
							// quando si arriva sul posto.
							interventoDO.setAnnullatoDa("REMOTE_SYSTEM");
							interventoDO.setDataAnnullamento(Calendar.getInstance().getTime());

							eventService.insertIntervento(interventoDO);
							logger.info("CLOSING/ABORT INTERVENTION INFORMATION UPDATED ON DATABASE...");
						} catch (Exception e) {
							logger.error("ERROR WHILE CLOSING/ABORT INTERVENTION INFORMATION  ON DATABASE.", e);
						}
					}
				}
			}
		}

		return total;
	}

	@Override
	public List<Intervento> updateEvent(Event event) {

		logger.info("I'VE TO UPDATE EVENT WITH CARTELLINO: " + event.getCartellino() + " AND CO RIFERIMENTO: "
				+ event.getCoRiferimento());
		Event retEvt = getEventByCartellinoAndCO(event.getCartellino(), event.getCoRiferimento());
		if (retEvt != null) {
			logger.info("EVENT  EVENT WITH CARTELLINO: " + event.getCartellino() + " AND CO RIFERIMENTO: "
					+ event.getCoRiferimento() + " FOUND WITH ID: " + retEvt.getId());
			event.setId(retEvt.getId());
			insertEvent(event);
			logger.info("EVENT UPDATED..:");
			InterventoFilterDO fltDO = new InterventoFilterDO();
			fltDO.setCartellinoEvento(retEvt.getCartellino());
			fltDO.setCoRiferimento(retEvt.getCoRiferimento());
			fltDO.setFetchRule(InterventoDeepDepthRule.NAME);
			fltDO.setAccepted(true);
			fltDO.setNotClosed(true);
			// Da controllare
			// fltDO.setNotClosed(true);
			List<InterventoDO> intDO = eventService.searchInterventionByFilter(fltDO);
			if (intDO.size() > 0) {
				logger.info(intDO.size() + " INTERVENTION FOUND FOR EVENT WITH CARTELLINO: " + event.getCartellino()
						+ " AND CO RIFERIMENTO: " + event.getCoRiferimento());
				for (InterventoDO interventoDO : intDO) {
					executor.execute(() -> {
						if (interventoDO.getEseguitoDa() != null
								&& interventoDO.getEseguitoDa().getDispositivo() != null) {
							String esitoPush = pushNotification.notifyEventUpdateToFirstResponder(
									interventoDO.getEvent().getId(),
									interventoDO.getEseguitoDa().getDispositivo().getPushToken());
							logger.info("UPDATE EVENT  PUSH SENT TO FR: " + interventoDO.getEseguitoDa().getId()
									+ " STATUS: " + esitoPush);
							NotificheEventoDO not = new NotificheEventoDO();
							EventDO eDO = eventService.getEventById(interventoDO.getEvent().getId());
							not.setEvent(eDO);
							not.setTimestamp(Calendar.getInstance().getTime());
							not.setFirstResponder(interventoDO.getEseguitoDa());
							not.setTipoNotifica(NotificheTypeEnum.UPDATE);
							not.setEsito(esitoPush);
							eventService.insertNotificheEvento(not);
							logger.info("UPDATE EVENT INFORMATION UPDATED...");
						}
					});

				}
			} else if (intDO.size() == 0) {
				logger.info("NOBODY HAS ACCEPTED  EVENT WITH CARTELLINO: " + event.getCartellino()
						+ " AND CO RIFERIMENTO: " + event.getCoRiferimento() + " RE-ALERTING EVERYONE");

				Integer distanceThreshold = Integer
						.valueOf(anagraficheService.getParameter(ParametersEnum.DISTANCE_THRESHOLD.name(), "10"));

				logger.info("DISTANCE THRESHOLD CONFIGURED:" + distanceThreshold);

				// Mauro - sostituito metodo per la ricerca dei fr
				List<FirstResponder> frsToNotified = frService.getFRToBeNotifiedModifyOrClosure(event.getId(),
						event.getCategoria().getPriority());
				for (FirstResponder firstResponder : frsToNotified) {
					executor.execute(() -> {
						try {
							// tolto controlli sulla distanza ecc
							pushNotification.notifyEventUpdateToFirstResponder(event, firstResponder);
						} catch (Exception e) {
							logger.error("CANNOT NOTIFY TO FR:" + firstResponder.getId() + " CAUSE LOCATION EXCEPTION",
									e);
						}
					});
				}
				logger.info("THE EVENT UPDATE: " + event.getId() + " WAS NOTIFIED TO " + frsToNotified.size()
						+ " FIRST RESPONDER");

			}

			List<Intervento> interv = (List<Intervento>) dtoService.convertCollection(intDO, Intervento.class,
					new CompoundDTORule(InterventoDO.class, Intervento.class, InterventoDeepDepthRule.NAME));
			return interv;
		}
		return null;
	}

	@Override
	public List<FirstResponder> alertFirstResponderForNewEvent(Event event) {
		// Carico le configurazioni
		Integer distanceThreshold = Integer
				.valueOf(anagraficheService.getParameter(ParametersEnum.DISTANCE_THRESHOLD.name(), "10"));
		logger.info("DISTANCE THRESHOLD CONFIGURED:" + distanceThreshold);

		long expirationGPS = anagraficheService.getParameter(ParametersEnum.GPS_COORD_MINUTES_EXPIRATION.name(), 10);
		logger.info("EXPIRATION GPS CONFIGURED:" + expirationGPS);

		boolean municipalityEqual = anagraficheService.getParameter(ParametersEnum.MUNICIPALITY_EQUAL.name(), false);

		List<FirstResponder> frsToNotified = frService
				.getFirstResponderToBeNotified(event.getCategoria().getPriority());

		logger.info("I HAVE TO CHECK " + frsToNotified.size() + " FR THAT ARE NOT IN INTERVENTION");

		long actualMillis = System.currentTimeMillis();

		logger.info("EVENT : " + event.getCartellino() + " " + event.getComune() + " "
				+ event.getCoordinate().getLatitudine() + "," + event.getCoordinate().getLongitudine());

		for (FirstResponder firstResponder : frsToNotified) {
			logger.info("USER : " + firstResponder.getEmail() + " " + firstResponder.getNome() + " "
					+ firstResponder.getCognome());
			// Se l'utente è super user bypasso qualsiasi controllo
			if (firstResponder.getCategoriaFr().getId().equals("SUPER_USER")) {

				executor.submit(() -> {
					logger.info("SUPER USER : " + firstResponder.getNome() + " " + firstResponder.getCognome());
					try {
						String esito = pushNotification.notifyNewEmergencyToFirstResponder(event, firstResponder,
								Type.SUPER);
						logger.info("ESITO PUSH: " + esito);

					} catch (Exception e) {
						logger.error(
								"CANNOT NOTIFY TO FR:" + firstResponder.getNome() + " " + firstResponder.getCognome(),
								e);
					}
				});
			} else {
				boolean invalid = false;
				if (firstResponder.getLastPosition() != null
						&& firstResponder.getLastPosition().getTimeStamp() != null) {
					long diff = actualMillis - firstResponder.getLastPosition().getTimeStamp().getTime();
					long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
					if (minutes > expirationGPS) {
						// se l'ultima posizione ha superato il tempo massimo
						invalid = true;
					}
				} else {
					// se l'ultima posizione non è presente
					invalid = true;
				}
				if (invalid) {
					executor.submit(() -> {
						try {
							logger.info("Position is invalid for : " + firstResponder.getEmail());
							// se la posizione non è valida, invio la notifica
							// solo se il comune dell'evento è compreso nella
							// lista dei comuni
							boolean present = false;

							for (Comune c : firstResponder.getComuniCompetenza()) {
								logger.info("Comune : " + c.getNomeComune());
								boolean equal = false;

								if (municipalityEqual) {
									String comune = event.getComune().split("-")[0].trim().toLowerCase();
									equal = comune.equals(c.getNomeComune().toLowerCase());
								} else {
									equal = event.getComune().toLowerCase().startsWith(c.getNomeComune().toLowerCase());
								}

								if (equal) {
									logger.info(
											"Municipality " + event.getComune() + " is present " + c.getNomeComune());
									present = true;
								}
							}
							if (present) {
								String esito = pushNotification.notifyNewEmergencyToFirstResponder(event,
										firstResponder, Type.COMUNE);
								logger.info("ESITO PUSH: " + esito);
							}
						} catch (Exception e) {
							logger.error("CANNOT NOTIFY TO FR:" + firstResponder.getNome() + " "
									+ firstResponder.getCognome(), e);
						}
					});

				} else if (firstResponder.getLastPosition() != null
						&& firstResponder.getLastPosition().getLatitudine() != null
						&& firstResponder.getLastPosition().getLongitudine() != null) {
					executor.submit(() -> {
						try {
							double dst = DistanceCalculator.distance(event.getCoordinate().getLatitudine(),
									event.getCoordinate().getLongitudine(),
									firstResponder.getLastPosition().getLatitudine(),
									firstResponder.getLastPosition().getLongitudine(), "K");

							logger.info("DISTANCE BETWEEN EVENT AND FR " + firstResponder.getNome() + " "
									+ firstResponder.getCognome() + " IS " + dst + " KM - THRESHOLD IS: "
									+ distanceThreshold);

							if (dst <= distanceThreshold) {

								String esito = pushNotification.notifyNewEmergencyToFirstResponder(event,
										firstResponder, Type.COORD);
								logger.info("ESITO PUSH: " + esito);
							}
						} catch (Exception e) {
							logger.error("CANNOT NOTIFY TO FR:" + firstResponder.getNome() + " "
									+ firstResponder.getCognome() + " CAUSE LOCATION EXCEPTION", e);
						}
					});

				} else {
					logger.warn("NO POSITION FOR FR: " + firstResponder.getNome() + " " + firstResponder.getCognome());
				}
			}
		}
		logger.info("THE EVENT: " + event.getId() + " WAS NOTIFIED TO FIRST RESPONDER");
		return frsToNotified;

	}

	@Override
	public Event insertEvent(Event event) {

		try {
			EventDO eventDO = (EventDO) dtoService.convertObject(event, EventDO.class,
					new CompoundDTORule(Event.class, EventDO.class, EventDeepDepthRule.NAME));
			if (eventDO.getCategoriaFr() == null
					|| (eventDO.getCategoriaFr() != null && StringUtils.isEmpty(eventDO.getCategoriaFr().getId()))) {
				CategoriaFrDO defCatFR = new CategoriaFrDO();
				defCatFR.setId("FIRST_RESPONDER");
				eventDO.setCategoriaFr(defCatFR);
			}

			if (eventDO.getTimestamp() == null) {
				eventDO.setTimestamp(Calendar.getInstance());
			}
			EventDO returnedEventDo = eventService.saveEvent(eventDO);
			returnedEventDo = eventService.getEventById(returnedEventDo.getId());

			Event returnedEvent = (Event) dtoService.convertObject(returnedEventDo, Event.class,
					new CompoundDTORule(EventDO.class, Event.class, EventDeepDepthRule.NAME));

			return returnedEvent;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING insertEvent", e);
			throw e;
		}
	}

	/*
	 * @Override public List<VCTEventDO> getAllVctEvent() { try {
	 * ContextHolder.setDataSourceType(DataSourceType.GIS); return
	 * eventService.getAllVctEvent(); } finally {
	 * ContextHolder.clearDataSourceType(); } }
	 */

	@Override
	public boolean deleteEventById(String id) {
		try {
			return eventService.deleteEventById(id);
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllVctDae", e);
			throw e;

		}
	}

	@Override
	public Intervento insertIntervento(Intervento intervento) {
		try {
			InterventoDO interventoDO = (InterventoDO) dtoService.convertObject(intervento, InterventoDO.class,
					new CompoundDTORule(Intervento.class, InterventoDO.class, InterventoDeepDepthRule.NAME));

			InterventoDO returnedInterventoDo = eventService.insertIntervento(interventoDO);
			returnedInterventoDo = eventService.getInterventoById(returnedInterventoDo.getId());

			Intervento returnedIntervento = (Intervento) dtoService.convertObject(returnedInterventoDo,
					Intervento.class,
					new CompoundDTORule(InterventoDO.class, Intervento.class, InterventoDeepDepthRule.NAME));

			return returnedIntervento;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING insertIntervento", e);
			throw e;
		}
	}

	@Override
	public List<Intervento> searchInterventoByFilter(InterventoFilter filter) {

		try {
			InterventoFilterDO filtDO = dtoService.convertObject(filter, InterventoFilterDO.class);

			List<InterventoDO> intsDO = eventService.searchInterventionByFilter(filtDO);

			List<Intervento> interv = (List<Intervento>) dtoService.convertCollection(intsDO, Intervento.class,
					new CompoundDTORule(InterventoDO.class, Intervento.class, filter.getFetchRule()));
			return interv;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING searchInterventoByFilter", e);
			throw e;
		}

	}

	@Override
	public Intervento getInterventoById(String id) {
		try {
			InterventoDO interventoDo = null;

			interventoDo = eventService.getInterventoById(id);

			Intervento intervento = (Intervento) dtoService.convertObject(interventoDo, Intervento.class,
					new CompoundDTORule(InterventoDO.class, Intervento.class, InterventoDeepDepthRule.NAME));
			return intervento;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getInterventoById", e);
			throw e;
		}
	}

	@Override
	public boolean deleteInterventoById(String id) {
		try {
			return eventService.deleteInterventoById(id);
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllVctDae", e);
			throw e;

		}
	}

	@Override
	public List<Intervento> getAllIntervento() {
		try {
			List<InterventoDO> interventoDoList = eventService.getAllIntervento();
			List<Intervento> interventoList = (List<Intervento>) dtoService.convertCollection(interventoDoList,
					Intervento.class,
					new CompoundDTORule(InterventoDO.class, Intervento.class, InterventoDeepDepthRule.NAME));
			return interventoList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllIntervento", e);
			throw e;
		}
	}

	@Override
	public List<Intervento> getInterventiByFirstResponder(FirstResponder firstResponder) {

		try {

			FirstResponderDO firstResponderDO = (FirstResponderDO) dtoService.convertObject(firstResponder,
					FirstResponderDO.class, new CompoundDTORule(FirstResponder.class, FirstResponderDO.class,
							FirstResponderDeepDepthRule.NAME));

			List<InterventoDO> interventoDoList = eventService.getInterventiByFirstResponder(firstResponderDO);
			List<Intervento> interventoList = (List<Intervento>) dtoService.convertCollection(interventoDoList,
					Intervento.class,
					new CompoundDTORule(InterventoDO.class, Intervento.class, InterventoDeepDepthRule.NAME));
			return interventoList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getInterventiByFirstResponder", e);
			throw e;
		}
	}

	@Override
	public boolean closeIntervention(String firstResponderId, String eventID, GPSLocation coordinate) {
		logger.info(
				"CHECKING IF FIRSTRESPONDER: " + firstResponderId + " HAS ACTIVE INTERVENTION FOR EVENT_ID " + eventID);
		Intervento interv = getInterventoAttivoByFirstResponderAndEventId(firstResponderId, eventID);
		// se ho un intervento e non è stato già chiuso dall'utente
		if (interv != null && interv.getDataChiusura() == null) {
			logger.info("FIRSTRESPONDER: " + firstResponderId + " HAS ACTIVE INTERVENTION FOR EVENT_ID " + eventID
					+ " ..CLOSING");
			interv.setChiusoDa(firstResponderId);
			interv.setDataChiusura(Calendar.getInstance().getTime());
			if (coordinate != null && coordinate.getLatitudine() != null && coordinate.getLongitudine() != null) {
				interv.setLatitudineArrivo(Double.valueOf(coordinate.getLatitudine()));
				interv.setLongitudineArrivo(Double.valueOf(coordinate.getLongitudine()));
			}
			insertIntervento(interv);

			// invio la mail
			try {
				String email = anagraficheService.getParameter(ParametersEnum.REGION_MASTER_EMAIL.name());
				mailService.sendMail(email, interv, MailTemplateEnum.FR_PLACE_ARRIVAL_MAIL_TEMPLATE, EntityType.USER,
						firstResponderId);
			} catch (Exception e) {
				logger.error("Sending mail error ", e);
			}
			return true;
		}

		return false;
	}

	@Override
	public Intervento getInterventoAttivoByFirstResponder(String firstResponderID) {

		try {

			InterventoFilterDO fltDO = new InterventoFilterDO();
			fltDO.setFirstResponder(firstResponderID);
			fltDO.setNotRejected(true);
			fltDO.setFetchRule(InterventoForEventDepthRule.NAME);
			List<InterventoDO> interventoDoList = eventService.searchInterventionByFilter(fltDO);
			List<Intervento> interventoList = (List<Intervento>) dtoService.convertCollection(interventoDoList,
					Intervento.class,
					new CompoundDTORule(InterventoDO.class, Intervento.class, InterventoForEventDepthRule.NAME));
			if (interventoList.size() > 0) {
				return interventoList.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getInterventoAttivoByFirstResponder", e);
			throw e;
		}

	}

	@Override
	public Intervento getInterventoAttivoByFirstResponderAndEventId(String firstResponderID, String eventId) {

		try {

			InterventoFilterDO fltDO = new InterventoFilterDO();
			fltDO.setFirstResponder(firstResponderID);
			// (Mauro) escludo il filtro sugli interventi già chiusi perchè così
			// evito di perdere le chiusure inviate dai client dopo la chiusura
			// da centrale
			// fltDO.setNotClosed(true);
			fltDO.setEventId(eventId);
			fltDO.setFetchRule(InterventoForEventDepthRule.NAME);
			List<InterventoDO> interventoDoList = eventService.searchInterventionByFilter(fltDO);
			List<Intervento> interventoList = (List<Intervento>) dtoService.convertCollection(interventoDoList,
					Intervento.class,
					new CompoundDTORule(InterventoDO.class, Intervento.class, InterventoForEventDepthRule.NAME));
			if (interventoList.size() > 0) {
				return interventoList.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getInterventoAttivoByFirstResponder", e);
			throw e;
		}

	}

	@Override
	public boolean insertNotificheEvento(NotificheEvento notificheEvento) {
		try {
			NotificheEventoDO notificheEventoDO = (NotificheEventoDO) dtoService.convertObject(notificheEvento,
					NotificheEventoDO.class, new CompoundDTORule(NotificheEvento.class, NotificheEventoDO.class,
							NotificheEventoDeepDepthRule.NAME));

			NotificheEventoDO returnedNotificheEventoDo = eventService.insertNotificheEvento(notificheEventoDO);
			returnedNotificheEventoDo = eventService.getNotificheEventoById(returnedNotificheEventoDo.getId());

			return true;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING insertNotificheEvento", e);
			throw e;
		}
	}

	@Override
	public List<NotificheEvento> getAllNotificheEvento() {
		try {
			List<NotificheEventoDO> notificheEventoDoList = eventService.getAllNotificheEvento();
			List<NotificheEvento> notificheEventoList = (List<NotificheEvento>) dtoService.convertCollection(
					notificheEventoDoList, NotificheEvento.class, new CompoundDTORule(NotificheEventoDO.class,
							NotificheEvento.class, NotificheEventoDeepDepthRule.NAME));
			return notificheEventoList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllNotificheEvento", e);
			throw e;
		}
	}

	@Override
	public List<NotificheEvento> getNotificheEventiByEventId(String eventId) {
		try {
			List<NotificheEventoDO> notificheEventoDoList = eventService.getNotificheEventiByEventId(eventId);
			List<NotificheEvento> notificheEventoList = (List<NotificheEvento>) dtoService.convertCollection(
					notificheEventoDoList, NotificheEvento.class, new CompoundDTORule(NotificheEventoDO.class,
							NotificheEvento.class, NotificheEventoDeepDepthRule.NAME));
			return notificheEventoList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getNotificheEventiById", e);
			throw e;
		}
	}

	@Override
	public Event getEventInterventionDetails(EventInfoRequest eventInfo) throws GenericException {
		if (!StringUtils.isEmpty(eventInfo.getCartellino()) && !StringUtils.isEmpty(eventInfo.getCoRiferimento())) {
			InterventoFilter intFilter = new InterventoFilter();
			intFilter.setAccepted(true);
			intFilter.setCartellinoEvento(eventInfo.getCartellino());
			intFilter.setCoRiferimento(eventInfo.getCoRiferimento());
			intFilter.setFetchRule(InterventoDeepDepthRule.NAME);
			List<Intervento> ints = searchInterventoByFilter(intFilter);
			Event respEvt = new Event();
			if (ints.size() > 0) {
				respEvt = ints.get(0).getEvent();
				for (Intervento intervento : ints) {
					intervento.setEvent(null);
				}
				respEvt.setInterventi(ints);
			} else {
				respEvt = getEventByCartellinoAndCO(eventInfo.getCartellino(), eventInfo.getCoRiferimento());
			}
			return respEvt;
		}
		throw new GenericException("Dati in input non validi");

	}

	@Override
	public GenericResponse rejectEvent(EventInfoRequest event, FirstResponder fr) {
		GenericResponse gr = new GenericResponse();
		gr.setResponse(true);
		try {
			Intervento inter = new Intervento();
			Event evt = getEventById(event.getEventId());
			inter.setEvent(evt);
			inter.setEseguitoDa(fr);
			inter.setDataCreazione(Calendar.getInstance().getTime());
			inter.setDataRifiuto(Calendar.getInstance().getTime());
			inter.setRifiutatoDa(fr.getId());
			inter.setOsservazione("RIFIUTATO DA APP");
			insertIntervento(inter);
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING refuseEvent", e);
			gr.setResponse(false);
		}

		return gr;
	}

	@Override
	public List<Event> searchEventByFilter(EventFilter filter) {
		if (filter.getFetchRule() == null) {
			filter.setFetchRule("SEARCH");
		}

		EventFilterDO filterDO = dtoService.convertObject(filter, EventFilterDO.class);
		List<EventDO> events = eventService.searchEventByFilter(filterDO);

		return (List<Event>) dtoService.convertCollection(events, Event.class,
				new CompoundDTORule(EventDO.class, Event.class, filter.getFetchRule()));
	}

	@Override
	public long countNotified(String id) {
		return eventService.countNotified(id);
	}

	@Override
	public Event saveEvent(Event event) {
		EventDO toSave = dtoService.convertObject(event, EventDO.class);
		eventService.saveAdditionalDataToEvent(toSave);

		EventFilter filter = new EventFilter();

		filter.setId(event.getId());
		filter.setFetchRule(EventDetailDepthRule.NAME);
		List<Event> events = searchEventByFilter(filter);

		return events.get(0);
	}

	@Override
	public List<NotificheEvento> searchNotifiche(NotificheEventoFilterDO filter) {
		filter.setFetchRule(NotificheEventoEseguitoDepthRule.NAME);

		List<NotificheEventoDO> notificheDO = eventService.searchNotificheEventoByFilter(filter);
		return (List<NotificheEvento>) dtoService.convertCollection(notificheDO, NotificheEvento.class,
				new CompoundDTORule(NotificheEventoDO.class, NotificheEvento.class, "USER"));
	}

}
