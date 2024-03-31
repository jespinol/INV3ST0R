package com.jmel.inv3st0r;

import com.jmel.inv3st0r.controller.SignInUpController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class Inv3st0rApplicationTests {
    @Autowired
    private SignInUpController signInUpController;

    @Test
    void contextLoads() {
    }

    @Test
    void checkSignInControllerIsInjected() {
        assertThat(signInUpController).isNotNull();
    }
}
