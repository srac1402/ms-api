package com.ms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ms.model.entity.Cliente;
import com.ms.model.repository.ClienteRepository;
import com.ms.service.ClientesService;

@Service
public class ClientesServiceImpl implements ClientesService {

	@Autowired
	private ClienteRepository clienteRepository;

	public ClientesServiceImpl() {

	}

	@Override
	public List<Cliente> listarTodos() {
		final List<Cliente> listaClientes = this.clienteRepository.findAll();
		return listaClientes;
	}

	@Override
	public Cliente salvar(final Cliente Cliente) {
		return clienteRepository.save(Cliente);
	}

	@Override
	public Optional<Cliente> obterPorId(final Long id) {
		final Optional<Cliente> cliente = this.clienteRepository.findById(id);
		return cliente;
	}

	@Override
	public void excluir(final Cliente cliente) {
		clienteRepository.delete(cliente);
	}

}
