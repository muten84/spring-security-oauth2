package it.eng.areas.ems.sdodaeservices.rest.security;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import it.eng.areas.ems.sdodaeservices.delegate.model.RuoliEnum;
import it.eng.areas.ems.sdodaeservices.delegate.model.Ruolo;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.esel.parsley.lang.StringUtils;

public class DAEMobileAppSecurityContext implements SecurityContext {

	private Utente user;

	public DAEMobileAppSecurityContext(Utente user) {
		this.user = user;
	}

	@Override
	public Principal getUserPrincipal() {
		return this.user;
	}

	@Override
	public boolean isUserInRole(String s) {
		if (StringUtils.isEmpty(s)) {
			return true;
		}
		// cerco nei ruoli dell'utente, se l'untente ha quel ruolo restituisco
		// true
		RuoliEnum rEnum = RuoliEnum.valueOf(s);
		if (user.getRuoli() != null) {
			for (Ruolo r : user.getRuoli()) {
				if (RuoliEnum.valueOf(r.getNome()).getWeight() >= rEnum.getWeight()) {
					return true;
				}
			}
		}
		// altrimenti false
		return false;
	}

	@Override
	public boolean isSecure() {
		return true;
	}

	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.BASIC_AUTH;
	}
}
