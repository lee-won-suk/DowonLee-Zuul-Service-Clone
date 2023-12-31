package com.example.apigatewayservice.filter;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // Custom Pre Filter
//        return (exchange, chain) -> {
//          ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//
//            log.info("Global filter baseMessage :{}", config.getBaseMessage());
//
//
//            if (config.isPreLogger()) {
//                log.info("Global filter start : request id ->{}", request.getId());
//            }
//
//            //Custom Post Filter
//            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//
//
//                if (config.isPostLogger()) {
//                    log.info("Global filter end : response code ->{}", request.getId());
//                }
//                log.info("Global PRE filter : response code -> {}", response.getStatusCode());
//
//            }));
//        };


        GatewayFilter filter = new OrderedGatewayFilter(new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                ServerHttpRequest request = exchange.getRequest();
                ServerHttpResponse response = exchange.getResponse();

                log.info("Logging Filter baseMessage :{}", config.getBaseMessage());
                if (config.isPreLogger()) {
                    log.info("Logging PRE Filter start : request id -> {}", request.getId());
                }
                //Custom Post Filter
                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    if (config.isPostLogger()) {
                        log.info("Logging POST Filter : response code -> {}", request.getId());
                    }
                }));
            }
        }, Ordered.LOWEST_PRECEDENCE);

        return filter;

    }

    @Data
    public static class Config {
        // Configuration 정보 입력

        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;

    }
}
