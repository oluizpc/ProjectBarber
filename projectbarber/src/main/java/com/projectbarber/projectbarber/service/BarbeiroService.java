package com.projectbarber.projectbarber.service;

import java.util.List;

import com.projectbarber.projectbarber.model.Barbeiro;
import com.projectbarber.projectbarber.repository.BarbeiroRepository;

import jakarta.validation.Valid;

public class BarbeiroService {
    private final BarbeiroRepository barbeiroRepository;
    
    public BarbeiroService(BarbeiroRepository barbeiroRepository) {
        this.barbeiroRepository = barbeiroRepository;
    }

    // Cadastro de barbeiro
    public Barbeiro cadastrarBarbeiro(@Valid Barbeiro barbeiro) {
        barbeiroRepository.findByEmail(barbeiro.getEmail()).ifPresent(b -> {
            throw new IllegalArgumentException("Email já cadastrado");
        });

        return barbeiroRepository.save(barbeiro);
    }

    //Buscar barbeiro por ID
    public Barbeiro buscarPorId(Long id) {
        return barbeiroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Barbeiro não encontrado"));
    }

    // Listando todos Barbeiros
    public List<Barbeiro> listarTodos() {
        return barbeiroRepository.findAll();
    }

    // Atualizando barbeiro
    public Barbeiro atualizarBarbeiro(Long id, Barbeiro barbeiroAtualizado) {
        Barbeiro barbeiro = buscarPorId(id);

        barbeiro.setNome(barbeiroAtualizado.getNome());
        barbeiro.setEmail(barbeiroAtualizado.getEmail());
        barbeiro.setSenha(null);
        
        return barbeiroRepository.save(barbeiro);
    }

    // Deletando barbeiro
    public void deletarBarbeiro(Long id) {
        Barbeiro barbeiro = buscarPorId(id);
        barbeiroRepository.delete(barbeiro);
    }
}    



