package br.com.ubots.chatbot;


import br.com.ubots.chatbot.dto.MessageResponse;
import br.com.ubots.chatbot.services.FaqService;
import br.com.ubots.chatbot.utils.WeatherApp;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static br.com.ubots.chatbot.utils.WeatherApp.cidade;

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
        Gson gson = new Gson();
        JsonObject requestJson = gson.fromJson(body, JsonObject.class);
        JsonArray entryArray = requestJson.getAsJsonArray("entry");
        JsonObject firstEntry = entryArray.get(0).getAsJsonObject();
        JsonArray messagingArray = firstEntry.getAsJsonArray("messaging");
        JsonObject firstMessaging = messagingArray.get(0).getAsJsonObject();
        JsonObject message = firstMessaging.getAsJsonObject("message");

        String recipientId = firstMessaging.getAsJsonObject("sender").get("id").getAsString();

        String inputMessage = "";
        if (message.has("text")) {
            inputMessage = message.get("text").getAsString();
        }

        String filePath = "C:\\Users\\aleks\\Downloads\\chatbot\\chatbot\\src\\main\\resources\\static\\answers.json";
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        JsonObject jsonObject = gson.fromJson(content, JsonObject.class);
        MessageResponse messageModel = new MessageResponse();

        this.faqService.sendMessage(jsonObject, inputMessage, messageModel);

        try {
            StringBuilder weather = weatherApp.getWeather(cidade);
            System.out.println("O clima hoje é: " + weather);
            weatherApp.handleMessage(inputMessage, recipientId);
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

