package com.thanos.mockserver.infrastructure.parser.schema;

import com.thanos.mockserver.domain.schema.NewSchema;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SchemaYamlParserTest {

    @Test
    public void parse() {

        SchemaYamlParser schemaYamlParser = new SchemaYamlParser();

        final List<NewSchema> result = schemaYamlParser.parse("schemas/", "10400.yml");

        System.out.println(result);
        assertEquals(1, result.size());
        assertEquals("20200101", result.get(0).getVersion());

        assertEquals(5, result.get(0).getRequest().size());
        assertEquals("MESSAGE_TYPE", result.get(0).getRequest().get(2).getName());
        assertTrue(result.get(0).getRequest().get(2).getMatchingKey());

        assertEquals(result.get(0).getResponse().size(), 5);

    }
}