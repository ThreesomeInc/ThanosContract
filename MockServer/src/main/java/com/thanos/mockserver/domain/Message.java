package com.thanos.mockserver.domain;

import com.thanos.mockserver.domain.schema.NewSchema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashMap;

@Getter
@ToString
@NoArgsConstructor
public class Message {

    private NewSchema matchedSchema;
    private LinkedHashMap<String, Object> parseRequest;
    private LinkedHashMap<String, Object> response;

    public Message(NewSchema matchedSchema, LinkedHashMap<String, Object> parseRequest) {
        this.matchedSchema = matchedSchema;
        this.parseRequest = parseRequest;
    }

    public void setResponse(LinkedHashMap<String, Object> response) {
        this.response = response;
    }
}
