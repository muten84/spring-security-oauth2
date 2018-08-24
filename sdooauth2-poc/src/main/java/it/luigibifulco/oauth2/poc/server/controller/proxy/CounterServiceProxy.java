package it.luigibifulco.oauth2.poc.server.controller.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "counter-service", path = "/counter-service")
//url = "localhost:9090")
@RibbonClient(name = "counter-service")
public interface CounterServiceProxy {

	@PostMapping("/newRequest")
	public CounterBean newRequest();

}
