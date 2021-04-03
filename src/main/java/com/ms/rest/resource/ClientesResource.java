package com.ms.rest.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.exception.RegraNegocioException;
import com.ms.model.entity.Cliente;
import com.ms.rest.dto.ClienteDTO;
import com.ms.service.ClientesService;
import com.ms.service.EmailsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/clientes")
@Api(value = "API REST Clientes")
public class ClientesResource {

	@Autowired
	private ClientesService clientesService;

	@Autowired
	private EmailsService emailsService;

	@GetMapping("/teste")
	public String teste() {
		return "Olá, testando api rest";
	}

	@GetMapping("/pesquisar")
	public ResponseEntity<?> pesquisar() {
		final List<Cliente> listarTodos = clientesService.listarTodos();
		final List<ClienteDTO> clientes = new ArrayList<>();
		listarTodos.stream().forEach(cliente -> {
			clientes.add(getClienteDTO(cliente));
		});
		return ResponseEntity.ok(clientes);
	}

	@GetMapping("/pesquisar/{id}")
	public ResponseEntity<?> obterCliente(@PathVariable("id") final Long id) {
		final Optional<Cliente> cliente = this.clientesService.obterPorId(id);
		if (!cliente.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(cliente);
	}

	@ApiOperation(value = "Salva um cliente")
	@PostMapping("/salvar")
	public ResponseEntity<?> salvar(@RequestBody @Valid ClienteDTO clienteDTO) {
		try {
			final Cliente clienteSalvo = clientesService.salvar(getCliente(clienteDTO));
			return new ResponseEntity<Cliente>(clienteSalvo, HttpStatus.CREATED);
		} catch (final RegraNegocioException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}

	@ApiOperation(value = "Exclui um cliente")
	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluir(@RequestBody @Valid final ClienteDTO clienteDTO) {
		try {
			clientesService.excluir(getCliente(clienteDTO));
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		} catch (final RegraNegocioException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}

	}

	@ApiOperation(value = "Envia emails para os clientes")
	@DeleteMapping("/enviar-emails")
	public ResponseEntity<?> enviarEmails(@RequestBody @Valid final List<ClienteDTO> clientesDTO) {
		final List<Cliente> clientes = new ArrayList<>();
		clientesDTO.stream().forEach(clienteDTO -> {
			clientes.add(getCliente(clienteDTO));
		});
		System.out.println(clientes);
		try {
			if (clientesDTO.size() == 1000) {
				emailsService.enviarEmails(clientes, null, null);
			}
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		} catch (IOException | MessagingException ex) {
			ex.printStackTrace();
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}

	public Cliente getCliente(final ClienteDTO clienteDTO) {
		return Cliente.builder().id(clienteDTO.getId()).strNome(clienteDTO.getNome()).strEmail(clienteDTO.getEmail())
				.build();
	}

	public ClienteDTO getClienteDTO(final Cliente cliente) {
		return ClienteDTO.builder().id(cliente.getId()).nome(cliente.getStrNome()).email(cliente.getStrEmail()).build();
	}

}
