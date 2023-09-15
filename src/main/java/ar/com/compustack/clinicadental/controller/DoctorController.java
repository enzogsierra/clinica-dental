package ar.com.compustack.clinicadental.controller;

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

import ar.com.compustack.clinicadental.model.Doctor;
import ar.com.compustack.clinicadental.repository.DoctorRepository;


@Controller
@RequestMapping("/doctores")
public class DoctorController 
{
    @Autowired
    private DoctorRepository doctorRepository;


    @GetMapping("")
    public String home(Model model)
    {
        model.addAttribute("doctores", doctorRepository.findAll());
        return "public/doctores";
    }


    // Devuelve datos de una entidad según su id, o una entidad vacia si no se encontró una
    @GetMapping("/get/{id}")
    public @ResponseBody Doctor get(@PathVariable Integer id)
    {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        return (doctor.isPresent()) ? (doctor.get()) : (new Doctor());
    }


    // Crea/edita datos de una entidad
    @PostMapping("/form")
    public ResponseEntity<?> form(@Valid Doctor doctor, BindingResult result)
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
        doctorRepository.save(doctor);

        return ResponseEntity.ok().build(); // Retornar un 200 - entidad creada correctamente
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Integer id)
    {
        try {
            doctorRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return ResponseEntity.ok().build();
    }
}
