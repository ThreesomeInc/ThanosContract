package com.thanos;


import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.thanos.contract.controller.RestApiController;
import com.thanos.mockserver.MockServerController;
import com.thanos.mockserver.ScheduleHelper;
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
            ScheduleHelper.initScheduler();

            startupWebServer();
            mockServerController.init();
            printStartupLog();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        addShutdownHook();
    }

    static void printStartupLog() {
        log.info("###########################################");
        log.info("            Service is up!                 ");
        log.info("###########################################");
    }

    static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down...");
            webServer.stop();
            mockServerController.shutdown();
            log.info("###########################################");
            log.info("           Shut down complete !            ");
            log.info("###########################################");
        }));
    }

    private static void startupWebServer() throws IOException {
        webServer = httpServer()
                .withHttpPort(8081)
                .addHandler(RestHandlerBuilder.restHandler(new RestApiController())
                        .withOpenApiJsonUrl("/openapi.json")
                        .addCustomWriter(new JacksonJaxbJsonProvider())
                        .addCustomReader(new JacksonJaxbJsonProvider())
                        .withOpenApiHtmlUrl("/api.html"))
                .addHandler(Method.GET, "/", ((muRequest, muResponse, map) -> {
                    muResponse.redirect("/api.html");
                })).start();

        log.info("Web Server started at " + webServer.uri());
    }

}
