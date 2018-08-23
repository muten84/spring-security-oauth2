package it.eng.areas.ems.sdodaeservices.delegate.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.hsqldb.lib.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;

import it.eng.area118.sdocommon.datasource.ContextHolder;
import it.eng.area118.sdocommon.datasource.constant.DataSourceType;
import it.eng.areas.ems.common.sdo.dto.CompoundDTORule;
import it.eng.areas.ems.common.sdo.dto.DTOService;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeFaultDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeImageRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeMinimalDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.DaeDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.MailDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.DAESortComparator;
import it.eng.areas.ems.sdodaeservices.delegate.model.Dae;
import it.eng.areas.ems.sdodaeservices.delegate.model.DaeFault;
import it.eng.areas.ems.sdodaeservices.delegate.model.DaeFaultTrace;
import it.eng.areas.ems.sdodaeservices.delegate.model.DaeImageUpload;
import it.eng.areas.ems.sdodaeservices.delegate.model.Disponibile;
import it.eng.areas.ems.sdodaeservices.delegate.model.DisponibilitaGiornaliera;
import it.eng.areas.ems.sdodaeservices.delegate.model.Distance;
import it.eng.areas.ems.sdodaeservices.delegate.model.EntityType;
import it.eng.areas.ems.sdodaeservices.delegate.model.GenericException;
import it.eng.areas.ems.sdodaeservices.delegate.model.Image;
import it.eng.areas.ems.sdodaeservices.delegate.model.MailTemplateEnum;
import it.eng.areas.ems.sdodaeservices.delegate.model.ProgrammaManutenzione;
import it.eng.areas.ems.sdodaeservices.delegate.model.ProgrammaManutenzioneHistory;
import it.eng.areas.ems.sdodaeservices.delegate.model.Strade;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.eng.areas.ems.sdodaeservices.delegate.model.VctDAE;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.DaeFilter;
import it.eng.areas.ems.sdodaeservices.delegate.utils.DaeUtils;
import it.eng.areas.ems.sdodaeservices.entity.ComuneDO;
import it.eng.areas.ems.sdodaeservices.entity.DaeDO;
import it.eng.areas.ems.sdodaeservices.entity.DaeFaultDO;
import it.eng.areas.ems.sdodaeservices.entity.DaeFaultStatoEnum;
import it.eng.areas.ems.sdodaeservices.entity.DaeStatoEnum;
import it.eng.areas.ems.sdodaeservices.entity.DisponibileDO;
import it.eng.areas.ems.sdodaeservices.entity.DisponibilitaGiornalieraDO;
import it.eng.areas.ems.sdodaeservices.entity.DistanceEnum;
import it.eng.areas.ems.sdodaeservices.entity.GPSLocationDO;
import it.eng.areas.ems.sdodaeservices.entity.ImageDO;
import it.eng.areas.ems.sdodaeservices.entity.LocationDO;
import it.eng.areas.ems.sdodaeservices.entity.ParametersEnum;
import it.eng.areas.ems.sdodaeservices.entity.ProgrammaManutenzioneHistoryDO;
import it.eng.areas.ems.sdodaeservices.entity.ResponsabileProvinciaDO;
import it.eng.areas.ems.sdodaeservices.entity.StatoDO;
import it.eng.areas.ems.sdodaeservices.entity.TipoDisponibilitaEnum;
import it.eng.areas.ems.sdodaeservices.entity.UtenteDO;
import it.eng.areas.ems.sdodaeservices.entity.enums.Operation;
import it.eng.areas.ems.sdodaeservices.entity.filter.DaeFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.gis.VCTDaeDO;
import it.eng.areas.ems.sdodaeservices.entity.gis.VctDaeDODistanceBean;
import it.eng.areas.ems.sdodaeservices.exception.DaeDuplicateException;
import it.eng.areas.ems.sdodaeservices.service.AnagraficheTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.DaeTransactionalService;
import net.javacrumbs.shedlock.core.SchedulerLock;

@Component
public class DaeDelegateServiceImpl implements DaeDelegateService {

	public static final int DAE_QUERY_PAGE_SIZE = 100;

	private Logger logger = LoggerFactory.getLogger(DaeDelegateServiceImpl.class);

