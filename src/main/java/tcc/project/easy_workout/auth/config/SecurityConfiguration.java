package tcc.project.easy_workout.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tcc.project.easy_workout.auth.security.UserAuthenticationFilter;
import tcc.project.easy_workout.common.exception.handler.HandlerController;

import java.util.List;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserAuthenticationFilter userAuthenticationFilter;
    private final HandlerController handlerController;

    @Bean
     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable).cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(HttpMethod.POST, "/personal-trainers", "/trainees", "/auth").permitAll()
                                .requestMatchers("/workouts/**").permitAll()
                                .requestMatchers(toH2Console()).permitAll()
                                .requestMatchers("/trainees/**").hasRole("TRAINEE")
                                .requestMatchers("/personal-trainers/**").hasRole("PERSONAL_TRAINER")
                                .requestMatchers("/register/**").permitAll()
                                .anyRequest().authenticated()
                                .and()
                                .addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class))
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(handlerController::accessDeniedHandler)
                        .authenticationEntryPoint(handlerController::authenticationHandler));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.applyPermitDefaultValues();
        corsConfiguration.setAllowedMethods(List.of("POST", "GET", "PUT", "DELETE"));
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }
}
