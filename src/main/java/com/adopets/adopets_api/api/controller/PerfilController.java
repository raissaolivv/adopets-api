package com.adopets.adopets_api.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.adopets.adopets_api.domain.exception.EntidadeEmUsoException;
import com.adopets.adopets_api.domain.exception.EntidadeNaoEncontradaException;
import com.adopets.adopets_api.domain.model.Perfil;
import com.adopets.adopets_api.domain.model.Publicacao;
import com.adopets.adopets_api.domain.repository.PerfilRepository;
import com.adopets.adopets_api.domain.repository.PublicacaoRepository;
import com.adopets.adopets_api.domain.service.CadastroPerfilService;

@RestController
@RequestMapping("/perfil")
public class PerfilController {
   
    @Autowired
    private PerfilRepository perfilRepository;

	@Autowired
    private PublicacaoRepository publicacaoRepository ;

    @Autowired
    private CadastroPerfilService cadastroPerfilService;

    @GetMapping
	public List<Perfil> listar() {
		return perfilRepository.findAll();
	}
	
	@GetMapping("/{perfilId}")
	public ResponseEntity<Perfil> buscar(@PathVariable Long perfilId) {
		Optional<Perfil> perfil = perfilRepository.findById(perfilId);
		
		if (perfil.isPresent()) {
			return ResponseEntity.ok(perfil.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Perfil adicionar(@RequestBody Perfil perfil) {
		return cadastroPerfilService.salvar(perfil);
	}
	
	@PutMapping("/{perfilId}")
	public ResponseEntity<Perfil> atualizar(@PathVariable Long perfilId,
			@RequestBody Perfil perfil) {
		Optional<Perfil> perfilAtual = perfilRepository.findById(perfilId);
		
		if (perfilAtual.isPresent()) {
			BeanUtils.copyProperties(perfil, perfilAtual.get(), "id");
			
			Perfil perfilSalvo = cadastroPerfilService.salvar(perfilAtual.get());
			return ResponseEntity.ok(perfilSalvo);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{perfilId}")
	public ResponseEntity<?> remover(@PathVariable Long perfilId) {
		try {
			cadastroPerfilService.remover(perfilId);	
			return ResponseEntity.noContent().build();
			
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
			
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(e.getMessage());
		}
	}

	@PutMapping("/{perfilId}/curtir/{publicacaoId}")
	public ResponseEntity<Void> curtirPublicacao(@PathVariable Long perfilId, @PathVariable Long publicacaoId){
		Publicacao publicacao = publicacaoRepository.findById(publicacaoId)
        .orElseThrow(() -> new RuntimeException("Publicação não encontrada"));

		cadastroPerfilService.curtirPublicacao(perfilId, publicacao);
		return ResponseEntity.notFound().build();
	}
}
