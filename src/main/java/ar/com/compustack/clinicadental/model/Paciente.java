package ar.com.compustack.clinicadental.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

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

    private String direccion;

    private String historialMedico;

    private String alergias;

    @NotNull(message = "Debes indicar una fecha de nacimiento")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaNacimiento;
    
    @Column(columnDefinition = "DATE")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdAt;


    @PrePersist
    private void defaultValues()
    {
        if(this.createdAt == null) this.createdAt = LocalDate.now();
    }
}
