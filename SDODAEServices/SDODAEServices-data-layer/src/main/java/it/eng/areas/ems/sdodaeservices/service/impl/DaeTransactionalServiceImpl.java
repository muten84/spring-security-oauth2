package it.eng.areas.ems.sdodaeservices.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;

import it.eng.areas.ems.sdodaeservices.dao.DaeDAO;
import it.eng.areas.ems.sdodaeservices.dao.DaeFaultDAO;
import it.eng.areas.ems.sdodaeservices.dao.DaeHistoryDAO;
import it.eng.areas.ems.sdodaeservices.dao.DisponibilitaDAO;
import it.eng.areas.ems.sdodaeservices.dao.ImageDAO;
import it.eng.areas.ems.sdodaeservices.dao.ProgrammaManutenzioneDAO;
import it.eng.areas.ems.sdodaeservices.dao.ProgrammaManutenzioneHistoryDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeFaultDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeImageRule;
import it.eng.areas.ems.sdodaeservices.entity.DaeDO;
import it.eng.areas.ems.sdodaeservices.entity.DaeFaultDO;
import it.eng.areas.ems.sdodaeservices.entity.DaeFaultTraceDO;
import it.eng.areas.ems.sdodaeservices.entity.DaeHistoryDO;
import it.eng.areas.ems.sdodaeservices.entity.DisponibileDO;
import it.eng.areas.ems.sdodaeservices.entity.ImageDO;
import it.eng.areas.ems.sdodaeservices.entity.ProgrammaManutenzioneDO;
import it.eng.areas.ems.sdodaeservices.entity.ProgrammaManutenzioneHistoryDO;
import it.eng.areas.ems.sdodaeservices.entity.UtenteDO;
import it.eng.areas.ems.sdodaeservices.entity.enums.Operation;
import it.eng.areas.ems.sdodaeservices.entity.filter.DaeFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.gis.VCTDaeDO;
import it.eng.areas.ems.sdodaeservices.entity.gis.VctDaeDODistanceBean;
import it.eng.areas.ems.sdodaeservices.exception.DaeDuplicateException;
import it.eng.areas.ems.sdodaeservices.gis.dao.VCTDaeDAO;
import it.eng.areas.ems.sdodaeservices.service.DaeTransactionalService;
import it.eng.areas.ems.sdodaeservices.utils.DAEUtils;
import it.esel.parsley.lang.StringUtils;

