package com.thanos.contract.controller.dto;

import com.thanos.contract.domain.Contract;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ContractResponse {

    private String name;
    private String schemaName;
    private String schemaVersion;
    private String consumer;
    private String provider;
    private Map<String, String> request = new HashMap<>();
    private Map<String, String> response = new HashMap<>();

    public ContractResponse(Contract contract) {
        this.name = contract.getName();
        this.schemaName = contract.getSchemaName();
        this.schemaVersion = contract.getSchemaVersion();
        this.consumer = contract.getConsumer();
        this.provider = contract.getProvider();
        contract.getReq()
                .forEach(contractField -> request.put(contractField.getName(), contractField.getContent()));
        contract.getRes()
                .forEach(contractField -> response.put(contractField.getName(), contractField.getContent()));
    }
}
