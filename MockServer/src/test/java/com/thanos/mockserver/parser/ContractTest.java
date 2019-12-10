package com.thanos.mockserver.parser;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ContractTest {

    @Test
    public void match() {
        final ContractParser contractParser = new ContractParser();
        final List<Contract> contractList = contractParser.parse("contracts/consumer1_provider/", "schema1_test.yml");
        Map<String, Object> fields = new HashMap<>();
        fields.put("Flag", 1);
        fields.put("TranCode", "AA");
        final Msg request = new Msg(fields);

        assertEquals(2, contractList.size());

        assertTrue(contractList.get(0).match(request));
        assertFalse(contractList.get(1).match(request));
    }

    @Test
    public void buildResponse() {
        final ContractParser contractParser = new ContractParser();
        final List<Contract> parse = contractParser.parse("contracts/consumer1_provider/", "schema1_test.yml");
        assertEquals(2, parse.size());
        assertEquals("40420191212", parse.get(0).buildResponse());
        assertEquals("50020191213", parse.get(1).buildResponse());
    }
}