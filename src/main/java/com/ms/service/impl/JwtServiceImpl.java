package com.ms.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import com.ms.MSApplication;
import com.ms.model.entity.Login;
import com.ms.model.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtServiceImpl {

	@Value("${security.jwt.expiracao}")
	private String expiracao;

	@Value("${security.jwt.chave-assinatura}")
	private String chaveAssinatura;

	public String gerarToken(Usuario usuario) {
		final Long intExpiracao = Long.valueOf(expiracao);
		final LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(intExpiracao);
		final Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
		final Date data = Date.from(instant);

		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("roles", "ADMIN"); // passar informações no token, ex: roles e authorities
		if (usuario.getLogin() != null) {
			claims.put("login", usuario.getLogin().getStrLogin());
		}

		return Jwts.builder() //
				.setClaims(claims) //
				.setSubject(usuario.getStrNome()) //
				.setExpiration(data) //
				.signWith(SignatureAlgorithm.HS512, chaveAssinatura) //
				.compact();
	}

	/**
	 * Obtém informações do token
	 *
	 * @param token
	 * @return
	 * @throws ExpiredJwtException
	 */
	private Claims obterClaims(final String token) throws ExpiredJwtException {
		return Jwts.parser() // decodifica o token
				.setSigningKey(chaveAssinatura) // utiliza a chave para decodificar
				.parseClaimsJws(token) //
				.getBody();
	}

	public boolean tokenValido(final String token) {
		try {
			final Claims claims = obterClaims(token);
			final Date dataExpiracao = claims.getExpiration();
			final LocalDateTime localDateTime = dataExpiracao.toInstant().atZone(ZoneId.systemDefault())
					.toLocalDateTime();
			return LocalDateTime.now().isBefore(localDateTime);
		} catch (final Exception e) {
			return false;
		}
	}

	public String obterLoginUsuario(final String token) {
		final Claims claims = obterClaims(token);
		// return claims.getSubject();
		return (String) claims.get("login");
	}

	public static void main(String[] args) {
		/**
		 * Teste para invocar o contexto da aplicação para rodar os beans gerenciados,
		 * assim, é possível rodar a aplicação em standalone
		 */
		final ConfigurableApplicationContext contexto = SpringApplication.run(MSApplication.class);
		final JwtServiceImpl service = contexto.getBean(JwtServiceImpl.class);
		final Usuario usuario = Usuario.builder().strNome("Sergio").build();
		final Login login = Login.builder().strLogin("034.667.643-60").build();
		usuario.setLogin(login);
		final String token = service.gerarToken(usuario);
		System.out.println(token);

		final boolean tokenValido = service.tokenValido(token);
		System.out.println(tokenValido);

		final String loginUsuario = service.obterLoginUsuario(token);
		System.out.println(loginUsuario);
	}

}
