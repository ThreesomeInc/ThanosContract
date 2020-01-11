package com.thanos.mockserver.controller;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.thanos.mockserver.infrastructure.eventbus.EventBusFactory;
import com.thanos.mockserver.infrastructure.eventbus.NewMockEvent;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Slf4j
@Path("/apis")
public class RestApiController {

    public RestApiController() {
        final AsyncEventBus asyncEventBus = EventBusFactory.getInstance();
        asyncEventBus.register(this);
    }

    @Subscribe
    public void handleAddNewMockEvent(NewMockEvent newMockEvent) {
        SimpleCache.addNewMockInfo(newMockEvent);
        log.info("Receive new mock startup and updated cache: {}", newMockEvent);
    }

    @GET
    @Path("/mappings")
    @Produces(MediaType.APPLICATION_JSON)
    public List<NewMockEvent> getAllMocks() {
        return SimpleCache.getMockInfoList();
    }


}
