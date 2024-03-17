package br.com.ubots.chatbot.services;

import br.com.ubots.chatbot.domain.FaqAnswer;
import br.com.ubots.chatbot.utils.FaqAnswers;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FaqService {
    final private FaqAnswers faqAnswers = new FaqAnswers();


    public String getAnswer(String message) {
        return message;
    }
}