	@Autowired
	private DaeTransactionalService daeService;

	@Autowired
	private AnagraficheDelegateService anagDelegService;

	@Autowired
	private AnagraficheTransactionalService anaService;

	@Autowired
	private DTOService dtoService;

	private LoadingCache<String, ImageDO> daeImageCache;

	@Autowired
	private MailDelegateService mailService;

	@Override
	public long countAll() {
		// TODO Auto-generated method stub

		return daeService.countAll();
	}

	@PostConstruct
	public void init() {
		daeImageCache = CacheBuilder.newBuilder().maximumSize(100).expireAfterAccess(60, TimeUnit.MINUTES)
				.build(new CacheLoader<String, ImageDO>() {
					@Override
					public ImageDO load(String daeId) throws Exception {
						ImageDO imgDO = daeService.getImageByDaeID(daeId);
						if (imgDO == null) {
							imgDO = daeService.getImageByID(daeId);
							if (imgDO == null) {
								imgDO = new ImageDO();
							}
						}
						return imgDO;
					}
				});
	}

	@Override
	public Dae uploadDaeImage(DaeImageUpload upload, Utente u) throws GenericException, DaeDuplicateException {
		DaeDO dae = daeService.getDaeById(DaeDeepDepthRule.NAME, upload.getDaeID());
		if (dae != null) {
			ImageDO img = dae.getImmagine();
			if (img == null) {
				img = new ImageDO();
			}
			img.setData(upload.getBase64Image());
			dae.setImmagine(img);

			daeService.insertDae(dae, dtoService.convertObject(u, UtenteDO.class), Operation.MODIFY);

			Dae returnedDae = (Dae) dtoService.convertObject(dae, Dae.class,
					new CompoundDTORule(DaeDO.class, Dae.class, DaeDeepDepthRule.NAME));

			daeImageCache.invalidate(dae.getId());
			return returnedDae;
		} else {
			throw new GenericException("NO DAE FOUND WITH ID: " + upload.getDaeID());
		}

	}

