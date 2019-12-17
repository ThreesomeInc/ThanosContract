package com.thanos.mockserver.handler;

import com.google.common.io.CharStreams;
import com.thanos.mockserver.exception.ParseException;
import com.thanos.mockserver.parser.Contract;
import com.thanos.mockserver.parser.Msg;
import com.thanos.mockserver.parser.MsgParser;
import com.thanos.mockserver.parser.Schema;
import com.thanos.mockserver.registry.RegisteredRecord;
import lombok.extern.slf4j.Slf4j;
import org.jooq.lambda.Unchecked;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

@Slf4j
public class RequestHandler implements Runnable {

	private static final String CRLF = System.lineSeparator();
	private static final String MISMATCH_RESPONSE = "Incoming request does not match any existing contract";

	private List<Schema> requestSchemaList;
	private List<Schema> responseSchemaList;
	private List<Contract> contractList;
	private RegisteredRecord registeredRecord;

	public RequestHandler(RegisteredRecord registeredRecord) {
		this.registeredRecord = registeredRecord;
	}

	@Override
	public void run() {
		init();

		try (ServerSocket ss = startServerSocket()) {
			while (true) {
				Socket socket = ss.accept();
				log.info("Consumer: " + socket.getInetAddress().getLocalHost() + " connected");
				new Thread(Unchecked.runnable(() -> {
					String input = CharStreams.toString(new InputStreamReader(socket.getInputStream()));
					socket.shutdownInput();
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					bw.write(process(input) + CRLF);
					bw.flush();
					socket.shutdownOutput();
				})).start();
			}
		} catch (IOException ioEx) {
			log.warn("IOException when create socket");
			ioEx.printStackTrace();
		}
	}

	private ServerSocket startServerSocket() throws IOException {
		ServerSocket ss = new ServerSocket(0);
		log.info("MockServer for {}-{}/{} start up @ {}",
				registeredRecord.getConsumer(), registeredRecord.getProvider(),
				registeredRecord.getSchemaName(), ss.getLocalPort());
		return ss;
	}

	void init() {
		try {
			contractList = registeredRecord.getContracts();
			requestSchemaList = registeredRecord.getReqSchemas();
			responseSchemaList = registeredRecord.getResSchemas();
		} catch (IOException e) {
			throw new ParseException("Fail to init RequesHandler", e);
		}
	}

	String process(String inputRequest) {
		log.info("Process request ：" + inputRequest);

		final Msg request = new MsgParser().parseByTypeAndLength(inputRequest, requestSchemaList);
		log.info(request.toString());

		if (request.validate(requestSchemaList)) {
			for (Contract contract : contractList) {
				if (contract.match(request)) {
					return contract.buildResponse(responseSchemaList);
				}
			}
		}
		return MISMATCH_RESPONSE;
	}
}
