package org.example.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
public class TriviaCard {
    private String question;
    private String correctAnswer;
    private List<String> allOptions;

    public TriviaCard(String question, String correctAnswer, List<String> allOptions){
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.allOptions = allOptions;
    }
}