	@Override
	public List<Dae> getAllDAE(String fetchRule) throws Exception {
		List<Dae> toret;
		try {
			toret = new ArrayList<Dae>();

			logger.info("EXECUTING getAllDAE");

			DaeFilterDO filter = new DaeFilterDO();
			filter.setFetchRule(fetchRule);
			filter.setOperativo(true);
			long start = System.currentTimeMillis();
			List<DaeDO> daeDoList = daeService.searchDaeByFilter(filter);

			logger.info("EXECUTING getAllDAE QUERY MS: " + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();
			toret = (List<Dae>) dtoService.convertCollection(daeDoList, Dae.class,
					new CompoundDTORule(DaeDO.class, Dae.class, filter.getFetchRule()));

			logger.info("EXECUTING getAllDAE DOZER MS: " + (System.currentTimeMillis() - start));
			return toret;
		} catch (Exception e1) {
			logger.error("ERROR WHILE EXECUTING getAllDAE", e1);
			throw e1;
		}

	}

	@Override
	public Dae getDaeById(String id) {
		try {
			DaeDO daeDo = null;
			daeDo = daeService.getDaeById(DaeDeepDepthRule.NAME, id);
			Dae dae = (Dae) dtoService.convertObject(daeDo, Dae.class,
					new CompoundDTORule(DaeDO.class, Dae.class, DaeDeepDepthRule.NAME));
			return dae;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getDaeById", e);
			return null;
		}

	}

	@Override
	public Dae getDaeWithImageById(String id) {
		try {
			DaeDO daeDo = null;
			daeDo = daeService.getDaeById(DaeImageRule.NAME, id);
			Dae dae = (Dae) dtoService.convertObject(daeDo, Dae.class,
					new CompoundDTORule(DaeDO.class, Dae.class, DaeImageRule.NAME));
			return dae;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getDaeWithImageById", e);
			return null;
		}

	}

	@Override
	public List<Dae> searchDaeByFilter(final DaeFilter daeFilter) throws Exception {

		List<DaeDO> daeDoList = new ArrayList<>();
		DaeFilterDO daeFilterDO = dtoService.convertObject(daeFilter, DaeFilterDO.class);

		List<String> ids = null;

		try {
			long startMs = System.currentTimeMillis();
			HashMap distanceMap = new HashMap();
			// Se ho delle coordinate di riferimento, posso invocare postGIS
			if (daeFilter.getLocation() != null) {

				daeFilter.setStatoVisible(true);

				List<VctDaeDODistanceBean> retBean = null;
				if (!it.esel.parsley.lang.StringUtils.isEmpty(daeFilter.getLocation().getGeoJSON())) {
					daeFilter.getLocation().setSrid(4326);
					retBean = getPagedVctDAEOrderByDistance(daeFilter);
				} else {
					retBean = getPagedVctDAEOrderByDistance(daeFilter.getPageSize(), daeFilter.getFromIndex(),
							(double) daeFilter.getLocation().getLatitudine(),
							(double) daeFilter.getLocation().getLongitudine(), 4326);
				}

				if (retBean == null || retBean.isEmpty()) {
					// se da postgress non ho ricevuto nessun bean restituisco
					// una lista vuota
					return new ArrayList<Dae>(0);
				}

				ids = new ArrayList<>(retBean.size());

				for (VctDaeDODistanceBean vctDaeDODistanceBean : retBean) {
					ids.add(vctDaeDODistanceBean.getId());
					distanceMap.put(vctDaeDODistanceBean.getId(), vctDaeDODistanceBean.getDistanceMt());
				}

				logger.info("searchDaeByFilter POSTGIS QUERY EXEC TIME : " + (System.currentTimeMillis() - startMs));

			}

			if (StringUtils.isEmpty(daeFilterDO.getFetchRule())) {
				daeFilterDO.setFetchRule(DaeDeepDepthRule.NAME);
			}

			startMs = System.currentTimeMillis();
			logger.info("FILTER: " + daeFilterDO.toString());

			if (ids != null) {
				int loaded = 0;
				int lastSize = 0;
				do {
					loaded = 0;
					int newSize = Math.min(lastSize + DAE_QUERY_PAGE_SIZE, ids.size());
					daeFilterDO.setIds(ids.subList(lastSize, newSize));
					lastSize = newSize;

					if (daeFilterDO.getIds().size() > 0) {
						List<DaeDO> result = daeService.searchDaeByFilter(daeFilterDO);
						loaded = result.size();
						daeDoList.addAll(result);
					}
				} while (loaded >= DAE_QUERY_PAGE_SIZE || daeFilterDO.getIds().size() == DAE_QUERY_PAGE_SIZE);
			} else {
				daeDoList.addAll(daeService.searchDaeByFilter(daeFilterDO));
			}

			List<Dae> daeList = (List<Dae>) dtoService.convertCollection(daeDoList, Dae.class,
					new CompoundDTORule(DaeDO.class, Dae.class, daeFilterDO.getFetchRule()));
			List<Dae> retList = new ArrayList<>();

			logger.info("LIST SIZE: " + daeList.size() + " searchDaeByFilter QUERY EXEC TIME : "
					+ (System.currentTimeMillis() - startMs));
			startMs = System.currentTimeMillis();
			for (Dae dae : daeList) {
				// CICLO SUI TUTTI I DAE
				if (dae.getDisponibilita() != null) {
					for (Disponibile disp : dae.getDisponibilita()) {
						// ORDINO I GIORNI DI DISPONIBILITA
						if (disp.getDisponibilitaSpecifica() != null
								&& disp.getDisponibilitaSpecifica().getDisponibilitaGiornaliera() != null) {

							List<DisponibilitaGiornaliera> giorni = new ArrayList<DisponibilitaGiornaliera>(
									disp.getDisponibilitaSpecifica().getDisponibilitaGiornaliera());

							Collections.sort(giorni, (a, b) -> {
								if (a.getGiornoSettimana().getOrder().equals(b.getGiornoSettimana().getOrder())) {
									return a.getOrarioDa().compareTo(b.getOrarioDa());
								}
								return a.getGiornoSettimana().getOrder().compareTo(b.getGiornoSettimana().getOrder());
							});

							disp.getDisponibilitaSpecifica().setDisponibilitaGiornaliera(giorni);
						}
					}
				}

				if (distanceMap != null && distanceMap.containsKey(dae.getId())) {
					Distance dstc = new Distance((Double) distanceMap.get(dae.getId()) / 1000, DistanceEnum.CHILOMETRI);
					dae.setDistance(dstc);
				}
				retList.add(dae);
			}
			logger.info("searchDaeByFilter FOR EXEC TIME : " + (System.currentTimeMillis() - startMs));

			if (daeFilter.getLocation() != null) {
				// ORDINO I RISULTATI PER DISTANZA
				Collections.sort(retList, new DAESortComparator());
			}

			startMs = System.currentTimeMillis();
			logger.info(" searchDaeByFilter SORT EXEC TIME : " + (System.currentTimeMillis() - startMs));

			return retList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING searchDaeByFilter", e);
			throw e;
		}

	}

	@Override
	public boolean deleteLogicallyDae(Dae dae, Utente utente) {
		daeService.deleteLogicallyDae(dae.getId(), dtoService.convertObject(utente, UtenteDO.class));
		// cancello pure il dae sul cartografico
		ContextHolder.setDataSourceType(DataSourceType.GIS.name());
		try {
			// se è da validare lo elimino dal vct
			daeService.deleteVctDaeById(dae.getId());
			logger.info("POSTGIS VCT DAE DELETED");
		} finally {
			ContextHolder.clearDataSourceType();
		}

		return true;
	}

	@Override
	public Image getImageByID(String id) {
		ImageDO img;
		try {
			img = daeImageCache.get(id);
			Image imgDTO = new Image();
			imgDTO.setData(img.getData());
			imgDTO.setId(img.getId());
			return imgDTO;
		} catch (ExecutionException e) {
			logger.error("ERROR WHILE EXECUTING getImageByID", e);
		}
		return null;
	}

	@Override
	public List<VctDaeDODistanceBean> getPagedVctDAEOrderByDistance(Integer pageSize, Integer start, Double latitudine,
			Double longitudine, Integer SRID) {
		List<VctDaeDODistanceBean> ret = null;
		try {
			ContextHolder.setDataSourceType(DataSourceType.GIS.name());
			logger.info("getPagedVctDAEOrderByDistance pageSize:" + pageSize + " start " + start);
			ret = daeService.getPagedVctDAEOrderByDistance(pageSize, start, latitudine, longitudine, SRID);
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getPagedVctDAEOrderByDistance", e);
		} finally {
			ContextHolder.clearDataSourceType();
		}

		return ret;
	}

	@Override
	public List<VctDaeDODistanceBean> getPagedVctDAEOrderByDistance(DaeFilter daeFilter) {
		List<VctDaeDODistanceBean> ret = null;
		try {
			ContextHolder.setDataSourceType(DataSourceType.GIS.name());
			logger.info("getPagedVctDAEOrderByDistance pageSize:" + daeFilter.getPageSize() + " start "
					+ daeFilter.getFromIndex());
			ret = daeService.getPagedVctDAEOrderByDistance(dtoService.convertObject(daeFilter, DaeFilterDO.class));
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getPagedVctDAEOrderByDistance", e);
		} finally {
			ContextHolder.clearDataSourceType();
		}

		return ret;
	}

	@Override
	public Dae insertDae(Dae dae, Utente u, boolean fromapp) throws DaeDuplicateException {

		Dae returnedDae = null;
		// invio la mail solo se è stato creato dall'app
		boolean sendMail = fromapp && StringUtils.isEmpty(dae.getId());

		if (dae.getId() == null) {
			dae.setCreatoDA(u);
		}

		if (dae.getProgrammiManutenzione() != null) {
			for (ProgrammaManutenzione pm : dae.getProgrammiManutenzione()) {
				pm.setDae(dae);
			}
		}
		// Se non hanno specificato il tipo di disponibilità lo ricavo
		if (dae.getTipologiaDisponibilita() == null) {
			// imposto il valore di default, poi se sono vere le condizioni lo
			// cambio
			dae.setTipologiaDisponibilita(TipoDisponibilitaEnum.DA_PROGRAMMA.getDescription());
			// controllo se dall'app è arrivata la disponibilità permanente
			if (dae.getDisponibilitaPermanente() != null && dae.getDisponibilitaPermanente()) {
				dae.setTipologiaDisponibilita(TipoDisponibilitaEnum.DISPONIBILITAH24.getDescription());
			} else {
				// controllo se l'utnete ha inserito solo i mesi senza
				// specificare nessun giorno
				if (dae.getDisponibilita() != null) {
					dae.getDisponibilita().forEach(d -> {
						if (d.getDisponibilitaSpecifica() == null
								|| d.getDisponibilitaSpecifica().getDisponibilitaGiornaliera() == null
								|| d.getDisponibilitaSpecifica().getDisponibilitaGiornaliera().isEmpty()) {
							// se è vuota la disponibilità giornaliera imposto
							// la disponibilità non definita
							dae.setTipologiaDisponibilita(TipoDisponibilitaEnum.NON_DEFINITA.getDescription());
						}
					});
				} else {
					dae.setTipologiaDisponibilita(TipoDisponibilitaEnum.NON_DEFINITA.getDescription());
				}
			}
		}
		// Mauro aggiungo il messaggio
		String message = "Disponibilità non programmata. Verificare l'accessibilità del sito";
		if (dae.getNotediAccessoallaSede() != null) {
			dae.setNotediAccessoallaSede(dae.getNotediAccessoallaSede().replace(message, ""));
		}
		if (dae.getTipologiaDisponibilita().equals(TipoDisponibilitaEnum.NON_DEFINITA.getDescription())) {
			dae.setNotediAccessoallaSede(dae.getNotediAccessoallaSede() + " " + message);
		}

		DaeDO daeDO = (DaeDO) dtoService.convertObject(dae, DaeDO.class,
				new CompoundDTORule(Dae.class, DaeDO.class, DaeDeepDepthRule.NAME));

		if (dae.getGpsLocation().getIndirizzo() != null
				&& StringUtils.isEmpty(dae.getGpsLocation().getIndirizzo().getId())) {
			logger.info("NEW STREET DETECTED I'VE TO PERSIST...." + dae.getGpsLocation().getIndirizzo().toString());
			Strade newStreet = dae.getGpsLocation().getIndirizzo();
			newStreet.setComune(dae.getGpsLocation().getComune());
			newStreet = anagDelegService.saveStrada(newStreet);
			dae.getGpsLocation().setIndirizzo(newStreet);
		}

		// Se si tratta di un aggiornamento, devo proteggere l'immagine
		if ((!StringUtils.isEmpty(dae.getId())) && dae.getImmagine() != null
				&& StringUtils.isEmpty(dae.getImmagine().getData())) {
			DaeDO actualDAE = daeService.getDaeById(DaeImageRule.NAME, dae.getId());
			daeDO.setImmagine(actualDAE.getImmagine());
		}

		if (daeDO.getDisponibilita() != null && daeDO.getDisponibilita().size() > 0) {
			for (DisponibileDO disp : daeDO.getDisponibilita()) {
				if (StringUtils.isEmpty(disp.getId())) {
					disp.setId(UUID.randomUUID().toString());
				}
				disp.setDae(daeDO);
				if (disp.getDisponibilitaSpecifica() != null) {
					disp.getDisponibilitaSpecifica().setDisponibilita(disp);
					for (DisponibilitaGiornalieraDO giorn : disp.getDisponibilitaSpecifica()
							.getDisponibilitaGiornaliera()) {
						giorn.setDisponibilitaSpecifica(disp.getDisponibilitaSpecifica());
					}
				}

			}
		}
		// (Mauro) inserisco lo stato di default
		if (daeDO.getCurrentStato() == null) {
			daeDO.setCurrentStato(new StatoDO("1"));
		}

		if (daeDO.getStatoValidazione() == null) {
			daeDO.setStatoValidazione(DaeStatoEnum.DA_VALIDARE);
		}

		if (dae.getGpsLocation() != null) {
			GPSLocationDO gpsLocDO = dtoService.convertObject(dae.getGpsLocation(), GPSLocationDO.class);
			gpsLocDO.setId(dae.getGpsLocation().getGpsId());

			LocationDO locDO = dtoService.convertObject(dae.getGpsLocation(), LocationDO.class);
			if (dae.getGpsLocation().getComune() != null) {
				ComuneDO comDO = dtoService.convertObject(dae.getGpsLocation().getComune(), ComuneDO.class);
				locDO.setComune(comDO);
			}
			locDO.setGpsLocation(gpsLocDO);
			daeDO.setPosizione(locDO);
		}

		UtenteDO userDO = dtoService.convertObject(u, UtenteDO.class);

		Operation operation = null;
		if (daeDO.getId() != null) {
			DaeDO old = daeService.getDaeById(DaeMinimalDepthRule.NAME, daeDO.getId());

			if (old.getStatoValidazione() == DaeStatoEnum.DA_VALIDARE
					&& daeDO.getStatoValidazione() == DaeStatoEnum.VALIDATO) {
				operation = Operation.VALIDATE;
			} else {
				operation = Operation.MODIFY;
			}
		}
		DaeDO returnedDaeDo = daeService.insertDae(daeDO, userDO, operation);

		returnedDaeDo = daeService.getDaeById(DaeDeepDepthRule.NAME, returnedDaeDo.getId());

		returnedDae = (Dae) dtoService.convertObject(returnedDaeDo, Dae.class,
				new CompoundDTORule(DaeDO.class, Dae.class, DaeDeepDepthRule.NAME));

		if (sendMail) {
			sendMailToAdministrator(returnedDae);
		}

		if (operation == Operation.VALIDATE) {
			sendMailToResponsible(returnedDae);
		}
		try {
			Boolean retParam = Boolean
					.valueOf(anagDelegService.getParameter(ParametersEnum.ENABLE_POSTGIS_SAVE.name(), "FALSE"));
			if (retParam) {
				logger.info("POSTGIS SAVING ENABLED");

				boolean visible = returnedDae.getCurrentStato() != null ? returnedDae.getCurrentStato().getVisible118()
						: true;

				if (returnedDae.getStatoValidazione() == DaeStatoEnum.VALIDATO && returnedDae.isOperativo()
						&& visible) {
					// se il dae è validato e operativo ed è visibile per il 118
					// lo salvo sulla cartografia
					GeometryFactory geometryFactory = new GeometryFactory();
					saveVctDae(convertDAE(returnedDae, geometryFactory));

					logger.info("POSTGIS VCT DAE SAVED");
				} else {
					ContextHolder.setDataSourceType(DataSourceType.GIS.name());
					try {
						logger.info("Dae status " + returnedDae.getStatoValidazione());
						// se è da validare lo elimino dal vct
						daeService.deleteVctDaeById(returnedDae.getId());
						logger.info("POSTGIS VCT DAE DELETED");
					} finally {
						ContextHolder.clearDataSourceType();
					}
				}
			} else {
				logger.info("POSTGIS SAVING DISABLED");
			}
		} catch (Exception e) {
			logger.error("ERROR WHILE SAVING ON POSTGIS LAYER", e);
		}

		return returnedDae;
	}

	protected void sendMailToAdministrator(Dae dae) {
		logger.info("Send mail to administrator of provinvce "
				+ dae.getGpsLocation().getComune().getProvincia().getNomeProvincia());
		// carico l'amministratore della provincia
		List<ResponsabileProvinciaDO> responsabili = anaService
				.searchResponsabileByProvince(dae.getGpsLocation().getComune().getProvincia().getNomeProvincia());

		responsabili.forEach(r -> {
			mailService.sendMail(r.getEmail(), dae, MailTemplateEnum.ADMINISTRATOR_NEW_DAE_MAIL_TEMPLATE,
					EntityType.DAE, dae.getId());
		});

	}

	private void sendMailToResponsible(Dae dae) {
		logger.info("Send mail to responsible of dae "
				+ dae.getGpsLocation().getComune().getProvincia().getNomeProvincia());
		if (dae.getResponsabile() != null
				&& !it.esel.parsley.lang.StringUtils.isEmpty(dae.getResponsabile().getEmail())) {
			mailService.sendMail(dae.getResponsabile().getEmail(), dae,
					MailTemplateEnum.RESPONSIBLE_DAE_VALIDATION_TEMPLATE, EntityType.DAE, dae.getId());
		}

	}

	private Geometry wktToGeometry(String wktPoint) {
		WKTReader fromText = new WKTReader();
		Geometry geom = null;
		try {
			geom = fromText.read(wktPoint);
		} catch (Exception e) {
			throw new RuntimeException("Not a WKT string:" + wktPoint);
		}
		return geom;
	}

	@Override
	public List<VctDAE> getAllVctDae() {
		List<VctDAE> daesDTO = new ArrayList<VctDAE>();
		try {
			ContextHolder.setDataSourceType(DataSourceType.GIS.name());
			List<VCTDaeDO> daesDO = daeService.getAllVctDae();
			for (VCTDaeDO vctDaeDO : daesDO) {
				VctDAE v1 = new VctDAE();
				v1.setDescrizione(vctDaeDO.getMatricola());
				v1.setId(vctDaeDO.getId());
				v1.setLoc(vctDaeDO.getLocation());
				daesDTO.add(v1);
			}
			return daesDTO;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllVctDae", e);
			return daesDTO;
		} finally {
			ContextHolder.clearDataSourceType();
		}
	}

	@Override
	public VctDAE saveVctDae(VctDAE daeDTO) {
		try {
			ContextHolder.setDataSourceType(DataSourceType.GIS.name());

			VCTDaeDO v1 = dtoService.convertObject(daeDTO, VCTDaeDO.class);
			v1.setLocation(daeDTO.getLoc());

			daeService.saveVctDae(v1);
			daeDTO.setId(daeDTO.getId());
			return daeDTO;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING saveVctDae " + daeDTO.getId(), e);
			throw e;
		} finally {
			ContextHolder.clearDataSourceType();
		}
	}

	@Override
	public boolean deleteAllVctDae() {
		try {
			logger.info("DELETING ALL VCT DAE...");
			ContextHolder.setDataSourceType(DataSourceType.GIS.name());
			daeService.deleteAllVctDAE();
			return true;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING deleteAllVctDae", e);
			throw e;
		} finally {
			ContextHolder.clearDataSourceType();
		}

	}

	@Override
	public boolean deleteDaeById(String id) {
		try {
			return daeService.deleteDaeById(id);
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING deleteDaeById", e);
			return false;
		}
	}

	@Override
	@SchedulerLock(name = "synchGisDATA")
	@Scheduled(cron = "0 0 */1 * * *")
	public boolean synchGisDATA() {
		try {
			ContextHolder.setDataSourceType(DataSourceType.GIS.name());
			deleteAllVctDae();
			List<VctDAE> vctDAES = new ArrayList<VctDAE>();
			ContextHolder.setDataSourceType(DataSourceType.DINAMICO.name());
			PrecisionModel precisionModel = new PrecisionModel();
			GeometryFactory geometryFactory = new GeometryFactory(precisionModel, 4326);

			DaeFilterDO filter = new DaeFilterDO();
			filter.setFetchRule(DaeDeepDepthRule.NAME);
			filter.setOperativo(true);
			filter.setStatoValidazione(DaeStatoEnum.VALIDATO);
			filter.setStatoVisible118(true);

			List<DaeDO> daeDOS = daeService.searchDaeByFilter(filter);
			for (DaeDO daeDO : daeDOS) {
				if (daeDO.getPosizione().getGpsLocation() != null) {

					Dae returnedDae = (Dae) dtoService.convertObject(daeDO, Dae.class,
							new CompoundDTORule(DaeDO.class, Dae.class, DaeDeepDepthRule.NAME));

					vctDAES.add(convertDAE(returnedDae, geometryFactory));
				}
			}

			for (VctDAE vctDAE : vctDAES) {
				saveVctDae(vctDAE);
			}
			return true;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING synchGisDATA", e);
			return false;
		}

	}

	protected VctDAE convertDAE(Dae dae, GeometryFactory geometryFactory) {

		VctDAE vctDae = new VctDAE();
		vctDae.setDescrizione(dae.getModello());
		vctDae.setId(dae.getId());
		Point point = geometryFactory
				.createPoint(new Coordinate(Double.valueOf(dae.getGpsLocation().getLongitudine().toString()),
						Double.valueOf(dae.getGpsLocation().getLatitudine().toString())));
		point.setSRID(4326);

		vctDae.setMatricola(dae.getMatricola());

		vctDae.setOperativo(dae.isOperativo());
		vctDae.setPrivato(dae.getCurrentStato() != null ? !dae.getCurrentStato().getVisible() : false);
		vctDae.setLoc(point);

		vctDae.setStato(dae.getCurrentStato() != null ? dae.getCurrentStato().getNome() : "PUBBLICO");
		vctDae.setNomeSede(dae.getNomeSede());
		vctDae.setIndirizzo(DaeUtils.getIndirizzo(dae));

		vctDae.setUbicazione(dae.getUbicazione());
		vctDae.setOrari(DaeUtils.getOrariString(dae));

		String responsabile = dae.getResponsabile().getNome() + " " + dae.getResponsabile().getCognome() + " TEL: "
				+ dae.getResponsabile().getTelefono();
		vctDae.setNoteresponsabile(responsabile);

		return vctDae;
	}

	protected void sendFaultmailToAdmin(DaeFault fault) {
		DaeDO daeDO = daeService.getDaeById(DaeMinimalDepthRule.NAME, fault.getDae().getId());

		Dae dae = (Dae) dtoService.convertObject(daeDO, Dae.class,
				new CompoundDTORule(DaeDO.class, Dae.class, DaeMinimalDepthRule.NAME));

		logger.info("Send mail to administrator of provinvce "
				+ dae.getGpsLocation().getComune().getProvincia().getNomeProvincia());

		List<ResponsabileProvinciaDO> responsabili = anaService
				.searchResponsabileByProvince(dae.getGpsLocation().getComune().getProvincia().getNomeProvincia());

		fault.setDae(dae);

		responsabili.forEach(r -> {
			mailService.sendMail(r.getEmail(), fault, MailTemplateEnum.ADMINISTRATOR_DAE_FAULT_MAIL_TEMPLATE,
					EntityType.DAE, dae.getId());
		});
	}

	protected void sendFaultmailToReporter(DaeFault fault, MailTemplateEnum template) {
		DaeDO daeDO = daeService.getDaeById(DaeMinimalDepthRule.NAME, fault.getDae().getId());

		Dae dae = (Dae) dtoService.convertObject(daeDO, Dae.class,
				new CompoundDTORule(DaeDO.class, Dae.class, DaeMinimalDepthRule.NAME));

		fault.setDae(dae);

		for (DaeFaultTrace trace : fault.getTrace()) {
			logger.info("Send mail to reporter " + trace.getUtente().getEmail());
			if (trace.getStato() == DaeFaultStatoEnum.APERTA) {
				mailService.sendMail(trace.getUtente().getEmail(), fault, template, EntityType.DAE, dae.getId());
			}
		}

	}

	@Override
	public DaeFault saveDaeFault(DaeFault fault) {
		DaeFaultDO faultDO = dtoService.convertObject(fault, DaeFaultDO.class);
		faultDO = daeService.saveDaeFault(faultDO);

		fault = dtoService.convertObject(faultDO, DaeFault.class,
				new CompoundDTORule(DaeFaultDO.class, DaeFault.class, DaeFaultDeepDepthRule.NAME));

		if (fault.getStatoAttuale() == DaeFaultStatoEnum.APERTA) {
			sendFaultmailToAdmin(fault);

			sendFaultmailToReporter(fault, MailTemplateEnum.DAE_FAULT_OPEN_MAIL_TEMPLATE);
		} else if (fault.getStatoAttuale() == DaeFaultStatoEnum.CHIUSA) {
			sendFaultmailToReporter(fault, MailTemplateEnum.DAE_FAULT_CLOSED_MAIL_TEMPLATE);
		}

		return fault;

	}

	@Override
	public void updateImageDae(String id, String encode) {
		daeService.updateImageDae(id, encode);
		daeImageCache.invalidate(id);
	}

	@Override
	public List<ProgrammaManutenzioneHistory> listProgrammaManutenzioneHistory(String id) {
		List<ProgrammaManutenzioneHistoryDO> histsDO = daeService.listProgrammaManutenzioneHistory(id);

		List<ProgrammaManutenzioneHistory> toRet = histsDO.stream().map(h -> {
			ProgrammaManutenzioneHistory hdto = dtoService.convertObject(h, ProgrammaManutenzioneHistory.class);
			hdto.setUtenteLogon(h.getUtente().getLogon());
			return hdto;
		}).collect(Collectors.toList());

		return toRet;
	}

}
