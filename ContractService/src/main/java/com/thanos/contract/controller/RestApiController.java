package com.thanos.contract.controller;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.thanos.contract.controller.dto.ContractResponse;
import com.thanos.contract.controller.dto.SchemaResponse;
import com.thanos.contract.domain.SimpleCache;
import com.thanos.contract.infrastructure.eventbus.EventBusFactory;
import com.thanos.contract.infrastructure.eventbus.NewMockEvent;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

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

    @GET
    @Path("/contracts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ContractResponse> getAllContracts() {
        return SimpleCache.getContracts().stream()
                .map(ContractResponse::new).collect(Collectors.toList());
    }

    @GET
    @Path("/schemas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SchemaResponse> getAllSchemas() {
        return SimpleCache.getSchemas().stream()
                .map(SchemaResponse::new).collect(Collectors.toList());
    }


}
