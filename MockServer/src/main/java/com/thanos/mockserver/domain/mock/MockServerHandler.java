package com.thanos.mockserver.domain.mock;

import com.google.common.io.CharStreams;
import com.thanos.mockserver.domain.Contract;
import com.thanos.mockserver.domain.Message;
import com.thanos.mockserver.domain.Schema;
import com.thanos.mockserver.domain.SimpleCache;
import com.thanos.mockserver.infrastructure.eventbus.EventBusFactory;
import com.thanos.mockserver.infrastructure.eventbus.NewMockEvent;
import lombok.extern.slf4j.Slf4j;
import org.jooq.lambda.Unchecked;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This is the handler for each MockServer thread
 */
@Slf4j
public class MockServerHandler implements Runnable {

    private static final String CRLF = System.lineSeparator();
    private static final String MISMATCH_RESPONSE = "Incoming request does not match any existing contract";

    private String index;
    private List<Contract> contractList;
    private List<Schema> schemaList;

    public MockServerHandler(String index) {
        this.index = index;
        buildLocalCache();
    }

    void buildLocalCache() {
        contractList = SimpleCache.getContracts().stream()
                .filter(contract -> contract.getIndex().equals(index))
                .collect(Collectors.toList());

        final List<String> schemaNeeded = contractList.stream()
                .map(Contract::getSchemaName).collect(Collectors.toList());
        schemaList = SimpleCache.getSchemas().stream()
                .filter(schema -> schemaNeeded.contains(schema.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void run() {
        try (ServerSocket ss = startServerSocket()) {
            while (true) {
                Socket socket = ss.accept();
                log.info("Consumer: " + socket.getInetAddress().getLocalHost() + " connected");
                new Thread(Unchecked.runnable(() -> {
                    String input = CharStreams.toString(new InputStreamReader(socket.getInputStream()));

                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    bw.write(process(input) + CRLF);
                    bw.flush();
                    socket.shutdownOutput();
                })).start();
            }
        } catch (IOException | UncheckedIOException ioEx) {
            log.warn("IOException when create socket");
            ioEx.printStackTrace();
        }
    }

    private ServerSocket startServerSocket() throws IOException {
        ServerSocket ss = new ServerSocket(0);
        log.info("MockServer for {} start up @ {}", index, ss.getLocalPort());

        EventBusFactory.getInstance().post(
                new NewMockEvent(contractList.get(0).getIndex(), ss.getLocalPort()));

        return ss;
    }


    String process(String inputRequest) {
        final Optional<Message> message = parseAndValidateMsg(inputRequest);

        if (message.isPresent()) {
            Message msg = message.get();
            log.info(msg.toString());

            final List<Contract> contractToMatch = contractList.stream()
                    .filter(newContract -> newContract.getSchemaName().equals(msg.getMatchedSchema().getName()))
                    .collect(Collectors.toList());
            for (Contract contract : contractToMatch) {
                if (contract.matchRequest(msg)) {
                    final String result = contract.buildResponse(msg.getMatchedSchema());
                    log.info("Matched contract {}/{}, responding [{}]",
                            contract.getIndex(), contract.getName(), result);
                    return result;
                }
            }
        }
        return MISMATCH_RESPONSE;
    }

    Optional<Message> parseAndValidateMsg(String inputRequest) {
        for (Schema schema : schemaList) {
            try {
                return Optional.of(schema.parseRequest(inputRequest));
            } catch (Exception ex) {
                log.debug("Schema {} fail to parse incoming request {}", schema.getName(), inputRequest);
            }
        }
        return Optional.empty();
    }
}
