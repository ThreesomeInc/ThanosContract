package com.thanos.mockserver.controller.dto;

import com.thanos.mockserver.domain.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedList;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SchemaResponse {

    private String name;
    private String version;
    private String provider;
    private LinkedList<SchemaFieldDTO> request;
    private LinkedList<SchemaFieldDTO> response;

    public SchemaResponse(Schema schema) {
        this.name = schema.getName();
        this.version = schema.getVersion();
        this.provider = schema.getProvider();
        this.request = schema.getRequest().stream().map(SchemaFieldDTO::new)
                .collect(Collectors.toCollection(LinkedList::new));
        this.response = schema.getResponse().stream().map(SchemaFieldDTO::new)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
