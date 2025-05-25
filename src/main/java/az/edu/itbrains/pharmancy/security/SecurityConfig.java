package az.edu.itbrains.pharmancy.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailService userDetailService;

    public SecurityConfig(CustomUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/home", "/shop", "/about", "/contact").permitAll()
                        .requestMatchers("/register", "/login", "/register/**").permitAll()
                        .requestMatchers("/receipt", "/receipt/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/admin/css/**", "/admin/js/**").permitAll()
                        .requestMatchers("/debug-users", "/test-register", "/create-admin").permitAll()
                        .requestMatchers("/admin/**").permitAll()  // TEMPORARILY ALLOW ALL ACCESS
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/receipt/**", "/admin/**")
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .userDetailsService(userDetailService);

        return http.build();
    }
}