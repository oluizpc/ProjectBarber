package com.projectbarber.projectbarber.service;

import com.projectbarber.projectbarber.model.Servico;
import com.projectbarber.projectbarber.repository.ServicoRepository;

public class ServicoService {
    
    private final ServicoRepository servicoRepository;  

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    // cadastro de servico
    public Servico cadastrarServico(Servico servico) {
        servicoRepository.findByNomeServico(servico.getNomeServico()).ifPresent(s -> {
            throw new IllegalArgumentException("Serviço já cadastrado");
        });
        return servicoRepository.save(servico);}

    // buscar por ID
    public Servico buscarPorId(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado"));     
    }

    // listar todos
    public java.util.List<Servico> listarTodos() {
        return servicoRepository.findAll();     
    }
    //Atualizando servico
    public Servico atualizarServico(Long id, Servico servicoAtualizado) {
        Servico servico = buscarPorId(id);

        servico.setNomeServico(servicoAtualizado.getNomeServico());
        servico.setPreco(servicoAtualizado.getPreco());

        return servicoRepository.save(servico);
    }

    //deletando servico
    public void deletarServico(Long id) {
        Servico servico = buscarPorId(id);
        servicoRepository.delete(servico);
    }  
}

