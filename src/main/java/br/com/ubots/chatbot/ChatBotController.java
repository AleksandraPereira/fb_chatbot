package br.com.ubots.chatbot;


import br.com.ubots.chatbot.dto.MessageRequest;
import br.com.ubots.chatbot.utils.FaqAnswers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
    public ResponseEntity<String> webhook(@RequestBody MessageRequest request) {
        FaqAnswers faqAnswers = new FaqAnswers();
      System.out.println(request.message());
    return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}

