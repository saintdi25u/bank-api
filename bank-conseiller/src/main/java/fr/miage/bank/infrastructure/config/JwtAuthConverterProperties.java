// package fr.miage.bank.infrastructure.config;

// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Configurable;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.validation.annotation.Validated;

// import jakarta.validation.constraints.NotBlank;

// @Validated
// @Configuration
// @ConfigurationProperties(prefix = "jwt.auth.converter")
// public class JwtAuthConverterProperties {
// @NotBlank
// private String resourceId;
// private String principalAttribute;

// public String getResourceId() {
// return resourceId;
// }

// public void setResourceId(String resourceId) {
// this.resourceId = resourceId;
// }

// public Optional<String> getPrincipalAttribute() {
// return Optional.ofNullable(principalAttribute);
// }

// public void setPrincipalAttribute(String principalAttribute) {
// this.principalAttribute = principalAttribute;
// }
// }
