package com.thanos.mockserver.infrastructure.parser;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ContractParserTest {

    @Test
    public void parse() {
        final ContractParser contractParser = new ContractParser();

        final List<Contract> parse = contractParser.parse("contracts/consumer1_provider/", "schema1_test.yml");

        assertEquals(2, parse.size());

        final Contract result = parse.get(0);
        assertEquals("test case 1", result.getName());
        assertTrue(result.getReq().containsKey("Flag"));
        assertTrue(result.getReq().containsKey("TranCode"));
        assertTrue(result.getRes().containsKey("Status"));
        assertTrue(result.getRes().containsKey("ResponseTime"));
    }

}