package com.adopets.adopets_api.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.adopets.adopets_api.domain.exception.EntidadeEmUsoException;
import com.adopets.adopets_api.domain.exception.EntidadeNaoEncontradaException;
import com.adopets.adopets_api.domain.model.Usuario;
import com.adopets.adopets_api.domain.repository.UsuarioRepository;

@Service
public class CadastroUsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario salvar(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public void remover(Long usuarioId){
        try {
			usuarioRepository.deleteById(usuarioId);
			
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
				String.format("Não existe um cadastro de usuario com código %d", usuarioId));
		
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
				String.format("Usuario de código %d não pode ser removido, pois está em uso", usuarioId));
		}
    }
}
