package ar.com.compustack.clinicadental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.com.compustack.clinicadental.model.Clinica;


public interface ClinicaRepository extends JpaRepository<Clinica, Integer> {
    
}
