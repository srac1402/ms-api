package com.ms.config;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	// http://localhost:8080/swagger-ui.html
	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select().paths(regex("/rest.*")).build().apiInfo(metaInfo());
	}

	private ApiInfo metaInfo() {

		@SuppressWarnings("rawtypes")
		final ApiInfo apiInfo = new ApiInfo("Colaboradores API REST", "API REST Sistema SCB", "1.0", "Terms of Service",
				new Contact("Sérgio", "s-rac@hotmail.com", "s-rac@hotmail.com"), "Apache License Version 2.0",
				"https://www.apache.org/licesen.html", new ArrayList<VendorExtension>());

		return apiInfo;
	}

}
