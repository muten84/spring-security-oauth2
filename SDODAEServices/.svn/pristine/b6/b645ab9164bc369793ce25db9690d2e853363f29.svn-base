package it.eng.areas.ems.sdodaeservices.delegate.impl;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.hash.Hashing;

import it.eng.areas.ems.common.sdo.dto.CompoundDTORule;
import it.eng.areas.ems.common.sdo.dto.DTOService;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.GruppoDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.UtenteDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.UtentePasswordDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.UserDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.Gruppo;
import it.eng.areas.ems.sdodaeservices.delegate.model.Image;
import it.eng.areas.ems.sdodaeservices.delegate.model.Ruolo;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.eng.areas.ems.sdodaeservices.delegate.model.exception.OldPasswordNotValidException;
import it.eng.areas.ems.sdodaeservices.delegate.model.exception.PasswordAlreadyUsedException;
import it.eng.areas.ems.sdodaeservices.delegate.model.exception.PasswordWithUserDetailsException;
import it.eng.areas.ems.sdodaeservices.delegate.model.exception.UserEmailAlreadyPresentException;
import it.eng.areas.ems.sdodaeservices.delegate.model.exception.UserLogonAlreadyPresentException;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.UtenteFilter;
import it.eng.areas.ems.sdodaeservices.entity.ComuneDO;
import it.eng.areas.ems.sdodaeservices.entity.GruppoDO;
import it.eng.areas.ems.sdodaeservices.entity.ImageDO;
import it.eng.areas.ems.sdodaeservices.entity.ParametersEnum;
import it.eng.areas.ems.sdodaeservices.entity.PasswordHistoryDO;
import it.eng.areas.ems.sdodaeservices.entity.RuoloDO;
import it.eng.areas.ems.sdodaeservices.entity.StradeDO;
import it.eng.areas.ems.sdodaeservices.entity.UtenteDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.UtenteFilterDO;
import it.eng.areas.ems.sdodaeservices.service.UserTransactionalService;
import it.esel.parsley.lang.StringUtils;

@Component
public class UserDelegateServiceImpl implements UserDelegateService {

	@Autowired
	protected UserTransactionalService usertTrans;

	@Autowired
	private AnagraficheDelegateService anagraficheService;

	@Autowired
	protected DTOService dtoService;

	@Override
	public Utente getUserById(String fetch, String userId) {
		UtenteDO user = usertTrans.getUserById(fetch, userId);
		return (Utente) dtoService.convertObject(user, Utente.class,
				new CompoundDTORule(UtenteDO.class, Utente.class, fetch));
	}

	@Override
	public Image getDefaultImmagine() {
		ImageDO imgDO = usertTrans.getDefaultImage();
		Image img = dtoService.convertObject(imgDO, Image.class);
		return img;
	}

	@Override
	public List<Utente> searchUtenteByFilter(UtenteFilter filter) {
		UtenteFilterDO filterDO = dtoService.convertObject(filter, UtenteFilterDO.class);

		List<UtenteDO> usersDO = usertTrans.searchUtenteByFilter(filterDO);

		return (List<Utente>) dtoService.convertCollection(usersDO, Utente.class,
				new CompoundDTORule(UtenteDO.class, Utente.class, filter.getFetchRule()));
	}

