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

import ar.com.compustack.clinicadental.model.Doctor;
import ar.com.compustack.clinicadental.repository.DoctorRepository;

@Controller
@RequestMapping("/doctores")
public class DoctorController 
{
    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping("/")
    public String home(Model model)
    {
        model.addAttribute("doctor", new Doctor());
        model.addAttribute("doctores", doctorRepository.findAll());
        return "doctores/home";
    }

    // Nuevo doctor
    @GetMapping("/nuevo")
    public String newPatient(Model model)
    {
        model.addAttribute("doctor", new Doctor());
        return "doctores/form";
    }

    // Editar datos de doctor
    @GetMapping("/editar/{id}")
    public String editPatient(Model model, @PathVariable("id") Integer id)
    {
        Optional<Doctor> opt = doctorRepository.findById(id); // Buscar doctor por id
        if(!opt.isPresent()) // El doctor no existe
        {
            return "redirect:/doctores/";
        }

        // Doctor encontrado
        Doctor doctor = opt.get();
        model.addAttribute("doctor", doctor);
        return "doctores/form";
    }

    // Cuando se envia un form con datos de doctor (creando doctor o editando)
    @PostMapping("/form")
    public String form(@Valid Doctor doctor, BindingResult result, Model model)
    {
        // Validar datos
        if(result.hasErrors())
        {
            return "doctores/form";
        }

        // Datos validos
        doctorRepository.save(doctor);

        // Redirigir a la vista de doctores indicando que la ventana debe cerrarse 
        return "redirect:/doctores/?close=true";
    }

    // Eliminar doctor
    @GetMapping("/eliminar/{id}")
    public String deletePatient(@PathVariable("id") Integer id)
    {
        Optional<Doctor> opt = doctorRepository.findById(id); // Buscar doctor por id
        if(!opt.isPresent()) // Doctor no encontrado
        {
            return "redirect:/doctores/";
        }

        // Doctor encontrado
        Doctor doctor = opt.get();
        doctorRepository.delete(doctor);
        
        return "redirect:/doctores/";
    }
}
