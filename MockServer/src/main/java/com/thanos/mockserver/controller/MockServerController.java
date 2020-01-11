package com.thanos.mockserver.controller;

import com.thanos.mockserver.domain.Contract;
import com.thanos.mockserver.domain.SimpleCache;
import com.thanos.mockserver.domain.mock.MockServerHandler;
import com.thanos.mockserver.infrastructure.eventbus.NewMockEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
public class MockServerController {

    public void init() {
        startNewMocks();
    }
    /**
     * Group mock server by provider-consumer
     */
    void startNewMocks() {
        final List<String> existingIndex = SimpleCache.getMockInfoList().stream()
                .map(NewMockEvent::getIndex).collect(Collectors.toList());

        final Set<String> indexs = SimpleCache.getContracts().stream()
                .collect(Collectors.groupingBy(Contract::getIndex)).keySet();

        indexs.stream().filter(index -> !existingIndex.contains(index))
                .forEach(this::createNewMockForIndex);
    }

    private void createNewMockForIndex(String index) {
        log.info("Creating new mock server {}", index);
        ExecutorService executor =
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        executor.execute(new MockServerHandler(index));
    }

}
