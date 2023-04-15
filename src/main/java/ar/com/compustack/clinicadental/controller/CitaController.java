package ar.com.compustack.clinicadental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ar.com.compustack.clinicadental.model.Cita;
import ar.com.compustack.clinicadental.repository.CitaRepository;
import ar.com.compustack.clinicadental.repository.DoctorRepository;
import ar.com.compustack.clinicadental.repository.PacienteRepository;


@Controller
@RequestMapping("/citas")
public class CitaController 
{
    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PacienteRepository pacienteRepository;


    @GetMapping("/")
    public String home(Model model)
    {
        model.addAttribute("citas", citaRepository.findAll());
        model.addAttribute("cita", new Cita());
        model.addAttribute("doctores", doctorRepository.findAll());
        model.addAttribute("pacientes", pacienteRepository.findAll());
        return "citas/home";
    }
}
