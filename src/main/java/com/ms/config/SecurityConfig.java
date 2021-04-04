package com.ms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.collect.ImmutableList;
import com.ms.filter.JwtAuthFilter;
import com.ms.service.impl.JwtServiceImpl;
import com.ms.service.impl.UsuariosServiceImpl;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UsuariosServiceImpl usuariosService;

	@Autowired
	private JwtServiceImpl jwtService;

	private static final String[] LISTA_BRANCA_DE_AUTORIZACOES = { //
			"/usuarios/autenticar", // Autenticação
			"/v2/api-docs", "/configuration/ui", // Swagger
			"/swagger-resources/**", "/configuration/security", //
			"/swagger-ui.html", "/webjars/**" };

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public OncePerRequestFilter jwtFilter() {
		return new JwtAuthFilter(jwtService, usuariosService);
	}

	/**
	 * Configurações de autenticação dos usuários para adicioná-los dentro do
	 * contexto do security, com o JWT, deixa de ser utilizado, pois o JwtAuthFilter
	 * irá interceptar a requisição, ler as informações de usuário no token e
	 * repassá-las ao contexto do spring security ao final.
	 */
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		// super.configure(auth);
//		auth.userDetailsService(usuariosService) // service de usuários implementa UserDetailsService
//				.passwordEncoder(passwordEncoder()); // tipo de criptografia que será utilizada na senha
//	}

	/**
	 * Autorização - verifica as autorizações para as páginas
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("**/clientes/*").hasAnyRole("ADMIN", "USER") //
				.antMatchers("**/usuarios/**").hasAnyRole("ADMIN") //
				/**
				 * Autentica a cada requisição realizada
				 */
				.anyRequest().authenticated() //
				// http.authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll(); //
				.and().csrf().disable() // testar com postman

				/**
				 * Não há mais sessão de usuário, passa a ser stateless
				 */
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //
				/**
				 * Executa o filtro que coloca o usuário no contexto do spring security, só
				 * então executa o filtro padrão do spring security
				 */
				.and() //
				.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

		http.headers().frameOptions().sameOrigin() // para o H2 Console
				.cacheControl(); // desabilita o cache
		/**
		 * Aguarda por um bean CorsConfigurationSource
		 */
		http.cors();

		// .and()
		// .formLogin() // Formulário default do springsecurity
		// .httpBasic(); // Credenciais no header Authorization
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		/**
		 * Ignorar rota de validação
		 */
		web.ignoring().antMatchers(LISTA_BRANCA_DE_AUTORIZACOES);
	}

	/**
	 * Cors para o Security
	 *
	 * @return
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(ImmutableList.of("*"));
		configuration.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
