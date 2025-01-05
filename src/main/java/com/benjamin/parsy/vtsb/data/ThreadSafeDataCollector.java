package com.benjamin.parsy.vtsb.data;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Component
public class ThreadSafeDataCollector {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, AtomicInteger>> countRequestByEndpointByUser = new ConcurrentHashMap<>();

    public int getTotalCalls() {

        return countRequestByEndpointByUser.values().stream()
                .flatMap(map -> map.values().stream())
                .mapToInt(AtomicInteger::get)
                .sum();
    }

    public void recordUserCall(String username, String method, String requestURI) {

        String callKey = method.concat(requestURI);

        countRequestByEndpointByUser.computeIfAbsent(username, key -> new ConcurrentHashMap<>())
                .computeIfAbsent(callKey, key -> new AtomicInteger(0))
                .incrementAndGet();
    }

    public int getTotalUsers() {
        return countRequestByEndpointByUser.size();
    }

    public Set<String> getUserList() {
        return Collections.unmodifiableSet(countRequestByEndpointByUser.keySet());
    }

    public Map<String, Integer> getTotalCallsByEndpoint() {

        Map<String, Integer> totalCallsByEndpoint = new HashMap<>();

        for (Map.Entry<String, ConcurrentHashMap<String, AtomicInteger>> entry : countRequestByEndpointByUser.entrySet()) {
            for (Map.Entry<String, AtomicInteger> subEntry : entry.getValue().entrySet()) {
                totalCallsByEndpoint.merge(subEntry.getKey(), subEntry.getValue().get(), Integer::sum);
            }
        }

        return totalCallsByEndpoint;
    }

}
