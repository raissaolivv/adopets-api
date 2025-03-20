package com.adopets.adopets_api.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Preferencias {
    
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int idade_max;

    @Column
    private int idade_min;

    @Column
    private String sexo;

    @Column
    private String raca;

    @Column
    private int distancia_max;

    @Enumerated(EnumType.STRING)
    @Column
    private Porte porte;

    @Enumerated(EnumType.STRING)
    @Column
    private Especie especie;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
}
