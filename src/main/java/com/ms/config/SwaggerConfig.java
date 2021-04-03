package com.ms.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private ApiKey apiKey() {
		return new ApiKey("JWT", "Authorization", "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}

	private List<SecurityReference> defaultAuth() {
		final AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		final AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
	}

	// http://localhost:8080/swagger-ui.html
	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2) //
				.securityContexts(Arrays.asList(securityContext())) //
				.securitySchemes(Arrays.asList(apiKey())) //
				.enable(true) //
				.useDefaultResponseMessages(false) //
				.select() //
				.apis(RequestHandlerSelectors.basePackage("com.ms.rest.resource"))
				// .paths(regex("/rest.*")) //
				.paths(PathSelectors.any()) //
				.build() //
				.apiInfo(apiInfo());
	}

	private Contact contact() {
		final String nome = "Sergio Abreu";
		final String url = "http://github.com";
		final String email = "sergioricardo.abreucosta@gmail.com";
		return new Contact(nome, url, email);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder() //
				.title("MS-API").description("Api para gerenciamento de clientes") //
				.version("0.0.1").contact(contact()) //
				.build();
	}

}
