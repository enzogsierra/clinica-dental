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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "turnos")
@Getter @Setter
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
    @ManyToOne(fetch = FetchType.EAGER)
    private Paciente paciente;
    
    @NotNull(message = "Debes seleccionar un doctor")
    @ManyToOne(fetch = FetchType.EAGER)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.EAGER)
    private Tratamiento tratamiento;

    @OneToOne(fetch = FetchType.EAGER)
    @JsonManagedReference
    private Pago pago;

    @Column(columnDefinition = "BIT DEFAULT 0")
    private Boolean completado;

    @Size(max = 255, message = "El texto no debe superar los 255 caracteres")
    private String observaciones;
    
    @CreationTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;
}
