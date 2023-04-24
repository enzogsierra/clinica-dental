package ar.com.compustack.clinicadental.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ar.com.compustack.clinicadental.repository.TurnoRepository;


@Controller
public class PublicController 
{
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
}
