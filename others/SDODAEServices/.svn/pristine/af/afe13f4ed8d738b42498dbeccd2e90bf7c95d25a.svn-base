package it.eng.areas.ems.sdodaeservices.dao;

import java.util.List;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.sdodaeservices.entity.DaeMobileTokenDO;

public interface MobileTokenDAO extends EntityDAO<DaeMobileTokenDO, String> {

	public List<DaeMobileTokenDO> getExpiredTokens(Integer timeOutMinutes);

	public List<DaeMobileTokenDO> getTokensNotExpiredByUserId(String userId);

	public List<DaeMobileTokenDO> getTokensByUserId(String userId);

}
