package com.adopets.adopets_api.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Pet {

    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String  nome;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Especie especie;

    @Column
    private int idade;

    @Column
    private String sexo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Porte porte;

    @Column
    private String raca;

    @Column
    private String descricao; 

    @Column(nullable = false)
    private Boolean adotado;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "perfil_id", nullable = false)
    private Perfil perfil;

}
