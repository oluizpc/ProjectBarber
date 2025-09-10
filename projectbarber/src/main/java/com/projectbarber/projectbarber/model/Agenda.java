package com.projectbarber.projectbarber.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Agenda {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")    
    private LocalDateTime dataHora;
    
    @ManyToOne
    private Cliente cliente;
    @ManyToOne
    private Barbeiro barbeiro;
    @ManyToOne
    private Servico servico;

    @Enumerated(EnumType.STRING)
    private StatusAgendamento status;
}
