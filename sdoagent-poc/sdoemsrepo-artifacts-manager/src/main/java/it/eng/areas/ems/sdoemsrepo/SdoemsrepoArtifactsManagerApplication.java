package it.eng.areas.ems.sdoemsrepo;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "it.eng.areas.ems.sdoemsrepo")
@EnableSwagger2
@ComponentScan(basePackages = { "it.eng.areas.ems.sdoemsrepo" })
public class SdoemsrepoArtifactsManagerApplication extends SpringBootServletInitializer {

	// public static void main(String[] args) {
	// SpringApplication.run(SdoemsrepoArtifactsManagerApplication.class, args);
	// }

	private ApiInfo apiInfo() {
		ApiInfo apiInfo = new ApiInfo("REST API", "REST API per la gestione degli artefatti", "API TOS",
				"Terms of service", "", "", "");

		return apiInfo;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public FilterRegistrationBean jwtFilter() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new JwtFilter());
		registrationBean.addUrlPatterns("/api/rest/resource/siteinfo/*");
		return registrationBean;
	}

	// @Bean
	// public RedisConnectionFactory redisConnectionFactory() {
	// return new JedisConnectionFactory();
	// }

	// @Bean
	// public RedisTemplate<String, Message> redisTemplate() {
	// RedisTemplate<String, Message> template = new RedisTemplate<String,
	// Message>();
	// template.setConnectionFactory(redisConnectionFactory());
	// // explicitly disable transaction support
	// template.setEnableTransactionSupport(false);
	// return template;
	// }

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build().apiInfo(apiInfo())
				.globalOperationParameters(Lists.newArrayList(new ParameterBuilder()//
						.name("X-Authorization-Token")//
						.description("Authorizaion token header")//
						.modelRef(new ModelRef("string"))//
						.parameterType("header")//
						.required(true).build()));//
		// .securitySchemes(Lists.newArrayList(apiKey());
	}

	private ApiKey apiKey() {
		return new ApiKey("X-Authorization-Token", "api_key", "header");
	}

	public static void main(String[] args) {
		// SpringApplication.run(OrdinariWebApplication.class, args);

		configureApplication(new SpringApplicationBuilder()).run(args);
	}

	private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
		return builder.sources(SdoemsrepoArtifactsManagerApplication.class).bannerMode(Banner.Mode.OFF);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return configureApplication(builder);
	}
}
