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

import ar.com.compustack.clinicadental.model.Tratamiento;
import ar.com.compustack.clinicadental.repository.TratamientoRepository;


@Controller
@RequestMapping("/tratamientos")
public class TratamientoController 
{
    @Autowired
    private TratamientoRepository tratamientoRepository;


    @GetMapping("")
    public String home(Model model)
    {
        model.addAttribute("tratamientos", tratamientoRepository.findAll());
        return "public/tratamientos";
    }


    // Devuelve datos de una entidad según su id, o una entidad vacia si no se encontró una
    @GetMapping("/get/{id}")
    public @ResponseBody Tratamiento get(@PathVariable Integer id)
    {
        Optional<Tratamiento> tratamiento = tratamientoRepository.findById(id);
        return (tratamiento.isPresent()) ? (tratamiento.get()) : (new Tratamiento());
    }


    // Crea/edita datos de una entidad
    @PostMapping("/form")
    public ResponseEntity<?> form(@Valid Tratamiento tratamiento, BindingResult result)
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
        if(tratamiento.getId() != null) // Verificar que es una entidad que está siendo editada
        {
            Optional<Tratamiento> tmp = tratamientoRepository.findById(tratamiento.getId()); // Buscar la entidad
            if(tmp.isPresent() && Double.compare(tmp.get().getPrecio(), tratamiento.getPrecio()) != 0) // Verificar si se encontró a la entidad y si está cambiando el precio
            {
                tratamiento.setPrecioLastUpdate(LocalDate.now()); // Establecer la fecha cuando se cambió el precio del tratamiento
            }
        }

        tratamientoRepository.save(tratamiento);

        return ResponseEntity.ok().build(); // Retornar un 200 - entidad creada correctamente
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Integer id)
    {
        try {
            tratamientoRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return ResponseEntity.ok().build();
    }
}
