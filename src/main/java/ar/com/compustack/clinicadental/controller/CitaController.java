package ar.com.compustack.clinicadental.controller;

import java.util.Date;
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

import ar.com.compustack.clinicadental.model.Cita;
import ar.com.compustack.clinicadental.repository.CitaRepository;
import ar.com.compustack.clinicadental.repository.DoctorRepository;


@Controller
@RequestMapping("/citas")
public class CitaController 
{
    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private DoctorRepository doctorRepository;


    @GetMapping("/")
    public String home(Model model)
    {
        Date yesterday = new Date(System.currentTimeMillis() - (24 * 3600000));
        Date today = new Date(System.currentTimeMillis());
        Date tomorrow = new Date(System.currentTimeMillis() + (24 * 3600000));

        model.addAttribute("citas", citaRepository.findAllByFechaAfterOrderByFechaAsc(yesterday));
        model.addAttribute("todayDate", today);
        model.addAttribute("tomorrowDate", tomorrow);
        return "citas/home";
    }

    // Nueva cita
    @GetMapping("/nueva")
    public String newDate(Model model)
    {
        model.addAttribute("cita", new Cita());
        model.addAttribute("doctores", doctorRepository.findAll());
        return "citas/form";
    }

    // Editar datos de cita
    @GetMapping("/editar/{id}")
    public String editDate(Model model, @PathVariable("id") Integer id)
    {
        Optional<Cita> opt = citaRepository.findById(id); // Buscar cita por id
        if(!opt.isPresent()) // El cita no existe
        {
            return "redirect:/citas/";
        }

        // Cita encontrada
        Cita cita = opt.get();

        model.addAttribute("cita", cita);
        model.addAttribute("doctores", doctorRepository.findAll());
        return "citas/form";
    }


    // Cuando se envia un form con datos de cita (creando una cita o editando)
    @PostMapping("/form")
    public String form(@Valid Cita cita, BindingResult result, Model model)
    {
        // Verificar disponibilidad de la cita
        Optional<Cita> checkDate = citaRepository.findByFechaAndHora(cita.getFecha(), cita.getHora());
        if(checkDate.isPresent() && checkDate.get().getId() != cita.getId()) // La fecha y hora ya está reservada
        {
            result.rejectValue("hora", "cita.hora", "Este horario ya está reservado para otra cita, elige otro");
        }

        // Validar datos
        if(result.hasErrors())
        {
            model.addAttribute("doctores", doctorRepository.findAll());
            return "citas/form";
        }

        // Datos validos
        citaRepository.save(cita);

        // Redirigir a la vista de citas indicando que la ventana debe cerrarse 
        return "redirect:/citas/form?close=true";
    }

    // Eliminar cita
    @GetMapping("/eliminar/{id}")
    public String deleteDate(@PathVariable("id") Integer id)
    {
        Optional<Cita> opt = citaRepository.findById(id); // Buscar cita por id
        if(!opt.isPresent()) // Cita no encontrada
        {
            return "redirect:/citas/";
        }

        // Cita encontrada
        Cita cita = opt.get();
        citaRepository.delete(cita);
        
        return "redirect:/citas/";
    }

    // Uso interno - este endpoint solo se utilizara cuando se necesite cerrar el modal del iframe
    @GetMapping("/form")
    public String _form(Model model)
    {
        model.addAttribute("cita", new Cita());
        model.addAttribute("doctores", null);
        return "citas/form";
    }
}
