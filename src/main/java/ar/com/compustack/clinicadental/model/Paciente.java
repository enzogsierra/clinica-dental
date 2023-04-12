package ar.com.compustack.clinicadental.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "pacientes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Paciente 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Debes asignar un nombre")
    private String nombre;
    
    @NotBlank(message = "Debes asignar un apellido")
    private String apellido;

    @NotBlank(message = "Debes especificar un número de teléfono")
    private String telefono;

    @NotBlank(message = "Debes especificar una dirección")
    private String direccion;

    private String historialMedico;

    private String alergias;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaNacimiento;
    
    @Column(nullable = false, columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private LocalDate createdAt;
}
