package it.eng.areas.ems.sdodaeservices.delegate;

import java.util.List;

import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.Messaggio;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.MessaggioFilter;

public interface MessagesDelegateService {

	public Messaggio getMessaggioById(String id);

	public boolean deleteMessaggioById(String id);

	public long countAll();

	// public List<VCTMessaggioDO> getAllVctMessaggio();

	public List<Messaggio> getMessaggioByFilter(MessaggioFilter messaggioFilter);

	public Messaggio insertMessaggio(Messaggio messaggio);

	public List<Messaggio> getAllMessaggio();

	public List<FirstResponder> getMessageResponders(String id);

}
