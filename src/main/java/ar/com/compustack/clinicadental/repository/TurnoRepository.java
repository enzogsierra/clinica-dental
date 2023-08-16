package ar.com.compustack.clinicadental.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ar.com.compustack.clinicadental.model.Paciente;
import ar.com.compustack.clinicadental.model.Turno;


public interface TurnoRepository extends JpaRepository<Turno, Integer> 
{
    // Buscar turnos por fecha
    List<Turno> findByFecha(LocalDate fecha);

    // Buscar turnos por fecha y ordenar por hora
    List<Turno> findByFechaOrderByHoraAsc(LocalDate fecha);

    // Busca un turno en una fecha y hora especifica
    Optional<Turno> findByFechaAndHora(LocalDate fecha, LocalTime hora);

    // Buscar turnos de un paciente
    List<Turno> findByPacienteOrderByFechaDesc(Paciente paciente);

    // Devuelve todas los turnos a partir de cierta fecha
    @Query("SELECT turno FROM Turno turno WHERE turno.fecha >= :fecha ORDER BY turno.hora")
    List<Turno> findAllAfterFechaOrderByHoraAsc(LocalDate fecha);

    @Query("SELECT turno FROM Turno turno WHERE turno.fecha >= :from AND turno.fecha <= :to ORDER BY turno.fecha, turno.hora ASC")
    List<Turno> findAllInRangeFecha(LocalDate from, LocalDate to);

    @Query("SELECT turno FROM Turno turno WHERE turno.fecha >= :from AND turno.fecha <= :to GROUP BY turno.fecha ORDER BY turno.fecha ASC")
    List<String> findAllInRangeFechaGroupByFecha(LocalDate from, LocalDate to);

    // Agrupa las fechas de todos los turnos (a partir del día actual) en una lista
    @Query(value = "SELECT fecha FROM turnos WHERE fecha >= CURDATE() GROUP BY fecha ORDER BY fecha ASC", nativeQuery = true)
    List<String> findAllAfterFechaGroupByFecha();

    // Busca turnos segun por MM/yyyy
    @Query("SELECT turno FROM Turno turno WHERE MONTH(fecha) = :month AND YEAR(fecha) = :year ORDER BY fecha, hora")
    List<Turno> findByMonthAndYear(Integer month, Integer year);
}
