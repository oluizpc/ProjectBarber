package com.projectbarber.projectbarber.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projectbarber.projectbarber.model.Servico;


public interface ServicoRepository extends JpaRepository<Servico, Long> {
    Optional<Servico> findByNomeServico (String nomeServico);
    Optional<Servico> findByPreco (String preco);
}
