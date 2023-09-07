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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.compustack.clinicadental.model.Pago;
import ar.com.compustack.clinicadental.model.Turno;
import ar.com.compustack.clinicadental.repository.PagoRepository;
import ar.com.compustack.clinicadental.repository.TurnoRepository;


@Controller
@RequestMapping("/pagos")
public class PagoController 
{
    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private TurnoRepository turnoRepository;


    @GetMapping("/")
    public String home(Model model)
    {
        LocalDate today = LocalDate.now();

        model.addAttribute("pagos", pagoRepository.getPagosFromLast30Days());
        model.addAttribute("todayDate", today);
        return "public/pagos";
    }


    // Devuelve datos de una entidad según su id, o una entidad vacia si no se encontró una
    @GetMapping("/get/{id}")
    public @ResponseBody Pago get(@PathVariable Integer id)
    {
        Optional<Pago> pago = pagoRepository.findById(id);
        return (pago.isPresent()) ? (pago.get()) : (new Pago());
    }

    // Crea/edita datos de una entidad
    @PostMapping("/form")
    public ResponseEntity<?> form(@Valid Pago pago, BindingResult result)
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

        // Relacionar el turno con el pago
        Integer turnoId = (pago.getTurno() != null) ? pago.getTurno().getId() : null; // Obtener el turno asociado al pago
        if(turnoId != null) // El pago tiene un turno asociado
        {
            Turno turno = turnoRepository.findById(turnoId).get(); // Obtener la entidad de turno
            turno.setPago(pago); // Asociar el turno con el pago
            turno.setCompletado(true);
            turnoRepository.save(turno); // Guardar entidad del turno
        }

        return ResponseEntity.ok().build(); // Retornar un status 200 - entidad creada correctamente
    }

    // Eliminar una entidad
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Integer id)
    {
        try {
            pagoRepository.deleteById(id);
        } catch(Exception e) {
            System.out.println("Error: " + e);
        }
        return ResponseEntity.ok().build();
    }
}
