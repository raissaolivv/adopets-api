package com.adopets.adopets_api.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adopets.adopets_api.domain.model.Preferencias;

@Repository
public interface PreferenciasRepository extends JpaRepository<Preferencias, Long>{

}
