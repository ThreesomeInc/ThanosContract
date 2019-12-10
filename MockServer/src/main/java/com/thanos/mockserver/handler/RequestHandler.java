package com.thanos.mockserver.handler;

import com.thanos.mockserver.exception.ParseException;
import com.thanos.mockserver.parser.Contract;
import com.thanos.mockserver.parser.Msg;
import com.thanos.mockserver.parser.MsgParser;
import com.thanos.mockserver.parser.Schema;

import java.util.List;

public class RequestHandler {

    private List<Schema> requestSchemaList;
    private List<Contract> contractList;

    public RequestHandler(List<Schema> requestSchemaList, List<Contract> contractList) {
        this.requestSchemaList = requestSchemaList;
        this.contractList = contractList;
    }

    public String process(String inputRequest) {
        final Msg request = new MsgParser().parseByTypeAndLength(inputRequest, requestSchemaList);
        if (request.validateByRegex(requestSchemaList)) {
            for (Contract contract : contractList) {
                if (contract.match(request)) {
                    return contract.buildResponse();
                }
            }
        }
        throw new ParseException("Incoming request does not match any existing contract");
    }
}
