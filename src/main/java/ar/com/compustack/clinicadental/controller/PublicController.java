package ar.com.compustack.clinicadental.controller;

import java.time.LocalDate;

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
import ar.com.compustack.clinicadental.repository.TurnoRepository;


@Controller
public class PublicController 
{
    @Autowired
    private ClinicaRepository clinicaRepository;

    @Autowired
    private TurnoRepository turnoRepository;


    @GetMapping("/")
    public String home(Model model)
    {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        
        model.addAttribute("turnosToday", turnoRepository.findByFechaOrderByHoraAsc(today));
        model.addAttribute("turnosTomorrow", turnoRepository.findByFechaOrderByHoraAsc(tomorrow));
        model.addAttribute("todayDate", today);
        model.addAttribute("tomorrowDate", tomorrow);
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
