package com.ms.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ms.service.impl.JwtServiceImpl;
import com.ms.service.impl.UsuariosServiceImpl;

/**
 * Este filtro deverá realizar o papel que seria de
 * AuthenticationManagerBuilder, capturar o token da requisição, extrair as
 * informações e repassar para o contexto do spring security, e deverá ser
 * executado antes (before) do filtro padrão.
 *
 * @author sergio
 *
 */
public class JwtAuthFilter extends OncePerRequestFilter {

	private JwtServiceImpl jwtServiceImpl;
	private UsuariosServiceImpl usuariosServiceImpl;

	/**
	 * Os services serão repassados na configuração SecurityConfig
	 *
	 * @param jwtServiceImpl
	 * @param usuariosServiceImpl
	 */
	public JwtAuthFilter(JwtServiceImpl jwtServiceImpl, UsuariosServiceImpl usuariosServiceImpl) {
		super();
		this.jwtServiceImpl = jwtServiceImpl;
		this.usuariosServiceImpl = usuariosServiceImpl;
	}

	/**
	 * Intercepta a requisição para pegar os dados do usuário, torna o uso de
	 * AuthenticationManagerBuilder no SecurityConfig depreciado
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authorization = request.getHeader("Authorization");
		if (authorization != null && authorization.startsWith("Bearer")) {
			final String token = authorization.split(" ")[1];
			final boolean tokenValido = jwtServiceImpl.tokenValido(token);
			if (tokenValido) {
				final String loginUsuario = jwtServiceImpl.obterLoginUsuario(token);
				final UserDetails userDetails = usuariosServiceImpl.loadUserByUsername(loginUsuario);
				/**
				 * Preparando os dados de usuário para inserir no contexto do spring securiry,
				 * encapsulando-o no objeto abaixo
				 */
				final UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				/**
				 * Passa para o spring security o contexto da requisição web
				 */
				user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				/**
				 * Insere o usuário no contexto do spring security
				 */
				SecurityContextHolder.getContext().setAuthentication(user);
			}
		}

		/**
		 * Despacha a requisição
		 */
		filterChain.doFilter(request, response);

	}

}
