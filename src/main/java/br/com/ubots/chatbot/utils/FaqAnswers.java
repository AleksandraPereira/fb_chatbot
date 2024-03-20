package br.com.ubots.chatbot.utils;

import br.com.ubots.chatbot.domain.FaqAnswer;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Getter
public class FaqAnswers {
    private ArrayList<FaqAnswer> answers;
    private String defaultAnswer;

    public FaqAnswers(){
        try {
            String conteudo = new String(Files.readAllBytes(Paths.get("src/main/resources/static/answers.json")));
            JSONTokener tokener = new JSONTokener(conteudo);

            JSONObject faqData = new JSONObject(tokener);
            JSONArray faqArray = faqData.getJSONArray("faq");
            this.answers = new ArrayList<>();

            for (int i = 0; i < faqArray.length(); i++) {
                JSONObject faqEntry = faqArray.getJSONObject(i);
                List<String> keywords = new ArrayList<>();
                if (faqEntry.has("keywords")) {
                    JSONArray keywordsArray = faqEntry.getJSONArray("keywords");
                    for (int j = 0; j < keywordsArray.length(); j++) {
                        keywords.add(keywordsArray.getString(j));
                    }
                }
                String answer = "";
                if (faqEntry.has("answer")) {
                    answer = faqEntry.getString("answer");
                }
                this.answers.add(new FaqAnswer(keywords, answer));
            }

            this.defaultAnswer = faqData.getString("default");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

}

