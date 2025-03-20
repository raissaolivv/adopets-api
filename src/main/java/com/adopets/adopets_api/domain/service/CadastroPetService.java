package com.adopets.adopets_api.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.adopets.adopets_api.domain.exception.EntidadeEmUsoException;
import com.adopets.adopets_api.domain.exception.EntidadeNaoEncontradaException;
import com.adopets.adopets_api.domain.model.Pet;
import com.adopets.adopets_api.domain.repository.PetRepository;

@Service
public class CadastroPetService{

    @Autowired
    private PetRepository petRepository;

    public Pet salvar(Pet pet){
        return petRepository.save(pet);
    }

    public void remover(Long petId){
        try {
			petRepository.deleteById(petId);
			
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
				String.format("Não existe um cadastro de pet com código %d", petId));
		
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
				String.format("Pet de código %d não pode ser removido, pois está em uso", petId));
		}
    }

    public void marcarComoAdotado(Long petId){
        try{
            petRepository.marcarComoAdotado(petId);
        }catch(EmptyResultDataAccessException e){
            throw new EntidadeNaoEncontradaException(
				String.format("Não existe um cadastro de pet com código %d", petId));
        }
    }
}
