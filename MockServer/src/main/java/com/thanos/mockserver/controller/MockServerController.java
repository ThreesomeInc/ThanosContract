package com.thanos.mockserver.controller;

import com.thanos.mockserver.domain.MockServerHandler;
import com.thanos.mockserver.domain.contract.Contract;
import com.thanos.mockserver.domain.contract.ContractService;
import com.thanos.mockserver.domain.schema.Schema;
import com.thanos.mockserver.domain.schema.SchemaService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
public class MockServerController {

    private ContractService contractService;
    private SchemaService schemaService;

    private List<Contract> contracts;
    private List<Schema> schemas;

    public MockServerController() {
        contractService = new ContractService();
        schemaService = new SchemaService();
    }

    /**
     * Group mock server by provider-consumer
     */
    public void startupMockServer() {
        contracts = contractService.loadAllContracts();
        schemas = schemaService.loadAllSchemas();

        final Map<String, List<Contract>> collect =
                contracts.stream().collect(Collectors.groupingBy(Contract::getIndex));
        collect.keySet().forEach(index ->
        {
            final List<Contract> contracts = collect.get(index);

            if (contracts.size() > 0) {
                String provider = contracts.get(0).getProvider();
                final List<Schema> schemas = this.schemas.stream()
                        .filter(newSchema -> newSchema.getProvider().equals(provider))
                        .collect(Collectors.toList());

                log.info("Going to startup Mock for {} which included {} contracts and {} schemas",
                        index, contracts.size(), schemas.size());
                ExecutorService executor =
                        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
                executor.execute(new MockServerHandler(index, contracts, schemas));
            }
        });
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public List<Schema> getSchemas() {
        return schemas;
    }
}
