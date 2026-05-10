package com.example;

import com.example.web.RedirectController;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class RedirectControllerTest {
    @Test
    void allowsRelativeApplicationPath() {
        URI u = new RedirectController().redirectTo("/dashboard");
        assertEquals("/dashboard", u.toString());
    }

    @Test
    void allowsSameOriginAbsolute() {
        URI u = new RedirectController().redirectTo("https://example.com/safe");
        assertEquals("example.com", u.getHost());
    }
}
