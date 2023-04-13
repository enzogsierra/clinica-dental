package ar.com.compustack.clinicadental.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "usuarios")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Usuario 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Debes ingresar un nombre de usuario")
    private String username;

    @NotBlank(message = "Debes ingresar una contraseña")
    @Size(min = 4, message = "La contraseña debe tener mínimo 4 caracteres")
    private String password;

    private String nombre;

    private String apellido;

    private String telefono;

    @Column(columnDefinition = "VARCHAR(32) DEFAULT 'USER'")
    private String role;
}
