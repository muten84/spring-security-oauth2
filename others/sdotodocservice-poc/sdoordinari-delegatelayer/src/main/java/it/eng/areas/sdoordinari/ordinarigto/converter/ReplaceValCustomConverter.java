package it.eng.areas.sdoordinari.ordinarigto.converter;

import org.dozer.ConfigurableCustomConverter;



public interface ReplaceValCustomConverter extends ConfigurableCustomConverter {
	
	public Object convert(Object existingDestinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass);

	void setParameter(String parameter); 
}