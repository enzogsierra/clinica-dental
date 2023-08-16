package ar.com.compustack.clinicadental.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TurnoPagoDTO 
{
    private LocalDate turnoFecha;
    private LocalTime turnoHora;
    private String turnoTratamiento;
    private String pacienteNombre;
    private String doctorNombre;

    //
    private Integer turno;
    private Integer paciente;
    private Double monto;
}