	@Override
	public Utente saveUtente(Utente utente) throws UserLogonAlreadyPresentException, UserEmailAlreadyPresentException {

		int historyPwdSize = Integer.valueOf(anagraficheService.getParameter(ParametersEnum.HISTORY_PWD_SIZE.name()));

		UtenteDO utenteDO = dtoService.convertObject(utente, UtenteDO.class);

		utenteDO.setLogon(utenteDO.getLogon().toLowerCase());

		// cerco un utente con lo stesso logon ma con id differente
		UtenteFilterDO filterDO = new UtenteFilterDO();
		filterDO.setLogon(utente.getLogon());
		filterDO.setLognEqual(true);
		filterDO.setNotId(utente.getId());
		List<UtenteDO> users = usertTrans.searchUtenteByFilter(filterDO);
		if (users != null && !users.isEmpty()) {
			throw new UserLogonAlreadyPresentException();
		}
		// cerco un utente con la stessa email ma con differente id
		filterDO = new UtenteFilterDO();
		filterDO.setEmail(utente.getEmail());
		filterDO.setLognEqual(true);
		filterDO.setNotId(utente.getId());
		users = usertTrans.searchUtenteByFilter(filterDO);
		if (users != null && !users.isEmpty()) {
			throw new UserEmailAlreadyPresentException();
		}

		// Se ha l'id è una modifica
		if (!StringUtils.isEmpty(utenteDO.getId())) {
			// carico i dati attuali dal db
			UtenteDO oldUser = usertTrans.getUserById("BARE", utenteDO.getId());
			// non vanno modificate le password e l'immagine
			utenteDO.setPasswordHash(oldUser.getPasswordHash());
			utenteDO.setImmagine(oldUser.getImmagine());
		} else {
			// è un nuovo utente, effettuo l'hash della password
			if (!StringUtils.isEmpty(utente.getPassword())) {
				String hashed = Hashing.sha256().hashString(utente.getPassword(), StandardCharsets.UTF_8).toString();
				utenteDO.setPasswordHash(hashed);
				// salvo l'history della password
				PasswordHistoryDO pHistory = new PasswordHistoryDO();
				pHistory.setUtente(utenteDO);
				pHistory.setCreationDate(new Date());
				pHistory.setTemporany(true);
				pHistory.setPasswordHash(utenteDO.getPasswordHash());

				utenteDO.getPasswordHistories().add(pHistory);
			}

		}
		utenteDO = usertTrans.saveUtente(utenteDO, historyPwdSize);
		utenteDO = usertTrans.getUserById(UtenteDeepDepthRule.NAME, utenteDO.getId());

		return dtoService.convertObject(utenteDO, Utente.class,
				new CompoundDTORule(UtenteDO.class, Utente.class, UtenteDeepDepthRule.NAME));
	}

	@Override
	public List<Ruolo> getAllRuoli() {
		List<RuoloDO> ruoli = usertTrans.getAllRuoli();
		return (List<Ruolo>) dtoService.convertCollection(ruoli, Ruolo.class,
				new CompoundDTORule(RuoloDO.class, Ruolo.class, "BARE"));

	}

	@Override
	public List<Gruppo> getAllGruppi(List<String> province, List<String> municipality) {
		List<GruppoDO> gruppi = usertTrans.getAllGruppi(province, municipality);
		return (List<Gruppo>) dtoService.convertCollection(gruppi, Gruppo.class,
				new CompoundDTORule(GruppoDO.class, Gruppo.class, GruppoDeepDepthRule.NAME));

	}

	@Override
	public boolean changePassword(String userId, String oldPassword, String newPassword)
			throws OldPasswordNotValidException, PasswordAlreadyUsedException, PasswordWithUserDetailsException {

		Boolean enablePrivacy = Boolean.valueOf(anagraficheService.getParameter(ParametersEnum.ENABLE_PRIVACY.name()));

		int historyPwdSize = Integer.valueOf(anagraficheService.getParameter(ParametersEnum.HISTORY_PWD_SIZE.name()));

		UtenteDO user = usertTrans.getUserById(UtentePasswordDepthRule.NAME, userId);

		List<PasswordHistoryDO> userWordInPwdList = usertTrans.searchWordInPassword(userId);

		String oldHash = Hashing.sha256().hashString(oldPassword, StandardCharsets.UTF_8).toString();
		// se l'utente ha inserito la vecchia password in modo corretto
		if (oldHash.equals(user.getPasswordHash())) {
			String hashed = Hashing.sha256().hashString(newPassword, StandardCharsets.UTF_8).toString();

			if (enablePrivacy) {
				// Cerco se dati del dettaglio dell'utente sono presenti nella
				// password
				if (checkDetailsInUserPwd(user, userWordInPwdList, newPassword)) {
					throw new PasswordWithUserDetailsException();
				}
			}
			// cerco nell'history la presenza della nuova password, per evitare
			// che un utente utlizizzi sempre le stesse password
			PasswordHistoryDO present = usertTrans.searchPasswordHistory(userId, hashed);
			if (present == null) {
				// se sull'history non è presente quella password
				user.setPasswordHash(hashed);
				// salvo l'history della password
				PasswordHistoryDO pHistory = new PasswordHistoryDO();
				pHistory.setUtente(user);
				pHistory.setCreationDate(new Date());
				pHistory.setTemporany(false);
				pHistory.setPasswordHash(user.getPasswordHash());
				pHistory.setType("PASSWORD");

				user.getPasswordHistories().add(pHistory);
				// salvo
				usertTrans.saveUtente(user, historyPwdSize);
				return true;
			} else {
				throw new PasswordAlreadyUsedException();
			}
		} else {
			throw new OldPasswordNotValidException();
		}
	}

