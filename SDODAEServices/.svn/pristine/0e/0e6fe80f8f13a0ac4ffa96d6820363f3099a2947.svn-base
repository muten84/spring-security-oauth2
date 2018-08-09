package it.eng.areas.ems.sdodaeservices.service;

import java.util.List;

import it.eng.areas.ems.sdodaeservices.entity.GruppoDO;
import it.eng.areas.ems.sdodaeservices.entity.ImageDO;
import it.eng.areas.ems.sdodaeservices.entity.PasswordHistoryDO;
import it.eng.areas.ems.sdodaeservices.entity.RuoloDO;
import it.eng.areas.ems.sdodaeservices.entity.UtenteDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.UtenteFilterDO;

public interface UserTransactionalService {

	public UtenteDO getUserById(String fetch, String userId);

	public ImageDO getDefaultImage();

	public ImageDO saveImage(ImageDO image);

	public List<UtenteDO> searchUtenteByFilter(UtenteFilterDO filterDO);

	public List<RuoloDO> getAllRuoli();

	public List<GruppoDO> getAllGruppi(List<String> province, List<String> municipality);

	public UtenteDO saveUtente(UtenteDO utenteDO, int historyPwdSize);

	public GruppoDO saveGruppo(GruppoDO gruppoDO);

	public PasswordHistoryDO searchPasswordHistory(String userId, String hashed);

	public UtenteDO getUserByLogon(String nAME, String username);

	public RuoloDO getRuoloByName(String name);

	public void updateImmagineUtente(String id, String encode);

	public List<PasswordHistoryDO> searchWordInPassword(String userId);

	public boolean getCheckDictionary(String pwd);

}
