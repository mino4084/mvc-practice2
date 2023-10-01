package org.example;

import org.example.RequestLine;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestLineTest {
    @Test
    void create() {
        RequestLine requestLine =
                new RequestLine("GET /calculate?operand1=1&operator+=&operand2=23 HTTP/1.1");
        assertThat(requestLine).isNotNull();
    }
}
