package it.eng.areas.ems.sdodaeservices.delegate.impl;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.hash.Hashing;

import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.UtenteLoginDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.FirstResponderDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.MailDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.AppSession;
import it.eng.areas.ems.sdodaeservices.delegate.model.CredentialsBean;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.MailTemplateEnum;
import it.eng.areas.ems.sdodaeservices.delegate.model.OperationResult;
import it.eng.areas.ems.sdodaeservices.delegate.model.ResetPasswordBean;
import it.eng.areas.ems.sdodaeservices.delegate.model.RuoliEnum;
import it.eng.areas.ems.sdodaeservices.delegate.service.AuthenticationDelegateService;
import it.eng.areas.ems.sdodaeservices.entity.DaeMobileTokenDO;
import it.eng.areas.ems.sdodaeservices.entity.ParametersEnum;
import it.eng.areas.ems.sdodaeservices.entity.PasswordHistoryDO;
import it.eng.areas.ems.sdodaeservices.entity.ResetPasswordDO;
import it.eng.areas.ems.sdodaeservices.entity.ResetPasswordDO.Stato;
import it.eng.areas.ems.sdodaeservices.entity.RuoloDO;
import it.eng.areas.ems.sdodaeservices.entity.UtenteDO;
import it.eng.areas.ems.sdodaeservices.exception.UserAlreadyLoggedException;
import it.eng.areas.ems.sdodaeservices.service.AuthenticationService;
import it.eng.areas.ems.sdodaeservices.service.FirstResponderTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.UserTransactionalService;

@Component
public class AuthenticationDelegateServiceImpl implements AuthenticationDelegateService {

	private Logger logger = LoggerFactory.getLogger(AuthenticationDelegateServiceImpl.class);

	@Autowired
	private AuthenticationService authService;

	@Autowired
	private FirstResponderDelegateService frService;

	@Autowired
	private FirstResponderTransactionalService frTransactionalService;

	@Autowired
	private AnagraficheDelegateService anaService;

	@Autowired
	private UserTransactionalService userService;

	@Autowired
	private MailDelegateService mailService;

	private SecureRandom random = new SecureRandom();

	@Override
	public AppSession authenticateUserPortal(CredentialsBean bean) {
		UtenteDO user = userService.getUserByLogon(UtenteLoginDepthRule.NAME, bean.getUsername());
		boolean valid = true;
		for (RuoloDO r : user.getRuoli()) {
			if (r.getNome().equals(RuoliEnum.FIRST_RESPONDER.name())) {
				valid = false;
			}
		}
		if (valid) {
			return authenticateUser(bean);
		} else {
			AppSession appSession = new AppSession();
			appSession.setMessage("User o password errati");
			appSession.setError(true);
			return appSession;
		}
	}

	@Override
	public AppSession authenticateUser(CredentialsBean bean) {
		try {
			logger.info("AUTHENTICATE USER: " + bean.getUsername());

			if (bean.getDeviceInfo() != null && StringUtils.isEmpty(bean.getDeviceInfo().getPushToken())) {
				// se mi arriva il dispositivo (login da app cellulare) ma non
				// ho il pushtoken restituisco un messaggio di errore
				AppSession appSession = new AppSession();
				appSession.setMessage("Errore in fase di Login. Riavviare l'applicazione e riprovare");
				appSession.setError(true);
				return appSession;
			}

			DaeMobileTokenDO tokenDO = null;
			if (!StringUtils.isEmpty(bean.getPassword())) {
				tokenDO = authService.authenticateUser(bean.getUsername(), bean.getPassword());
			} else {
				logger.info("AUTHENTICATE USER: " + bean.getUsername() + " WITH HASH");
				tokenDO = authService.authenticateUserByHashedPassword(bean.getUsername(), bean.getPasswordHash());
			}
			if (tokenDO != null) {
				AppSession appSession = new AppSession();
				appSession.setStartSessionTime(tokenDO.getCreationTimeStamp());
				appSession.setToken(tokenDO.getToken());
				FirstResponder fr = frService.getFirstResponderByUserId(FirstResponderDeepDepthRule.NAME,
						tokenDO.getUtenteId());
				if (fr != null) {
					// metto il fr sempre attivo alla login
					// frService.updateAvailability(fr.getId(), true);

					appSession.setAvailable(true);
					appSession.setPrivacyAccepted(fr.getPrivacyAcceptedDate() != null);

					if (bean.getDeviceInfo() != null) {
						frService.updateDeviceInfo(fr.getId(), bean.getDeviceInfo());
					}
				}

				appSession.setError(false);

				// controllo se l'utente deve aggiornare la password
				String hashed = null;
				if (!StringUtils.isEmpty(bean.getPassword())) {
					hashed = Hashing.sha256().hashString(bean.getPassword(), StandardCharsets.UTF_8).toString();
				} else {
					hashed = bean.getPasswordHash();
				}
				PasswordHistoryDO pHist = userService.searchPasswordHistory(tokenDO.getUtenteId(), hashed);
				if (pHist != null) {
					int daysUpdatePwd = anaService.getParameter(ParametersEnum.UPDATE_PASSWORD_DAYS.name(), 365);
					Instant from = Instant.ofEpochMilli(pHist.getCreationDate().getTime());
					Instant to = Instant.now();
					long days = ChronoUnit.DAYS.between(from, to);

					appSession.setUpdatePassword(days > daysUpdatePwd || pHist.isTemporany());
				} else {
					appSession.setUpdatePassword(true);
				}
				return appSession;
			} else {
				AppSession appSession = new AppSession();
				appSession.setMessage("User o password errati");
				appSession.setError(true);
				return appSession;
			}
		} catch (UserAlreadyLoggedException ue) {
			AppSession appSession = new AppSession();
			appSession.setMessage("Utente gia loggato");
			appSession.setError(true);
			return appSession;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING authenticateUser", e);
			return null;
		}

	}
	//
	// @Override
	// public OperationResult resetPassword(ResetPasswordBean resetBean) {
	//

