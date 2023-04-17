package ar.com.compustack.clinicadental.controller;

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

import ar.com.compustack.clinicadental.model.Paciente;
import ar.com.compustack.clinicadental.repository.PacienteRepository;


@Controller
@RequestMapping("/pacientes")
public class PacienteController 
{
    @Autowired
    private PacienteRepository pacienteRepository;


    @GetMapping("/")
    public String home(Model model)
    {
        model.addAttribute("pacientes", pacienteRepository.findAll());
        return "public/pacientes";
    }


    // Devuelve datos de una entidad según su id, o una entidad vacia si no se encontró una
    @GetMapping("/get/{id}")
    public @ResponseBody Paciente get(@PathVariable Integer id)
    {
        Optional<Paciente> paciente = pacienteRepository.findById(id);
        return (paciente.isPresent()) ? (paciente.get()) : (new Paciente());
    }


    // Crea/edita datos de una entidad
    @PostMapping("/form")
    public ResponseEntity<?> form(@Valid Paciente paciente, BindingResult result)
    {
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
        pacienteRepository.save(paciente);

        return ResponseEntity.ok().build(); // Retornar un 200 - entidad creada correctamente
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Integer id)
    {
        try {
            pacienteRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return ResponseEntity.ok().build();
    }


    // Autocomplete
    @GetMapping(value = "/findAll/{query}", produces = {"application/json"})
    private @ResponseBody List<Paciente> findAll(@PathVariable String query)
    {
        return pacienteRepository.findBy(query);
    }
}
