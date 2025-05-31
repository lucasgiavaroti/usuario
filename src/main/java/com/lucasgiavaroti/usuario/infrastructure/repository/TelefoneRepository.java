package com.lucasgiavaroti.usuario.infrastructure.repository;

import com.lucasgiavaroti.usuario.entity.Telefone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
}
