package org.example.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TriviaApiModel {
    private String question;
    private String correct_answer;
    private List<String> incorrect_answers;
}
