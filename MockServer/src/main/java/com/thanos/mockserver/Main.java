package com.thanos.mockserver;


import com.google.common.io.CharStreams;
import com.google.common.net.MediaType;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import com.thanos.mockserver.handler.RequestHandler;
import com.thanos.mockserver.parser.Contract;
import com.thanos.mockserver.parser.Schema;
import com.thanos.mockserver.registry.RegisteredRecord;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class Main {

    static final List<RegisteredRecord> fullRecord = Arrays.asList(
            new RegisteredRecord("consumer0", "provider", "schema1"),
            new RegisteredRecord("consumer1", "provider", "schema1"));


    public static void main(String[] args) {
        log.info("Mock Server is up!");
        startup();
    }

    private static void startup() {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            for (RegisteredRecord registeredRecord : fullRecord) {
                final List<Contract> contracts = registeredRecord.getContracts();
                final List<Schema> reqSchemas = registeredRecord.getReqSchemas();
                executor.execute(new RequestHandler(registeredRecord));
            }
            startupSchemaServer();
        } catch (IOException ioException) {
            log.error("Schema or contract not found");
            ioException.printStackTrace();
            System.exit(0);
        }
    }

    private static void startupSchemaServer() throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8081), 3);
        httpServer.createContext("/ep-by-schema", (ctx) -> {
            Headers headers = ctx.getRequestHeaders();

            Function<String, Map<String, String>> paramMapFunc = (query) -> Stream.of(query.split("&"))
                    .map(s -> s.split("=", 2))
                    .collect(Collectors.toMap(a -> a[0], a -> a.length > 1 ? URLDecoder.decode(a[1]) : ""));

            Map<String, String> queryMap = paramMapFunc.apply(ctx.getRequestURI().getQuery());
            String schema = queryMap.getOrDefault("schema", "");

            if (!queryMap.containsKey("schema") && MediaType.FORM_DATA.toString().equals(headers.getFirst("Content-Type"))) {
                Map<String, String> paramMap = paramMapFunc.apply(CharStreams.toString(new InputStreamReader(ctx.getRequestBody())));
                schema = paramMap.getOrDefault("schema", "");
            }
            if ("".equals(schema)) {
                ctx.getResponseBody().write("{'data':{},'msg':'the schema is empty', 'code':1}".getBytes());
                ctx.getResponseBody().flush();
                return;
            }
            ctx.getResponseBody().write(fetchEndpointsBySchema(schema));
            ctx.getResponseBody().flush();
        });
        httpServer.start();
    }

    private static byte[] fetchEndpointsBySchema(String schema) {
        // TODO: to be implemented...
        return "{'data':'127.0.0.1:38810', 'msg':'success', 'code':0}".getBytes();
    }
}
