package com.thanos.mockserver;


import com.thanos.mockserver.handler.RequestHandler;
import com.thanos.mockserver.parser.Contract;
import com.thanos.mockserver.parser.ContractParser;
import com.thanos.mockserver.parser.Schema;
import com.thanos.mockserver.parser.SchemaParser;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class Main {

    public static void main(String[] args) {
        log.info("Mock Server is up!");
        startup();
    }

    private static void startup() {
        try {
            final ContractParser contractParser = new ContractParser();
            final List<Contract> contracts =
                    contractParser.parse("contracts/consumer1_provider/", "schema1_test.yml");

            final String schemaName = "schema1";
            final SchemaParser schemaParser = new SchemaParser();
            final List<Schema> schemas = schemaParser.parseReq(schemaName);

            new RequestHandler(schemas, contracts).run();

        } catch (IOException ioException) {
            log.error("Schema or contract not found");
            ioException.printStackTrace();
            System.exit(0);
        }
    }
}
