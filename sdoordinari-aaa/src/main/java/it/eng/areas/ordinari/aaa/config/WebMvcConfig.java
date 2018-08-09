/**
 * 
 */
package it.eng.areas.ordinari.aaa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Bifulco Luigi
 *
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	// @Autowired
	// private Interceptor securityInterceptor;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
	 * #addInterceptors(org.springframework.web.servlet.config.annotation.
	 * InterceptorRegistry)
	 */
	// @Override
	// public void addInterceptors(InterceptorRegistry registry) {
	// // registry.addInterceptor(securityInterceptor);//
	// // .addPathPatterns("/safe/*");
	// }

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

	}

}
