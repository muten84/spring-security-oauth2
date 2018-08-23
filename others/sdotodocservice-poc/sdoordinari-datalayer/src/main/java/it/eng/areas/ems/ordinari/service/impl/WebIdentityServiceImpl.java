/**
 * 
 */
package it.eng.areas.ems.ordinari.service.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Calendar;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.eng.areas.ems.ordinari.dao.WebIdentityDAO;
import it.eng.areas.ems.ordinari.dao.WebSessionDAO;
import it.eng.areas.ems.ordinari.entity.WebIdentityDO;
import it.eng.areas.ems.ordinari.entity.WebSessionDO;
import it.eng.areas.ems.ordinari.service.WebIdentityService;

/**
 * @author Bifulco Luigi
 *
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class WebIdentityServiceImpl implements WebIdentityService {

	@Autowired
	private WebSessionDAO sessionDAO;

	@Autowired
	private WebIdentityDAO webIdentityDAO;

	public WebIdentityDO getUser(String username) {
		return webIdentityDAO.getIdentityByUsername(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eng.areas.ems.ordinari.service.WebIdentityService#getUserSession(java.
	 * lang.String)
	 */
	@Override
	public String getUserSession(String username) {
		WebIdentityDO identityDO = getUser(username);
		return sessionDAO.getUserSession(identityDO.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eng.areas.ems.ordinari.service.WebIdentityService#createSession(java.
	 * lang.String, java.lang.String)
	 */
	@Override
	public WebSessionDO createSession(String username, String token) {
		WebIdentityDO identityDO = getUser(username);
		if (identityDO == null) {
			return null;
		}
		WebSessionDO existent = sessionDAO.getSession(identityDO.getId());
		if (existent != null) {
			boolean removed = sessionDAO.delete(existent.getId());
			sessionDAO.flushAndClear();
		}
		WebSessionDO newSession = new WebSessionDO();
		newSession.setId(UUID.randomUUID().toString().substring(0, 20));
		newSession.setLoginTimestamp(Calendar.getInstance().getTime());
		newSession.setSessionId(token);
		newSession.setWebIdentityId(identityDO.getId());
		sessionDAO.save(newSession);
		return newSession;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eng.areas.ems.ordinari.service.WebIdentityService#logout(java.lang.
	 * String)
	 */
	@Override
	public boolean destroySession(String username) {
		WebIdentityDO identityDO = getUser(username);
		WebSessionDO existent = sessionDAO.getSession(identityDO.getId());
		if (existent != null) {
			boolean removed = sessionDAO.delete(existent.getId());
			sessionDAO.flushAndClear();
			return removed;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eng.areas.ems.ordinari.service.WebIdentityService#needToRenewPassword(
	 * java.lang.String)
	 */
	@Override
	public WebIdentityDO needToRenewPassword(String username, long days) {
		WebIdentityDO identityDO = getUser(username);
		// boolean isNew = identityDO.getIsNew();

		Calendar c = Calendar.getInstance();
		if (identityDO.getPasswdModifyDate() != null) {
			c.setTime(identityDO.getPasswdModifyDate());
		} else {
			return identityDO;
		}
		Period p = Period.between(
				LocalDate.of(c.get(Calendar.YEAR), //
						c.get(Calendar.MONTH), //
						c.get(Calendar.DAY_OF_MONTH)), //
				LocalDate.now());
		// se la durata dall'ultima modifica passsowrd è maggiore dei giorni
		// limite allora restituisci l'utente
		long durationInDays = p.get(ChronoUnit.DAYS);
		if (durationInDays > days || identityDO.getPasswdModifyDate() == null) {
			return identityDO;
		}
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eng.areas.ems.ordinari.service.WebIdentityService#changePassword(java.
	 * lang.String, java.lang.String)
	 */
	@Override
	public WebIdentityDO changePassword(String username, String password) {
		WebIdentityDO identityDO = getUser(username);
		identityDO.setPasswd(password);
		identityDO.setPasswdModifyDate(Calendar.getInstance().getTime());
		return identityDO;
	}

}
