
package br.com.ubots.chatbot.utils;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WeatherApp {
    private static final String API_KEY = "beec0bce9f3e21c7d0c26b135c731b4c";
    public static final String cidade = "porto alegre";

    public StringBuilder getWeather(String cidade) throws Exception {
        String link = "https://api.openweathermap.org/data/2.5/weather?q=" + URLEncoder.encode(cidade, "UTF-8") + "&appid=" + API_KEY;
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String output;
        StringBuilder response = new StringBuilder();
        while ((output = br.readLine()) != null) {
            response.append(output);
        }

        conn.disconnect();

        ConversaoKelvinCelcius parser = new ConversaoKelvinCelcius();
        return new StringBuilder(parser.converte(response.toString()));
    }


    public void handleMessage(String message, String recipientId) throws Exception {
        Pattern pattern = Pattern.compile("(previsão do tempo|previsão do tempo hoje|clima hoje|clima)");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String weather = String.valueOf(getWeather(cidade));
            System.out.println("A previsão do tempo hoje é: " + weather);
            sendToFacebook(weather, recipientId);
        }
    }

    public void sendToFacebook(String response, String recipientId) throws Exception {
        String url = "https://graph.facebook.com/v19.0/255909690937724/messages?access_token=EAAS8uQG7KXsBOxEi2d8tGj1yhqojz3DUIHNbKLIsxbf0J8UBBCtfMVTxcEzZCNILEyIjb2i8X0wWlZChQWSd1bLmpLg0qmZBzVHPUGlleOYZAuC9pZCe86I35YsgbZAiRYZARJi56tXZAWLFL028xBkEydJ2ij33ZBkPrmdulOoRq0inQIi1dZAP5XfVZAjezNw4R6ZC";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<>();
        map.put("id", recipientId);
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("text", response);
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("recipient", map);
        jsonMap.put("message", messageMap);
        String jsonInputString = gson.toJson(jsonMap);

        con.setDoOutput(true);
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

    }
}

