package com.projectbarber.projectbarber.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
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

    // ===================== CRUD =====================

    public Agenda criarAgendamento(Agenda agenda) {
        validarExistenciaEntidades(agenda);
        validarHorario(agenda);

        agenda.setDataHoraFim(agenda.getDataHora().plusMinutes(agenda.getServico().getTempoDuracao()));
        verificarHorarioOcupado(agenda, null);

        // Verificar conflito com horário de almoço
        if (isDentroAlmoco(agenda.getBarbeiro(), agenda.getDataHora().toLocalTime(), agenda.getDataHoraFim().toLocalTime())) {
            throw new BadRequestException("Horário coincide com o horário de almoço do barbeiro.");
        }

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

        agendaAtualizado.setDataHoraFim(
            agendaAtualizado.getDataHora().plusMinutes(agendaAtualizado.getServico().getTempoDuracao())
        );
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
        validarTransicaoStatus(agenda.getStatus(), novoStatus);
        agenda.setStatus(novoStatus);
        return agendaRepository.save(agenda);
    }

    // ===================== LÓGICA DE HORÁRIOS =====================

    public List<LocalDateTime> listarHorariosDisponiveis(Integer barbeiroId, LocalDate dia, Integer duracaoServicoBase) {
        Barbeiro barbeiro = barbeiroRepository.findById(barbeiroId)
                .orElseThrow(() -> new BadRequestException("Barbeiro não encontrado"));

        LocalTime inicioExpediente = barbeiro.getInicioExpediente();
        LocalTime fimExpediente = barbeiro.getFimExpediente();

        List<Agenda> agendamentos = agendaRepository.findByBarbeiroAndDataHoraBetween(
                barbeiro,
                dia.atTime(inicioExpediente),
                dia.atTime(fimExpediente)
        );
        agendamentos.sort(Comparator.comparing(Agenda::getDataHora));

        List<LocalDateTime> horariosDisponiveis = new ArrayList<>();
        LocalDateTime horarioAtual = dia.atTime(inicioExpediente);

        while (horarioAtual.plusMinutes(duracaoServicoBase).isBefore(dia.atTime(fimExpediente).plusSeconds(1))) {
            LocalDateTime fim = horarioAtual.plusMinutes(duracaoServicoBase);

            boolean dentroAlmoco = isDentroAlmoco(barbeiro, horarioAtual.toLocalTime(), fim.toLocalTime());
            boolean ocupado = agendamentos.stream().anyMatch(a -> isConflitante(horarioAtual, fim, a));

            if (!dentroAlmoco && !ocupado) {
                horariosDisponiveis.add(horarioAtual);
            }

            horarioAtual = horarioAtual.plusMinutes(duracaoServicoBase);
        }

        return horariosDisponiveis;
    }

    // ===================== MÉTODOS AUXILIARES =====================

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
        int minuto = agenda.getDataHora().getMinute();
        if (minuto % 15 != 0) {
            throw new BadRequestException("Agendamento só pode ser em intervalos de 15 minutos (ex: 09:00, 09:15, 09:30)");
        }

        LocalTime horaAgendamento = agenda.getDataHora().toLocalTime();
        Barbeiro barbeiro = agenda.getBarbeiro();
        if (horaAgendamento.isBefore(barbeiro.getInicioExpediente()) || horaAgendamento.isAfter(barbeiro.getFimExpediente())) {
            throw new BadRequestException("Horário fora do expediente do barbeiro");
        }
    }

    private void verificarHorarioOcupado(Agenda agenda, Integer idAtual) {
        List<Agenda> agendasBarbeiro = agendaRepository.findByBarbeiro(agenda.getBarbeiro());

        for (Agenda a : agendasBarbeiro) {
            if (idAtual != null && a.getId().equals(idAtual)) continue;

            if (isConflitante(agenda.getDataHora(), agenda.getDataHoraFim(), a)) {
                throw new BadRequestException("Horário conflitante com outro agendamento");
            }
        }
    }

    private void copiarCampos(Agenda original, Agenda atualizado) {
        original.setCliente(atualizado.getCliente());
        original.setBarbeiro(atualizado.getBarbeiro());
        original.setServico(atualizado.getServico());
        original.setDataHora(atualizado.getDataHora());
        original.setStatus(atualizado.getStatus());
        original.setDataHoraFim(atualizado.getDataHoraFim());
    }

    private boolean isDentroAlmoco(Barbeiro b, LocalTime inicio, LocalTime fim) {
        return !(fim.isBefore(b.getInicioAlmoco()) || inicio.isAfter(b.getFimAlmoco()));
    }

    private boolean isConflitante(LocalDateTime inicio, LocalDateTime fim, Agenda a) {
        return inicio.isBefore(a.getDataHoraFim()) && fim.isAfter(a.getDataHora());
    }

    private void validarTransicaoStatus(StatusAgendamento atual, StatusAgendamento novo) {
        if (atual == StatusAgendamento.CANCELADO && novo == StatusAgendamento.MARCADO) {
            throw new BadRequestException("Não é permitido reativar um agendamento cancelado");
        }
    }
}
