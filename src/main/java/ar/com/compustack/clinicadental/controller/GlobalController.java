package ar.com.compustack.clinicadental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import ar.com.compustack.clinicadental.model.Clinica;
import ar.com.compustack.clinicadental.repository.ClinicaRepository;


@ControllerAdvice
public class GlobalController 
{
    @Autowired
    private ClinicaRepository clinicaRepository;

    
    @ModelAttribute
    public void addAttributes(Model model)
    {
        Clinica clinica = clinicaRepository.findById(1).get();
        model.addAttribute("clinica", clinica);
    }
}
