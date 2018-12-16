package it.luigibifulco.springboot.cloud.controller.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "myprefix-ms-cloud-service")
//url = "localhost:9090")
@RibbonClient(name = "myprefix-ms-cloud-service")
public interface ClientProxy {

	@GetMapping("/ping")
	public String ping();

}
