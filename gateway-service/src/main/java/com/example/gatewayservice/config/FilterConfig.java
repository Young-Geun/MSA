package com.example.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {

    //@Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/first-service/**") // 라우터 등록. 해당 패턴으로 요청이오면 uri()에 설정된 곳으로 호출을 보낸다.
                        .filters(f -> f.addRequestHeader("request", "request-first-value")
                                .addResponseHeader("response", "response-first-value"))
                        .uri("http://localhost:8081"))
                .route(r -> r.path("/second-service/**") // 라우터 등록. 해당 패턴으로 요청이오면 uri()에 설정된 곳으로 호출을 보낸다.
                        .filters(f -> f.addRequestHeader("request", "request-second-value")
                                .addResponseHeader("response", "response-second-value"))
                        .uri("http://localhost:8082"))
                .build();
    }

}
