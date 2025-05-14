package com.adopets.adopets_api.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.adopets.adopets_api.domain.model.Perfil;
import com.adopets.adopets_api.domain.model.Publicacao;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long>{
    @Modifying
    @Transactional
    @Query("UPDATE Perfil p SET p.publicacoesCurtidas = :publicacoes WHERE p.id = :perfilId")
    void atualizarPublicacoesCurtidas(@Param("perfilId") Long perfilId, @Param("publicacoes") List<Publicacao> publicacoes);

    Perfil findByUsuarioId(Long id);
    void deleteByUsuarioId(Long idUsuario);
}
