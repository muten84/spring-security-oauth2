/**
 * 
 */
package it.eng.areas.ordinari.aaa.delegate.impl;

import it.eng.areas.ordinari.aaa.delegate.AAAModuleDelegateService;
import org.springframework.stereotype.Component;

@Component("AAAModuleDelegate")
public class AAAModuleDelegateServiceImpl implements AAAModuleDelegateService{

	
	public String operationDelegate(){
		/* transcational service calls
		 * do to dto conversion
		 * other service orhestration like jmsService
		 * or other stuffs before and after transaction on db
		*/
		return "OK";
	}
}