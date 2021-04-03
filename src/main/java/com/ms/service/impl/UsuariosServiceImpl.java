package com.ms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ms.exception.ErroAutenticacaoException;
import com.ms.model.entity.Login;
import com.ms.model.entity.Usuario;
import com.ms.model.repository.LoginRepository;
import com.ms.model.repository.UsuarioRepository;
import com.ms.service.UsuariosService;

@Service
public class UsuariosServiceImpl implements UsuariosService, UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private LoginRepository loginRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public UsuariosServiceImpl() {

	}

	@Override
	public List<Usuario> listarTodos() {
		final List<Usuario> listaUsuarios = this.usuarioRepository.findAll();
		return listaUsuarios;
	}

	@Override
	public Usuario salvar(final Usuario usuario) {
		final Login login = usuario.getLogin();
		final String senha = login.getStrSenha();
		final String senhaCriptografada = passwordEncoder.encode(senha);
		login.setStrSenha(senhaCriptografada);
		usuario.setLogin(login);
		return usuarioRepository.save(usuario);
	}

	@Override
	public Optional<Usuario> obterPorId(final Long id) {
		final Optional<Usuario> usuario = this.usuarioRepository.findById(id);
		return usuario;
	}

	public Usuario obterPorLogin(final String strLogin) {
		final Optional<Login> findByStrLogin = loginRepository.findByStrLogin(strLogin);
		if (findByStrLogin.isPresent()) {
			return findByStrLogin.get().getUsuario();
		}
		return null;
	}

	@Override
	public void excluir(final Usuario usuario) {
		usuarioRepository.delete(usuario);
	}

	@Override
	public UserDetails autenticar(final String strLogin, final String senha) {
		final UserDetails userDetails = loadUserByUsername(strLogin);
		final boolean senhaOk = this.passwordEncoder.matches(senha, userDetails.getPassword());
		if (!senhaOk) {
			throw new ErroAutenticacaoException("Senha inválida!");
		}
		return userDetails;
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final Optional<Login> login = this.loginRepository.findByStrLogin(username);
		if (!login.isPresent()) {
			throw new UsernameNotFoundException("Usuário não encontrado!");
		}
		// User - permite criar um objeto UserDetails com build
		return User.builder() //
				.username(login.get().getStrLogin()) //
				.password(login.get().getStrSenha()) //
				.roles("ADMIN", "USER") // carregar as permissões do banco
				.build();
	}

}
