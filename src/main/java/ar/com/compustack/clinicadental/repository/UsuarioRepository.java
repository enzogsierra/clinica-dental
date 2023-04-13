package ar.com.compustack.clinicadental.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.com.compustack.clinicadental.model.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);
}
