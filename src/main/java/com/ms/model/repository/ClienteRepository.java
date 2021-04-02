package com.ms.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ms.model.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
