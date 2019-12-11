package com.thanos.mockserver.registry;

import com.thanos.mockserver.parser.Contract;
import com.thanos.mockserver.parser.ContractParser;
import com.thanos.mockserver.parser.Schema;
import com.thanos.mockserver.parser.SchemaParser;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Getter
@Slf4j
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredRecord {

    private String consumer;
    private String provider;
    private String schemaName;

    public String getContractPath() {
        return new StringBuilder("contracts/")
                .append(consumer).append("_").append(provider).append("/")
                .append(schemaName).append("_test.yml").toString();
    }

    public List<Contract> getContracts() {
        return new ContractParser().parse(getContractPath());
    }

    public List<Schema> getReqSchemas() throws IOException {
        return new SchemaParser().parseReq(schemaName);
    }

    public List<Schema> getResSchemas() throws IOException {
        return new SchemaParser().parseRes(schemaName);
    }

}
