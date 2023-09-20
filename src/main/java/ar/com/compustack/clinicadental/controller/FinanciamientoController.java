package ar.com.compustack.clinicadental.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.compustack.clinicadental.model.Cuota;
import ar.com.compustack.clinicadental.model.Financiamiento;
import ar.com.compustack.clinicadental.repository.FinanciamientoRepository;
import ar.com.compustack.clinicadental.repository.TratamientoRepository;


@Controller
@RequestMapping("/financiamientos")
public class FinanciamientoController 
{
    @Autowired
    private FinanciamientoRepository financiamientoRepository;

    @Autowired
    private TratamientoRepository tratamientoRepository;


    @GetMapping("")
    public String index(Model model)
    {
        List<Financiamiento> financiamientos = financiamientoRepository.findAll();

        // Filtrar financiamientos, obtener solo los que están activos (sin pagar por completo)
        for(Financiamiento financiamiento: financiamientos)
        {
            if(financiamiento.isPaymentComplete()) // Verificar si el financiamiento está completamente pagado
            {
                financiamientos.remove(financiamiento); // Quitar el financiamiento de la lista de financiamientos activos
            }
        }

        model.addAttribute("financiamientos", financiamientos);
        return "public/financiamientos";
    }

    @GetMapping("/{id}")
    public String financiamiento(Model model, @PathVariable Integer id)
    {
        Optional<Financiamiento> tmp = financiamientoRepository.findById(id);
        if(!tmp.isPresent()) return "redirect:/financiamientos"; // Si no se encontró un financiamiento, simplemente redirigir a la vista de financiamientos

        Financiamiento financiamiento = tmp.get();

        model.addAttribute("financiamiento", financiamiento);
        model.addAttribute("tratamientos", tratamientoRepository.findAll());
        model.addAttribute("todayDateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:ss")));
        return "public/financiamientoForm";
    }

    // Crear un nuevo financiamiento
    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) String id)
    {
        Financiamiento financiamiento = null;

        if(id == null) // Nuevo financiamiento
        {
            financiamiento = new Financiamiento();
            financiamiento.setFechaInicio(LocalDate.now());
        }
        else // Editando financiamiento
        {
            Optional<Financiamiento> opt = financiamientoRepository.findById(Integer.parseInt(id)); // Buscar entidad
            if(opt.isPresent()) financiamiento = opt.get(); // Cargar datos de la entidad
            else return "redirect:/financiamientos"; // Si no se encontro una entidad, redirigir a la vista de financiamientos
        }
        
        model.addAttribute("financiamiento", financiamiento);
        model.addAttribute("tratamientos", tratamientoRepository.findAll());
        return "public/financiamientoForm";
    }

    @PostMapping("/generarCuotas")
    public String generarCuotas(@Valid Financiamiento financiamiento, BindingResult result, Model model)
    {
        // Verificar datos
        if(result.hasErrors())
        {
            model.addAttribute("tratamientos", tratamientoRepository.findAll());
            return "public/financiamientoForm";
        }

        // Calcular el valor de las cuotas
        Integer cantidadCuotas = financiamiento.getCantidadCuotas();
        Double valor = financiamiento.getValor();
        Double interesMensual = financiamiento.getInteresMensual();
        Double cuotaMonto = (valor * (interesMensual / 100.0)) / (1 - Math.pow(1 + (interesMensual / 100.0), -cantidadCuotas)); // Calculamos el valor de cada cuota
        Double cuotaMontoRound = cuotaMonto.intValue() * 1.0; // Redondeamos el valor de cada cuota, para que no tenga decimales

        List<Cuota> cuotas = new ArrayList<>(); // Lista de cuotas
        for(int i = 0; i < cantidadCuotas; i++) // Iteramos segun la cantidad de cuotas seleccionada
        {
            Cuota cuota = new Cuota(); // Creamos una cuota
            cuota.setNroCuota(i + 1);
            cuota.setMonto(cuotaMontoRound);
            cuota.setFechaCobro(financiamiento.getFechaInicio().plusMonths(i));
            cuotas.add(cuota); // Añadimos la cuota a la lista de cuotas del financiamiento
        }
        financiamiento.setCuotas(cuotas); // Establecemos la lista de cuotas al financiamiento

        model.addAttribute("tratamientos", tratamientoRepository.findAll());
        return "public/financiamientoForm";
    }

    
    // Obtener datos de una entidad
    @GetMapping("/get/{id}")
    public @ResponseBody Financiamiento get(@PathVariable Integer id)
    {
        Optional<Financiamiento> financiamiento = financiamientoRepository.findById(id);
        return (financiamiento.isPresent()) ? (financiamiento.get()) : (new Financiamiento());
    }

    // Crea/edita datos de una entidad
    @PostMapping("/form")
    public String form(@Valid Financiamiento financiamiento, BindingResult result, Model model)
    {
        // Verificar errores de validación
        if(result.hasErrors())
        {
            System.out.println(result.getAllErrors().toString());
            model.addAttribute("tratamientos", tratamientoRepository.findAll());
            return "public/financiamientoForm";
        }

        if(financiamiento.getId() == null) // Verificar si la entidad aun no existe
        {
            financiamientoRepository.save(financiamiento); // Guardar la entidad, así también se actualizará su id

            // Actualizar el financiamiento_id en las cuotas
            for(Cuota cuota: financiamiento.getCuotas()) // Iterar sobre todas las cuotas
            {
                cuota.setFinanciamiento(financiamiento); // Establecer el financiamiento_id en cada cuota (con la id del financiamiento que se acaba de crear)
            }
        }        

        financiamientoRepository.save(financiamiento); // Guardar la informacion de la entidad
        return "redirect:/financiamientos";
    }
}