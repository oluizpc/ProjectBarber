package com.projectbarber.projectbarber.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projectbarber.projectbarber.model.Agenda;
import com.projectbarber.projectbarber.model.Barbeiro;
import com.projectbarber.projectbarber.model.Cliente;
import com.projectbarber.projectbarber.model.Servico;
import com.projectbarber.projectbarber.model.StatusAgendamento;


public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    List<Agenda> findByCliente(Cliente cliente);
    List<Agenda> findByBarbeiro(Barbeiro barbeiro);
    List<Agenda> findByServico(Servico servico);
    Optional<Agenda> findByDataHoraAndBarbeiro(LocalDateTime dataHora, Barbeiro barbeiro); 
    List<Agenda> findByStatus(StatusAgendamento status);
}