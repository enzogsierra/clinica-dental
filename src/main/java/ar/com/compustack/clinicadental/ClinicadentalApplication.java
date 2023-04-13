package ar.com.compustack.clinicadental;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ar.com.compustack.clinicadental.model.Clinica;
import ar.com.compustack.clinicadental.model.Usuario;
import ar.com.compustack.clinicadental.repository.ClinicaRepository;
import ar.com.compustack.clinicadental.repository.UsuarioRepository;

@SpringBootApplication
public class ClinicadentalApplication 
{
	@Autowired
	private ClinicaRepository clinicaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;


	public static void main(String[] args) {
		SpringApplication.run(ClinicadentalApplication.class, args);
	}


	// Este método se llama luego de que la aplicación carga
	@PostConstruct
	public void init()
	{
		// Datos de la clinica
		Optional<Clinica> opt = clinicaRepository.findById(1);
		if(!opt.isPresent()) // Crear datos por default de la clínica
		{
			Clinica clinica = new Clinica();
			clinica.setNombre("Clínica Dental");
			clinica.setLogoUrl("logo.png");
			clinica.setDireccion("-");
			clinica.setTelefono1("-");
			clinicaRepository.save(clinica);
		}

		// Crear admin
		List<Usuario> usuarios = usuarioRepository.findAll();
		if(usuarios.size() == 0) // First init - significa que es la primera vez que se lanza la aplicación
		{
			// Crearemos un usuario por defecto llamado "admin", contraseña "admin" y con rol de "ADMIN"
			Usuario usuario = new Usuario();
			usuario.setUsername("admin");
			usuario.setPassword(passwordEncoder.encode("admin"));
			usuario.setRole("ADMIN");
			usuarioRepository.save(usuario);
		}
	}
}
