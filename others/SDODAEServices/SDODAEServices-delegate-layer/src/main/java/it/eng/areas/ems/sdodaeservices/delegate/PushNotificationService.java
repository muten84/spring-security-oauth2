package it.eng.areas.ems.sdodaeservices.delegate;

import java.util.List;

import it.eng.areas.ems.sdodaeservices.delegate.model.Event;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.NotificheEvento;
import it.eng.areas.ems.sdodaeservices.entity.EventDO;
import it.eng.areas.ems.sdodaeservices.entity.FirstResponderDO;

public interface PushNotificationService {

	public String notifyNewEmergencyToFirstResponder(Event event, FirstResponder fR,
			NotificheEvento.Type tipoSelezione);

	public String notifyMessageToFirstResponderList(String message, List<FirstResponder> fRs) throws Exception;

	public String notifyEventUpdateToFirstResponder(Event event, FirstResponder fR);

	public String notifyEventUpdateToFirstResponder(EventDO event, FirstResponderDO fR);

	String notifyEventUpdateToFirstResponder(Event event, String pushToken);

	String notifyEventUpdateToFirstResponder(String eventId, String pushToken);

	String notifyAbortToFirstResponder(String messageToSend, String eventId, String pushToken);

	String notifyAbortToFirstResponder(String messageToSend, Event event, String pushToken);

	String notifyAbortToFirstResponder(String messageToSend, Event event, FirstResponder fR);

}
