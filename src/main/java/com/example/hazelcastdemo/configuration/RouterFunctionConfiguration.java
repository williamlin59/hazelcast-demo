package com.example.hazelcastdemo.configuration;

import com.example.hazelcastdemo.handler.SampleResourceService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@AllArgsConstructor
public class RouterFunctionConfiguration {
    private SampleResourceService sampleResourceService;

    @Bean
    public RouterFunction<ServerResponse> createRoute() {
        return route(POST("/sampleRouter"), sampleResourceService::handleSampleResource).
                andRoute(GET("/sampleRouter"), sampleResourceService::handleGetSampleResource);
    }
}
