package com.thanos.contract.domain;

import com.thanos.contract.infrastructure.eventbus.NewMockEvent;
import com.thanos.contract.infrastructure.parser.contract.ContractParser;
import com.thanos.contract.infrastructure.parser.schema.SchemaParser;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SimpleCache {

    private static final String CONTRACTS_PATH = "contracts";
    private static final String SCHEMAS_PATH = "schemas";

    private static List<Contract> contracts;
    private static List<Schema> schemas;
    private static List<NewMockEvent> mockInfoList;

    static {
        final List<String> contractPaths = getAllContractPaths();
        final ContractParser contractParser = new ContractParser();

        contracts = contractPaths.stream()
                .map(contractParser::parse)
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    static {
        final List<String> schemaPaths = getAllSchemaPaths();
        final SchemaParser schemaParser = new SchemaParser();
        schemas = schemaPaths.stream()
                .map(schemaParser::parse)
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    static {
        mockInfoList = new ArrayList<>();
    }

    static List<String> getAllContractPaths() {
        final URL resource = SimpleCache.class.getClassLoader().getResource(CONTRACTS_PATH);
        try {
            return Files.walk(Paths.get(resource.getPath()))
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.warn("Fail to getAllContractPaths");
            return Collections.EMPTY_LIST;
        }
    }

    static List<String> getAllSchemaPaths() {
        final URL resource = SimpleCache.class.getClassLoader().getResource(SCHEMAS_PATH);
        try {
            return Files.walk(Paths.get(resource.getPath()))
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.warn("Fail to getAllContractPaths");
            return Collections.EMPTY_LIST;
        }
    }

    public static List<Contract> getContracts() {
        return contracts;
    }

    public static List<Schema> getSchemas() {
        return schemas;
    }

    public static List<NewMockEvent> getMockInfoList() {
        return mockInfoList;
    }

    public static void addNewMockInfo(NewMockEvent newMockEvent) {
        mockInfoList.add(newMockEvent);
    }

    public static void addSchema(Schema schema) {
        schemas.add(schema);
    }

    public static void addContract(Contract contract) {
        contracts.add(contract);
    }
}
