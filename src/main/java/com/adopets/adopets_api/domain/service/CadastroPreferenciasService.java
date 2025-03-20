package com.adopets.adopets_api.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.adopets.adopets_api.domain.exception.EntidadeEmUsoException;
import com.adopets.adopets_api.domain.exception.EntidadeNaoEncontradaException;
import com.adopets.adopets_api.domain.model.Preferencias;
import com.adopets.adopets_api.domain.repository.PreferenciasRepository;

@Service
public class CadastroPreferenciasService {
    @Autowired
    private PreferenciasRepository PreferenciasRepository;

    public Preferencias salvar(Preferencias preferencias){
        return PreferenciasRepository.save(preferencias);
    }

    public void remover(Long preferenciasId){
        try {
			PreferenciasRepository.deleteById(preferenciasId);
			
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
				String.format("Não existe um cadastro de Preferencias com código %d", preferenciasId));
		
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
				String.format("Preferencias de código %d não pode ser removido, pois está em uso", preferenciasId));
		}
    }
}
