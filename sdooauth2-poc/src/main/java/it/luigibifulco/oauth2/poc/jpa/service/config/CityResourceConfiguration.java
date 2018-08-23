package it.luigibifulco.oauth2.poc.jpa.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "city-service")
@Component
@RefreshScope
public class CityResourceConfiguration {

	private String unknownCity;

	private int maxResult;

	public CityResourceConfiguration() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CityResourceConfiguration(String unknownCity, int maxResult) {
		super();
		this.unknownCity = unknownCity;
		this.maxResult = maxResult;
	}

	public String getUnknownCity() {
		return unknownCity;
	}

	public void setUnknownCity(String unknownCity) {
		this.unknownCity = unknownCity;
	}

	public int getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

}
