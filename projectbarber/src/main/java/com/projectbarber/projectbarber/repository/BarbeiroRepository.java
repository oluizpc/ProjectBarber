package com.projectbarber.projectbarber.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.projectbarber.projectbarber.model.Barbeiro;

public interface BarbeiroRepository extends JpaRepository<Barbeiro, Long> {
    Optional<Barbeiro> findByEmail(String email);    
    Optional<Barbeiro> findByNome(String nome);

}
