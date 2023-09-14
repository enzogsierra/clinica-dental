package ar.com.compustack.clinicadental.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cuotas")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Cuota 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Debes asignar el N° de esta cuota")
    @Min(value = 1, message = "El N° de cuota no puede ser menor a 1")
    private Integer nroCuota;

    @Column(columnDefinition = "DATE")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaCobro;

    @NotNull(message = "Debes asignar el monto de esta cuota")
    @NumberFormat(pattern = "#,##0.00", style = Style.CURRENCY)
    @Min(value = 0, message = "El monto de esta cuota no puede ser menor a $0")
    private Double monto;

    @OneToOne(fetch = FetchType.EAGER)
    @JsonManagedReference
    private Pago pago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financiamiento_id", referencedColumnName = "id")
    private Financiamiento financiamiento;
}
