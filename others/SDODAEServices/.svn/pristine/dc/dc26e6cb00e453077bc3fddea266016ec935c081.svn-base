package it.eng.areas.ems.sdodaeservices.service;

import it.eng.areas.ems.sdodaeservices.entity.DaeMobileTokenDO;
import it.eng.areas.ems.sdodaeservices.entity.ResetPasswordDO;
import it.eng.areas.ems.sdodaeservices.entity.ResetPasswordDO.Stato;
import it.eng.areas.ems.sdodaeservices.exception.UserAlreadyLoggedException;

public interface AuthenticationService {

	public DaeMobileTokenDO authenticateUser(String logon, String password) throws UserAlreadyLoggedException;

	public DaeMobileTokenDO getToken(String token);

	public DaeMobileTokenDO updateToken(String token);

	public DaeMobileTokenDO expireToken(String token);

	public DaeMobileTokenDO authenticateBackOfficeOperator(String logon, String password)
			throws UserAlreadyLoggedException;

	public void deleteToken(String token);

	public DaeMobileTokenDO authenticateUserByHashedPassword(String logon, String hashed)
			throws UserAlreadyLoggedException;

	public ResetPasswordDO createResetPassword(String emailAddress, String ip);

	public ResetPasswordDO getResetPasswordByToken(String token, Stato stato);

	public ResetPasswordDO saveResetPassword(ResetPasswordDO resetPassword);

	public void deleteExpiredTokens();

}
