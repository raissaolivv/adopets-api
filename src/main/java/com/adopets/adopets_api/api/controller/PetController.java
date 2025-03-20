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
import com.adopets.adopets_api.domain.model.Pet;
import com.adopets.adopets_api.domain.repository.PetRepository;
import com.adopets.adopets_api.domain.service.CadastroPetService;

@RestController
@RequestMapping("/pets")
public class PetController {
    
    @Autowired
    private PetRepository petRepository;
    
    @Autowired
    private CadastroPetService cadastroPetService;

   @GetMapping
	public List<Pet> listar() {
		return petRepository.findAll();
	}
	
	@GetMapping("/{petId}")
	public ResponseEntity<Pet> buscar(@PathVariable Long petId) {
		Optional<Pet> pet = petRepository.findById(petId);
		
		if (pet.isPresent()) {
			return ResponseEntity.ok(pet.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Pet adicionar(@RequestBody Pet pet) {
		return cadastroPetService.salvar(pet);
	}
	
	@PutMapping("/{petId}")
	public ResponseEntity<Pet> atualizar(@PathVariable Long petId,
			@RequestBody Pet pet) {
		Optional<Pet> petAtual = petRepository.findById(petId);
		
		if (petAtual.isPresent()) {
			BeanUtils.copyProperties(pet, petAtual.get(), "id");
			
			Pet petSalvo = cadastroPetService.salvar(petAtual.get());
			return ResponseEntity.ok(petSalvo);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{petId}")
	public ResponseEntity<?> remover(@PathVariable Long petId) {
		try {
			cadastroPetService.remover(petId);	
			return ResponseEntity.noContent().build();			
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();			
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(e.getMessage());
		}
	}

    @PutMapping("/{id}/adotar")
    public ResponseEntity<?> marcarComoAdotado(Long petId){
        try {
			cadastroPetService.marcarComoAdotado(petId);
            return ResponseEntity.noContent().build();				
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();			
		}
    }

}