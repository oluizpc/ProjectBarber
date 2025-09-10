package com.projectbarber.projectbarber.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projectbarber.projectbarber.exception.BadRequestException;
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


    public Agenda criarAgendamento(Agenda agenda) {
        validarExistenciaEntidades(agenda);
        validarHorario(agenda);
        verificarHorarioOcupado(agenda, null);

        agenda.setStatus(StatusAgendamento.MARCADO);
        return agendaRepository.save(agenda);
    }

    public List<Agenda> listarTodos() {
        return agendaRepository.findAll();
    }

    public Agenda buscarPorId(Integer id) {
        return agendaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Agendamento não encontrado"));
    }

    public Agenda atualizar(Integer id, Agenda agendaAtualizado) {
        Agenda agenda = buscarPorId(id);

        validarExistenciaEntidades(agendaAtualizado);
        validarHorario(agendaAtualizado);
        verificarHorarioOcupado(agendaAtualizado, id);

        copiarCampos(agenda, agendaAtualizado);

        return agendaRepository.save(agenda);
    }

    public void deletar(Integer id) {
        Agenda agenda = buscarPorId(id);
        agendaRepository.delete(agenda);
    }

    public List<Agenda> listarPorBarbeiro(Integer barbeiroId) {
        Barbeiro barbeiro = barbeiroRepository.findById(barbeiroId)
            .orElseThrow(() -> new BadRequestException("Barbeiro não encontrado"));
        return agendaRepository.findByBarbeiro(barbeiro);
    }

    public List<Agenda> listarPorCliente(Integer clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new BadRequestException("Cliente não encontrado"));
        return agendaRepository.findByCliente(cliente);
    }

    public Agenda alterarStatus(Integer id, StatusAgendamento novoStatus) {
        Agenda agenda = buscarPorId(id);
        agenda.setStatus(novoStatus);
        return agendaRepository.save(agenda);
    }


    private void validarExistenciaEntidades(Agenda agenda) {
        Cliente cliente = clienteRepository.findById(agenda.getCliente().getId())
                .orElseThrow(() -> new BadRequestException("Cliente não encontrado"));
        Barbeiro barbeiro = barbeiroRepository.findById(agenda.getBarbeiro().getId())
                .orElseThrow(() -> new BadRequestException("Barbeiro não encontrado"));
        Servico servico = servicoRepository.findById(agenda.getServico().getId())
                .orElseThrow(() -> new BadRequestException("Serviço não encontrado"));

        agenda.setCliente(cliente);
        agenda.setBarbeiro(barbeiro);
        agenda.setServico(servico);
    }

    private void validarHorario(Agenda agenda) {
        if (agenda.getDataHora().getMinute() != 0) {
            throw new BadRequestException("Agendamento só pode ser em horários inteiros (ex: 09:00, 10:00)");
        }
        int hora = agenda.getDataHora().getHour();
        if (hora < 9 || hora >= 18) {
            throw new BadRequestException("Horário fora do expediente");
        }
    }

    private void verificarHorarioOcupado(Agenda agenda, Integer idAtual) {
        agendaRepository.findByDataHoraAndBarbeiro(agenda.getDataHora(), agenda.getBarbeiro())
            .ifPresent(a -> {
                if (idAtual == null || !a.getId().equals(idAtual)) {
                    throw new BadRequestException("Horário já reservado para este barbeiro");
                }
            });
    }

    private void copiarCampos(Agenda original, Agenda atualizado) {
        original.setCliente(atualizado.getCliente());
        original.setBarbeiro(atualizado.getBarbeiro());
        original.setServico(atualizado.getServico());
        original.setDataHora(atualizado.getDataHora());
        original.setStatus(atualizado.getStatus());
    }
}
