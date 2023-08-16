package ar.com.compustack.clinicadental.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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

    private String descripcion;

    @ManyToOne(fetch = FetchType.EAGER)
    private Paciente paciente;

    @OneToMany(mappedBy = "financiamiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cuota> cuotas = new ArrayList<>();
}
