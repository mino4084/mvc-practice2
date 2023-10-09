package org.example;

import org.example.calculator.domain.Calculator;
import org.example.calculator.domain.PositiveNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CustomWebApplicationServer {
    private final int port;

    private static final Logger logger = LoggerFactory.getLogger(CustomWebApplicationServer.class);

    public CustomWebApplicationServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("[CustomWebApplicationServer] started {}", port);

            Socket clientSocket;
            logger.info("[CustomWebApplicationServer] waiting for client");

            // 서버 소켓을 만든 다음, accept로 클라이언트를 기다린다
            while ((clientSocket = serverSocket.accept()) != null) {
                logger.info("[CustomWebApplicationServer] client connected");

                /**
                 * step 1 : 사용자 요청을 메인 Thread가 처리
                 */



                try (
                        InputStream in = clientSocket.getInputStream();
                        OutputStream out = clientSocket.getOutputStream();
                ) {
                    BufferedReader br =
                            new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                    DataOutputStream dos = new DataOutputStream(out);


                    /**
                     * 아래 내용대로 로그 출력
                     * GET / HTTP/1.1
                     * Host: localhost:8080
                     * Connection: Keep-Alive
                     * User-Agent: Apache-HttpClient/4.5.13 (Java/17.0.4)
                     * Accept-Encoding: gzip,deflate
                     */

                    /*

                    String line;
                    while ((line = br.readLine()) != ""){
                        System.out.println(line);

                    }
                     */

                    HttpRequest httpRequest = new HttpRequest(br);

                    if (httpRequest.isGetRequest() && httpRequest.matchPath("/calculate")) {
                         QueryStrings queryStrings = httpRequest.getQueryStrings();

                         int operand1 = Integer.parseInt(queryStrings.getValue("operand1"));
                         String operator = queryStrings.getValue("operator");
                         int operand2 = Integer.parseInt(queryStrings.getValue("operand2"));

                         int result = Calculator.calculate(new PositiveNumber(operand1), operator, new PositiveNumber(operand2));
                         byte[] body = String.valueOf(result).getBytes();

                         HttpResponse response = new HttpResponse(dos);
                         response.response200Header("application/json", body.length);
                         response.responseBody(body);
                    }

                }
            }
        }
    }
}
