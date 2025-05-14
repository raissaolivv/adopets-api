package com.adopets.adopets_api.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adopets.adopets_api.domain.model.ImagemPet;

@Repository
public interface ImagemPetRepository extends JpaRepository<ImagemPet, Long> {
    List<ImagemPet> findByPetId(Long petId);
}