	boolean checkDetailsInUserPwd(UtenteDO user, List<PasswordHistoryDO> userWordInPwdList, String newPwd) {
		String pwd = newPwd;
		if (user.getLogon() != null && pwd.toLowerCase().contains(user.getLogon().toLowerCase())) {
			return true;
		}
		if (user.getNome() != null && pwd.toLowerCase().contains(user.getNome().toLowerCase())) {
			return true;
		}
		if (user.getCognome() != null && pwd.toLowerCase().contains(user.getCognome().toLowerCase())) {
			return true;
		}

		if (getPrivacyDictionary(newPwd.toLowerCase())) {
			return true;
		}

		if (userWordInPwdList != null && !userWordInPwdList.isEmpty()) {
			for (PasswordHistoryDO userWordInPwd : userWordInPwdList) {
				if (pwd.toLowerCase().contains(userWordInPwd.getPasswordHash().toLowerCase())) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean getPrivacyDictionary(String pwd) {

		return usertTrans.getCheckDictionary(pwd);

	}

	@Override
	public Utente updateUtente(Utente utente) {
		// carico i dati dal DB
		int historyPwdSize = Integer.valueOf(anagraficheService.getParameter(ParametersEnum.HISTORY_PWD_SIZE.name()));

		UtenteDO utenteDO = usertTrans.getUserById(UtenteDeepDepthRule.NAME, utente.getId());

		// sostituisco solo i dati che l'utente può modificare

		utenteDO.setNome(utente.getNome());
		utenteDO.setCognome(utente.getCognome());
		utenteDO.setEmail(utente.getEmail());
		utenteDO.setCodiceFiscale(utente.getCodiceFiscale());
		utenteDO.setDataNascita(utente.getDataNascita());

		utenteDO.setCivico(utente.getCivico());
		utenteDO.setIndirizzo(dtoService.convertObject(utente.getIndirizzo(), StradeDO.class));
		utenteDO.setComuneResidenza(dtoService.convertObject(utente.getComuneResidenza(), ComuneDO.class));

		utenteDO.setLanguage(utente.getLanguage());

		usertTrans.saveUtente(utenteDO, historyPwdSize);

		utenteDO = usertTrans.getUserById(UtenteDeepDepthRule.NAME, utente.getId());

		return dtoService.convertObject(utenteDO, Utente.class,
				new CompoundDTORule(UtenteDO.class, Utente.class, UtenteDeepDepthRule.NAME));
	}

	@Override
	public Gruppo saveGruppo(Gruppo gruppo) {
		GruppoDO gruppoDO = dtoService.convertObject(gruppo, GruppoDO.class);

		gruppoDO = usertTrans.saveGruppo(gruppoDO);

		return dtoService.convertObject(gruppoDO, Gruppo.class);
	}

	@Override
	public void updateImmagineUtente(String id, String encode) {
		usertTrans.updateImmagineUtente(id, encode);
	}
}
