package com.adopets.adopets_api.api.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adopets.adopets_api.domain.model.Perfil;
import com.adopets.adopets_api.domain.model.Usuario;
import com.adopets.adopets_api.domain.repository.PerfilRepository;
import com.adopets.adopets_api.domain.repository.UsuarioRepository;
import com.adopets.adopets_api.dto.LoginRequest;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByLogin(loginRequest.getLogin());

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            if (usuario.getSenha().equals(loginRequest.getSenha())) {
                Perfil perfil = perfilRepository.findByUsuarioId(usuario.getId());

                Map<String, Object> resposta = new HashMap<>();
                resposta.put("id", usuario.getId());
                resposta.put("nome", usuario.getNome());
                resposta.put("login", usuario.getLogin());
                resposta.put("perfil", Map.of("id", perfil.getId()));

                return ResponseEntity.ok(resposta);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
    }
}
