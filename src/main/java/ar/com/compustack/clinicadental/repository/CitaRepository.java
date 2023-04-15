package ar.com.compustack.clinicadental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.com.compustack.clinicadental.model.Cita;


public interface CitaRepository extends JpaRepository<Cita, Integer> {
    
}
