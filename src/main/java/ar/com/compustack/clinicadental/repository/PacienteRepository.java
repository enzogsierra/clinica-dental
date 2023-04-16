package ar.com.compustack.clinicadental.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ar.com.compustack.clinicadental.model.Paciente;


public interface PacienteRepository extends JpaRepository<Paciente, Integer> 
{
    @Query("SELECT paciente FROM Paciente paciente WHERE paciente.nombre LIKE %:query% OR paciente.apellido LIKE %:query% OR paciente.telefono LIKE %:query%")
    List<Paciente> findBy(@Param("query") String query);
}
