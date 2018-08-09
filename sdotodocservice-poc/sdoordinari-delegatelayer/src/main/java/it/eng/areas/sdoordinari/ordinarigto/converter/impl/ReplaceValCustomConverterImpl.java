package it.eng.areas.sdoordinari.ordinarigto.converter.impl;

import it.eng.areas.sdoordinari.ordinarigto.converter.ConverterService;
import it.eng.areas.sdoordinari.ordinarigto.converter.ReplaceValCustomConverter;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReplaceValCustomConverterImpl implements ReplaceValCustomConverter {

	String parameter;
/*	@Resource(name="modalitaTrasporto")
	Map<String, String> modalitaTrasporto;
*/	
	//@Autowired
	//private @Autowired @Qualifier(value = "modalitaTrasporto") 
	@Autowired
	private ConverterService converterService;
	
	
	Map<String, String> keyval = null;
			
	//	ReplaceValModelBean replaceValModelBean;

	
	
	@Override
	public Object convert(Object existingDestinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {
		
		
		String val= null;
		String key ="";
		if(sourceFieldValue != null){
			key=sourceFieldValue.toString();
					key=key.replace("[", "").replace("]", "");
		}		
		Map<String, String> keyval = converterService.getMapFromString(parameter, converterService);
		 
				
		val = keyval.get(key);
		return val  ;
	}

	@Override
	public void setParameter(String parameter) {
		this.parameter = parameter;
		
	}
	

}

