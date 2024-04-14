package runnershigh.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/v1/join", "/api/v1/enquiry/idCheck", "/api/v1/login", "/api/v1/logout","/api/v1/auth/sms", "/api/v1/auth/smsCheck", "/api/v1/refresh").permitAll()
                        .anyRequest().authenticated());

        http
                .httpBasic((auth) -> auth.disable());

        http
                .formLogin((auth) -> auth.disable());

        http
                .csrf((auth) -> auth.disable());

        http
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }


}
