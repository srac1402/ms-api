package com.ms.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ms.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
