package com.thanos.mockserver.handler;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.thanos.mockserver.infrastructure.eventbus.EventBusFactory;
import com.thanos.mockserver.infrastructure.eventbus.NewMockEvent;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Path("/mocks")
public class MockMappingHandler {

    List<NewMockEvent> mockInfoList = new ArrayList<>();

    public MockMappingHandler() {
        final AsyncEventBus asyncEventBus = EventBusFactory.getInstance();
        asyncEventBus.register(this);
    }

    @Subscribe
    public void handleAddNewMockEvent(NewMockEvent newMockEvent) {
        log.info("Receive new mock startup: {}", newMockEvent);
        mockInfoList.add(newMockEvent);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<NewMockEvent> getAllMocks() {
        return mockInfoList;
    }


}
