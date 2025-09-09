package com.projectbarber.projectbarber.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projectbarber.projectbarber.model.Agenda;
import com.projectbarber.projectbarber.model.Barbeiro;
import com.projectbarber.projectbarber.model.Cliente;
import com.projectbarber.projectbarber.model.Servico;
import com.projectbarber.projectbarber.model.StatusAgendamento;
import com.projectbarber.projectbarber.repository.AgendaRepository;
import com.projectbarber.projectbarber.repository.BarbeiroRepository;
import com.projectbarber.projectbarber.repository.ClienteRepository;
import com.projectbarber.projectbarber.repository.ServicoRepository;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final ClienteRepository clienteRepository;
    private final BarbeiroRepository barbeiroRepository;
    private final ServicoRepository servicoRepository;

    public AgendaService(AgendaRepository agendaRepository,
                         ClienteRepository clienteRepository,
                         BarbeiroRepository barbeiroRepository,
                         ServicoRepository servicoRepository) {
        this.agendaRepository = agendaRepository;
        this.clienteRepository = clienteRepository;
        this.barbeiroRepository = barbeiroRepository;
        this.servicoRepository = servicoRepository;
    }

    // Criar agendamento
    public Agenda criarAgendamento(Agenda agenda) {
        // 1. Verifica se cliente, barbeiro e serviço existem
        Cliente cliente = clienteRepository.findById(agenda.getCliente().getId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        Barbeiro barbeiro = barbeiroRepository.findById(agenda.getBarbeiro().getId())
                .orElseThrow(() -> new IllegalArgumentException("Barbeiro não encontrado"));
        Servico servico = servicoRepository.findById(agenda.getServico().getId())
                .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado"));

        agenda.setCliente(cliente);
        agenda.setBarbeiro(barbeiro);
        agenda.setServico(servico);

        // 2. Verifica se horário é inteiro (ex: 09:00, 10:00)
        if (agenda.getDataHora().getMinute() != 0) {
            throw new IllegalArgumentException("Agendamento só pode ser em horários inteiros (ex: 09:00, 10:00)");
        }

        // 3. Verifica horário dentro do expediente (09h-18h)
        int hora = agenda.getDataHora().getHour();
        if (hora < 9 || hora >= 18) {
            throw new IllegalArgumentException("Horário fora do expediente");
        }

        // 4. Verifica se horário já está ocupado pelo barbeiro
        boolean ocupado = agendaRepository
                .findByDataHoraAndBarbeiro(agenda.getDataHora(), barbeiro)
                .isPresent();
        if (ocupado) {
            throw new IllegalArgumentException("Horário já reservado para este barbeiro");
        }

        // 5. Define status padrão
        agenda.setStatus(StatusAgendamento.MARCADO);

        // 6. Salva agendamento
        return agendaRepository.save(agenda);
    }

    // Listar todos os agendamentos
    public List<Agenda> listarTodos() {
        return agendaRepository.findAll();
    }

    // Buscar por ID
    public Agenda buscarPorId(Long id) {
        return agendaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado"));
    }

    // Atualizar agendamento
    public Agenda atualizar(Long id, Agenda agendaAtualizado) {
        Agenda agenda = buscarPorId(id);

        agenda.setCliente(agendaAtualizado.getCliente());
        agenda.setBarbeiro(agendaAtualizado.getBarbeiro());
        agenda.setServico(agendaAtualizado.getServico());
        agenda.setDataHora(agendaAtualizado.getDataHora());
        agenda.setStatus(agendaAtualizado.getStatus());

        // Reaplica validações de horário
        if (agenda.getDataHora().getMinute() != 0) {
            throw new IllegalArgumentException("Agendamento só pode ser em horários inteiros (ex: 09:00, 10:00)");
        }
        int hora = agenda.getDataHora().getHour();
        if (hora < 9 || hora >= 18) {
            throw new IllegalArgumentException("Horário fora do expediente");
        }
        boolean ocupado = agendaRepository
                .findByDataHoraAndBarbeiro(agenda.getDataHora(), agenda.getBarbeiro())
                .stream()
                .anyMatch(a -> a.getId() != agenda.getId());

        if (ocupado) {
            throw new IllegalArgumentException("Horário já reservado para este barbeiro");
        }



        return agendaRepository.save(agenda);
    }

    // Deletar agendamento
    public void deletar(Long id) {
        Agenda agenda = buscarPorId(id);
        agendaRepository.delete(agenda);
    }

    public List<Agenda> listarPorBarbeiro(Long barbeiroId) {
        Barbeiro barbeiro = barbeiroRepository.findById(barbeiroId)
            .orElseThrow(() -> new IllegalArgumentException("Barbeiro não encontrado"));
        return agendaRepository.findByBarbeiro(barbeiro).stream().toList();
    }

    public List<Agenda> listarPorCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        return agendaRepository.findByCliente(cliente).stream().toList();
    }

    public Agenda alterarStatus(Long id, StatusAgendamento novoStatus) {
        Agenda agenda = buscarPorId(id);
        agenda.setStatus(novoStatus);
        return agendaRepository.save(agenda);
    }


}
