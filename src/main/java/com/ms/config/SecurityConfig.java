package com.ms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ms.filter.JwtAuthFilter;
import com.ms.service.impl.JwtServiceImpl;
import com.ms.service.impl.UsuariosServiceImpl;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UsuariosServiceImpl usuariosService;

	@Autowired
	private JwtServiceImpl jwtService;

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
	 * contexto do security Com JWT deixa de ser utilizado
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
		// super.configure(http);
		http.authorizeRequests().antMatchers("**/clientes/*").hasAnyRole("ADMIN") //
				.antMatchers("**/usuarios/**").hasAnyRole("ADMIN", "USER"); //

		http.csrf().disable();
		/**
		 * Não há mais sessão de usuário, passa a ser stateless
		 */
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		/**
		 * Executa o filtro que coloca o usuário no contexto do spring security, só
		 * então executa o filtro padrão do spring security
		 */
		http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

		// .and()
		// .formLogin() // Formulário default do springsecurity
		// .httpBasic(); // Credenciais no header Authorization
	}

}
