package com.thanos.mockserver.parser;

import org.junit.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.*;

public class ContractTest {

    @Test
    public void match() {
        final ContractParser contractParser = new ContractParser();
        final List<Contract> contractList = contractParser.parse("contracts/consumer1_provider/", "schema1_test.yml");
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
        fields.put("Flag", 1);
        fields.put("TranCode", "AA");
        final Msg request = new Msg(fields);

        assertEquals(2, contractList.size());

        assertTrue(contractList.get(0).match(request));
        assertFalse(contractList.get(1).match(request));
    }

    @Test
    public void buildResponse() throws IOException {
        final ContractParser contractParser = new ContractParser();
        final List<Contract> parse = contractParser.parse("contracts/consumer1_provider/", "schema1_test.yml");
        final SchemaParser schemaParser = new SchemaParser();
        final List<Schema> resSchema = schemaParser.parseRes("schema1");

        assertEquals(2, parse.size());

        assertTrue(parse.get(0).buildResponse(resSchema)
                .toFixLengthString(resSchema).startsWith("40420191212"));
        assertTrue(parse.get(1).buildResponse(resSchema).
                toFixLengthString(resSchema).startsWith("50020191213"));
    }

    @Test
    public void test_regex_generation() {
//        Generex generex = new Generex("(0|1){2}");
//        final String random = generex.random();
//        System.out.println("'"+random+"'");
        assertTrue("          ".matches("\\s{10}"));

    }
}