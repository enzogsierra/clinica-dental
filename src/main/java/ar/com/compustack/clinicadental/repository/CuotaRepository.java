package ar.com.compustack.clinicadental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.com.compustack.clinicadental.model.Cuota;

public interface CuotaRepository extends JpaRepository<Cuota, Integer> {
    
}
