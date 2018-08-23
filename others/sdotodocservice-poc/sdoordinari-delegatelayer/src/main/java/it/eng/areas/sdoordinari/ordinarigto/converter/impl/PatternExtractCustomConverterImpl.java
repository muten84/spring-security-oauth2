package it.eng.areas.sdoordinari.ordinarigto.converter.impl;

import it.eng.areas.sdoordinari.ordinarigto.converter.ConverterService;
import it.eng.areas.sdoordinari.ordinarigto.converter.PatternExtractCustomConverter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatternExtractCustomConverterImpl implements
		PatternExtractCustomConverter {

	String parameter;
	/*
	 * @Resource(name="modalitaTrasporto") Map<String, String>
	 * modalitaTrasporto;
	 */
	// @Autowired
	// private @Autowired @Qualifier(value = "modalitaTrasporto")
	@Autowired
	private ConverterService converterService;

	Map<String, String> keyval = null;

	// ReplaceValModelBean replaceValModelBean;

	@Override
	public Object convert(Object existingDestinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {
		
		// stringa del tipo delimitatore1:#delimitatore2#numeromaxcaratteri sul db
		
		String result = null;

		String[] paramAsVector = parameter.split("#");
		String delimiterSecond="";
		String delimiterFirst ="";
		String delimiterThird ="0";
		if (paramAsVector.length > 0 && !paramAsVector[0].equals(null)) {
			 delimiterFirst = paramAsVector[0];
		}

		if (paramAsVector.length > 1 && !paramAsVector[1].equals(null)) {
			 delimiterSecond = paramAsVector[1];
		}
		
		String sourceFieldValueStr = sourceFieldValue.toString();
		 
		result = sourceFieldValueStr.substring(
				sourceFieldValueStr.indexOf(delimiterFirst) + delimiterFirst.length()+1);
		
		result=result.substring(0,result.indexOf(delimiterSecond));
		
		if (paramAsVector.length > 2 && !paramAsVector[2].equals(null)){
			if( paramAsVector[2].matches("^-?\\d+$")) {
			 delimiterThird = paramAsVector[2];
			 
			 delimiterThird = new Integer(result.length()) < Integer.parseInt(paramAsVector[2]) ?   new Integer(result.length()).toString() :  new Integer(paramAsVector[2]).toString();   
			}
		} else {
			
			delimiterThird = new Integer(result.length()).toString();
		}
		
		return result.substring(0,Integer.parseInt(delimiterThird));

	}

	@Override
	public void setParameter(String parameter) {
		this.parameter = parameter;

	}

}
