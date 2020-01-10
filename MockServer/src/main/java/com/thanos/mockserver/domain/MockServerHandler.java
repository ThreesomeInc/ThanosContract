package com.thanos.mockserver.domain;

import com.google.common.io.CharStreams;
import com.thanos.mockserver.domain.contract.NewContract;
import com.thanos.mockserver.domain.schema.NewSchema;
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
    private List<NewContract> contractList;
    private List<NewSchema> schemaList;

    public MockServerHandler(String index, List<NewContract> contractList, List<NewSchema> schemaList) {
        this.index = index;
        this.contractList = contractList;
        this.schemaList = schemaList;
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

        EventBusFactory.getInstance().post(new NewMockEvent(ss.getLocalPort(),
                contractList.get(0).getConsumer(),
                contractList.get(0).getProvider())
        );

        return ss;
    }


    String process(String inputRequest) {
        final Optional<Message> message = parseAndValidateMsg(inputRequest);

        if (message.isPresent()) {
            Message msg = message.get();
            log.info(msg.toString());

            final List<NewContract> contractToMatch = contractList.stream()
                    .filter(newContract -> newContract.getSchemaName().equals(msg.getMatchedSchema().getName()))
                    .collect(Collectors.toList());
            for (NewContract contract : contractToMatch) {
                if (contract.matchRequest(msg)) {
                    final String result = contract.buildResponse(msg.getMatchedSchema());
                    log.info("Contract {}/{} matched and response is [{}]",
                            contract.getIndex(), contract.getName(), result);
                    return result;
                }
            }
        }
        return MISMATCH_RESPONSE;
    }

    Optional<Message> parseAndValidateMsg(String inputRequest) {
        for (NewSchema schema : schemaList) {
            try {
                return Optional.of(schema.parseRequest(inputRequest));
            } catch (Exception ex) {
                log.debug("Schema {} fail to parse incoming request {}", schema.getName(), inputRequest);
            }
        }
        return Optional.empty();
    }
}
