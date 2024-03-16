package br.com.ubots.chatbot;


import br.com.ubots.chatbot.dto.MessageRequest;
import br.com.ubots.chatbot.dto.MessageResponse;
import br.com.ubots.chatbot.services.FaqService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class ChatBotController {
    private static final String VERIFY_TOKEN = "batata";

    final private FaqService faqService;

    public ChatBotController(FaqService faqService){
        this.faqService = faqService;
    }

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
    public ResponseEntity<MessageResponse> webhook(@RequestBody MessageRequest request) {
        String answer = this.faqService.getAnswer(request.message());
        MessageResponse response = new MessageResponse(answer);
    return ResponseEntity.ok(response);
    }
}

