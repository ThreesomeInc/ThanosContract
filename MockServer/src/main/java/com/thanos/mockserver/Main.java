package com.thanos.mockserver;


import com.thanos.mockserver.handler.RequestHandler;
import com.thanos.mockserver.parser.Contract;
import com.thanos.mockserver.parser.Schema;
import com.thanos.mockserver.registry.RegisteredRecord;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Main {

    static final List<RegisteredRecord> fullRecord = Arrays.asList(
            new RegisteredRecord("consumer0", "provider", "schema1"),
            new RegisteredRecord("consumer1", "provider", "schema1"));


    public static void main(String[] args) {
        log.info("Mock Server is up!");
        startup();
    }

    private static void startup() {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            for (RegisteredRecord registeredRecord : fullRecord) {
                final List<Contract> contracts = registeredRecord.getContracts();
                final List<Schema> reqSchemas = registeredRecord.getReqSchemas();
                executor.execute(new RequestHandler(registeredRecord));
            }
        } catch (IOException ioException) {
            log.error("Schema or contract not found");
            ioException.printStackTrace();
            System.exit(0);
        }
    }
}
