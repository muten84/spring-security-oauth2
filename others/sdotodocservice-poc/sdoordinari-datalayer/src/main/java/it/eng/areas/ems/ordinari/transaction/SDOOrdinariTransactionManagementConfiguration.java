package it.eng.areas.ems.ordinari.transaction;

import it.eng.area118.sdocommon.persistence.configuration.PersistenceConfig;
import it.eng.area118.sdocommon.persistence.configuration.TransactionManagementConfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
//@Profile("oracle")
@Import({ PersistenceConfig.class, SDOOrdinariDataSourceConfiguration.class })
public class SDOOrdinariTransactionManagementConfiguration extends
		TransactionManagementConfiguration {

	// @Autowired
	// protected PersistenceConfigBean config;
	//
	// @Value("${lab.driverClassName}")
	// private String labSourceDriverClassName;
	//
	// @Value("${lab.url}")
	// private String labtDataSourceUrl;
	//
	// @Value("${lab.username}")
	// private String labDataSourceUsername;
	//
	// @Value("${lab.password}")
	// private String labDataSourcePassword;
	//
	// @Value("${lab.databasePlatform}")
	// private String databasePlatform;
	//
	// @Value("${lab.defaultSchema}")
	// private String labDefaultSchema;

	// @Override
	// public Map<String, DataSourceConfiguration> getDataSourceMap() {
	//
	// Map<String, DataSourceConfiguration> cfgMap = new HashMap<>();
	// cfgMap.put("lab", getLabDataSource());
	// return cfgMap;
	// }

	// public DataSourceConfiguration getLabDataSource() {
	//
	// DataSourceConfiguration dataSource = new DataSourceConfiguration();
	//
	// dataSource.setName("lab");
	// DataSourceProperties prop = new DataSourceProperties();
	// prop.setDataSourceDriverClassName(labSourceDriverClassName);
	// prop.setDataSourceUrl(labtDataSourceUrl);
	// prop.setdDataSourcUsername(labDataSourceUsername);
	// prop.setDataSourcePassword(labDataSourcePassword);
	// prop.setDatabasePlatform(databasePlatform);
	// // prop.setDatabaseDefaultSchema(labDefaultSchema);
	//
	// dataSource.setConfig(prop);
	// return dataSource;
	// }

}
