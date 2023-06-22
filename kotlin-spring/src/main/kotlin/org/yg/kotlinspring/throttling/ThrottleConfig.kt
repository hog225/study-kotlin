package org.yg.kotlinspring.throttling

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Bucket4j
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import java.time.Duration

@Configuration
@EnableAsync
class ThrottleConfig {
    @Bean
    fun restTemplate(): RestTemplate  {
        return RestTemplate()
    }

    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .baseUrl("http://localhost:8000")
            .build()
    }

    @Bean
    fun bucket(): Bucket {

        return Bucket.builder()
            .addLimit(Bandwidth.simple(1, Duration.ofSeconds(4))) // RPM 제한 설정
            .build()
    }



}