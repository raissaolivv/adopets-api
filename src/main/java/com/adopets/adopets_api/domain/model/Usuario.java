package com.adopets.adopets_api.domain.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {

    @Id
    @EqualsAndHashCode.Include 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    
    @Column(nullable = false)
    private LocalDate data_nasc;

    
    @Column(nullable = false)
    private String sexo;
    
    
    @Column(nullable = false)
    private String endereco;
    
    
    @Column()
    private String tipoMoradia;

    
    @Column
    private String email;

    
    @Column(nullable = false)
    private String telefone;

    
    @Column(nullable = false)
    //anotação de unico
    private String login;

    
    @Column(nullable = false)
    private String senha;
    
}
