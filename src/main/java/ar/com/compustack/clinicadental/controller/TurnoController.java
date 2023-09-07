package ar.com.compustack.clinicadental.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import ar.com.compustack.clinicadental.dto.TurnoDTO;
import ar.com.compustack.clinicadental.model.Doctor;
import ar.com.compustack.clinicadental.model.Turno;
import ar.com.compustack.clinicadental.repository.TurnoRepository;
import ar.com.compustack.clinicadental.repository.DoctorRepository;
import ar.com.compustack.clinicadental.repository.TratamientoRepository;


@Controller
@RequestMapping("/turnos")
public class TurnoController 
{
    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private TratamientoRepository tratamientoRepository;


    @GetMapping("")
    public String home(Model model, @RequestParam(name = "from", defaultValue = "") String pFrom) throws Exception
    {
        LocalDate today = LocalDate.now(); // Fecha actual
        LocalDate from = (pFrom.isBlank()) ? (today) : (LocalDate.parse(pFrom)); // Fecha desde la cual empezara a obtener turnos...
        LocalDate to = from.plusDays(6); // ...hasta esta fecha

        List<LocalDate> semana = new ArrayList<>();
        for(int i = 0; from.plusDays(i - 1).isBefore(to); i++)
        {
            semana.add(from.plusDays(i));
        }

        LocalTime apertura = LocalTime.of(8, 0, 0);
        LocalTime cierre = LocalTime.of(22, 0, 0);
        List<LocalTime> horarios = new ArrayList<>();
        while(apertura.isBefore(cierre))
        {
            horarios.add(apertura);
            apertura = apertura.plusMinutes(30);
        }

        model.addAttribute("turnos", turnoRepository.findAllInRangeFecha(from, to));
        model.addAttribute("doctores", doctorRepository.findAll());
        model.addAttribute("tratamientos", tratamientoRepository.findAll());
        
        model.addAttribute("semana", semana);
        model.addAttribute("horarios", horarios);
        model.addAttribute("todayDate", today);
        model.addAttribute("fromDate", from);
        model.addAttribute("toDate", to);
        return "public/turnos";
    }

    @GetMapping("/{id}")
    public String turno(Model model, @PathVariable Integer id)
    {
        Optional<Turno> tmp = turnoRepository.findById(id);
        if(!tmp.isPresent()) return "redirect:/turnos";

        Turno turno = tmp.get();
        LocalDate todayDate = LocalDate.now();

        model.addAttribute("turno", turno);
        model.addAttribute("doctores", doctorRepository.findAll());
        model.addAttribute("tratamientos", tratamientoRepository.findAll());
        model.addAttribute("todayDate", todayDate);
        model.addAttribute("title", "TURNO_" + id);
        return "public/turno";
    }

    @GetMapping("/print/{id}")
    public String printTurno(Model model, @PathVariable Integer id)
    {
        Optional<Turno> tmp = turnoRepository.findById(id);
        if(!tmp.isPresent()) return "redirect:/turnos";

        Turno turno = tmp.get();
        model.addAttribute("turno", turno);
        return "print/turno";
    }

    @GetMapping("/historial")
    public String history(Model model, @RequestParam(name = "month", defaultValue = "") String pMonth, @RequestParam(name = "year", defaultValue = "") String pYear)
    {
        LocalDate today = LocalDate.now();
        Integer month = (pMonth.isBlank()) ? (today.getMonthValue()) : (Integer.parseInt(pMonth));
        Integer year = (pYear.isBlank()) ? (today.getYear()) : (Integer.parseInt(pYear));

        List<Turno> turnos = turnoRepository.findByMonthAndYear(month, year);

        // Crear una lista de fechas que contengan al menos 1 turno
        List<LocalDate> fechas = new ArrayList<>(); // Lista que almacenara las fechas
        turnos.forEach(turno -> // Iteramos en la lista de turnos
        {
            if(!fechas.contains(turno.getFecha())) fechas.add(turno.getFecha()); // Si la fecha del turno no está en la lista de fechas, añadimos la fecha a la lista
        });

        
        model.addAttribute("turnos", turnos);
        model.addAttribute("fechas", fechas);
        model.addAttribute("todayDate", today);
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("dateFormat", LocalDate.of(year, month, 1));
        return "public/turnosHistorial";
    }

    // Devuelve datos de una entidad según su id, o una entidad vacia si no se encontró una
    @GetMapping("/get/{id}")
    public @ResponseBody Turno get(@PathVariable Integer id)
    {
        Optional<Turno> turno = turnoRepository.findById(id);
        return (turno.isPresent()) ? (turno.get()) : (new Turno());
    }


