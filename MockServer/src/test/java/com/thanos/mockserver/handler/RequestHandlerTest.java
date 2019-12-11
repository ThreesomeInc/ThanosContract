package com.thanos.mockserver.handler;

import com.thanos.mockserver.registry.RegisteredRecord;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RequestHandlerTest {

    RequestHandler requestHandler;

    @Before
    public void setUp() throws Exception {
        final RegisteredRecord registeredRecord =
                new RegisteredRecord("consumer1", "provider", "schema1");
        requestHandler = new RequestHandler(registeredRecord);
        requestHandler.init();
    }

    @Test
    public void process_match() {
        final String response = requestHandler.process("1AA");
        assertEquals("40420191212", response);
    }

    @Test
    public void process_mismatch() {
        final String response = requestHandler.process("1AB");
        assertEquals("Incoming request does not match any existing contract", response);
    }
}