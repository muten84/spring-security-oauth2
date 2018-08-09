package it.eng.areas.ems.sdodaeservices.delegate.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.GenericResponse;
import it.eng.areas.ems.sdodaeservices.delegate.model.PrivacyAgreement;
import it.eng.areas.ems.sdodaeservices.delegate.service.PrivacyDelegateService;
import it.eng.areas.ems.sdodaeservices.entity.ParametersEnum;
import it.eng.areas.ems.sdodaeservices.service.FirstResponderTransactionalService;

@Component
public class PrivacyDelegateServiceImpl implements PrivacyDelegateService {

	private Logger logger = LoggerFactory.getLogger(PrivacyDelegateServiceImpl.class);

	@Autowired
	private AnagraficheDelegateService anaService;

	@Autowired
	private FirstResponderTransactionalService frTransaService;

	@Override
	public PrivacyAgreement getCurrentPrivacyAgreement() {
		try {
			String text = anaService.getParameter(ParametersEnum.PRIVACY_AGREEMENT_TEXT.name(), "Testo disclaimer");

			PrivacyAgreement pa = new PrivacyAgreement();
			pa.setAgreementText(text);
			pa.setId(ParametersEnum.PRIVACY_AGREEMENT_TEXT.name());

			return pa;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getCurrentPrivacyAgreement", e);
			throw e;
		}
	}

	@Override
	public GenericResponse acceptPrivacyAgreementForCurrentUser(FirstResponder fr) {
		GenericResponse gr = new GenericResponse();
		try {
			boolean ret = frTransaService.acceptPrivacyAgreement(fr.getId());
			gr.setResponse(ret);
			gr.setMessage("Operazione eseguita correttamente");
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING acceptPrivacyAgreementForCurrentUser", e);
			gr.setResponse(false);
			gr.setMessage("Errore durante esecuzione operazione");
		}
		return gr;
	}

}
