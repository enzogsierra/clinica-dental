package ar.com.compustack.clinicadental.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ar.com.compustack.clinicadental.model.Cita;


public interface CitaRepository extends JpaRepository<Cita, Integer> 
{
    // Busca una cita en una fecha y hora especifica
    Optional<Cita> findByFechaAndHora(LocalDate fecha, LocalTime hora);

    // Devuelve todas las citas a partir de cierta fecha
    @Query("SELECT cita FROM Cita cita WHERE cita.fecha >= :fecha ORDER BY cita.hora")
    List<Cita> findAllAfterFechaOrderByHoraAsc(LocalDate fecha);

    // Agrupa las fechas de todas las citas (a partir del día actual) en una lista
    @Query(value = "SELECT fecha FROM citas WHERE fecha >= CURDATE() GROUP BY fecha ORDER BY fecha", nativeQuery = true)
    List<String> findAllAfterFechaGroupByFecha();
}
