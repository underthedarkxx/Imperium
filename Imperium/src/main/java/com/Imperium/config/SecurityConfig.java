package com.Imperium.config; // define o pacote desta classe

import java.util.List; // usado para criar listas de origens, métodos e headers permitidos no CORS

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration; // indica que esta classe contém beans de configuração
import org.springframework.http.HttpMethod; // enum para definir métodos HTTP (GET, POST, etc.)
import org.springframework.security.authentication.AuthenticationManager; // gerencia autenticação de usuários
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // usado para obter o AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // permite configurar regras de segurança HTTP
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // habilita segurança web do Spring
import org.springframework.security.config.http.SessionCreationPolicy; // define política de sessão (stateless para JWT)
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // implementa hash de senha com BCrypt
import org.springframework.security.crypto.password.PasswordEncoder; // interface para codificar senhas
import org.springframework.security.web.SecurityFilterChain; // representa a cadeia de filtros de segurança
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // filtro padrão de login do Spring Security
import org.springframework.web.cors.CorsConfiguration; // configura CORS
import org.springframework.web.cors.CorsConfigurationSource; // fonte de configuração CORS
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // aplica configuração CORS baseada em URL

import com.Imperium.config.security.SecurityFilter; // filtro customizado de autenticação JWT

@Configuration // marca a classe como de configuração Spring
@EnableWebSecurity // habilita segurança web do Spring Security
public class SecurityConfig {

    private final SecurityFilter securityFilter; // injeção do filtro JWT

    // construtor para injetar o SecurityFilter
    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean // bean para codificação de senha
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // usa BCrypt para hashing de senhas
    }

    @Bean // expõe AuthenticationManager como bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean // define a cadeia de filtros de segurança
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // desativa CSRF (necessário para APIs JWT stateless)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // não cria sessão HTTP
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // habilita CORS com configuração customizada
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll() // permite login sem autenticação
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // permite requisições OPTIONS (CORS preflight)
                        .requestMatchers("/api/admin/**").hasRole("ADMINISTRADOR_PRINCIPAL") // protege endpoints admin
                        .anyRequest().authenticated() // qualquer outra requisição precisa estar autenticada
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // adiciona filtro JWT antes do filtro de login padrão
                .build();
    }

    @Bean // bean de configuração CORS
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://127.0.0.1:5500", "http://localhost:5500")); // origens permitidas
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // métodos HTTP permitidos
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); // headers permitidos
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // aplica a configuração para todos os endpoints
        return source;
    }
}
