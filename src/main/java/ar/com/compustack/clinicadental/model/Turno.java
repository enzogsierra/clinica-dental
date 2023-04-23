package ar.com.compustack.clinicadental.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "turnos")
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class Turno 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Debes seleccionar una fecha")
    @Column(columnDefinition = "DATE")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;

    @NotNull(message = "Debes seleccionar una hora")
    @Column(columnDefinition = "TIME")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime hora;
    
    @NotNull(message = "Debes seleccionar una paciente")
    @ManyToOne(fetch = FetchType.LAZY)
    private Paciente paciente;
    
    @NotNull(message = "Debes seleccionar un doctor")
    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tratamiento tratamiento;

    private String observaciones;
    
    @Column(columnDefinition = "DATETIME")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;

    
    @PrePersist
    private void defaultValues()
    {
        if(this.createdAt == null) this.createdAt = LocalDateTime.now();
    }
}
