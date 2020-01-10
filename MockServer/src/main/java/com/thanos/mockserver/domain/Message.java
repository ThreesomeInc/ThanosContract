package com.thanos.mockserver.domain;

import com.thanos.mockserver.domain.schema.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashMap;

@Getter
@ToString
@NoArgsConstructor
public class Message {

    private Schema matchedSchema;
    private LinkedHashMap<String, Object> parseRequest;

    public Message(Schema matchedSchema, LinkedHashMap<String, Object> parseRequest) {
        this.matchedSchema = matchedSchema;
        this.parseRequest = parseRequest;
    }

}
