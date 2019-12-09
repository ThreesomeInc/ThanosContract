package com.thanos.mockserver.handler;

import com.thanos.mockserver.exception.ParseException;
import com.thanos.mockserver.parser.Schema;
import com.thanos.mockserver.parser.SchemaParser;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class RequestHandlerTest {

    RequestHandler requestHandler;
    List<Schema> schema1;

    @Before
    public void setUp() throws Exception {
        requestHandler = new RequestHandler();
        final SchemaParser schemaParser = new SchemaParser();
        schema1 = schemaParser.parseReq("schema1");
    }

    @Test
    public void parse_health() throws IOException {
        final Request parse = requestHandler.parseByTypeAndLength("0AA", schema1);

        assertEquals(2, parse.getFields().size());
        assertTrue(parse.getFields().containsKey("TranCode"));
        assertEquals("AA", parse.getFields().get("TranCode"));
        assertTrue(parse.getFields().containsKey("Flag"));
        assertEquals(0, parse.getFields().get("Flag"));
    }

    @Test(expected = ParseException.class)
    public void parse_excpetion_when_too_short() throws IOException {
        final Request parse = requestHandler.parseByTypeAndLength("0A", schema1);
    }

    @Test
    public void parse_too_long_still_working() throws IOException {
        final Request parse = requestHandler.parseByTypeAndLength("0AAB", schema1);
        assertEquals(2, parse.getFields().size());
    }

    @Test(expected = ParseException.class)
    public void parse_exception_when_not_num() throws IOException {
        final Request parse = requestHandler.parseByTypeAndLength("AAA", schema1);
    }

    @Test
    public void validateByRegex_health() {
        final Request request = requestHandler.parseByTypeAndLength("0AA", schema1);
        final Boolean result = requestHandler.validateByRegex(request, schema1);
        assertTrue(result);
    }

    @Test
    public void validateByRegex_invalid() {
        final Request request = requestHandler.parseByTypeAndLength("0ZZ", schema1);
        final Boolean result = requestHandler.validateByRegex(request, schema1);
        assertFalse(result);
    }
}