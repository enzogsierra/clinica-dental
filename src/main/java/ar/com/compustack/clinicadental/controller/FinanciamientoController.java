package ar.com.compustack.clinicadental.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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


    @GetMapping("/")
    public String index()
    {
        return "public/financiamientos";
    }

    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) String id)
    {
        Financiamiento financiamiento = new Financiamiento();
        if(id == null)
        {
            financiamiento.setFechaInicio(LocalDate.now());
        }
        else 
        {
            financiamiento = financiamientoRepository.findById(Integer.parseInt(id)).get();
        }

        model.addAttribute("financiamiento", financiamiento);
        model.addAttribute("tratamientos", tratamientoRepository.findAll());
        return "public/financiamientoForm";
    }
}