package it.eng.areas.ems.sdodaeservices.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.eng.areas.ems.sdodaeservices.dao.GruppoDAO;
import it.eng.areas.ems.sdodaeservices.dao.ImageDAO;
import it.eng.areas.ems.sdodaeservices.dao.PasswordHistoryDAO;
import it.eng.areas.ems.sdodaeservices.dao.PrivacyDictionaryDAO;
import it.eng.areas.ems.sdodaeservices.dao.RuoloDAO;
import it.eng.areas.ems.sdodaeservices.dao.UtenteDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.GruppoDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.GruppoDO;
import it.eng.areas.ems.sdodaeservices.entity.ImageDO;
import it.eng.areas.ems.sdodaeservices.entity.PasswordHistoryDO;
import it.eng.areas.ems.sdodaeservices.entity.RuoloDO;
import it.eng.areas.ems.sdodaeservices.entity.UtenteDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.GruppoFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.PasswordHistoryFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.UtenteFilterDO;
import it.eng.areas.ems.sdodaeservices.service.UserTransactionalService;

@Component
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class UserTransactionalServiceImpl implements UserTransactionalService {

	@Autowired
	protected UtenteDAO utenteDAO;

	@Autowired
	private ImageDAO imageDAO;

	@Autowired
	private RuoloDAO ruoloDAO;

	@Autowired
	private GruppoDAO gruppoDAO;

	@Autowired
	private PasswordHistoryDAO pwHistoryDAO;

	@Autowired
	private PrivacyDictionaryDAO privacyDictionaryDAO;

	@Override
	public UtenteDO getUserById(String fetch, String userId) {
		return utenteDAO.get(fetch, userId);
	}

	@Override
	public ImageDO getDefaultImage() {
		return imageDAO.get("DEFAULT");
	}

	@Override
	public ImageDO saveImage(ImageDO image) {
		return imageDAO.save(image);
	}

	@Override
	public List<UtenteDO> searchUtenteByFilter(UtenteFilterDO filterDO) {
		return utenteDAO.searchUtenteByFilter(filterDO);
	}

	@Override
	public List<RuoloDO> getAllRuoli() {
		return ruoloDAO.getAllRuoli();
	}

	@Override
	public List<GruppoDO> getAllGruppi(List<String> province, List<String> municipality) {
		GruppoFilterDO gruppoFilter = new GruppoFilterDO();
		gruppoFilter.setProvinces(province);
		gruppoFilter.setMunicipalities(municipality);
		gruppoFilter.setFetchRule(GruppoDeepDepthRule.NAME);

		return gruppoDAO.searchGruppoByFilter(gruppoFilter);
	}

	@Override
	public boolean getCheckDictionary(String pwd) {
		return privacyDictionaryDAO.checkDictionary(pwd);
	}

	@Override
	public UtenteDO saveUtente(UtenteDO utenteDO, int historyPwdSize) {

		if (utenteDO.getPasswordHistories() != null && !utenteDO.getPasswordHistories().isEmpty()) {
			// PasswordHistoryFilterDO filter = new PasswordHistoryFilterDO();
			// filter.setUserId(utenteDO.getId());
			// filter.setMaxResults(historyPwdSize);
			// filter.setMaxResult(historyPwdSize);
			List<PasswordHistoryDO> list = pwHistoryDAO.searchPasswordHistoryByUserId(utenteDO.getId());

			if (list.size() >= historyPwdSize) {
				PasswordHistoryDO histToDelete = list.get(0);
				PasswordHistoryDO tempHistTotDelete = null;

				for (PasswordHistoryDO pwdHistUser : utenteDO.getPasswordHistories()) {
					if (pwdHistUser.getId() != null && pwdHistUser.getId().equals(histToDelete.getId())) {
						tempHistTotDelete = pwdHistUser;
						break;
					}
				}
				if (tempHistTotDelete != null && utenteDO.getPasswordHistories().contains(tempHistTotDelete)) {
					utenteDO.getPasswordHistories().remove(tempHistTotDelete);
					pwHistoryDAO.delete(tempHistTotDelete);
				}
			}
		}

		return utenteDAO.save(utenteDO);
	}

	@Override
	public GruppoDO saveGruppo(GruppoDO gruppoDO) {

		gruppoDO.getProvince().forEach(p -> {
			p.setGruppo(gruppoDO);
		});
		if (gruppoDO.getComuni() != null) {
			gruppoDO.getComuni().forEach(c -> {
				c.setGruppo(gruppoDO);
			});
		}

		return gruppoDAO.save(gruppoDO);
	}

	@Override
	public PasswordHistoryDO searchPasswordHistory(String userId, String hashed) {
		// return pwHistoryDAO.searchPasswordHistory(userId, hashed);
		/*
		 * Luigi inserita nuova gestione password history come richiesto da
		 * Donatella, al momento il parametro maxResults non è configurabile ma
		 * è parametrizzabile sul filtro senza cambiare la logica di esecuzione
		 */
		PasswordHistoryFilterDO filter = new PasswordHistoryFilterDO();
		filter.setUserId(userId);
		filter.setMaxResults(3);
		filter.setMaxResult(3);
		filter.setType("PASSWORD");
		List<PasswordHistoryDO> list = pwHistoryDAO.searchPasswordHistoryByfilter(filter);
		if (list != null) {
			for (PasswordHistoryDO passwordHistoryDO : list) {
				if (passwordHistoryDO.getPasswordHash().equals(hashed)) {
					return passwordHistoryDO;
				}

			}
		}
		return null;
	}

	@Override
	public List<PasswordHistoryDO> searchWordInPassword(String userId) {
		// return pwHistoryDAO.searchPasswordHistory(userId, hashed);
		/*
		 * Luigi inserita nuova gestione password history come richiesto da
		 * Donatella, al momento il parametro maxResults non è configurabile ma
		 * è parametrizzabile sul filtro senza cambiare la logica di esecuzione
		 */
		return pwHistoryDAO.searchWordInPassword(userId);
	}

	@Override
	public UtenteDO getUserByLogon(String fetch, String username) {
		UtenteFilterDO filterDO = new UtenteFilterDO();
		filterDO.setFetchRule(fetch);
		filterDO.setLogon(username);
		filterDO.setLognEqual(true);
		List<UtenteDO> users = utenteDAO.searchUtenteByFilter(filterDO);
		return users.size() == 1 ? users.get(0) : null;
	}

	@Override
	public RuoloDO getRuoloByName(String name) {
		return ruoloDAO.getRuoloByName(name);
	}

	@Override
	public void updateImmagineUtente(String id, String encode) {
		UtenteDO utente = utenteDAO.get(id);
		ImageDO img = utente.getImmagine();
		if (img == null) {
			img = new ImageDO();
		}
		img.setData(encode);
		img = imageDAO.save(img);
		utente.setImmagine(img);
	}
}
