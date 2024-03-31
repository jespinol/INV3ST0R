package com.jmel.inv3st0r;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {
    @LocalServerPort
    private int port = 8080;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void unauthenticatedUserGoingHomeReturnsCreateSignInPage() throws Exception {
        String uniqueTextInLogInPage = "Remember Me";
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/home", String.class))
                .contains(uniqueTextInLogInPage);
    }
}
