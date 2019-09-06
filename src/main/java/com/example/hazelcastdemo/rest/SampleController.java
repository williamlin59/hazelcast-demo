package com.example.hazelcastdemo.rest;

import com.example.hazelcastdemo.handler.SampleResourceService;
import com.example.hazelcastdemo.resource.SampleResource;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class SampleController {
    private SampleResourceService sampleResourceService;

    @PostMapping(value = "/sample", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<HttpEntity<HttpStatus>> sample(@RequestBody Mono<SampleResource> sampleResourceMono) {
        return sampleResourceMono.flatMap(sampleResource -> sampleResourceService.handleSampleResource.apply(sampleResource)).thenReturn(ResponseEntity.ok().build());
    }

    @GetMapping(value = "/sample")
    public Flux<SampleResource> getAll() {
        return sampleResourceService.getAllEntries.get();
    }
}
