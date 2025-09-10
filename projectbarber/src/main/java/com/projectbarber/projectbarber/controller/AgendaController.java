package com.projectbarber.projectbarber.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projectbarber.projectbarber.model.Agenda;
import com.projectbarber.projectbarber.service.AgendaService;

import java.util.List;

@RestController
@RequestMapping("/agendas")
public class AgendaController {
    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    // Cadastrando horário
    @PostMapping
    public ResponseEntity<Agenda> criarAgenda(@RequestBody Agenda agenda) {
        return ResponseEntity.ok(agendaService.criarAgendamento(agenda));
    }

    // Listando todos os horários
    @GetMapping
    public ResponseEntity<List<Agenda>> listarTodos() {
        return ResponseEntity.ok(agendaService.listarTodos());
    }

    // Buscando agendamento por id
    @GetMapping("/{id}")
    public ResponseEntity<Agenda> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(agendaService.buscarPorId(id));
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
}
