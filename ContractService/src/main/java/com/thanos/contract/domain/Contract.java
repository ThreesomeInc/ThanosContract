package com.thanos.contract.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Contract {

    String name;
    String schemaName;
    String schemaVersion;
    String consumer;
    String provider;

    LinkedList<ContractField> req;
    LinkedList<ContractField> res;

    public String getIndex() {
        return provider + '-' + consumer;
    }

    /**
     * The field listed in contract should match the value in request
     */
    public Boolean matchRequest(Message msg) {
        for (ContractField field : req) {
            if (msg.getParseRequest().containsKey(field.getName()) &&
                    (!field.match(msg.getParseRequest().get(field.getName())))) {
                return false;
            }
        }
        return true;
    }

    public String buildResponse(Schema matchedSchema) {
        StringBuilder result = new StringBuilder();

        for (SchemaField schemaField : matchedSchema.getResponse()) {
            final Optional<ContractField> contractField = getContractFieldByName(schemaField);
            if (contractField.isPresent()) {
                result.append(contractField.get().getContent());
            } else {
                result.append((schemaField.getValidator()).getExpectedValue());
            }
        }
        return result.toString();
    }

    Optional<ContractField> getContractFieldByName(SchemaField schemaField) {
        final List<ContractField> result = res.stream()
                .filter(response -> response.getName().equals(schemaField.getName()))
                .collect(Collectors.toList());

        if (result.size() >= 1) {
            return Optional.of(result.get(0));
        } else {
            return Optional.empty();
        }
    }
}
