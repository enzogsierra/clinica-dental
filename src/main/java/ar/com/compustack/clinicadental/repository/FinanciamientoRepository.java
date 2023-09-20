package ar.com.compustack.clinicadental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.com.compustack.clinicadental.model.Financiamiento;
import ar.com.compustack.clinicadental.model.Paciente;

import java.util.List;



public interface FinanciamientoRepository extends JpaRepository<Financiamiento, Integer> 
{
    List<Financiamiento> findByPaciente(Paciente paciente);
}