@Component
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class DaeTransactionalServiceImpl implements DaeTransactionalService {

	private Logger logger = LoggerFactory.getLogger(DaeTransactionalServiceImpl.class);

	@Autowired
	private DaeDAO daeDAO;

	@Autowired
	private VCTDaeDAO vctDaeDAO;

	private Gson gson;

	@Autowired
	private DisponibilitaDAO dispDAO;

	@Autowired
	private ImageDAO imageDAO;

	@Autowired
	private DaeFaultDAO daeFaultDAO;

	@Autowired
	private DaeHistoryDAO daeHistoryDAO;

	@Autowired
	private DAEUtils daeUtils;

	@Autowired
	private ProgrammaManutenzioneDAO programmaManutenzioneDAO;

	@Autowired
	private ProgrammaManutenzioneHistoryDAO programmaManutenzioneHistoryDAO;

	// create a cache for employees based on their employee id
	private LoadingCache<String, List<DaeDO>> daeSearchCache;

	@Value("${dae.duplicate.fields}")
	private String[] duplicateDaeField;

	public DaeTransactionalServiceImpl() {

	}

	@PostConstruct
	public void init() {
		// TODO: all dependendecies injected, init your stateless service here
		// TODO: tutte le dipendenze sono state inettate inizializza qui il tuo
		// stateless service
		gson = new Gson();
		daeSearchCache = CacheBuilder.newBuilder().maximumSize(100).expireAfterAccess(1, TimeUnit.MINUTES)

				.build(new CacheLoader<String, List<DaeDO>>() {

					@Override
					public List<DaeDO> load(String jsonFilter) throws Exception {

						DaeFilterDO filter = gson.fromJson(jsonFilter, DaeFilterDO.class);
						return daeDAO.searchDaeByFilter(filter);
					}
				});

		logger.info("Duplicate fields " + Arrays.toString(duplicateDaeField));
	}

	@Override
	public DaeDO getDaeById(String fetchRule, String id) {

		return daeDAO.get(fetchRule, id);

	}

	public long countAll() {
		long num = daeDAO.countAll();

		// if (example == null) {
		// throw new NullPointerException("The entity does not exist");
		// }
		return num;
	}

	@Override
	public DaeDO deleteLogicallyDae(String id, UtenteDO user) {
		DaeDO dae = daeDAO.get(id);
		if (dae != null) {
			dae.setDeleted(true);
			dae = daeDAO.save(dae);

			// Creo l'history
			DaeHistoryDO hist = daeUtils.copyToHistory(dae, user, Operation.DELETE);

			daeHistoryDAO.save(hist);
		}
		return dae;
	}

	@Override
	public List<DaeDO> searchDaeByFilter(DaeFilterDO daeFilter) throws ExecutionException {
		try {
			daeFilter.setFetchRule(daeFilter.getFetchRule());
			return daeDAO.searchDaeByFilter(daeFilter);
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING searchDaeByFilter", e);
			throw e;
		}

	}

	@Override
	public List<VctDaeDODistanceBean> getPagedVctDAEOrderByDistance(Integer pageSize, Integer start, Double latitudine,
			Double longitudine, Integer SRID) {
		return vctDaeDAO.getPagedVctDAEOrderByDistance(pageSize, start, latitudine, longitudine, SRID);
	}

	@Override
	public List<VctDaeDODistanceBean> getPagedVctDAEOrderByDistance(DaeFilterDO daeFilter) {
		return vctDaeDAO.getPagedVctDAEOrderByDistance(daeFilter);
	}

	@Override
	public DaeDO insertDae(DaeDO daeDO, UtenteDO user, Operation operation) throws DaeDuplicateException {

		if (daeDO.getTimestampInserimento() == null) {
			daeDO.setTimestampInserimento(new Date());
		}
		if (daeDO.getDataInserimento() == null) {
			daeDO.setDataInserimento(new Date());
		}

		if (daeDO.getGuasti() != null) {
			daeDO.getGuasti().forEach(g -> g.setDae(daeDO));
		}

		if (daeDO.getId() == null) {
			operation = Operation.CREATE;

			List<DaeDO> duplicates = daeDAO.findDuplicate(daeDO, Arrays.asList(duplicateDaeField));
			if (duplicates != null && !duplicates.isEmpty()) {
				// TODO
				throw new DaeDuplicateException(duplicates);
			}
		}

		if (daeDO.getProgrammiManutenzione() != null && !daeDO.getProgrammiManutenzione().isEmpty()) {
			for (ProgrammaManutenzioneDO p : daeDO.getProgrammiManutenzione()) {
				saveProgrammaManutenzioneHistory(p, user);
			}
		}

		if (operation == Operation.VALIDATE) {
			daeDO.setDataValidazione(new Date());
			daeDO.setUtenteValidazione(user.getEmail());
		}

		DaeDO retDO = daeDAO.insertDae(daeDO);
		daeSearchCache.invalidateAll();

		if (user != null) {
			// Creo l'history
			DaeHistoryDO hist = daeUtils.copyToHistory(retDO, user, operation);
			daeHistoryDAO.save(hist);
		}
		return retDO;
	}

	private void saveProgrammaManutenzioneHistory(ProgrammaManutenzioneDO p, UtenteDO user) {
		// carico dal db il programma
		if (!StringUtils.isEmpty(p.getId())) {
			ProgrammaManutenzioneDO oldP = programmaManutenzioneDAO.get(p.getId());
			if (oldP != null && !compareProgrammaManutenzione(oldP, p)) {
				// se l'utente è null vuol dire che sto salvando da un job,
				// qiundi evito di salvare gli history
				if (user != null) {
					// se il nuovo è diverso dal vecchio salvo
					// l'history
					ProgrammaManutenzioneHistoryDO hist = daeUtils.copyProgrammaToHistory(p, user);
					// aggiungo il nuovo history al DB
					oldP.getHistory().add(hist);
				}
			}
			// aggiungo la lista di history al nuovo programma da salvare
			p.setHistory(oldP.getHistory());
		} else {
			// creo l'history per la prima creazione
			ProgrammaManutenzioneHistoryDO hist = daeUtils.copyProgrammaToHistory(p, user);
			// aggiungo il nuovo history al DB
			p.setHistory(Collections.singleton(hist));
		}
	}

	private boolean compareProgrammaManutenzione(ProgrammaManutenzioneDO oldP, ProgrammaManutenzioneDO other) {
		if (oldP.getNota() == null) {
			if (other.getNota() != null)
				return false;
		} else if (!oldP.getNota().equals(other.getNota()))
			return false;
		if (oldP.getScadenzaDopo() == null) {
			if (other.getScadenzaDopo() != null)
				return false;
		} else {
			DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			if (!format.format(oldP.getScadenzaDopo()).equals(format.format(other.getScadenzaDopo())))
				return false;

		}
		if (oldP.getTipoManutenzione() == null) {
			if (other.getTipoManutenzione() != null)
				return false;
		} else if (!oldP.getTipoManutenzione().equals(other.getTipoManutenzione()))
			return false;
		return true;
	}

	@Override
	public List<VCTDaeDO> getAllVctDae() {
		return vctDaeDAO.getAllVCTDAE();
	}

	@Override
	public boolean deleteDaeById(String id) {
		daeSearchCache.invalidateAll();
		DaeDO daeDO = getDaeById(DaeDeepDepthRule.NAME, id);
		if (daeDO != null) {
			daeDAO.delete(daeDO);
			return true;
		}
		return false;
	}

	@Override
	public VCTDaeDO saveVctDae(VCTDaeDO daeDO) {
		// effettuo prima la select per capire se devo fare l'insert o l'update
		return vctDaeDAO.saveOrUpdate(daeDO);
	}

	@Override
	public ImageDO getImageByDaeID(String ID) {
		DaeDO dae = daeDAO.get(DaeImageRule.NAME, ID);
		return dae != null ? dae.getImmagine() : null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public DaeDO cleanDisponibilita(DaeDO daeDO) {
		daeDO = getDaeById(DaeDeepDepthRule.NAME, daeDO.getId());
		if (daeDO.getDisponibilita() != null) {
			for (DisponibileDO disp : daeDO.getDisponibilita()) {
				dispDAO.delete(disp);
			}
		}
		daeSearchCache.invalidateAll();
		return daeDO;

	}

	@Override
	public Integer deleteAllVctDAE() {
		Integer deleted = vctDaeDAO.executeSQLInsert("DELETE  FROM VCT_DAE");
		logger.info("DELETED vctDaeDAO SIZE: " + deleted);
		return deleted;
	}

	@Override
	public DaeFaultDO saveDaeFault(DaeFaultDO faultDO) {
		if (faultDO.getTrace() == null) {
			faultDO.setTrace(new HashSet<>());
		} else {
			DaeFaultDO tmp = faultDO;
			faultDO.getTrace().forEach(t -> {
				t.setFault(tmp);
			});
		}

		// aggiungo il trace al fault da salvare
		DaeFaultTraceDO trace = new DaeFaultTraceDO();

		trace.setFault(faultDO);
		trace.setUtente(faultDO.getUtente());
		trace.setDataModifica(new Date());
		trace.setStato(faultDO.getStatoAttuale());
		trace.setTipologia(faultDO.getTipologia());
		trace.setNote(faultDO.getNote());

		faultDO.getTrace().add(trace);

		// salvo
		faultDO = daeFaultDAO.save(faultDO);

		// ricarico il fault dal DB con tutti i trace caricati in precedenza
		faultDO = daeFaultDAO.get(DaeFaultDeepDepthRule.NAME, faultDO.getId());
		return faultDO;
	}

	@Override
	public ImageDO saveImage(ImageDO img) {
		return imageDAO.save(img);
	}

	@Override
	public void updateImageDae(String id, String encode) {
		DaeDO daeDO = daeDAO.get(id);
		ImageDO img = daeDO.getImmagine();
		if (img == null) {
			img = new ImageDO();
		}
		img.setData(encode);
		img = imageDAO.save(img);
		daeDO.setImmagine(img);
	}

	@Override
	public ImageDO getImageByID(String daeId) {
		return imageDAO.get(daeId);
	}

	@Override
	public void deleteVctDaeById(String id) {
		Integer deleted = vctDaeDAO.executeSQLInsert("DELETE  FROM VCT_DAE where id = '" + id + "'");
		logger.info("DELETED " + id + " vctDaeDAO SIZE: " + deleted);
	}

	@Override
	public List<ProgrammaManutenzioneHistoryDO> listProgrammaManutenzioneHistory(String id) {
		return programmaManutenzioneHistoryDAO.listByProgrammaId(id);
	}

}
