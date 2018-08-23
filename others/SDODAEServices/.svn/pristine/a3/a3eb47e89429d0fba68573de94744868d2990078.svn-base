package it.eng.areas.ems.sdodaeservices.datasource;

import java.util.logging.Logger;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return ContextHolder.getDataSourceType();
	}

	public Logger getParentLogger() {
		return Logger.getGlobal();
	}

}
