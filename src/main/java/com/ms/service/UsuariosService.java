package com.ms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

import com.ms.model.entity.Usuario;

public interface UsuariosService {

	Usuario salvar(final Usuario usuario);

	Optional<Usuario> obterPorId(final Long id);

	List<Usuario> listarTodos();

	UserDetails autenticar(final String strLogin, final String senha);

	void excluir(final Usuario usuario);
}
