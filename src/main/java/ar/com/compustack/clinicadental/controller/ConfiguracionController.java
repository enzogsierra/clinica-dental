package ar.com.compustack.clinicadental.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import ar.com.compustack.clinicadental.model.Clinica;
import ar.com.compustack.clinicadental.repository.ClinicaRepository;
import ar.com.compustack.clinicadental.service.FileService;


@Controller
@RequestMapping("/configuracion")
public class ConfiguracionController 
{
    @Autowired
    private ClinicaRepository clinicaRepository;

    @Autowired
    private FileService fileService;

    
    @GetMapping("/")
    public String configuration(Model model)
    {
        return "public/configuracion";
    }

    @PostMapping("/form")
    public String configurationPost(@Valid Clinica clinica, BindingResult result, MultipartFile logoFile, Model model) throws IOException
    {
        // Verificar errores de formulario
        if(result.hasErrors())
        {
            return "public/configuracion";
        }

        // Verificar archivo de imagen
        if(!logoFile.isEmpty()) // Se esta subiendo un nuevo logo
        {
            if(!clinica.getLogoUrl().isEmpty()) // Existe un logo anterior
            {
                try {
                    fileService.delete(clinica.getLogoUrl()); // Eliminar logo anterior
                }
                catch(IOException e) {
                    System.out.println("Error:" + e);
                }
            }
                
            String filename = fileService.save(logoFile, "logo"); // Guardar nuevo logo con el nombre "logo" - retorna el nombre "logo" + extension
            clinica.setLogoUrl(filename); // Establecer el nombre del archivo
        }
        
        clinicaRepository.save(clinica);
        return "redirect:/configuracion/";
    }
}
