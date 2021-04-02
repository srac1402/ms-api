package com.ms.service;

import java.util.List;
import java.util.Optional;

import com.ms.model.entity.Cliente;

public interface ClientesService {

	Cliente salvar(final Cliente cliente);

	Optional<Cliente> obterPorId(final Long id);

	List<Cliente> listarTodos();

	void excluir(final Cliente cliente);
}
