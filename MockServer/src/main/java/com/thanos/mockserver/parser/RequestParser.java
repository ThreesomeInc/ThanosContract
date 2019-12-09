package com.thanos.mockserver.parser;

import com.thanos.mockserver.exception.ParseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestParser {

    public Request parseByTypeAndLength(String inputRequest, List<Schema> requestSchemaList) {
        Map<String, Object> fields = new HashMap<>();

        int startIndex = 0;
        try {
            for (Schema requestSchema : requestSchemaList) {

                if (requestSchema.getType().equals("CHAR")) {
                    fields.put(requestSchema.getName(),
                            inputRequest.substring(startIndex, startIndex + requestSchema.getLength()));
                    startIndex += requestSchema.getLength();

                } else if (requestSchema.getType().equals("NUM")) {
                    fields.put(requestSchema.getName(),
                            Integer.valueOf(inputRequest.substring(startIndex, startIndex + requestSchema.getLength())));
                    startIndex += requestSchema.getLength();

                } else {
                    throw new ParseException("Invalid Request Schema Type");
                }
            }
        } catch (StringIndexOutOfBoundsException stringIndexEx) {
            throw new ParseException("Input Request is shorter then schema expected!", stringIndexEx.getCause());
        } catch (NumberFormatException numFormatEx) {
            throw new ParseException("Expected to be NUM but turn out to be no!", numFormatEx.getCause());
        }


        return new Request(fields);
    }
}
