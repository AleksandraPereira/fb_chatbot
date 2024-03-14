package br.com.ubots.chatbot;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

@RestController
public class ChatBotController {
    private static final String VERIFY_TOKEN = "batata";

    @GetMapping("/webhook")
    public String verify( @RequestParam("hub.verify_token") String verifyToken, @RequestParam("hub.challenge")
                         String challenge) {
        if (Objects.equals(VERIFY_TOKEN, verifyToken)) {
            return challenge;
        } else {
            return String.valueOf(Integer.valueOf("Falha na verificação do token"));

        }
    }


    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(@RequestBody String message) {
      System.out.println(message);
    return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}

