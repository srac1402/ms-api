package com.ms.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ms.model.entity.Login;

public interface LoginRepository extends JpaRepository<Login, String> {

	Optional<Login> findByStrLogin(final String strLogin);

}
