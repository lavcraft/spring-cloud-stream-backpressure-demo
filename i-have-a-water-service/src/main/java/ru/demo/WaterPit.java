package ru.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

@Service
@EnableBinding(Source.class)
@RequiredArgsConstructor
public class WaterPit {
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final AtomicLong            atomicLong = new AtomicLong();

    @Bean
    public Supplier<Flux<Message<Long>>> emit() {
        return () -> Flux
                .<Long>generate(longSynchronousSink -> longSynchronousSink.next(atomicLong.incrementAndGet()))
                .map(aLong -> MessageBuilder.withPayload(aLong).build())
                .delayElements(Duration.ofMillis(1));
    }

}
