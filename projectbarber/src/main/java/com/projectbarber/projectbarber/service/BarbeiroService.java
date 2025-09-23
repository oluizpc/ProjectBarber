package com.projectbarber.projectbarber.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projectbarber.projectbarber.exception.BadRequestException;
import com.projectbarber.projectbarber.model.Barbeiro;
import com.projectbarber.projectbarber.repository.BarbeiroRepository;

import jakarta.validation.Valid;

@Service
public class BarbeiroService {
    private final BarbeiroRepository barbeiroRepository;
    
    public BarbeiroService(BarbeiroRepository barbeiroRepository) {
        this.barbeiroRepository = barbeiroRepository;
    }

    // Cadastro de barbeiro
    public Barbeiro cadastrarBarbeiro(@Valid Barbeiro barbeiro) {
        barbeiroRepository.findByEmail(barbeiro.getEmail()).ifPresent(b -> {
            throw new BadRequestException("Email já cadastrado");
        });

        return barbeiroRepository.save(barbeiro);
    }

    //Buscar barbeiro por ID
    public Barbeiro buscarPorId(Integer id) {
        return barbeiroRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Barbeiro não encontrado"));
    }

    // Listando todos Barbeiros
    public List<Barbeiro> listarTodos() {
        return barbeiroRepository.findAll();
    }

    // Atualizando barbeiro
    public Barbeiro atualizarBarbeiro(Integer id, Barbeiro barbeiroAtualizado) {
        Barbeiro barbeiro = buscarPorId(id);

        barbeiroRepository.findByEmail(barbeiroAtualizado.getEmail()).ifPresent(b -> {
            if (!b.getId().equals(id)) {
                throw new BadRequestException("Email já cadastrado");
            }
        }); 

        barbeiro.setNome(barbeiroAtualizado.getNome());
        barbeiro.setEmail(barbeiroAtualizado.getEmail());
        barbeiro.setSenha(barbeiroAtualizado.getSenha());
        barbeiro.setInicioExpediente(barbeiroAtualizado.getInicioExpediente());
        barbeiro.setFimExpediente(barbeiroAtualizado.getFimExpediente());
        barbeiro.setInicioAlmoco(barbeiroAtualizado.getInicioAlmoco());
        barbeiro.setFimAlmoco(barbeiroAtualizado.getFimAlmoco());
        
        return barbeiroRepository.save(barbeiro);
    }

    // Deletando barbeiro
    public void deletarBarbeiro(Integer id) {
        Barbeiro barbeiro = buscarPorId(id);
        barbeiroRepository.delete(barbeiro);
    }
}    



