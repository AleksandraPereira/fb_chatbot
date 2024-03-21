package br.com.ubots.chatbot.utils;

import br.com.ubots.chatbot.domain.FaqAnswer;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Getter
public class FaqAnswers {
    private ArrayList<FaqAnswer> answers;
    private String defaultAnswer;

    public FaqAnswers() {
        try {
            String conteudo = new String(Files.readAllBytes(Paths.get("src/main/resources/static/answers.json")));

            Pattern pattern = Pattern.compile("\"faq\"\\s*:\\s*\\[(.*?)\\]\\s*,\\s*\"default\"\\s*:\\s*\"(.*?)\"");
            Matcher matcher = pattern.matcher(conteudo);

            if (matcher.find()) {
                String faqArray = matcher.group(1);
                this.defaultAnswer = matcher.group(2);

                pattern = Pattern.compile("\\{\\s*\"keywords\"\\s*:\\s*\\[(.*?)\\]\\s*,\\s*\"answer\"\\s*:\\s*\"(.*?)\"\\s*\\}");
                matcher = pattern.matcher(faqArray);

                this.answers = new ArrayList<>();

                while (matcher.find()) {
                    String keywordsString = matcher.group(1);
                    String answer = matcher.group(2);

                    List<String> keywords = Arrays.asList(keywordsString.split("\\s*,\\s*"));
                    this.answers.add(new FaqAnswer(keywords, answer));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
