package com.projectbarber.projectbarber.service;

import com.projectbarber.projectbarber.exception.BadRequestException;
import com.projectbarber.projectbarber.model.Cliente;
import com.projectbarber.projectbarber.repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // Cadastro de cliente
    public Cliente cadastrarCliente(@Valid Cliente cliente) {
        clienteRepository.findByEmail(cliente.getEmail()).ifPresent(c -> {
            throw new BadRequestException("Email já cadastrado");
        });

        return clienteRepository.save(cliente);
    }

    // Buscar por ID
    public Cliente buscarPorId(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Cliente não encontrado"));
    }

    // Listando todos
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    // Atualizando cliente
    public Cliente atualizarCliente(Integer id, Cliente clienteAtualizado) {
        Cliente cliente = buscarPorId(id);

        clienteRepository.findByEmail(clienteAtualizado.getEmail()).ifPresent(c -> {
            if (!c.getId().equals(id)) {
                throw new BadRequestException("Email já cadastrado");
            }
        }); 

        cliente.setNome(clienteAtualizado.getNome());
        cliente.setTelefone(clienteAtualizado.getTelefone());
        cliente.setEmail(clienteAtualizado.getEmail());
        cliente.setSenha(clienteAtualizado.getSenha());

        return clienteRepository.save(cliente);
    }

    // Deletando cliente
    public void deletarCliente(Integer id) {
        Cliente cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
    }
}
