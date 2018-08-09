package it.eng.areas.ems.sdodaeservices.delegate.service;

import it.eng.areas.ems.sdodaeservices.delegate.model.AppSession;
import it.eng.areas.ems.sdodaeservices.delegate.model.CredentialsBean;
import it.eng.areas.ems.sdodaeservices.delegate.model.OperationResult;
import it.eng.areas.ems.sdodaeservices.delegate.model.ResetPasswordBean;
import it.eng.areas.ems.sdodaeservices.entity.UtenteDO;

public interface AuthenticationDelegateService {

	public AppSession authenticateUser(CredentialsBean bean);

	public OperationResult resetPassword(ResetPasswordBean resetBean, String ip);

	public UtenteDO allowResetPassword(String token);

	public UtenteDO denyResetPassword(String token);

	public void deleteToken(String token);

	public void expireToken(String token);

	public AppSession authenticateUserPortal(CredentialsBean credentials);

}
