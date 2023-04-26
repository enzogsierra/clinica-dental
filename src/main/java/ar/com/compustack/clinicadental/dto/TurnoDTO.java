package ar.com.compustack.clinicadental.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class TurnoDTO 
{
    private Integer id;
    private LocalDate fecha;
    private LocalTime hora;
    private Integer paciente;
    private String pacienteNombre;
    private String pacienteTelefono;
    private Integer doctor;
    private String doctorNombre;
    private Integer tratamiento;
    private String tratamientoNombre;
    private String observaciones;
    private LocalDateTime createdAt;
}
