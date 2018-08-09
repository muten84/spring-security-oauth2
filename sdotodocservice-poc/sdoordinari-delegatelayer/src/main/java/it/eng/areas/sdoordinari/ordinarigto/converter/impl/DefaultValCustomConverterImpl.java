package it.eng.areas.sdoordinari.ordinarigto.converter.impl;

import it.eng.areas.sdoordinari.ordinarigto.converter.ConverterService;
import it.eng.areas.sdoordinari.ordinarigto.converter.DefaultValCustomConverter;
import it.eng.areas.sdoordinari.ordinarigto.converter.ReplaceValCustomConverter;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultValCustomConverterImpl implements DefaultValCustomConverter {

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
				
		}		
		
		String[] paramAsVector = parameter.split(",");  
				
		if (paramAsVector[0].equals("true")){
		val= paramAsVector[1];
		}
		
		if (paramAsVector[0].equals("false")){
			val= null;
			}
			
		
		return val  ;
		
	}

	@Override
	public void setParameter(String parameter) {
		this.parameter = parameter;
		
	}
	

}

