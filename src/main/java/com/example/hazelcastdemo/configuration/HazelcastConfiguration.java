package com.example.hazelcastdemo.configuration;

import com.example.hazelcastdemo.listener.SampleCacheEntryEventListener;
import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PreDestroy;

@Configuration
public class HazelcastConfiguration {
    public static final String MAP_NAME = "SampleMap";
    private final SampleCacheEntryEventListener sampleCacheEntryEventListener;

    @Autowired
    public HazelcastConfiguration(@Lazy SampleCacheEntryEventListener sampleCacheEntryEventListener) {
        this.sampleCacheEntryEventListener = sampleCacheEntryEventListener;
    }

    @PreDestroy
    public void shutdown() {
        Hazelcast.shutdownAll();
    }

    @Bean
    public HazelcastInstance hazelcastInstance() {
        Config config = new Config();
        config.getGroupConfig().setName("sample");
        MapConfig mapConfig = new MapConfig(MAP_NAME);
        mapConfig.setBackupCount(1);
        mapConfig.setAsyncBackupCount(1);
        mapConfig.setEvictionPolicy(EvictionPolicy.LRU);
        mapConfig.setInMemoryFormat(InMemoryFormat.OBJECT);
        EntryListenerConfig entryListenerConfig = new EntryListenerConfig();
        entryListenerConfig.setImplementation(sampleCacheEntryEventListener);
        mapConfig.addEntryListenerConfig(entryListenerConfig);
        config.addMapConfig(mapConfig);
        return Hazelcast.newHazelcastInstance(config);
    }
}
