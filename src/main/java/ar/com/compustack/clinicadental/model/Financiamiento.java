package ar.com.compustack.clinicadental.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "financiamientos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Financiamiento 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Valor del financiamiento
    @NotNull(message = "Debes asignar el valor del financiamiento")
    @Min(value = 0, message = "El valor del financiamiento no puede ser menor a $0")
    @NumberFormat(pattern = "#,##0.00", style = Style.CURRENCY)
    private Double valor;

    // Interés mensual del financiamiento
    @NotNull(message = "Debes asignar el valor del financiamiento")
    @Min(value = 0, message = "El el interés mensual no puede ser menor a 0%")
    private Double interesMensual;

    @NotNull(message = "Debes indicar la fecha de inicio")
    @Column(columnDefinition = "DATE")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaInicio;

    @NotNull(message = "Debes indicar la cantidad de cuotas")
    @Min(value = 2, message = "La cantidad de cuotas no puede ser menor a 2")
    @Max(value = 24, message = "La cantidad de cuotas no puede ser mayor a 24")
    private Integer cantidadCuotas;

    // Lista de cuotas
    @OneToMany(mappedBy = "financiamiento", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("nroCuota ASC")
    private List<Cuota> cuotas = new ArrayList<>();

    @NotNull(message = "Debes asignar el paciente que recibe el financiamiento")
    @ManyToOne(fetch = FetchType.EAGER)
    private Paciente paciente;

    @NotNull(message = "Debes especificar el tratamiento que quieres financiar")
    @ManyToOne(fetch = FetchType.EAGER)
    private Tratamiento tratamiento;

    private String detalles;
    
    @CreationTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime createdAt;


    // Verifica si el financiamiento está completamente pagado (todas sus cuotas fueron pagadas)
    public boolean isPaymentComplete()
    {
        for(Cuota cuota: this.getCuotas())
        {
            if(cuota.getPago() == null) return false;
        }
        return true;
    }

    // Retorna la cantidad de cuotas que faltan por pagar
    public int getRemainingPayments()
    {
        int count = this.cuotas.size();

        for(Cuota cuota: this.getCuotas())
        {
            if(cuota.getPago() == null) count --;
        }
        return count;
    }

    // Retorna la fecha de la última cuota pagada
    public LocalDate getLastPayment()
    {
        LocalDate lastPay = null;

        for(Cuota cuota: this.getCuotas())
        {
            if(cuota.getPago() != null && cuota.getPago().getFechaPago().isAfter(lastPay)) lastPay = cuota.getPago().getFechaPago();
        }
        return lastPay;
    }

    // Obtiene el costo total del financiamiento
    public Double getFinalCost()
    {
        Double sum = 0.0;
        for(Cuota cuota: this.getCuotas()) {
            sum += cuota.getMonto();
        }
        return sum;
    }
}
