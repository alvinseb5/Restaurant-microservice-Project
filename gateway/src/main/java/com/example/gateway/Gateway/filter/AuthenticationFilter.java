package com.example.gateway.Gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

	@Autowired
	private WebClient.Builder webClientBuilder;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String requestPath = exchange.getRequest().getPath().toString();

		// Bypass token validation for the /register endpoint
		if (requestPath.endsWith("/register") || requestPath.endsWith("/authenticate")) {
			return chain.filter(exchange);
		}

		String token = extractToken(exchange);

		if (token == null) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}

		return webClientBuilder.build().post().uri("http://AUTH/auth/validateToken")
				.header("Authorization", "Bearer " + token).retrieve()
				.onStatus(httpStatus -> httpStatus.is4xxClientError(),
						response -> Mono.error(new RuntimeException("Unauthorized")))
				.bodyToMono(Boolean.class).flatMap(isValid -> {
					if (Boolean.TRUE.equals(isValid)) {
						return chain.filter(exchange);
					} else {
						exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
						return exchange.getResponse().setComplete();
					}
				});
	}

	private String extractToken(ServerWebExchange exchange) {
		// Extract the token from the Authorization header
		String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		return null;
	}

	@Override
	public int getOrder() {
		return -100; // Order of the filter
	}
}
