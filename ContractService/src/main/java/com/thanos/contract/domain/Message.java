package com.thanos.contract.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;

@Getter
@NoArgsConstructor
public class Message {

    private Schema matchedSchema;
    private LinkedHashMap<String, String> parseRequest;

    public Message(Schema matchedSchema, LinkedHashMap<String, String> parseRequest) {
        this.matchedSchema = matchedSchema;
        this.parseRequest = parseRequest;
    }

    @Override
    public String toString() {
        return "Message{" +
                "matchedSchema=" +
                matchedSchema.getName() + '|' +
                matchedSchema.getVersion() + '|' +
                matchedSchema.getProvider() +
                ", parseRequest=" + parseRequest +
                '}';
    }
}
