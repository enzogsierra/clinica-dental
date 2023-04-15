package ar.com.compustack.clinicadental.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ar.com.compustack.clinicadental.model.Usuario;
import ar.com.compustack.clinicadental.repository.UsuarioRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService 
{
    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
    {
        Optional<Usuario> opt = usuarioRepository.findByUsername(username);

        if(opt.isPresent()) // Username valido
        {
            Usuario usuario = opt.get();

            // Crear roles
            List<GrantedAuthority> roleList = new ArrayList<>(); // Crear lista de roles. En este sistema, los usuarios solo pueden tener 1 rol
            GrantedAuthority role = new SimpleGrantedAuthority(usuario.getRole());  // Crear el rol del usuario
            roleList.add(role); // Añadir rol a la lista de roles del usuario

            return new User(usuario.getUsername(), usuario.getPassword(), roleList);
        }

        throw new UsernameNotFoundException("Usuario no encontrado");
    }  
}
