package com.thanos.mockserver.validate;

import com.thanos.mockserver.parser.Schema;

public class SchemaValidator {

    private Schema requestSchema;
    private Schema responseSchema;

    public SchemaValidator(Schema requestSchema, Schema responseSchema) {
        this.requestSchema = requestSchema;
        this.responseSchema = responseSchema;
    }

    public Boolean validate(String incomingRequest) {


        return false;
    }
}
