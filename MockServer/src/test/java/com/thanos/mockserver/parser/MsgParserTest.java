package com.thanos.mockserver.parser;

import com.thanos.mockserver.exception.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class MsgParserTest {

    MsgParser msgParser;
    List<Schema> schema1;

    @Before
    public void setUp() throws Exception {
        final SchemaParser schemaParser = new SchemaParser();
        schema1 = schemaParser.parseReq("schema1");
        msgParser = new MsgParser();
    }

    @Test
    public void parse_health() throws IOException {
        final Msg parse = msgParser.parseByTypeAndLength("0AA", schema1);

        assertEquals(2, parse.getFields().size());
        assertTrue(parse.getFields().containsKey("TranCode"));
        assertEquals("AA", parse.getFields().get("TranCode"));
        assertTrue(parse.getFields().containsKey("Flag"));
        assertEquals(0, parse.getFields().get("Flag"));
    }

    @Test(expected = ParseException.class)
    public void parse_excpetion_when_too_short() throws IOException {
        final Msg parse = msgParser.parseByTypeAndLength("0A", schema1);
    }

    @Test
    public void parse_too_long_still_working() throws IOException {
        final Msg parse = msgParser.parseByTypeAndLength("0AAB", schema1);
        assertEquals(2, parse.getFields().size());
    }

    @Test(expected = ParseException.class)
    public void parse_exception_when_not_num() throws IOException {
        final Msg parse = msgParser.parseByTypeAndLength("AAA", schema1);
    }

    @Test
    public void validateByRegex_health() {
        final Msg msg = msgParser.parseByTypeAndLength("0AA", schema1);
        final Boolean result = msg.validate(schema1);
        assertTrue(result);
    }

    @Test
    public void validateByRegex_invalid() {
        final Msg msg = msgParser.parseByTypeAndLength("0ZZ", schema1);
        final Boolean result = msg.validate(schema1);
        assertFalse(result);
    }
}