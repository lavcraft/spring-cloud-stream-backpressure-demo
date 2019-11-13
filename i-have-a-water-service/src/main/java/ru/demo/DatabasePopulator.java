package ru.demo;

import com.github.javafaker.Address;
import com.github.javafaker.Beer;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabasePopulator {
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final Faker                 faker = new Faker();

    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        Beer    beer    = faker.beer();
        Address address = faker.address();

        reactiveMongoTemplate.collectionExists(BeerInfo.class)
                .flatMapMany(aBoolean -> {
                    if(!aBoolean) {
                        return Flux.range(0, 1_000_000)
                                .map(integer -> new BeerInfo()
                                        .setId(integer)
                                        .setBeerName(beer.name())
                                        .setWhereDrink(address.fullAddress()))
                                .bufferTimeout(100, Duration.ofSeconds(5))
                                .doOnNext(beerInfos -> log.info("------ write next {} documents ------", beerInfos.size()))
                                .flatMap(reactiveMongoTemplate::insertAll);
                    }
                    return Flux.empty();
                })
                .blockLast();

    }
}
