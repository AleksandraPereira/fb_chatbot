package br.com.ubots.chatbot;


import br.com.ubots.chatbot.dto.MessageResponse;
import br.com.ubots.chatbot.services.FaqService;
import br.com.ubots.chatbot.utils.WeatherApp;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@RestController
public class ChatBotController {
    private static final String VERIFY_TOKEN = "batata";

    final private FaqService faqService;
    @Autowired
    private WeatherApp weatherApp;

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
    public ResponseEntity<MessageResponse> webhook(@RequestBody String body) throws IOException, URISyntaxException, InterruptedException {
        JSONObject requestJson = new JSONObject(body);
        JSONArray entryArray = requestJson.getJSONArray("entry");
        JSONObject firstEntry = entryArray.getJSONObject(0);
        JSONArray messagingArray = firstEntry.getJSONArray("messaging");
        JSONObject firstMessaging = messagingArray.getJSONObject(0);
        JSONObject message = firstMessaging.getJSONObject("message");


        String recipientId = firstMessaging.getJSONObject("sender").getString("id");

        String inputMessage = "";
        if (message.has("text")) {
            inputMessage = message.getString("text");
        }

        String filePath = "C:\\Users\\aleks\\Downloads\\chatbot\\chatbot\\src\\main\\resources\\static\\answers.json";
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONObject jsonObject = new JSONObject(content);
        MessageResponse messageModel = new MessageResponse();

        this.faqService.sendMessage(jsonObject, inputMessage, messageModel);

        try {
            String weather = weatherApp.getWeather();
            System.out.println("O tempo atual é: " + weather);
            weatherApp.sendToFacebook(weather, recipientId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(jsonObject);
        return ResponseEntity.ok().build();
    }

    public FaqService getFaqService() {
        return faqService;
    }
}

