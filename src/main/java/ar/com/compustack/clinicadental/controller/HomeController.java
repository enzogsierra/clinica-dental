package ar.com.compustack.clinicadental.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ar.com.compustack.clinicadental.model.Doctor;
import ar.com.compustack.clinicadental.model.Paciente;
import ar.com.compustack.clinicadental.model.Tratamiento;
import ar.com.compustack.clinicadental.model.Turno;
import ar.com.compustack.clinicadental.repository.DoctorRepository;
import ar.com.compustack.clinicadental.repository.PacienteRepository;
import ar.com.compustack.clinicadental.repository.TratamientoRepository;
import ar.com.compustack.clinicadental.repository.TurnoRepository;



@Controller
public class HomeController 
{
    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private TratamientoRepository tratamientoRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PacienteRepository pacienteRepository;
    


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

    //
    @GetMapping("/generateDefaults")
    public String generateDefaults()
    {
        Random rand = new Random();

        // Crear tratamientos
        List<Tratamiento> tratamientos = new ArrayList<>();
        Tratamiento tratamiento = null;

        tratamiento = new Tratamiento(null, "Consulta", null, 4000.0, null, null);
        tratamientos.add(tratamiento);
        tratamiento = new Tratamiento(null, "Extracción de diente/muela", "Precio por cada diente o muela extraida", 9000.0, null, null);
        tratamientos.add(tratamiento);
        tratamiento = new Tratamiento(null, "Limpieza dental inferior", null, 10000.0, null, null);
        tratamientos.add(tratamiento);
        tratamiento = new Tratamiento(null, "Limpieza dental superior", null, 8000.0, null, null);
        tratamientos.add(tratamiento);
        tratamiento = new Tratamiento(null, "Limpieza dental completa", null, 16000.0, null, null);
        tratamientos.add(tratamiento);
        tratamiento = new Tratamiento(null, "Ortodoncia convencional", "Realizar financiamiento a este valor", 150000.0, null, null);
        tratamientos.add(tratamiento);

        for(Tratamiento entity: tratamientos) {
            tratamientoRepository.save(entity);
        }

        // Crear doctores
        List<Doctor> doctores = new ArrayList<>();
        Doctor doctor = null;

        doctor = new Doctor(null, "Lionel", "Scaloni", "3644025420", "Calle Qatar, 200", "Rompecolas", null);
        doctores.add(doctor);
        doctor = new Doctor(null, "Pablo", "Aimar", "3644934124", "Calle Qatar, 200", "Ayudante de Rompecolas", null);
        doctores.add(doctor);

        for(Doctor entity: doctores) {
            doctorRepository.save(entity);
        }


        // Pacientes
        List<Paciente> pacientes = new ArrayList<>();
        Paciente paciente = null;
        LocalDate startDate = LocalDate.of(1994, 1, 1);

        paciente = new Paciente(null, "Lionel", "Messi", "364434215", "Rosario", null, "Alergico al matrimonio", startDate.plusDays(rand.nextInt(365 * 10)), null);
        pacientes.add(paciente);
        paciente = new Paciente(null, "Paulo", "Dybolas", "3644039433", "Calle Campeon y Sexo, 500", null, "Alergico al matrimonio", startDate.plusDays(rand.nextInt(365 * 10)), null);
        pacientes.add(paciente);
        paciente = new Paciente(null, "Enzo", "Fernández", "3644340423", "Calle Cince, 100", null, "Alergico al matrimonio", startDate.plusDays(rand.nextInt(365 * 10)), null);
        pacientes.add(paciente);
        paciente = new Paciente(null, "Lisando", "Paredes", "3644123094", "Calle Construccion, 600", null, "Alergico al matrimonio", startDate.plusDays(rand.nextInt(365 * 10)), null);
        pacientes.add(paciente);
        paciente = new Paciente(null, "Julian", "Alvarez", "3644493032", "B° Silencio e Incomodidad", null, "Alergico al matrimonio", startDate.plusDays(rand.nextInt(365 * 10)), null);
        pacientes.add(paciente);
        paciente = new Paciente(null, "Franco", "Armanco", "3644020542", "Av. Sin Manos, 340", null, "Alergico al matrimonio", startDate.plusDays(rand.nextInt(365 * 10)), null);
        pacientes.add(paciente);

        for(Paciente entity: pacientes) {
            pacienteRepository.save(entity);
        }

        // Turnos
        startDate = LocalDate.now();

        for(int i = 0; i < 400; i++)
        {
            Turno turno = new Turno();
            turno.setDoctor(doctores.get(rand.nextInt(doctores.size())));
            turno.setFecha(startDate.plusDays(-30 + rand.nextInt(60)));
            turno.setHora(LocalTime.of(8 + rand.nextInt(10), 30 * rand.nextInt(2)));
            turno.setPaciente(pacientes.get(rand.nextInt(pacientes.size())));
            turno.setTratamiento(tratamientos.get(rand.nextInt(tratamientos.size())));
            turnoRepository.save(turno);
        }

        return "redirect:/";
    }
}
    
    

