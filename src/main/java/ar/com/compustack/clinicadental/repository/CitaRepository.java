package ar.com.compustack.clinicadental.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.com.compustack.clinicadental.model.Cita;


public interface CitaRepository extends JpaRepository<Cita, Integer> 
{
    Optional<Cita> findByFechaAndHora(Date fecha, Date hora);
    List<Cita> findAllByFechaAfterOrderByFechaAsc(Date fecha);
}
