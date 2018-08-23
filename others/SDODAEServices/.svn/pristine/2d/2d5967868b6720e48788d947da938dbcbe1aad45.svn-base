/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.delegate.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.eng.areas.ems.common.sdo.dto.DTOService;
import it.eng.areas.ems.sdodaeservices.bean.DaeEventActivation;
import it.eng.areas.ems.sdodaeservices.bean.DaeNumbers;
import it.eng.areas.ems.sdodaeservices.bean.DaeSubscription;
import it.eng.areas.ems.sdodaeservices.bean.FirstResponderSubscription;
import it.eng.areas.ems.sdodaeservices.delegate.GraphServiceDelegate;
import it.eng.areas.ems.sdodaeservices.delegate.model.DaeEventActivationDTO;
import it.eng.areas.ems.sdodaeservices.delegate.model.DaeNumbersDTO;
import it.eng.areas.ems.sdodaeservices.delegate.model.DaeSubscriptionDTO;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponderSubscriptionDTO;
import it.eng.areas.ems.sdodaeservices.service.GraphTransactionalService;

/**
 * @author Bifulco Luigi
 *
 */
@Component
public class GraphServiceDelegateImpl implements GraphServiceDelegate {

	@Autowired
	private GraphTransactionalService graphService;

	@Autowired
	private DTOService dtoService;

	@Override
	public List<FirstResponderSubscriptionDTO> listFrSubscriptions(Calendar from, Calendar to) {
		List<FirstResponderSubscription> list = graphService.countFrSubscription(from, to);
		return new ArrayList<>(dtoService.convertCollection(list, FirstResponderSubscriptionDTO.class));
	}

	@Override
	public List<DaeSubscriptionDTO> listDAESubscriptions(Calendar from, Calendar to) {
		List<DaeSubscription> list = graphService.countDaeSubscription(from, to);
		return new ArrayList<>(dtoService.convertCollection(list, DaeSubscriptionDTO.class));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.sdodaeservices.delegate.GraphServiceDelegate#
	 * listDAEActivations()
	 */
	@Override
	public List<DaeEventActivationDTO> listDAEActivationsByProvince(String categoryId, Calendar from, Calendar to) {
		List<DaeEventActivation> list = graphService.countDaeActivationByProvince(categoryId, from, to);
		return new ArrayList<>(dtoService.convertCollection(list, DaeEventActivationDTO.class));
	}

	@Override
	public List<DaeEventActivationDTO> listDAEActivationsByDay(Calendar from, Calendar to, String categoryId) {
		List<DaeEventActivation> list = graphService.countDAEActivationByDay(from, to, categoryId);
		return new ArrayList<>(dtoService.convertCollection(list, DaeEventActivationDTO.class));
	}

	@Override
	public List<DaeEventActivationDTO> listDAEAccepetedActivationsByDay(Calendar from, Calendar to, String categoryId) {
		List<DaeEventActivation> list = graphService.countDAEAccepetedActivationByDay(from, to, categoryId);
		return new ArrayList<>(dtoService.convertCollection(list, DaeEventActivationDTO.class));
	}

	@Override
	public List<DaeEventActivationDTO> listFRActivationsByType(String categoryId, Calendar fromCal, Calendar toCal) {
		List<DaeEventActivation> list = graphService.listFRActivationsByType(categoryId, fromCal, toCal);
		return new ArrayList<>(dtoService.convertCollection(list, DaeEventActivationDTO.class));
	}

	@Override
	public List<DaeEventActivationDTO> listFRAcceptedByType(String categoryId, Calendar fromCal, Calendar toCal) {
		List<DaeEventActivation> list = graphService.listFRAcceptedByType(categoryId, fromCal, toCal);
		return new ArrayList<>(dtoService.convertCollection(list, DaeEventActivationDTO.class));
	}

	@Override
	public List<DaeSubscriptionDTO> listDAEValidation(Calendar from, Calendar to) {
		List<DaeSubscription> list = graphService.listDAEValidation(from, to);
		return new ArrayList<>(dtoService.convertCollection(list, DaeSubscriptionDTO.class));
	}

	@Override
	public List<DaeNumbersDTO> fetchDaeNumbersByStatus(Calendar from, Calendar to) {
		List<DaeNumbers> list = graphService.fetchDaeNumbersByStatus(from, to);
		return new ArrayList<>(dtoService.convertCollection(list, DaeNumbersDTO.class));
	}

	@Override
	public List<DaeNumbersDTO> fetchFrNumbersByCategory(Calendar from, Calendar to) {
		List<DaeNumbers> list = graphService.fetchFrNumbersByCategory(from, to);
		return new ArrayList<>(dtoService.convertCollection(list, DaeNumbersDTO.class));
	}

}
