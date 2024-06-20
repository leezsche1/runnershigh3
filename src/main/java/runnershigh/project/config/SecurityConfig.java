package runnershigh.project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import runnershigh.project.security.exception.CustomAuthenticationEntryPoint;
import runnershigh.project.security.filter.JwtAuthenticationFilter;
import runnershigh.project.security.provider.JwtAuthenticationProvider;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final AuthenticationConfiguration authenticationConfiguration;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/v1/enquiry/**", "/api/v1/join", "/api/v1/enquiry/idCheck", "/api/v1/login", "/api/v1/logout","/api/v1/auth/sms", "/api/v1/auth/smsCheck", "/api/v1/refresh").permitAll()
                        .anyRequest().authenticated());

        http
                .httpBasic((auth) -> auth.disable());

        http
                .formLogin((auth) -> auth.disable());

        http
                .logout((auth) -> auth
                        .disable());                //로그아웃은 disable로 해줘야 한다. 안그러면 member controller에 있는 /logout url이 통하지 않는다.

        http
                .exceptionHandling((auth) -> auth
                        .authenticationEntryPoint(customAuthenticationEntryPoint));


        http
                .authenticationProvider(jwtAuthenticationProvider);

        http
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class);


        http
                .csrf((auth) -> auth.disable());

        http
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }


}
