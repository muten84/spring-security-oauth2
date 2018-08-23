package it.eng.areas.ems.sdodaeservices.rest.listener;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

public class DAEApplicationEventListener implements ApplicationEventListener {
	@Override
	public void onEvent(ApplicationEvent applicationEvent) {
		switch (applicationEvent.getType()) {
		case INITIALIZATION_FINISHED:
			System.out.println("Jersey application started.");
			break;
		}
	}

	@Override
	public RequestEventListener onRequest(RequestEvent requestEvent) {
		return new DAERequestEventListener();
	}
}
