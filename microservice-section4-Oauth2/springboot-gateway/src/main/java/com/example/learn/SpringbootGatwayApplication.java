package com.example.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class SpringbootGatwayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootGatwayApplication.class, args);
	}


	//https://spring.io/projects/spring-cloud-gateway
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

	return 	builder.routes()
			       .route(p -> p
						.path("/accounts/**")
					   .filters(f-> f.rewritePath("/accounts/(?<segment>.*)", "/${segment}")
					   .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())//adding our own header in the filter that will show in Postman Header ,like for time calculation
					   .circuitBreaker(config -> config.setName("accountsCircuitBreaker")// setting accountsCircuitBreaker for accounts
					   .setFallbackUri("forward:/contactSupport")))// fallback method in controller
		            	.uri("lb://ACCOUNTS"))//IT should be capital as showing in Eureka dashboard

			//Cards filter
					.route(p -> p
							.path("/cards/**")
							.filters(f-> f.rewritePath("/cards/(?<segment>.*)", "/${segment}")
							.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))// //adding our own header in the filter that will show in Postman Header ,like for time calculation
							.uri("lb://CARDS")) //IT should be capital as showing in Eureka dashboard

			.build();
	}

}