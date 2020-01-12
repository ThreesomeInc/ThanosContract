package com.thanos.contract.infrastructure.parser.contract;

import com.thanos.contract.domain.Contract;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ContractParserTest {

    @Test
    public void parse() {

        final URL resource = ContractParserTest.class.getClassLoader().getResource("contracts/lims/bocs/");

        final ContractParser contractParser = new ContractParser();

        final List<Contract> result =
                contractParser.parse(resource.getPath(), "10400_test.yml");

        System.out.println(result);

        assertEquals(1, result.size());
        assertEquals("10400", result.get(0).getSchemaName());
        assertEquals("20200101", result.get(0).getSchemaVersion());
    }

    @Test
    public void parse_with_full_path() {
        final URL resource = ContractParserTest.class.getClassLoader().getResource("contracts/lims/bocs/10400_test.yml");

        final ContractParser contractParser = new ContractParser();

        final List<Contract> result =
                contractParser.parse(resource.getPath());

        System.out.println(result);

        assertEquals(1, result.size());
        assertEquals("10400", result.get(0).getSchemaName());
        assertEquals("20200101", result.get(0).getSchemaVersion());
    }

}