package com.thanos.mockserver.infrastructure.parser.contract;

import com.thanos.mockserver.domain.contract.NewContract;
import com.thanos.mockserver.domain.schema.SchemaService;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NewContractParserTest {

    @Test
    public void parse() {

        final URL resource = SchemaService.class.getClassLoader().getResource("contracts/lims/bocs/");

        final NewContractParser newContractParser = new NewContractParser();

        final List<NewContract> result =
                newContractParser.parse(resource.getPath(), "10400_test.yml");

        System.out.println(result);

        assertEquals(1, result.size());
        assertEquals("10400", result.get(0).getSchemaName());
        assertEquals("20200101", result.get(0).getSchemaVersion());
    }

    @Test
    public void parse_with_full_path() {
        final URL resource = SchemaService.class.getClassLoader().getResource("contracts/lims/bocs/10400_test.yml");

        final NewContractParser newContractParser = new NewContractParser();

        final List<NewContract> result =
                newContractParser.parse(resource.getPath());

        System.out.println(result);

        assertEquals(1, result.size());
        assertEquals("10400", result.get(0).getSchemaName());
        assertEquals("20200101", result.get(0).getSchemaVersion());
    }

}