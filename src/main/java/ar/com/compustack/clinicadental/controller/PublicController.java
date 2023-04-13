package ar.com.compustack.clinicadental.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ar.com.compustack.clinicadental.model.Clinica;
import ar.com.compustack.clinicadental.repository.ClinicaRepository;


@Controller
public class PublicController 
{
    @Autowired
    private ClinicaRepository clinicaRepository;

    @GetMapping("/")
    public String home(Model model)
    {
        return "public/home";
    }

    @GetMapping("/configuracion")
    public String configuration(Model model)
    {
        return "public/configuracion";
    }

    @PostMapping("/configuracion")
    public String configuracionPost(@Valid Clinica clinica, BindingResult result, RedirectAttributes redirect, Model model)
    {
        if(result.hasErrors())
        {
            return "public/configuracion";
        }

        clinicaRepository.save(clinica);
        return "redirect:/configuracion";
    }
}
