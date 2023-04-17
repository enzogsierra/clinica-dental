package ar.com.compustack.clinicadental.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.compustack.clinicadental.dto.CitaDTO;
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
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        //
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<String> fechasList = citaRepository.findAllAfterFechaGroupByFecha(); // Agrupamos las fechas de todas las citas en una lista
        List<LocalDate> fechas = fechasList.stream().map(fecha -> LocalDate.parse(fecha, formatter)).collect(Collectors.toList()); // Convertimos las fechas de String a LocalDate

        model.addAttribute("citas", citaRepository.findAllAfterFechaOrderByHoraAsc(today)); // Obtenemos todas las citas a partir del día actual
        model.addAttribute("fechas", fechas);
        model.addAttribute("doctores", doctorRepository.findAll());
        model.addAttribute("todayDate", today);
        model.addAttribute("tomorrowDate", tomorrow);
        return "public/citas";
    }


    // Devuelve datos de una entidad según su id, o una entidad vacia si no se encontró una
    @GetMapping("/get/{id}")
    public @ResponseBody CitaDTO get(@PathVariable Integer id)
    {
        Optional<Cita> cita = citaRepository.findById(id);
        CitaDTO dto = new CitaDTO();

        if(cita.isPresent())
        {
            Cita data = cita.get();
            dto.setId(data.getId());
            dto.setFecha(data.getFecha());
            dto.setHora(data.getHora());
            dto.setPaciente(data.getPaciente().getId());
            dto.setPacienteNombre(data.getPaciente().getNombre());
            dto.setDoctor(data.getDoctor().getId());
            dto.setDoctorNombre(data.getDoctor().getNombre());
            dto.setObservaciones(data.getObservaciones());
            dto.setCreatedAt(data.getCreatedAt());
        }
        return dto;
    }

    // Crea/edita datos de una entidad
    @PostMapping("/form")
    public ResponseEntity<?> form(@Valid Cita cita, BindingResult result)
    {
        // Verificar que la fecha elegida no sea una fecha pasada
        LocalDate curDate = LocalDate.now(); // Obtenemos el día actual
        if(cita.getFecha().isBefore(curDate)) // Verificamos si la fecha elegida para la cita es pasada a la fecha actual
        {
            result.rejectValue("fecha", "cita.fecha", "No es posible agendar una cita en una fecha pasada");
        }

        // Verificar disponibilidad de la cita
        Optional<Cita> checkDate = citaRepository.findByFechaAndHora(cita.getFecha(), cita.getHora());
        if(checkDate.isPresent() && checkDate.get().getId() != cita.getId()) // La fecha y hora ya está reservada
        {
            result.rejectValue("hora", "cita.hora", "Este horario ya está reservado para otra cita, elige otro");
        }

        // Verificar errores de validacion
        if(result.hasErrors())
        {
            Map<String, String> errors = new HashMap<>(); // Esta variable almacenará los errores en forma de map
            result.getFieldErrors().forEach(error -> // Recorrer todos los errores
            {
                errors.put(error.getField(), error.getDefaultMessage()); // Añadir errores al map
            });
            return ResponseEntity.badRequest().body(errors); // Devolver con un status 400 junto con los mensajes de validaciones
        }

        // Validacion correcta
        citaRepository.save(cita);

        return ResponseEntity.ok().build(); // Retornar un 200 - entidad creada correctamente
    }

    // Elimina una entidad
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Integer id)
    {
        try {
            citaRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return ResponseEntity.ok().build();
    }
}
