package ar.com.compustack.clinicadental.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "tratamientos")
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class Tratamiento 
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
    @Min(value = 0, message = "El precio no puede ser menor a $0")
    private Double precio;

    @Column(columnDefinition = "DATE")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdAt;


    @PrePersist
    private void defaultValues()
    {
        if(this.createdAt == null) this.createdAt = LocalDate.now();
    }
}
