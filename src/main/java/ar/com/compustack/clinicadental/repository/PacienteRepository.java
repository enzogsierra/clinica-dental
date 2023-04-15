package ar.com.compustack.clinicadental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.com.compustack.clinicadental.model.Paciente;


public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    
}
