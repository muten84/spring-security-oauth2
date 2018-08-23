package it.eng.areas.ems.sdodaeservices.datasource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

import it.eng.area118.sdocommon.datasource.constant.DataSourceType;

/**
 * 
 * @author Gaetano
 * 
 */
public class DataSourceAdvice implements MethodInterceptor {

	private final DataSourceType dataSourceType;
	private final Logger logger = Logger.getLogger(DataSourceAdvice.class);

	public DataSourceAdvice(String strType) {
		super();
		this.dataSourceType = obtainDataSourceType(strType);
	}

	@Override
	public Object invoke(MethodInvocation method) throws Throwable {
		DataSourceType parentDataSource = null;
		// if (!StringUtils.isEmpty(ContextHolder.getDataSourceType().name())) {
		// parentDataSource = obtainDataSourceType(ContextHolder.getDataSourceType().name());
		// }
		try {
			ContextHolder.setDataSourceType(dataSourceType);
			logger.info("DataSourceAdvice: dataSourceType= " + dataSourceType.name());
			logger.info("DataSourceAdvice: method= " + method.getMethod().getName());
			return method.proceed();
		} finally {
			// if (parentDataSource != null) {
			// ContextHolder.setDataSourceType(parentDataSource);
			// } else {
			ContextHolder.clearDataSourceType();
			// }
		}
	}

	private DataSourceType obtainDataSourceType(String text) {
		DataSourceType[] values = DataSourceType.values();
		for (int i = 0; i < values.length; i++) {
			DataSourceType type = values[i];
			if (type.name().equals(text)) {
				return type;
			}
		}
		return null;
	}
}
