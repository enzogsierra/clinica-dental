package ar.com.compustack.clinicadental.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "dentistas")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Dentista 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Debes asignar un nombre")
    private String nombre;
    
    @NotBlank(message = "Debes asignar un apellido")
    private String apellido;
    
    @NotBlank(message = "Debes especificar la dirección")
    private String direccion;
    
    @NotBlank(message = "Debes especificar un número de teléfono")
    private String telefono;

    @NotBlank(message = "Debes especificar la especialidad")
    private String especialidad;

    @Column(nullable = false, columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private LocalDate createdAt;
}
