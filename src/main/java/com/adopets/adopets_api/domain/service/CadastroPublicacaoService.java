package com.adopets.adopets_api.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.adopets.adopets_api.domain.exception.EntidadeEmUsoException;
import com.adopets.adopets_api.domain.exception.EntidadeNaoEncontradaException;
import com.adopets.adopets_api.domain.model.Publicacao;
import com.adopets.adopets_api.domain.repository.PublicacaoRepository;

@Service
public class CadastroPublicacaoService {

    @Autowired
    private PublicacaoRepository publicacaoRepository;

    public Publicacao salvar(Publicacao publicacao){
        return publicacaoRepository.save(publicacao);
    }

    public void remover(Long publicacaoId){
        try {
			publicacaoRepository.deleteById(publicacaoId);
			
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
				String.format("Não existe um cadastro de publicacao com código %d", publicacaoId));
		
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
				String.format("publicacao de código %d não pode ser removido, pois está em uso", publicacaoId));
		}
    }
}
