package com.example.hazelcastdemo.handler;

import com.example.hazelcastdemo.configuration.HazelcastConfiguration;
import com.example.hazelcastdemo.resource.SampleResource;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class SampleResourceService {
    private final HazelcastInstance hazelcastInstance;
    public Supplier<Flux<SampleResource>> getAllEntries = () -> Flux.fromIterable(getSampleMap().values());
    private Predicate<SampleResource> sampleResourcePredicate = sampleResource -> getSampleMap().containsKey(sampleResource.getId());
    public Function<SampleResource, Mono<Void>> handleSampleResource = sampleResource -> {
        if (sampleResourcePredicate.test(sampleResource)) {
            Mono.fromRunnable(() -> Try.of(() -> getSampleMap().tryLock(sampleResource.getId()))
                    .andThen(() -> getSampleMap().set(sampleResource.getId(), sampleResource))
                    .andFinally(() -> getSampleMap().unlock(sampleResource.getId())).getOrElseThrow(() -> new RuntimeException("Can't update map"))).publishOn(Schedulers.elastic()).subscribe();
        } else {
            getSampleMap().set(sampleResource.getId(), sampleResource);
        }
        return Mono.empty();
    };

    public Mono<ServerResponse> handleSampleResource(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SampleResource.class).flatMap(sampleResource -> handleSampleResource.apply(sampleResource)).flatMap(value -> ServerResponse.ok().build());
    }

    public Mono<ServerResponse> handleGetSampleResource(ServerRequest serverRequest) {
        return ServerResponse.ok().body(getAllEntries.get(), SampleResource.class);
    }


    private IMap<Long, SampleResource> getSampleMap() {
        IMap<Long, SampleResource> map = hazelcastInstance.getMap(HazelcastConfiguration.MAP_NAME);
        return map;
    }
}
