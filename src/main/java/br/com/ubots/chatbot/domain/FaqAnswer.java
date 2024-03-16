package br.com.ubots.chatbot.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FaqAnswer {
    private List<String> keywords;
    private String answer;

    public static String getDefaultAnswer() {
        return null;
    }
}
