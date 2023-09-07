package ar.com.compustack.clinicadental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.com.compustack.clinicadental.model.Financiamiento;


public interface FinanciamientoRepository extends JpaRepository<Financiamiento, Integer> {
    
}
