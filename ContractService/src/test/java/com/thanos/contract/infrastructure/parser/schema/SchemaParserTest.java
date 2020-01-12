package com.thanos.contract.infrastructure.parser.schema;

import com.thanos.contract.domain.Schema;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SchemaParserTest {

    @Test
    public void parse() {

        final URL resource = SchemaParserTest.class.getClassLoader().getResource("schemas/");
        SchemaParser schemaParser = new SchemaParser();

        final List<Schema> result = schemaParser.parse(resource.getPath(), "10400.yml");

        System.out.println(result);
        assertEquals(1, result.size());
        assertEquals("20200101", result.get(0).getVersion());

        assertEquals(5, result.get(0).getRequest().size());
        assertEquals("MESSAGE_TYPE", result.get(0).getRequest().get(2).getName());

        assertEquals(result.get(0).getResponse().size(), 5);

    }
}