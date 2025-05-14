package com.adopets.adopets_api.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adopets.adopets_api.domain.model.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long>{
    void deleteByUsuarioId(Long idUsuario);
    
    default void marcarComoAdotado(Long id) {
        findById(id).ifPresent(pet -> {
            pet.setAdotado(true);
            save(pet);  
        });
    }
}
