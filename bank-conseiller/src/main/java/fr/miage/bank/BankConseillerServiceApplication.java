package fr.miage.bank;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import fr.miage.bank.infrastructure.config.ClientConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
@SpringBootApplication
@LoadBalancerClients({
		@LoadBalancerClient(name = "finance-service", configuration = ClientConfiguration.class),
})
public class BankConseillerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankConseillerServiceApplication.class, args);
	}

	@Bean
	RestTemplate template() {
		return new RestTemplate();
	}

	@Bean
	public OpenAPI bankAPI() {
		return new OpenAPI().info(new Info().title("Bank API").version("1.0").description("Bank API Documentation"));
	}

}
