package com.projectbarber.projectbarber.model;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Barbeiro {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;
    
    private String nome;
    @Column(unique=true)
    private String email;   
    private String senha;

    //exemplo: arthur vai trabalhar das 09:00 as 18:00
    private LocalTime inicioExpediente; 
    private LocalTime fimExpediente;  

    private LocalTime inicioAlmoco;
    private LocalTime fimAlmoco;
}
