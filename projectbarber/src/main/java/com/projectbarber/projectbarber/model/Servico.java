package com.projectbarber.projectbarber.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Servico {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY) 
    private Integer id;

    private String nomeServico;
    private double preco;
    private Integer tempoDuracao;
    
}
