package com.microservicios.msmedicos.repository;

import com.microservicios.msmedicos.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    
    Optional<Medico> findByEmail(String email);
    
    Optional<Medico> findByRut(String rut);
    
    boolean existsByEmail(String email);
    
    boolean existsByRut(String rut);
    
}
