package hexlet.code.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import io.sentry.Sentry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@RestController
public class WelcomeController {

    @GetMapping(path = "/welcome")
    public String welcome() {
        return "Welcome to Spring!";
    }


    @GetMapping(path = "/test", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> testError() throws IOException {
        CompletableFuture.runAsync(() -> {
            try {
                throw new Exception("This is a test.");
            } catch (Exception e) {
                Sentry.captureException(e);
            }
        });

        var resource = new ClassPathResource("static/test-error.html");
        String html = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        return ResponseEntity.ok(html);
    }


    @GetMapping(path = "/tester")
    public void testErrorNext() {
        try {
            throw new Exception("This is a test.");
        } catch (Exception e) {
            Sentry.captureException(e);
        }
    }
}

