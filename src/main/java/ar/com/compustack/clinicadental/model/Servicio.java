package ar.com.compustack.clinicadental.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "servicios")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Servicio 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Debes asignar un nombre")
    private String nombre;

    @NotBlank(message = "Debes asignar una descripcion")
    private String descripcion;

    @NotNull(message = "Debes asignar un precio")
    @NumberFormat(pattern = "#,##0.00", style = Style.CURRENCY)
    private double precio;

    @Column(nullable = false, columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private LocalDate createdAt;    
}
