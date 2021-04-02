package com.ms.rest.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ms.exception.ErroAutenticacaoException;
import com.ms.model.entity.Usuario;
import com.ms.rest.dto.LoginDTO;
import com.ms.rest.dto.TokenDTO;
import com.ms.service.impl.JwtServiceImpl;
import com.ms.service.impl.UsuariosServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/usuarios")
@Api(value = "API REST Usuários")
public class UsuariosResource {

	@Autowired
	private UsuariosServiceImpl usuariosService;

	@Autowired
	private JwtServiceImpl jwtServiceImpl;

	@GetMapping("/teste")
	public String teste() {
		return "Olá, testando api rest";
	}

	@ApiOperation(value = "Salva um usuário")
	@PostMapping("/salvar")
	public Usuario salvar(@RequestBody @Valid Usuario usuario) {
		return usuariosService.salvar(usuario);
	}

	@ApiOperation(value = "Exclui um usuário")
	@DeleteMapping("/excluir")
	public void excluir(@RequestBody @Valid Usuario usuario) {
		usuariosService.excluir(usuario);
	}

	/**
	 * 90951535099 | 123
	 *
	 * @param loginDTO
	 * @return
	 */
	@PostMapping("/autenticar")
	public TokenDTO autenticar(@RequestBody @Valid final LoginDTO loginDTO) {
		try {
			final UserDetails userDetails = usuariosService.autenticar(loginDTO.getLogin(), loginDTO.getSenha());
			final Usuario usuario = usuariosService.obterPorLogin(userDetails.getUsername());
			final String token = jwtServiceImpl.gerarToken(usuario);
			return TokenDTO.builder().usuario(usuario.getStrNome()).token(token).build();
		} catch (ErroAutenticacaoException | UsernameNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}

	}

}
