package com.projectbarber.projectbarber.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.projectbarber.projectbarber.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);    
    Optional<Cliente> findByTelefone(String telefone);    

}
