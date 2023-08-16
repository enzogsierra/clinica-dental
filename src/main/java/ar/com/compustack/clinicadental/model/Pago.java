package ar.com.compustack.clinicadental.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
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
@Table(name = "pagos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class Pago 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Debes asignar el monto del pago")
    @NumberFormat(pattern = "#,##0.00", style = Style.CURRENCY)
    @Min(value = 0, message = "El monto del pago no puede ser menor a $0")
    private Double monto;

    private String metodoPago;

    private String detalles;

    @ManyToOne(fetch = FetchType.LAZY)
    private Paciente paciente;

    @OneToOne(fetch = FetchType.LAZY)
    private Cuota cuota;

    @OneToOne(fetch = FetchType.LAZY)
    private Turno turno;

    @NotNull(message = "Debes elegir la fecha del pago")
    @Column(columnDefinition = "DATE")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaPago;
}
