package it.eng.areas.ems.sdodaeservices.dao;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.sdodaeservices.entity.ResetPasswordDO;
import it.eng.areas.ems.sdodaeservices.entity.ResetPasswordDO.Stato;

public interface ResetPasswordDAO extends EntityDAO<ResetPasswordDO, String> {

	ResetPasswordDO searchByTokenAndStato(String token, Stato stato);

}
