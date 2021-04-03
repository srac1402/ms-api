package com.ms.config;

import java.util.ArrayList;
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

	/**
	 * Implementação de segurança no Swagger para testar os endpoints que dependem
	 * de autorização via token, se as configurações estiverem ok, o botão Authorize
	 * estará disponível. Ao acessar o swagger, basta gerar o token no endpoint de
	 * autenticação, digitar "Bearer + token gerado" na caixa de texto e clicar no
	 * botão Authorize.
	 *
	 * @return
	 */
	private ApiKey apiKey() {
		return new ApiKey("JWT", "Authorization", "header");
	}

	private List<SecurityReference> defaultAuth() {
		final AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		final AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		final List<SecurityReference> auths = new ArrayList<>();
		// JWT - mesmo nome que foi definido em apiKey()
		auths.add(new SecurityReference("JWT", authorizationScopes));
		return auths;
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder() //
				.securityReferences(defaultAuth()) //
				// mesmos caminhos definidos em docket paths, no caso, qualquer caminho
				.forPaths(PathSelectors.any()) //
				.build();
	}

	// http://localhost:8080/swagger-ui.html
	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2) //
				.enable(true) //
				.useDefaultResponseMessages(false) //
				.select() //
				.apis(RequestHandlerSelectors.basePackage("com.ms.rest.resource"))
				// .paths(regex("/rest.*")) //
				.paths(PathSelectors.any()) //
				.build() //
				// Adicionando a segurança, para utilizar token, ao swagger
				.securityContexts(Arrays.asList(securityContext())) //
				.securitySchemes(Arrays.asList(apiKey())) //
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
