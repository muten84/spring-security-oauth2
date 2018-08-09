package it.eng.areas.ems.sdodaeservices.transaction;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import it.eng.area118.sdocommon.persistence.configuration.DataSourceConfiguration;
import it.eng.area118.sdocommon.persistence.configuration.DataSourceProperties;
import it.eng.area118.sdocommon.persistence.configuration.DefaultDataSourceConfiguration;
import it.eng.area118.sdocommon.persistence.configuration.PersistenceConfig;
import it.eng.area118.sdocommon.persistence.configuration.TransactionManagementConfiguration;

@Configuration
@Import({ PersistenceConfig.class, DefaultDataSourceConfiguration.class })
public class DAETransactionManagementConfiguration extends TransactionManagementConfiguration {

	@Value("${gis.driverClassName}")
	private String gisSourceDriverClassName;

	@Value("${gis.url}")
	private String gistDataSourceUrl;

	@Value("${gis.username}")
	private String gisDataSourcUsername;

	@Value("${gis.password}")
	private String gisDataSourcePassword;

	@Value("${gis.dialect}")
	private String gisDialect;

	@Value("${gis.defaultSchema}")
	private String gisDefaultSchema;

	/**
	 * REstituisce la mappa di default dei ds più la mappa dei figli
	 * 
	 * @return
	 */
	@Override
	public Map<String, DataSourceConfiguration> getDataSourceMap() {

		Map<String, DataSourceConfiguration> cfgMap = new HashMap<>();
		cfgMap.put("GIS", getGISDS());

		return cfgMap;
	}

	public DataSourceConfiguration getGISDS() {

		DataSourceConfiguration dataSource = new DataSourceConfiguration();
		dataSource.setName("GIS");
		DataSourceProperties prop = new DataSourceProperties();
		prop.setDataSourceDriverClassName(gisSourceDriverClassName);
		prop.setDataSourceUrl(gistDataSourceUrl);
		prop.setDataSourceUsername(gisDataSourcUsername);
		prop.setDataSourcePassword(gisDataSourcePassword);
		prop.setDatabasePlatform(gisDialect);
		prop.setDatabaseDefaultSchema(gisDefaultSchema);

		dataSource.setConfig(prop);
		return dataSource;
	}

}
