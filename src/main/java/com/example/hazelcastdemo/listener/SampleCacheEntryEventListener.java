package com.example.hazelcastdemo.listener;

import com.example.hazelcastdemo.resource.SampleResource;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SampleCacheEntryEventListener implements
        EntryUpdatedListener<Long, SampleResource>,
        EntryAddedListener<Long, SampleResource> {
    @Override
    public void entryAdded(EntryEvent<Long, SampleResource> event) {
        log.info("New SampleResource added {}", event.getValue());
    }

    @Override
    public void entryUpdated(EntryEvent<Long, SampleResource> event) {
        log.info("Updated SampleResource. Old SampleResource: {}. New SampleResource {}", event.getOldValue(), event.getValue());
    }
}
