package com.thanos.mockserver.parser;

import com.thanos.mockserver.validate.RegexValidator;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SchemaParserTest {

    @Test
    public void parse() throws IOException {
        final String path = "/schemas/schema1_req.csv";
        final SchemaParser schemaParser = new SchemaParser();

        final List<Schema> result = schemaParser.parse(path);

        assertEquals(2, result.size());
        final Schema schema1 = new Schema(1, "Flag", "NUM", 1, new RegexValidator("0|1"));
        final Schema schema2 = new Schema(2, "TranCode", "CHAR", 2, new RegexValidator("AA|BB|CC|DD|EE|FF"));
        assertTrue(result.contains(schema1));
        assertTrue(result.contains(schema2));
    }

    @Test
    public void parseReq() throws IOException {
        final String schemaName = "schema1";

        final SchemaParser schemaParser = new SchemaParser();
        final List<Schema> result = schemaParser.parseReq(schemaName);

        assertEquals(2, result.size());
        final Schema schema1 = new Schema(1, "Flag", "NUM", 1, new RegexValidator("0|1"));
        final Schema schema2 = new Schema(2, "TranCode", "CHAR", 2, new RegexValidator("AA|BB|CC|DD|EE|FF"));
        assertTrue(result.contains(schema1));
        assertTrue(result.contains(schema2));
    }
}