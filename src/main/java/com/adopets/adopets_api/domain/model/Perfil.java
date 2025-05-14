package com.adopets.adopets_api.domain.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Perfil {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToMany
    @JoinTable(
        name = "perfil_publicacoes_curtidas",
        joinColumns = @JoinColumn(name = "perfil_id"),
        inverseJoinColumns = @JoinColumn(name = "publicacao_id")
    )
    // @JsonInclude(JsonInclude.Include.NON_NULL)
    // @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JsonIgnoreProperties({"perfil", "pet", "usuario", "perfisQueCurtiram"})
    private List<Publicacao> publicacoesCurtidas = new ArrayList<>();;

    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Evita o loop infinito na serialização
    private List<Pet> animaisCadastrados;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    public void curtirPublicacao(Publicacao publicacao){
        if(!publicacoesCurtidas.contains(publicacao)){
            publicacoesCurtidas.add(publicacao);
        }
    }
}