	// boolean enabledMail =
	// Boolean.valueOf(anaService.getParametro(ParametersEnum.ENABLE_MAIL.name()).getValore());
	//
	// if (firstResponder.getId() == null && enabledMail) {
	// String textMail = prepareMailText(firstResponder, tempPWD,
	// ParametersEnum.PWD_RESET_MAIL_TEMPLATE);
	// mailService.sendMail(firstResponder.getEmail(), textMail);
	// }

	// return null;
	// }

	@Override
	public void deleteToken(String token) {
		authService.deleteToken(token);
	}

	@Override
	public OperationResult resetPassword(ResetPasswordBean resetBean, String ip) {
		logger.info("RESETTING PASSWORD: " + resetBean.getEmailAddress() + " IP: " + ip);
		// creo una riga sul db con le informazioni sulla richiesta di cambio
		// password
		ResetPasswordDO resetPassword = authService.createResetPassword(resetBean.getEmailAddress(), ip);
		if (resetPassword != null) {
			// parametri per la mail
			Map<String, String> valuesMap = new HashMap<String, String>();
			valuesMap.put("nome", resetPassword.getUtente().getNome());
			valuesMap.put("cognome", resetPassword.getUtente().getCognome());

			// url base per gli indirizzi
			String url = anaService.getParameter(ParametersEnum.PUBLIC_ADDRESS.name());
			valuesMap.put("cambioPWD", String.format("<a href=\"%s/authenticationService/allowReset/%s\">Procedi</a>",
					url, resetPassword.getToken()));
			valuesMap.put("rifiutoPWD", String.format("<a href=\"%s/authenticationService/denyReset/%s\">Rifiuta</a>",
					url, resetPassword.getToken()));
			// invio la mail
			mailService.sendMail(resetPassword.getUtente().getEmail(), valuesMap,
					MailTemplateEnum.RESET_PWD_MAIL_TEMPLATE);

			logger.info("RESETTING PASSWORD: " + resetBean.getEmailAddress() + " IP: " + ip);
			OperationResult or = new OperationResult();
			or.setMessage("Reset password avvenuto correttamente");
			or.setResponse(true);
			return or;
		} else {
			OperationResult or = new OperationResult();
			or.setMessage("Indirizzo email non trovato in archivio");
			or.setResponse(false);
			return or;
		}
	}

	@Override
	public UtenteDO allowResetPassword(String token) {
		// se l'utente ha cliccato su l'URL per procedere al cambio PWD
		// carico la richiesta precedentemente generata
		ResetPasswordDO resetPassword = authService.getResetPasswordByToken(token, Stato.ATTESA);
		if (resetPassword != null) {
			logger.info("Found password reset " + resetPassword);
			// controllo che la richiesta di reset password non sia troppo
			// vecchia
			int timeout = anaService.getParameter(ParametersEnum.TIMEOUT_RESET_PASSWORD.name(), 24);
			long difference = TimeUnit.MILLISECONDS
					.toHours(System.currentTimeMillis() - resetPassword.getDataRichiesta().getTime());
			if (difference < timeout) {
				// genero la password randomicamente
				String tempPWD = "";
				do {
					tempPWD = new BigInteger(300, random).toString(32);

					if (tempPWD.length() > 8) {
						tempPWD = tempPWD.substring(0, 8);
					}
				} while (tempPWD.length() < 8);

				logger.info("Generated new pass");

				// invio la mail all'utente
				Map<String, String> valuesMap = new HashMap<String, String>();
				valuesMap.put("nome", resetPassword.getUtente().getNome());
				valuesMap.put("password", tempPWD);

				mailService.sendMail(resetPassword.getUtente().getEmail(), valuesMap,
						MailTemplateEnum.NEW_PWD_MAIL_TEMPLATE);

				// hash della password da salvare sul db
				String hashPwd = Hashing.sha256().hashString(tempPWD, StandardCharsets.UTF_8).toString();
				// cambio lo stato
				resetPassword.setStato(Stato.ACCETTATO);
				authService.saveResetPassword(resetPassword);
				// salvo la password sul FR
				resetPassword.getUtente().setPasswordHash(hashPwd);
				// aggiungo l'history della password
				PasswordHistoryDO pHistory = new PasswordHistoryDO();
				pHistory.setUtente(resetPassword.getUtente());
				pHistory.setCreationDate(new Date());
				pHistory.setTemporany(true);
				pHistory.setPasswordHash(resetPassword.getUtente().getPasswordHash());

				resetPassword.getUtente().getPasswordHistories().add(pHistory);
				//
				return frTransactionalService.saveUtenteDO(resetPassword.getUtente());
			}
		}
		return null;
	}

	@Override
	public UtenteDO denyResetPassword(String token) {
		// se l'utente ha rifiutato la richiesta di cambio password
		ResetPasswordDO resetPassword = authService.getResetPasswordByToken(token, Stato.ATTESA);
		if (resetPassword != null) {
			resetPassword.setStato(Stato.RIFIUTATO);
			authService.saveResetPassword(resetPassword);
			return resetPassword.getUtente();
		}
		return null;
	}

	@Override
	public void expireToken(String token) {
		authService.expireToken(token);
	}

}
