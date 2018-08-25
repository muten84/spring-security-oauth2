package it.luigibifulco.oauth2.poc.server.controller.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "api-gateway")
//url = "localhost:9090")
@RibbonClient(name = "counter-service")
public interface CounterServiceProxy {

	@PostMapping("/counter-service/counter-service/newRequest")
	public CounterBean newRequest();

}