    // Devuelve datos de una entidad según su id, o una entidad vacia si no se encontró una
    // @GetMapping("/get/{id}")
    // public @ResponseBody TurnoDTO get(@PathVariable Integer id)
    // {
    //     Optional<Turno> turno = turnoRepository.findById(id);
    //     TurnoDTO dto = new TurnoDTO();

    //     if(turno.isPresent())
    //     {
    //         Turno data = turno.get();
    //         dto.setId(data.getId());
    //         dto.setFecha(data.getFecha());
    //         dto.setHora(data.getHora());
    //         dto.setPaciente(data.getPaciente().getId());
    //         dto.setPacienteNombre(data.getPaciente().getNombre() + " " + data.getPaciente().getApellido());
    //         dto.setPacienteTelefono(data.getPaciente().getTelefono());
    //         dto.setDoctor(data.getDoctor().getId());
    //         dto.setDoctorNombre(data.getDoctor().getNombre() + " " + data.getDoctor().getApellido());
    //         dto.setTratamiento(data.getTratamiento() == null ? 0 : data.getTratamiento().getId());
    //         dto.setTratamientoNombre(data.getTratamiento() == null ? ("") : data.getTratamiento().getNombre());
    //         dto.setPago(data.getPago() == null ? null : data.getPago().getId());
    //         dto.setCompletado(data.getCompletado());
    //         dto.setObservaciones(data.getObservaciones());
    //         dto.setCreatedAt(data.getCreatedAt());
    //     }
    //     return dto;
    // }

    // Crea/edita datos de una entidad
    @PostMapping("/form")
    public ResponseEntity<?> form(@Valid Turno turno, BindingResult result)
    {
        // Verificar que la fecha elegida no sea una fecha pasada
        LocalDate curDate = LocalDate.now(); // Obtenemos el día actual
        if(turno.getFecha().isBefore(curDate)) // Verificamos si la fecha elegida para el turno es pasada a la fecha actual
        {
            result.rejectValue("fecha", "turno.fecha", "No es posible agendar un turno en una fecha pasada");
        }

        // Verificar disponibilidad del turno
        Optional<Turno> checkDate = turnoRepository.findByFechaAndHora(turno.getFecha(), turno.getHora());
        if(checkDate.isPresent() && checkDate.get().getId() != turno.getId()) // La fecha y hora ya está reservada
        {
            result.rejectValue("hora", "turno.hora", "Este horario ya está reservado para otro turno, elige otro");
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
        turnoRepository.save(turno);

        return ResponseEntity.ok().build(); // Retornar un 200 - entidad creada correctamente
    }

    // Elimina una entidad
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Integer id)
    {
        try {
            turnoRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return ResponseEntity.ok().build();
    }


    // @GetMapping("/pdf")
    // public String pdf() throws InterruptedException
    // {
    //     // Pasamos el archivo html que queremos procesar 
    //     Context context = new Context(); // Variables que existiran en el contexto del html
    //     context.setVariable("clinica", clinicaRepository.findById(1).get()); // Añadimos la variable "clinica" al contexto
    //     String html = templateEngine.process("facturas/turno", context); // Procesamos el archivo html y lo devolvemos como un string

    //     try 
    //     {
    //         final String filename = "/facturas/turno.pdf"; // Direccion donde se guardará el pdf (src/main/resources/static)
    //         PdfService.generate(html, filename); // Generamos el archivo pdf usando el html como plantilla
    //     } 
    //     catch (Exception e) 
    //     {
    //         System.out.println("Error: " + e);
    //     }

    //     return "redirect:/facturas/turno.pdf";
    // }


    // @PostMapping("/generarFactura")
    // public ResponseEntity<?> generateInvoice(@RequestParam Integer id)
    // {
    //     // Crea las variables que existiran en la vista que queremos renderizar (es como model.addAttribute)
    //     Context context = new Context();
    //     context.setVariable("turnos", turnoRepository.findAll());

    //     // Pasamos el archivo html que queremos procesar 
    //     String html = templateEngine.process("facturas/turno", context);

    //     Map<String, Object> response = new HashMap<>();
    //     try 
    //     {
    //         final String filename = "/facturas/turno.pdf"; // Direccion donde se guardará el pdf (src/main/resources/static)
    //         PdfService.generate(html, filename); // Generamos el archivo pdf usando el html como plantilla

    //         response.put("filename", filename); // Devolvemos el nombre del archivo que se generó en la respuesta del controlador
    //     } 
    //     catch (Exception e) 
    //     {
    //         System.out.println("Error: " + e);

    //         response.put("error", e.getMessage()); // Devolvemos el error que se produció
    //         return ResponseEntity.badRequest().body(response); // Respondemos con un error 400
    //     }

    //     return ResponseEntity.ok().body(response); // Solicitud correcta
    // }
}
