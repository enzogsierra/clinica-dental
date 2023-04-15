package ar.com.compustack.clinicadental.controller;

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

import ar.com.compustack.clinicadental.model.Paciente;
import ar.com.compustack.clinicadental.repository.PacienteRepository;


@Controller
@RequestMapping("/pacientes")
public class PacienteController 
{
    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping("/")
    public String home(Model model)
    {
        model.addAttribute("paciente", new Paciente());
        model.addAttribute("pacientes", pacienteRepository.findAll());
        return "pacientes/home";
    }

    // Nuevo paciente
    @GetMapping("/nuevo")
    public String newPatient(Model model)
    {
        model.addAttribute("paciente", new Paciente());
        return "pacientes/form";
    }

    // Editar datos de paciente
    @GetMapping("/editar/{id}")
    public String editPatient(Model model, @PathVariable("id") Integer id)
    {
        Optional<Paciente> opt = pacienteRepository.findById(id); // Buscar paciente por id
        if(!opt.isPresent()) // El paciente no existe
        {
            return "redirect:/pacientes/";
        }

        // Paciente encontrado
        Paciente paciente = opt.get();
        model.addAttribute("paciente", paciente);
        return "pacientes/form";
    }

    // Cuando se envia un form con datos de paciente (creando paciente o editando)
    @PostMapping("/form")
    public String form(@Valid Paciente paciente, BindingResult result, Model model)
    {
        // Validar datos
        if(result.hasErrors())
        {
            return "pacientes/form";
        }

        // Datos validos
        pacienteRepository.save(paciente);

        // Redirigir a la vista de pacientes indicando que la ventana debe cerrarse 
        return "redirect:/pacientes/?close=true";
    }

    // Eliminar paciente
    @GetMapping("/eliminar/{id}")
    public String deletePatient(@PathVariable("id") Integer id)
    {
        Optional<Paciente> opt = pacienteRepository.findById(id); // Buscar paciente por id
        if(!opt.isPresent()) // Paciente no encontrado
        {
            return "redirect:/pacientes/";
        }

        // Paciente encontrado
        Paciente paciente = opt.get();
        pacienteRepository.delete(paciente);
        
        return "redirect:/pacientes/";
    }
}
