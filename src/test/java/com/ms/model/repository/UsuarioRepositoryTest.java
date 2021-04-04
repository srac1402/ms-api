package com.ms.model.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ms.model.entity.Usuario;

/**
 * Testes de Integração
 *
 * @author Sérgio R A Costa
 *
 */

/**
 * Causa erro com swagger, pois esta anotação não deixa carregar o Spring MVC
 * com suas estruturas
 *
 * @author sergio
 *
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE, connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;
//
//	@Autowired
//	private TestEntityManager entityManager;

	@Test
	public void deveSalvarUmUsuario() {
		final Usuario usuario = Usuario.builder().strNome("USUARIO TESTE").build();
		final Usuario usuarioSalvo = this.usuarioRepository.save(usuario);
		Assertions.assertNotNull(usuarioSalvo.getId());
	}

}
