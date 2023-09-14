package ar.com.compustack.clinicadental.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Clinica 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Debes asignar un nombre a la clínica")
    private String nombre;

    private String eslogan;

    @NotBlank(message = "Debes asignar un número de teléfono")
    private String telefono1;

    private String telefono2;

    @NotBlank(message = "Debes indicar una dirección")
    private String direccion;

    @Column(columnDefinition = "VARCHAR(32) DEFAULT 'logo.png'")
    private String logoUrl;
}
