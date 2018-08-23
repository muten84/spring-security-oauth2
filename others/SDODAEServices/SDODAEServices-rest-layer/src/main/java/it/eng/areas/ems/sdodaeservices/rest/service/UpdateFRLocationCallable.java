package it.eng.areas.ems.sdodaeservices.rest.service;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.areas.ems.sdodaeservices.delegate.FirstResponderDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.FRLocation;

public class UpdateFRLocationCallable implements Callable<Boolean> {

	private Logger logger = LoggerFactory.getLogger(UpdateFRLocationCallable.class);

	private String frID;

	private FRLocation location;

	private FirstResponderDelegateService frDelegateService;

	public UpdateFRLocationCallable(String frID, FRLocation location, FirstResponderDelegateService frDelegateService) {
		super();
		this.frID = frID;
		this.location = location;
		this.frDelegateService = frDelegateService;
	}

	@Override
	public Boolean call() throws Exception {
		frDelegateService.updatePositionFirstResponderById(frID, location);

		return true;
	}
}
