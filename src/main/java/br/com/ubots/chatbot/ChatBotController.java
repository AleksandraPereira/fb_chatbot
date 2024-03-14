package br.com.ubots.chatbot;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChatBotController {
    private static final String VERIFY_TOKEN = "batata";

    @GetMapping("/webhook")
    public String verify(@RequestParam("hub.verify_token") String verifyToken, @RequestParam("hub.challenge")
                         String challenge) {
        if (verifyToken.equals(VERIFY_TOKEN)) {
            return challenge;
        } else {
            return "Falha na verificação do token";

        }
    }

    /*
    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(@RequestBody String message) {
      System.out.println(message);
    return ResponseEntity.status(HttpStatus.OK).body("OK");
    */
}


