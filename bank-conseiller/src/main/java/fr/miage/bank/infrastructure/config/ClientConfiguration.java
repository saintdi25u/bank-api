package fr.miage.bank.infrastructure.config;

import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;


public class ClientConfiguration {
    @Bean
    public RoundRobinLoadBalancer roundRobinContextLoadBalancer(LoadBalancerClientFactory loadBalancerClientFactory, Environment env) {
        String serviceId = LoadBalancerClientFactory.getName(env);
        return new RoundRobinLoadBalancer(loadBalancerClientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class), serviceId, -1);
    }
}
