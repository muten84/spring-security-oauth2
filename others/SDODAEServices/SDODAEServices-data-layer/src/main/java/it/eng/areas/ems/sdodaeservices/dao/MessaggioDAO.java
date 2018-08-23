package it.eng.areas.ems.sdodaeservices.dao;

import java.util.List;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.sdodaeservices.entity.MessaggioDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.MessaggioFilterDO;

public interface MessaggioDAO extends EntityDAO<MessaggioDO, String> {

	public List<MessaggioDO> searchMessaggioByFilter(MessaggioFilterDO messaggioFilter);

	public List<MessaggioDO> getAll();

	public MessaggioDO insertMessaggio(MessaggioDO messaggioDO);

	public MessaggioDO getMessaggioWithResponders(String id);

}
