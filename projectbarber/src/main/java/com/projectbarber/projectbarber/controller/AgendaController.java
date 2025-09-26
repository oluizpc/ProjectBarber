package com.projectbarber.projectbarber.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projectbarber.projectbarber.exception.BadRequestException;
import com.projectbarber.projectbarber.model.Agenda;
import com.projectbarber.projectbarber.model.Servico;
import com.projectbarber.projectbarber.repository.ServicoRepository;
import com.projectbarber.projectbarber.service.AgendaService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/agendas")
public class AgendaController {
    private final AgendaService agendaService;
    private final ServicoRepository servicoRepository;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    // Cadastrando horário
    @PostMapping
    public ResponseEntity<Agenda> criarAgenda(@RequestBody Agenda agenda) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendaService.criarAgendamento(agenda));
    }

    // Listando todos os horários
    @GetMapping
    public ResponseEntity<List<Agenda>> listarTodos() {
        return ResponseEntity.ok(agendaService.listarTodos());
    }

    // Buscando agendamento por id
    @GetMapping("/{id}")
    public ResponseEntity<Agenda> buscarPorId(@PathVariable Integer id) {
        Agenda agenda = agendaService.buscarPorId(id);
        return agenda != null ? ResponseEntity.ok(agenda) : ResponseEntity.notFound().build();
    }

    // Listar agendas por barbeiro
    @GetMapping("/barbeiro/{id}")
    public ResponseEntity<List<Agenda>> listarPorBarbeiro(@PathVariable Integer id) {
        return ResponseEntity.ok(agendaService.listarPorBarbeiro(id));
    }

    // Listar agendas por cliente
    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<Agenda>> listarPorCliente(@PathVariable Integer id) {
        return ResponseEntity.ok(agendaService.listarPorCliente(id));
    }

    // Atualizando agendamento
    @PutMapping("/{id}")
    public ResponseEntity<Agenda> atualizar(@RequestBody Agenda agenda, @PathVariable Integer id) {
        return ResponseEntity.ok(agendaService.atualizar(id, agenda));
    }

    // Deletando agendamento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        agendaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

@GetMapping("/horarios")
public ResponseEntity<List<LocalDateTime>> listarHorariosDisponiveis(
        @RequestParam Integer barbeiroId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
        @RequestParam Integer servicoId) {

    Servico servico = servicoRepository.findById(servicoId)
            .orElseThrow(() -> new BadRequestException("Serviço não encontrado"));

    int duracaoServico = servico.getTempoDuracao();

    return ResponseEntity.ok(
        agendaService.listarHorariosDisponiveis(barbeiroId, data, duracaoServico)
    );
}


    
}
