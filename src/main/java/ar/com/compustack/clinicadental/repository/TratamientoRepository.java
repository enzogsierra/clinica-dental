package ar.com.compustack.clinicadental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.com.compustack.clinicadental.model.Tratamiento;


public interface TratamientoRepository extends JpaRepository<Tratamiento, Integer> {
    
}
