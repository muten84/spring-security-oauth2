package it.eng.areas.sdoordinari.ordinarigto.test;

import it.eng.areas.ems.common.sdo.dto.configuration.DTOConfiguration;
import it.eng.areas.ems.ordinari.dao.GuidSequenceDAO;
import it.eng.areas.ems.ordinari.dao.WebBookingDAO;
import it.eng.areas.ems.ordinari.dao.WebBookingEquipmentDAO;
import it.eng.areas.ems.ordinari.dao.impl.GuidSequenceDAOImpl;
import it.eng.areas.ems.ordinari.dao.impl.WebBookingDAOImpl;
import it.eng.areas.ems.ordinari.dao.impl.WebBookingEquipmentDAOImpl;
import it.eng.areas.ems.ordinari.delegate.SDOOrdinariDelegateService;
import it.eng.areas.ems.ordinari.delegate.impl.SDOOrdinariDelegateServiceImpl;
import it.eng.areas.ems.ordinari.service.WebBookingTransactionalService;
import it.eng.areas.ems.ordinari.service.impl.WebBookingTransactionalServiceImpl;
import it.eng.areas.ems.ordinari.transaction.SDOOrdinariTransactionManagementConfiguration;
import it.eng.areas.sdoordinari.ordinarigto.converter.ConditionalValCustomConverter;
import it.eng.areas.sdoordinari.ordinarigto.converter.ConverterService;
import it.eng.areas.sdoordinari.ordinarigto.converter.DefaultValCustomConverter;
import it.eng.areas.sdoordinari.ordinarigto.converter.PatternExtractCustomConverter;
import it.eng.areas.sdoordinari.ordinarigto.converter.ReplaceValCustomConverter;
import it.eng.areas.sdoordinari.ordinarigto.converter.impl.ConditionalValCustomConverterImpl;
import it.eng.areas.sdoordinari.ordinarigto.converter.impl.ConverterServiceImpl;
import it.eng.areas.sdoordinari.ordinarigto.converter.impl.DefaultValCustomConverterImpl;
import it.eng.areas.sdoordinari.ordinarigto.converter.impl.PatternExtractCustomConverterImpl;
import it.eng.areas.sdoordinari.ordinarigto.converter.impl.ReplaceValCustomConverterImpl;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@PropertySources(//
value = { //
@PropertySource(ignoreResourceNotFound = false, //
value = "classpath:META-INF/cfg-oracle-jpa.properties"),
})

@Configuration
@Import({ DTOConfiguration.class, SDOOrdinariTransactionManagementConfiguration.class })
//@ImportResource({"classpath:META-INF/gto-util-map.xml", "classpath:META-INF/gto-converters.xml"})
@ImportResource({"classpath:META-INF/ordinari-util-map.xml", "classpath:META-INF/ordinari-converters.xml"})


public class SDOOrdinariDelegateTestCtx {

	
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer conf = new PropertySourcesPlaceholderConfigurer();
		// conf.
		conf.setIgnoreUnresolvablePlaceholders(true);
		return conf;
	}

	//Bean 
	@Bean
	public WebBookingDAO getWebBookingDAO() {
		return new WebBookingDAOImpl();
	}
	//@Bean
	//public GuidSequenceDAO getGuidSequenceDAO() {
	//	return new GuidSequenceDAOImpl();
	//}

	/*@Bean
	public WebBookingEquipmentDAO getWebBookingEquipmentDAO() {
		return new WebBookingEquipmentDAOImpl();
	}
*/
	
	@Bean
	public SDOOrdinariDelegateService getSDOOrdinariDelegateService() {
		return new SDOOrdinariDelegateServiceImpl();
	}
	
	
	@Bean
	public WebBookingTransactionalService getWebBookingTransactionalService() {
		return new WebBookingTransactionalServiceImpl();
	}


	@Bean
	public ReplaceValCustomConverter getReplaceValCustomConverter() {
		return new ReplaceValCustomConverterImpl();
	}
	@Bean
	public ConditionalValCustomConverter getConditionalValCustomConverter() {
		return new ConditionalValCustomConverterImpl();
	}
	@Bean
	public DefaultValCustomConverter getDefaultValCustomConverter() {
		return new DefaultValCustomConverterImpl();
	}
	@Bean
	public PatternExtractCustomConverter getPatternExtractCustomConverter() {
		return new PatternExtractCustomConverterImpl();
	}
	
	@Bean
	public ConverterService getConverterService() {
		return new ConverterServiceImpl();
	}
	

	
	
}
