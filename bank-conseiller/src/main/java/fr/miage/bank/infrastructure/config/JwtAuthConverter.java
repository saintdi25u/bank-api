// package fr.miage.bank.infrastructure.config;

// import java.util.Collection;
// import java.util.Collections;
// import java.util.Map;
// import java.util.stream.Collector;
// import java.util.stream.Collectors;
// import java.util.stream.Stream;

// import org.springframework.core.convert.converter.Converter;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.oauth2.jwt.Jwt;
// import org.springframework.security.oauth2.jwt.JwtClaimNames;
// import
// org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
// import
// org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
// import org.springframework.stereotype.Component;

// @Component
// public class JwtAuthConverter implements Converter<Jwt,
// JwtAuthenticationToken> {

// private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
// new JwtGrantedAuthoritiesConverter();
// private final JwtAuthConverterProperties properties;

// public JwtAuthConverter(JwtAuthConverterProperties properties) {
// this.properties = properties;
// }

// @Override
// public JwtAuthenticationToken convert(Jwt jwt) {
// var accesses = ((Map<String, Collection<?>>) ((Map<String, Collection<?>>)
// jwt.getClaims()
// .getOrDefault("resourceaccess", Collections.emptyMap()))
// .getOrDefault(properties.getResourceId(), null))
// .getOrDefault("roles", Collections.emptyList())
// .stream()
// .map(role -> new SimpleGrantedAuthority("ROLE" + role))
// .distinct();

// Collection<GrantedAuthority> authorities =
// Stream.concat(jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
// accesses).collect(Collectors.toSet());

// String principalClaimName = properties.getPrincipalAttribute()
// .map(claim ->
// jwt.getClaimAsString(claim)).orElse(jwt.getClaimAsString(JwtClaimNames.SUB));

// return new JwtAuthenticationToken(jwt, authorities, principalClaimName);
// }

// }
