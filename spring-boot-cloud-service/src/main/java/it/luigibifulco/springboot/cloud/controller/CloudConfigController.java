package it.luigibifulco.springboot.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import it.luigibifulco.springboot.cloud.config.model.MagicCloudServiceConfiguration;

@RestController
public class CloudConfigController {

//	@Value("${/config/applications/ms-cloud-service}")
//	String value;

	@Autowired
	private MagicCloudServiceConfiguration properties;

	@GetMapping("/getConfigFromValue")
	public String getConfigFromValue() {
		return properties.getKey1();
	}

	@GetMapping("/getConfigFromProperty")
	public String getConfigFromProperty() {
		return properties.getKey1();
	}

}
