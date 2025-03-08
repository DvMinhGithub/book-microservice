package com.mdv.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.mdv.gateway.repository.IdentityClient;

@Configuration
public class WebClientConfiguration {
    @Bean
    WebClient webClient() {
        return WebClient.builder().baseUrl("http://localhost:8080/identity").build();
    }

    @Bean
    IdentityClient identityClient(WebClient webClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient))
                .build();
                
        return factory.createClient(IdentityClient.class);
    }

    @Bean
    CorsWebFilter corsFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("*");
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(urlBasedCorsConfigurationSource);
    }
}
