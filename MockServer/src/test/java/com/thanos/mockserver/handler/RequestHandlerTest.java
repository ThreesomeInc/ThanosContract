package com.thanos.mockserver.handler;

import com.thanos.mockserver.exception.ParseException;
import com.thanos.mockserver.parser.Schema;
import com.thanos.mockserver.parser.SchemaParser;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RequestHandlerTest {

    RequestHandler requestHandler;

    @Before
    public void setUp() throws Exception {
        final SchemaParser schemaParser = new SchemaParser();
        List<Schema> schema1 = schemaParser.parseReq("schema1");
        requestHandler = new RequestHandler(schema1, new ArrayList<>());
    }

    @Test
    public void parse_health() throws IOException {
        final Request parse = requestHandler.parseByTypeAndLength("0AA");

        assertEquals(2, parse.getFields().size());
        assertTrue(parse.getFields().containsKey("TranCode"));
        assertEquals("AA", parse.getFields().get("TranCode"));
        assertTrue(parse.getFields().containsKey("Flag"));
        assertEquals(0, parse.getFields().get("Flag"));
    }

    @Test(expected = ParseException.class)
    public void parse_excpetion_when_too_short() throws IOException {
        final Request parse = requestHandler.parseByTypeAndLength("0A");
    }

    @Test
    public void parse_too_long_still_working() throws IOException {
        final Request parse = requestHandler.parseByTypeAndLength("0AAB");
        assertEquals(2, parse.getFields().size());
    }

    @Test(expected = ParseException.class)
    public void parse_exception_when_not_num() throws IOException {
        final Request parse = requestHandler.parseByTypeAndLength("AAA");
    }

    @Test
    public void validateByRegex_health() {
        final Request request = requestHandler.parseByTypeAndLength("0AA");
        final Boolean result = request.validateByRegex(requestHandler.getRequestSchemaList());
        assertTrue(result);
    }

    @Test
    public void validateByRegex_invalid() {
        final Request request = requestHandler.parseByTypeAndLength("0ZZ");
        final Boolean result = request.validateByRegex(requestHandler.getRequestSchemaList());
        assertFalse(result);
    }
}