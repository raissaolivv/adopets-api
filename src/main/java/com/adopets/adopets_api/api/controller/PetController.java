package com.adopets.adopets_api.api.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.adopets.adopets_api.domain.exception.EntidadeEmUsoException;
import com.adopets.adopets_api.domain.exception.EntidadeNaoEncontradaException;
import com.adopets.adopets_api.domain.model.ImagemPet;
import com.adopets.adopets_api.domain.model.Pet;
import com.adopets.adopets_api.domain.repository.ImagemPetRepository;
import com.adopets.adopets_api.domain.repository.PetRepository;
import com.adopets.adopets_api.domain.service.CadastroPetService;

@RestController
@RequestMapping("/pets")
public class PetController {

	@Autowired
	private PetRepository petRepository;

	@Autowired
	private CadastroPetService cadastroPetService;

	@Autowired
	private ImagemPetRepository imagemPetRepository;

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

	@PutMapping("/{petId}/adotar")
	public ResponseEntity<?> marcarComoAdotado(@PathVariable long petId) {
		try {
			cadastroPetService.marcarComoAdotado(petId);
			return ResponseEntity.noContent().build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/{id}/imagens")
	public ResponseEntity<?> uploadImagens(@PathVariable Long id,
			@RequestParam("imagens") List<MultipartFile> imagens) throws IOException {
		System.out.println("Recebendo imagens para o pet ID: " + id);
		System.out.println("Número de imagens recebidas: " + imagens.size());
		Optional<Pet> optionalPet = petRepository.findById(id);
		if (!optionalPet.isPresent()) {
			System.out.println("Pet não encontrado!");
			return ResponseEntity.notFound().build();
		}
		Pet pet = optionalPet.get();

		for (MultipartFile imagem : imagens) {
			System.out.println("Salvando imagem: " + imagem.getOriginalFilename());
			String nomeArquivo = UUID.randomUUID() + "_" + imagem.getOriginalFilename();
			Path caminho = Paths.get("uploads", nomeArquivo);
			Files.write(caminho, imagem.getBytes());

			ImagemPet img = new ImagemPet();
			img.setCaminho("/uploads/" + nomeArquivo);
			img.setPet(pet);
			imagemPetRepository.save(img);
		}

		return ResponseEntity.ok("Imagens salvas com sucesso.");
	}

	@GetMapping("/uploads/{nomeArquivo:.+}")
	public ResponseEntity<Resource> servirImagem(@PathVariable String nomeArquivo) throws IOException {
		Path caminho = Paths.get("uploads").resolve(nomeArquivo).normalize();
		Resource recurso = new UrlResource(caminho.toUri());

		if (!recurso.exists()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_JPEG) // ou use MediaTypeFactory para detectar
				.body(recurso);
	}

	@GetMapping("/{id}/imagens")
	public ResponseEntity<List<Map<String, String>>> listarImagensDoPet(@PathVariable Long id) {
		Optional<Pet> optionalPet = petRepository.findById(id);
		if (!optionalPet.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		List<ImagemPet> imagens = imagemPetRepository.findByPetId(id);
		List<Map<String, String>> caminhos = imagens.stream()
				.map(img -> Map.of("caminho", img.getCaminho()))
				.toList();

		return ResponseEntity.ok(caminhos);
	}

}