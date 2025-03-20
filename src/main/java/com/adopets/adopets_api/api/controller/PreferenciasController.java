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
import com.adopets.adopets_api.domain.model.Preferencias;
import com.adopets.adopets_api.domain.repository.PreferenciasRepository;
import com.adopets.adopets_api.domain.service.CadastroPreferenciasService;

@RestController
@RequestMapping("/preferencias")
public class PreferenciasController {
    
    @Autowired
    private PreferenciasRepository preferenciasRepository;

    @Autowired
    private CadastroPreferenciasService cadastroPreferenciasService;

      @GetMapping
	public List<Preferencias> listar() {
		return preferenciasRepository.findAll();
	}
	
	@GetMapping("/{preferenciasId}")
	public ResponseEntity<Preferencias> buscar(@PathVariable Long preferenciasId) {
		Optional<Preferencias> preferencias = preferenciasRepository.findById(preferenciasId);
		
		if (preferencias.isPresent()) {
			return ResponseEntity.ok(preferencias.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Preferencias adicionar(@RequestBody Preferencias preferencias) {
		return cadastroPreferenciasService.salvar(preferencias);
	}
	
	@PutMapping("/{preferenciasId}")
	public ResponseEntity<Preferencias> atualizar(@PathVariable Long preferenciasId,
			@RequestBody Preferencias preferencias) {
		Optional<Preferencias> preferenciasAtual = preferenciasRepository.findById(preferenciasId);
		
		if (preferenciasAtual.isPresent()) {
			BeanUtils.copyProperties(preferencias, preferenciasAtual.get(), "id");
			
			Preferencias preferenciasSalvas = cadastroPreferenciasService.salvar(preferenciasAtual.get());
			return ResponseEntity.ok(preferenciasSalvas);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{preferenciasId}")
	public ResponseEntity<?> remover(@PathVariable Long preferenciasId) {
		try {
			cadastroPreferenciasService.remover(preferenciasId);	
			return ResponseEntity.noContent().build();
			
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
			
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(e.getMessage());
		}
	}
}
