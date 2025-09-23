package com.projectbarber.projectbarber.model;

import lombok.Data;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Data
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String telefone;
    
    @Column(unique=true)
    private String email;
    private String senha; //armazenar hash/cryptografia depois

}


