package com.adopets.adopets_api.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.adopets.adopets_api.domain.exception.EntidadeEmUsoException;
import com.adopets.adopets_api.domain.exception.EntidadeNaoEncontradaException;
import com.adopets.adopets_api.domain.model.Perfil;
import com.adopets.adopets_api.domain.model.Publicacao;
import com.adopets.adopets_api.domain.model.Usuario;
import com.adopets.adopets_api.domain.repository.PerfilRepository;
import com.adopets.adopets_api.domain.repository.UsuarioRepository;

@Service
public class CadastroPerfilService {
    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Perfil salvar(Perfil perfil){
        if(perfil.getUsuario() != null && perfil.getUsuario().getId() != null){
            Usuario usuarioGerenciado = usuarioRepository.findById(perfil.getUsuario().getId()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            perfil.setUsuario(usuarioGerenciado);
        }
        return perfilRepository.save(perfil);
    }

    public void remover(Long perfilId){
        try {
			perfilRepository.deleteById(perfilId);
			
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
				String.format("Não existe um cadastro de perfil com código %d", perfilId));
		
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
				String.format("perfil de código %d não pode ser removido, pois está em uso", perfilId));
		}
    }

    public void curtirPublicacao(Long perfilId, Publicacao publicacao){
        Perfil perfil = perfilRepository.findById(perfilId)
        .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));

        if (!perfil.getPublicacoesCurtidas().contains(publicacao)) {
            perfil.getPublicacoesCurtidas().add(publicacao);
            perfilRepository.save(perfil); // Atualiza automaticamente
        }
    }
}
