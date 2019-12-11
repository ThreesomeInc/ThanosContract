package com.thanos.mockserver.parser;

import com.mifmif.common.regex.Generex;
import org.junit.Test;

import java.io.IOException;
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
    public void buildResponse() throws IOException {
        final ContractParser contractParser = new ContractParser();
        final List<Contract> parse = contractParser.parse("contracts/consumer1_provider/", "schema1_test.yml");
        final SchemaParser schemaParser = new SchemaParser();
        final List<Schema> resSchema = schemaParser.parseRes("schema1");

        assertEquals(2, parse.size());
        assertTrue(parse.get(0).buildResponse(resSchema).startsWith("40420191212"));
        assertTrue(parse.get(1).buildResponse(resSchema).startsWith("50020191213"));
    }

    @Test
    public void test_regex_generation() {
        Generex generex = new Generex("\\d{3}");
        final String random = generex.random();
        System.out.println(random);
    }
}