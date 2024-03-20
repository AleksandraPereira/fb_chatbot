
package br.com.ubots.chatbot.utils;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class WeatherApp {
    private static final String API_KEY = "beec0bce9f3e21c7d0c26b135c731b4c";
    private static final String cidade = "porto alegre";
    private static final String link = "https://api.openweathermap.org/data/2.5/weather?q=porto%20alegre&appid=beec0bce9f3e21c7d0c26b135c731b4c";

    public String getWeather() throws Exception {
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String output;
        String response = "";
        while ((output = br.readLine()) != null) {
            response += output;
        }

        conn.disconnect();

        JSONObject jsonObject = new JSONObject(response);
        String descricao = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
        double temperatura = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;

        return descricao + ", " + temperatura;
    }

    public void sendToFacebook(String response, String recipientId) throws Exception {
        String url = "https://graph.facebook.com/v19.0/255909690937724/messages?access_token=EAAS8uQG7KXsBOxEi2d8tGj1yhqojz3DUIHNbKLIsxbf0J8UBBCtfMVTxcEzZCNILEyIjb2i8X0wWlZChQWSd1bLmpLg0qmZBzVHPUGlleOYZAuC9pZCe86I35YsgbZAiRYZARJi56tXZAWLFL028xBkEydJ2ij33ZBkPrmdulOoRq0inQIi1dZAP5XfVZAjezNw4R6ZC";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        // Create the JSON object to send to the Facebook API
        String jsonInputString = "{\"recipient\":{\"id\":\"" + recipientId + "\"},\"message\":{\"text\":\"" + response + "\"}}";

        // Send post request
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

