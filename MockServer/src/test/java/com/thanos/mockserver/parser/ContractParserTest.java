package com.thanos.mockserver.parser;

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

    @Test
    public void parse_single() {
        final ContractParser contractParser = new ContractParser();

        final Contract contract = contractParser.parseSingle("contracts/consumer0_provider/", "schema1_test.yml");

        assertEquals("test case 1", contract.getName());
        assertTrue(contract.getReq().containsKey("Flag"));
        assertTrue(contract.getReq().containsKey("TranCode"));
        assertTrue(contract.getRes().containsKey("Status"));
        assertTrue(contract.getRes().containsKey("ResponseTime"));

    }
}