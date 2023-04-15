package ar.com.compustack.clinicadental.model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "citas")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Cita 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Debes seleccionar una fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @NotNull(message = "Debes seleccionar una hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Paciente paciente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor doctor;

    private String observaciones;
    
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
