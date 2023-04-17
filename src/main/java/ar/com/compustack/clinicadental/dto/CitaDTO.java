package ar.com.compustack.clinicadental.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class CitaDTO 
{
    private Integer id;
    private Date fecha;
    private Date hora;
    private Integer paciente;
    private String pacienteNombre;
    private Integer doctor;
    private String doctorNombre;
    private String observaciones;
    private LocalDateTime createdAt;
}
