package ar.com.compustack.clinicadental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.com.compustack.clinicadental.model.Doctor;


public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    
}
