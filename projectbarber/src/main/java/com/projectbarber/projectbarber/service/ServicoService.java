package com.projectbarber.projectbarber.service;

import org.springframework.stereotype.Service;

import com.projectbarber.projectbarber.exception.BadRequestException;
import com.projectbarber.projectbarber.exception.ResourceNotFoundException;
import com.projectbarber.projectbarber.model.Servico;
import com.projectbarber.projectbarber.repository.ServicoRepository;

@Service
public class ServicoService {
    
    private final ServicoRepository servicoRepository;  

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    // cadastro de servico
    public Servico cadastrarServico(Servico servico) {
        servicoRepository.findByNomeServico(servico.getNomeServico()).ifPresent(s -> {
            throw new BadRequestException("Serviço já cadastrado");
        });
        return servicoRepository.save(servico);}

    // buscar por ID
    public Servico buscarPorId(Integer id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Serviço não encontrado"));     
    }

    // listar todos
    public java.util.List<Servico> listarTodos() {
        return servicoRepository.findAll();     
    }
    //Atualizando servico
    public Servico atualizarServico(Integer id, Servico servicoAtualizado) {
        Servico servico = buscarPorId(id);

        servicoRepository.findByNomeServico(servicoAtualizado.getNomeServico()).ifPresent(s -> {
            if (!s.getId().equals(id)) {
                throw new BadRequestException("Serviço já cadastrado");
            }
        });

        servico.setNomeServico(servicoAtualizado.getNomeServico());
        servico.setPreco(servicoAtualizado.getPreco());
        servico.setTempoDuracao(servicoAtualizado.getTempoDuracao());

        return servicoRepository.save(servico);
    }

    //deletando servico
    public void deletarServico(Integer id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));
        servicoRepository.delete(servico);
    }
}

