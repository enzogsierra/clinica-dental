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
                auth.antMatchers("/assets/logo.**").permitAll();
                auth.anyRequest().authenticated();
                //auth.anyRequest().hasAuthority("USER");
            })
            .formLogin(form ->
            {
                form.loginPage("/login");
                form.loginProcessingUrl("/login");
                form.defaultSuccessUrl("/");
                form.permitAll();
            })
            // .formLogin(form ->
            // {
            //     form.loginPage("/login");
            //     form.loginProcessingUrl("/login");
            //     form.defaultSuccessUrl("/");
            //     form.permitAll();
            // })
            .formLogin(Customizer.withDefaults())
            // .logout(logout ->
            // {
            //     logout.logoutUrl("/logout");
            //     logout.logoutSuccessUrl("/");
            //     logout.permitAll();
            // })
            .httpBasic(Customizer.withDefaults())
            .build();

        // return http
        //     .csrf(csrf -> csrf.disable())
        //     .authorizeHttpRequests(auth ->
        //     {
        //         auth.antMatchers("/admin/**").hasAuthority("ADMIN");
        //         auth.anyRequest().authenticated();
        //     })
        //     .formLogin(Customizer.withDefaults())
        //     .httpBasic(Customizer.withDefaults())
        // .build();
    }
}
