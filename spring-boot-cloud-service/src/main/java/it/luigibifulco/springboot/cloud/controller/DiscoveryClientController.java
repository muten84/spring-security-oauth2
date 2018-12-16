package it.luigibifulco.springboot.cloud.controller;

import java.net.URI;
import java.util.Optional;

import javax.naming.ServiceUnavailableException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import it.luigibifulco.springboot.cloud.controller.proxy.ClientProxy;

@RestController
public class DiscoveryClientController {

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ClientProxy proxy;
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DiscoveryClientController.class);

	public Optional<URI> serviceUrl() {
		return discoveryClient.getInstances("myprefix-ms-cloud-service").//
				stream().//
				map(si -> si.getUri()).findFirst();//
	}

	@GetMapping("/discoveryClient")
	public String discoveryPing() throws RestClientException, ServiceUnavailableException {
		URI service = serviceUrl().map(s -> s.resolve("/ping")).orElseThrow(ServiceUnavailableException::new);
		return restTemplate.getForEntity(service, String.class).getBody();
	}

	@GetMapping("/proxy")
	public String proxyPing() throws RestClientException, ServiceUnavailableException {
		return this.proxy.ping();
	}

	@GetMapping("/ping")
	public String ping() {
		LOGGER.info("processing ping....");
		return "pong";
	}

}
