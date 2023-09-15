package ar.com.compustack.clinicadental.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ar.com.compustack.clinicadental.model.Pago;
import ar.com.compustack.clinicadental.model.Turno;
import ar.com.compustack.clinicadental.repository.PagoRepository;
import ar.com.compustack.clinicadental.repository.TurnoRepository;


@Controller
public class HomeController 
{
    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private PagoRepository pagoRepository;


    @GetMapping("")
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


    // Archivar turno
    @PostMapping("/finishDate")
    public ResponseEntity<?> finishDate(@RequestParam Integer id)
    {
        Optional<Turno> opt = turnoRepository.findById(id);

        // El turno no existe
        if(!opt.isPresent()) 
        {
            return ResponseEntity.badRequest().build(); // Devolver un codigo de error
        }

        // El turno existe
        Turno turno = opt.get();
        turno.setCompletado(true);
        turnoRepository.save(turno);

        return ResponseEntity.ok().build();
    }

    // Rellenar formulario del pago de un turno
    // @GetMapping("/getTurnoPago/{id}")
    // public @ResponseBody TurnoPagoDTO getTurnoPago(@PathVariable Integer id)
    // {
    //     Turno turno = turnoRepository.findById(id).get();

    //     TurnoPagoDTO turnoPagoDTO = new TurnoPagoDTO();
    //     turnoPagoDTO.setTurnoFecha(turno.getFecha());
    //     turnoPagoDTO.setTurnoHora(turno.getHora());
    //     turnoPagoDTO.setPacienteNombre(turno.getPaciente().getNombre() + ' ' + turno.getPaciente().getApellido());
    //     turnoPagoDTO.setDoctorNombre(turno.getDoctor().getNombre() + ' ' + turno.getDoctor().getApellido());
    //     turnoPagoDTO.setTurnoTratamiento((turno.getTratamiento() != null) ? turno.getTratamiento().getNombre() : "");

    //     turnoPagoDTO.setTurno(id);
    //     turnoPagoDTO.setPaciente(turno.getPaciente().getId());
    //     turnoPagoDTO.setMonto((turno.getTratamiento() != null) ? turno.getTratamiento().getPrecio() : 0.0);
    //     return turnoPagoDTO;
    // }

    // Registrar pago de un turno
    @PostMapping("/pagarTurno")
    public ResponseEntity<?> pagarTurno(@Valid Pago pago, BindingResult result)
    {
        // Verificar errores de validacion
        if(result.hasErrors())
        {
            Map<String, String> errors = new HashMap<>(); // Esta variable almacenara todos los erroes en forma de map
            result.getFieldErrors().forEach(error -> // Recorrer todos los errores
            {
                errors.put(error.getField(), error.getDefaultMessage()); // Añadir errores al map
            });
            return ResponseEntity.badRequest().body(errors); // Devolver con un status 400 junto con los mensajes de validacion
        }

        // Validacion correcta
        pagoRepository.save(pago);

        if(pago.getTurno() != null) // Verificar si el pago tiene un turno asociado
        {
            Integer turnoId = pago.getTurno().getId();

            Turno turno = turnoRepository.findById(turnoId).get(); // Obtener el turno
            turno.setPago(pago); // Relacionar el pago al turno
            turno.setCompletado(true);
            turnoRepository.save(turno); // Guardar informacion del turno
        }
        
        return ResponseEntity.ok().build(); // Retornar un status 200 - entidad creada correctamente
    }
}
