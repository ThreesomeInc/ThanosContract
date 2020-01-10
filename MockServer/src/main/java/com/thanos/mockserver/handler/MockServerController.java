package com.thanos.mockserver.handler;

import com.thanos.mockserver.domain.MockServerHandler;
import com.thanos.mockserver.domain.contract.ContractService;
import com.thanos.mockserver.domain.contract.NewContract;
import com.thanos.mockserver.domain.schema.NewSchema;
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

    private List<NewContract> contracts;
    private List<NewSchema> newSchemas;

    public MockServerController() {
        contractService = new ContractService();
        schemaService = new SchemaService();
    }

    /**
     * Group mock server by provider-consumer
     */
    public void startupMockServer() {
        contracts = contractService.loadAllContracts();
        newSchemas = schemaService.loadAllSchemas();

        final Map<String, List<NewContract>> collect =
                contracts.stream().collect(Collectors.groupingBy(NewContract::getIndex));
        collect.keySet().forEach(index ->
        {
            final List<NewContract> contracts = collect.get(index);

            if (contracts.size() > 0) {
                String provider = contracts.get(0).getProvider();
                final List<NewSchema> schemas = newSchemas.stream()
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

    public List<NewContract> getContracts() {
        return contracts;
    }

    public List<NewSchema> getNewSchemas() {
        return newSchemas;
    }
}
