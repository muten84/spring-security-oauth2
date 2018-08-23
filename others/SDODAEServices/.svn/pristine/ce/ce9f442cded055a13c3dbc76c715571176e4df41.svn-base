package it.eng.areas.ems.sdodaeservices.delegate;

import java.util.List;

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

public interface UserDelegateService {

	public Utente getUserById(String fetchRule, String userId);

	public Image getDefaultImmagine();

	public List<Utente> searchUtenteByFilter(UtenteFilter filter);

	public Utente saveUtente(Utente utente) throws UserLogonAlreadyPresentException, UserEmailAlreadyPresentException;

	public List<Ruolo> getAllRuoli();

	public List<Gruppo> getAllGruppi(List<String> province, List<String> municipalities);

	public boolean changePassword(String userId, String oldPassword, String newPassword)
			throws OldPasswordNotValidException, PasswordAlreadyUsedException, PasswordWithUserDetailsException;

	public Utente updateUtente(Utente utente);

	public Gruppo saveGruppo(Gruppo gruppo);

	public void updateImmagineUtente(String id, String encode);

	public boolean getPrivacyDictionary(String pwd);

}
