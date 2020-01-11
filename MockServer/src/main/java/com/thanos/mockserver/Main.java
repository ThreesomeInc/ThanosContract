package com.thanos.mockserver;


import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.thanos.mockserver.controller.MockServerController;
import com.thanos.mockserver.controller.RestApiController;
import io.muserver.Method;
import io.muserver.MuServer;
import io.muserver.rest.RestHandlerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static io.muserver.MuServerBuilder.httpServer;

@Slf4j
public class Main {

    private static MuServer webServer;

    private static MockServerController mockServerController = new MockServerController();

    public static void main(String[] args) {
        try {
            startupWebServer();
            mockServerController.init();
            log.info("Service is up!");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down...");
            webServer.stop();
            log.info("Shut down complete.");
        }));
    }

    private static void startupWebServer() throws IOException {
        webServer = httpServer()
                .withHttpPort(8081)
                .addHandler(RestHandlerBuilder.restHandler(new RestApiController())
                        .withOpenApiJsonUrl("/openapi.json")
                        .withOpenApiHtmlUrl("/api.html")
                        .addCustomWriter(new JacksonJaxbJsonProvider())
                        .addCustomReader(new JacksonJaxbJsonProvider())
                        .withOpenApiHtmlUrl("/api.html"))
                .addHandler(Method.GET, "/", ((muRequest, muResponse, map) -> {
                    muResponse.redirect("/api.html");
                })).start();

        System.out.println("Web Server started at " + webServer.uri());
    }

}
