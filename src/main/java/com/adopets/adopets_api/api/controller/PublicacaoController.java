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
import com.adopets.adopets_api.domain.model.Publicacao;
import com.adopets.adopets_api.domain.repository.PublicacaoRepository;
import com.adopets.adopets_api.domain.service.CadastroPublicacaoService;

@RestController
@RequestMapping("/publicacao")
public class PublicacaoController {
    
    @Autowired
    private PublicacaoRepository publicacaoRepository;

    @Autowired
    private CadastroPublicacaoService cadastroPublicacaoService;

    @GetMapping
	public List<Publicacao> listar() {
		return publicacaoRepository.findAll();
	}
	
	@GetMapping("/{publicacaoId}")
	public ResponseEntity<Publicacao> buscar(@PathVariable Long publicacaoId) {
		Optional<Publicacao> publicacao = publicacaoRepository.findById(publicacaoId);
		
		if (publicacao.isPresent()) {
			return ResponseEntity.ok(publicacao.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Publicacao adicionar(@RequestBody Publicacao publicacao) {
		return cadastroPublicacaoService.salvar(publicacao);
	}
	
	@PutMapping("/{publicacaoId}")
	public ResponseEntity<Publicacao> atualizar(@PathVariable Long publicacaoId,
			@RequestBody Publicacao publicacao) {
		Optional<Publicacao> publicacaoAtual = publicacaoRepository.findById(publicacaoId);
		
		if (publicacaoAtual.isPresent()) {
			BeanUtils.copyProperties(publicacao, publicacaoAtual.get(), "id");
			
			Publicacao publicacaoSalva = cadastroPublicacaoService.salvar(publicacaoAtual.get());
			return ResponseEntity.ok(publicacaoSalva);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{publicacaoId}")
	public ResponseEntity<?> remover(@PathVariable Long publicacaoId) {
		try {
			cadastroPublicacaoService.remover(publicacaoId);	
			return ResponseEntity.noContent().build();
			
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
			
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(e.getMessage());
		}
	}
}
