package com.thanos.mockserver.parser;

import com.thanos.mockserver.exception.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RequestParserTest {

    @Test
    public void parse_health() throws IOException {
        final RequestParser requestParser = new RequestParser();
        final SchemaParser schemaParser = new SchemaParser();
        final List<Schema> schema1 = schemaParser.parseReq("schema1");

        final Request parse = requestParser.parseByTypeAndLength("0AA", schema1);

        assertEquals(2, parse.getFields().size());
        assertTrue(parse.getFields().containsKey("TranCode"));
        assertEquals("AA", parse.getFields().get("TranCode"));
        assertTrue(parse.getFields().containsKey("Flag"));
        assertEquals(0, parse.getFields().get("Flag"));
    }

    @Test(expected = ParseException.class)
    public void parse_excpetion_when_too_short() throws IOException {
        final RequestParser requestParser = new RequestParser();
        final SchemaParser schemaParser = new SchemaParser();
        final List<Schema> schema1 = schemaParser.parseReq("schema1");

        final Request parse = requestParser.parseByTypeAndLength("0A", schema1);
    }

    @Test
    public void parse_too_long_still_working() throws IOException {
        final RequestParser requestParser = new RequestParser();
        final SchemaParser schemaParser = new SchemaParser();
        final List<Schema> schema1 = schemaParser.parseReq("schema1");

        final Request parse = requestParser.parseByTypeAndLength("0AAB", schema1);

        assertEquals(2, parse.getFields().size());
    }

    @Test(expected = ParseException.class)
    public void parse_exception_when_not_num() throws IOException {
        final RequestParser requestParser = new RequestParser();
        final SchemaParser schemaParser = new SchemaParser();
        final List<Schema> schema1 = schemaParser.parseReq("schema1");

        final Request parse = requestParser.parseByTypeAndLength("AAA", schema1);
    }
}