package br.com.ubots.chatbot.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ConversaoKelvinCelcius {
    public String converte(String response) {
        // Suponha que a resposta seja um JSON e que a temperatura em Kelvin esteja armazenada no campo "temp".
        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
        double tempKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();

        // Convertendo a temperatura de Kelvin para Celsius.
        double tempCelsius = tempKelvin - 273.15;

        return String.format("A temperatura é %.0f°C", tempCelsius);
    }
}

