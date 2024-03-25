package br.com.ubots.chatbot.services;

import br.com.ubots.chatbot.dto.MessageResponse;
import br.com.ubots.chatbot.utils.FaqAnswers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FaqService {
    final private FaqAnswers faqAnswers;
    final private static String PAGE_ACCESS_TOKEN = "EAAS8uQG7KXsBOxEi2d8tGj1yhqojz3DUIHNbKLIsxbf0J8UBBCtfMVTxcEzZCNILEyIjb2i8X0wWlZChQWSd1bLmpLg0qmZBzVHPUGlleOYZAuC9pZCe86I35YsgbZAiRYZARJi56tXZAWLFL028xBkEydJ2ij33ZBkPrmdulOoRq0inQIi1dZAP5XfVZAjezNw4R6ZC";

    @Autowired
    public FaqService(FaqAnswers faqAnswers) {
        this.faqAnswers = faqAnswers;
    }

    public void sendMessage(JsonObject jsonObject, String inputMessage, MessageResponse messageModel) throws IOException, URISyntaxException, InterruptedException {
        JsonArray faqArray = jsonObject.getAsJsonArray("faq");
        String responseMessage = "";
        for (int i = 0; i < faqArray.size(); i++) {
            JsonObject faqItem = faqArray.get(i).getAsJsonObject();
            JsonArray keywords = faqItem.getAsJsonArray("keywords");
            String answer = faqItem.get("answer").getAsString();

            for (int j = 0; j < keywords.size(); j++) {
                Pattern pattern = Pattern.compile("\\b" + keywords.get(j).getAsString() + "\\b", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(inputMessage);
                if (matcher.find()) {
                    responseMessage = answer;
                    break;
                }
            }

            if (!responseMessage.isEmpty()) {
                break;
            }
        }

        if (responseMessage.isEmpty()) {
            responseMessage = jsonObject.get("default").getAsString();
        }

        MessageResponse.Recipient recipient = new MessageResponse.Recipient();
        recipient.setId("8143569979003236");

        MessageResponse.Message message = new MessageResponse.Message();
        message.setText(responseMessage);

        messageModel.setRecipient(recipient);
        messageModel.setMessaging_type("RESPONSE");
        messageModel.setMessage(message);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://graph.facebook.com/v19.0/255909690937724/messages?access_token=" + PAGE_ACCESS_TOKEN))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(messageModel.responseBody()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response status code: " + response.statusCode());
        System.out.println("Response body: " + response.body());
    }
}

