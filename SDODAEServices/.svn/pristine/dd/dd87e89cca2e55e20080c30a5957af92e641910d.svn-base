package it.eng.areas.ems.sdodaeservices.datasource;

import it.eng.area118.sdocommon.datasource.constant.DataSourceType;


public class ContextHolder {

	private static final ThreadLocal<DataSourceType> contextHolder = new ThreadLocal<DataSourceType>();

	public static void setDataSourceType(DataSourceType dataSourceType) {
		// Assert.notNull(dataSourceType, "dataSourceType cannot be null");
		contextHolder.set(dataSourceType);
	}

	public static DataSourceType getDataSourceType() {
		return contextHolder.get();
	}

	public static void clearDataSourceType() {
		contextHolder.remove();
	}

}
