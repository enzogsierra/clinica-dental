package ar.com.compustack.clinicadental.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig 
{
    @Bean
    static BCryptPasswordEncoder getEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth ->
            {
                auth.antMatchers("/admin/**").hasAuthority("ADMIN");
                auth.antMatchers("/assets/**").permitAll(); // Permitir que se muestren todos los recursos - evita falsos redirects al hacer login
                auth.antMatchers("/css/**").permitAll(); // ^
                auth.anyRequest().authenticated();
            })
            .formLogin(form ->
            {
                form.loginPage("/login");
                form.permitAll();
            })
            .logout(logout ->
            {
                logout.logoutUrl("/logout");
                logout.permitAll();
            })
            .httpBasic(Customizer.withDefaults())
            .headers(header -> header.frameOptions().disable()) // Habilita los iframe dentro de la misma aplicacion
            .build();
    }
}
