package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final WebClient.Builder builder;

    public Mono<?> call() {
        return builder.build().method(HttpMethod.GET)
                .uri("http://localhost:8080/delay")
                .headers(httpHeaders -> httpHeaders.set("Content-Type", "application/json"))
                .retrieve()
                .toEntity(String.class)
                .flatMap(Mono::just)
                .timeout(Duration.ofSeconds(20))
                .onErrorResume(TimeoutException.class, ex -> {
                    String message = "Webclient timeout";
                    return Mono.error(new TimeoutException(message).initCause(ex));
                });
    }

    @GetMapping("test")
    public Mono<?> test() {
        return call();
    }

}
