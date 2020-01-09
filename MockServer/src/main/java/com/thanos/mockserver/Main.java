package com.thanos.mockserver;


import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.thanos.mockserver.handler.MockMappingHandler;
import com.thanos.mockserver.handler.RequestHandler;
import com.thanos.mockserver.registry.RegisteredRecord;
import io.muserver.Method;
import io.muserver.MuServer;
import io.muserver.rest.RestHandlerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.muserver.MuServerBuilder.httpServer;

@Slf4j
public class Main {

    static final List<RegisteredRecord> fullRecord = Arrays.asList(
            new RegisteredRecord("consumer0", "provider", "schema1"),
            new RegisteredRecord("consumer1", "provider", "schema1"));
//            new RegisteredRecord("bocs", "lims", "10400"));


    public static void main(String[] args) {
        try {
            startupWebServer();
            startupRegistedMock();
            log.info("Mock Server is up!");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private static void startupWebServer() throws IOException {
        MuServer server = httpServer()
                .withHttpPort(8081)
                .addHandler(RestHandlerBuilder.restHandler(new MockMappingHandler())
                        .withOpenApiJsonUrl("/openapi.json")
                        .withOpenApiHtmlUrl("/api.html")
                        .addCustomWriter(new JacksonJaxbJsonProvider())
                        .addCustomReader(new JacksonJaxbJsonProvider())
                        .withOpenApiHtmlUrl("/api.html"))
                .addHandler(Method.GET, "/", ((muRequest, muResponse, map) -> {
//                    muResponse.write("HelloWorld");
                    muResponse.redirect("/api.html");
                }))
                .start();
        System.out.println("Web Server started at " + server.uri());
    }

    private static void startupRegistedMock() {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (RegisteredRecord registeredRecord : fullRecord) {
            executor.execute(new RequestHandler(registeredRecord));
        }
    }

}
