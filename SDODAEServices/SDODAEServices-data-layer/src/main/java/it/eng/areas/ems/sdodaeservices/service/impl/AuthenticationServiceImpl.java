package it.eng.areas.ems.sdodaeservices.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.hash.Hashing;

import it.eng.areas.ems.sdodaeservices.dao.MobileTokenDAO;
import it.eng.areas.ems.sdodaeservices.dao.ResetPasswordDAO;
import it.eng.areas.ems.sdodaeservices.dao.UtenteDAO;
import it.eng.areas.ems.sdodaeservices.entity.DaeMobileTokenDO;
import it.eng.areas.ems.sdodaeservices.entity.ParametersEnum;
import it.eng.areas.ems.sdodaeservices.entity.ResetPasswordDO;
import it.eng.areas.ems.sdodaeservices.entity.ResetPasswordDO.Stato;
import it.eng.areas.ems.sdodaeservices.entity.UtenteDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.UtenteFilterDO;
import it.eng.areas.ems.sdodaeservices.exception.UserAlreadyLoggedException;
import it.eng.areas.ems.sdodaeservices.service.AnagraficheTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.AuthenticationService;
import it.eng.areas.ems.sdodaeservices.utils.PasswordUtils;

@Component
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class AuthenticationServiceImpl implements AuthenticationService {

	private Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	@Autowired
	private UtenteDAO utenteDAO;

	@Autowired
	private MobileTokenDAO tokenDAO;

	@Autowired
	private ResetPasswordDAO resetPasswordDAO;

	@Autowired
	private AnagraficheTransactionalService anagService;

	@Autowired
	private PasswordUtils passwordUtils;

	@Override
	public DaeMobileTokenDO authenticateUser(String logon, String password) {
		String hashed = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
		return authenticateUserByHashedPassword(logon, hashed);
	}

	@Override
	public DaeMobileTokenDO authenticateUserByHashedPassword(String logon, String hashed) {
		UtenteDO uDO = utenteDAO.getUtenteByLogonAndPasswordHash(logon, hashed);
		if (uDO != null) {
			DaeMobileTokenDO token = new DaeMobileTokenDO();
			token.setToken(UUID.randomUUID().toString());
			token.setCreationTimeStamp(Calendar.getInstance());
			token.setUtenteId(uDO.getId());

			return tokenDAO.save(token);

		} else {
			return null;
		}
	}

	@Override
	public DaeMobileTokenDO authenticateBackOfficeOperator(String logon, String password)
			throws UserAlreadyLoggedException {
		String hashed = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
		return authenticateUserByHashedPassword(logon, hashed);
	}

	@Override
	public DaeMobileTokenDO getToken(String token) {
		return tokenDAO.get(token);
	}

	@Override
	public DaeMobileTokenDO updateToken(String token) {
		DaeMobileTokenDO tkn = tokenDAO.get(token);
		tkn.setRenewedTimeStamp(Calendar.getInstance());
		return tokenDAO.save(tkn);
	}

	@Override
	public void deleteToken(String token) {
		tokenDAO.delete(token);
	}

	@Override
	public ResetPasswordDO createResetPassword(String emailAddress, String ip) {
		UtenteFilterDO filter = new UtenteFilterDO();
		filter.setEmail(emailAddress);
		List<UtenteDO> utenti = utenteDAO.searchUtenteByFilter(filter);
		if (utenti != null && utenti.size() == 1) {
			// non ci possono stare più utenti con la setssa mail
			ResetPasswordDO pwd = new ResetPasswordDO();
			pwd.setToken(UUID.randomUUID().toString());
			pwd.setIp(ip);
			pwd.setDataRichiesta(new Date());
			pwd.setStato(Stato.ATTESA);

			pwd.setUtente(utenti.get(0));
			// salvo la richiesta sul db
			return saveResetPassword(pwd);
		}
		return null;
	}

	@Scheduled(fixedDelay = 600000)
	@Override
	public void deleteExpiredTokens() {
		Integer timeOutMinutes = Integer
				.valueOf(anagService.getParametro(ParametersEnum.TOKEN_EXPIRED_TIMEOUT.name()).getValore());

		logger.info("delete Expired Tokens by " + timeOutMinutes + " min");
		List<DaeMobileTokenDO> tokenToDelete = tokenDAO.getExpiredTokens(timeOutMinutes);
		for (DaeMobileTokenDO daeMobileTokenDO : tokenToDelete) {
			tokenDAO.delete(daeMobileTokenDO);
			logger.info("DELETED EXPIRED TOKEN FOR USER : " + daeMobileTokenDO.getUtenteId());
		}
	}

	@Override
	public ResetPasswordDO getResetPasswordByToken(String token, Stato stato) {
		return resetPasswordDAO.searchByTokenAndStato(token, stato);
	}

	@Override
	public ResetPasswordDO saveResetPassword(ResetPasswordDO resetPassword) {
		// salvo
		ResetPasswordDO pwd = resetPasswordDAO.save(resetPassword);
		// ricarico
		return resetPasswordDAO.get(pwd.getId());
	}

	@Override
	public DaeMobileTokenDO expireToken(String token) {
		DaeMobileTokenDO tokenDO = tokenDAO.get(token);
		tokenDO.setExpiredTimeStamp(Calendar.getInstance());
		return tokenDAO.save(tokenDO);
	}

}
