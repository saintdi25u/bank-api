// package fr.miage.bank.infrastructure.config;

// import org.apache.http.protocol.HTTP;
// import org.springframework.beans.factory.annotation.Configurable;
// import org.springframework.cloud.sleuth.TraceKeys.Http;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.convert.converter.Converter;
// import org.springframework.http.HttpMethod;
// import
// org.springframework.security.authentication.AbstractAuthenticationToken;
// import
// org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import
// org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.oauth2.jwt.Jwt;
// import org.springframework.security.web.SecurityFilterChain;

// import static org.springframework.security.config.Customizer.withDefaults;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfiguration {

// public static final String RESPONSABLE = "RESPONSABLE";
// public static final String CONSEILLER = "CONSEILLER";

// private final JwtAuthConverter jwtAuthConverter;

// public SecurityConfiguration(JwtAuthConverter jwtAuthConverter) {
// this.jwtAuthConverter = jwtAuthConverter;
// }

// // @Bean
// // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
// {
// // return http.authorizeHttpRequests(authorize -> authorize
// // .requestMatchers(HttpMethod.GET,
// "/api/conseillers/**").hasRole(CONSEILLER)
// // .requestMatchers(HttpMethod.GET,
// "/api/responsable/**").hasRole(RESPONSABLE)
// // .requestMatchers(HttpMethod.POST,
// "/api/conseiller/**").hasRole(CONSEILLER)
// // .requestMatchers(HttpMethod.POST,
// "/api/responsable/**").hasRole(RESPONSABLE)
// // .anyRequest().authenticated())
// // .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt ->
// // jwt.jwtAuthenticationConverter(jwtAuthConverter)))
// // .sessionManagement(session ->
// // session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
// // .cors(withDefaults()).csrf(withDefaults()).build();
// // }
// }
