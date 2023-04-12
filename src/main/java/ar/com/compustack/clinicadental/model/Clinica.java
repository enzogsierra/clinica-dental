package ar.com.compustack.clinicadental.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Clinica 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Debes asignar un nombre a la clínica")
    private String nombre;

    @NotBlank(message = "Debes asignar un número de teléfono")
    private String telefono;

    private String direccion;

    private String eslogan;

    //
    private static Clinica clinica;

    private Clinica() {
    }

    public static Clinica getInstance() 
    {
        if(clinica == null) clinica = new Clinica();

        return clinica;
    }
}
