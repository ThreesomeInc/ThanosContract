package com.thanos.mockserver.handler;

import com.thanos.mockserver.parser.Contract;
import com.thanos.mockserver.parser.Msg;
import com.thanos.mockserver.parser.MsgParser;
import com.thanos.mockserver.parser.Schema;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

@Slf4j
public class RequestHandler extends Thread {

    private static final String CRLF = "\n";
    private static final String MISMATCH_RESPONSE = "Incoming request does not match any existing contract";

    private List<Schema> requestSchemaList;
    private List<Contract> contractList;

    public RequestHandler(List<Schema> requestSchemaList, List<Contract> contractList) {
        this.requestSchemaList = requestSchemaList;
        this.contractList = contractList;
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(0);
            log.info("MockServer up @ " + ss.getLocalPort());

            while (true) {
                Socket socket = ss.accept();
                log.info("Consumer: " + socket.getInetAddress().getLocalHost() + " connected");

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String input = br.readLine();

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bw.write(process(input) + CRLF);
                bw.flush();
            }
        } catch (IOException ioEx) {
            log.warn("IOException when create socket");
            ioEx.printStackTrace();
        }
    }

    String process(String inputRequest) {
        log.info("Process request ：" + inputRequest);

        final Msg request = new MsgParser().parseByTypeAndLength(inputRequest, requestSchemaList);
        if (request.validateByRegex(requestSchemaList)) {
            for (Contract contract : contractList) {
                if (contract.match(request)) {
                    return contract.buildResponse();
                }
            }
        }
        return MISMATCH_RESPONSE;
    }
}
