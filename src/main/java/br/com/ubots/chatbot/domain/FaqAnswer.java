package br.com.ubots.chatbot.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FaqAnswer {
    private List<String> keywords;
    private String answer;

    public FaqAnswer(List<String> keywords, String answer) {
        this.keywords = keywords;
        this.answer = answer;
    }
}