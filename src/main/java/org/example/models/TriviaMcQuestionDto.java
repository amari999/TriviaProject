package org.example.models;

import lombok.Getter;

import java.util.List;

@Getter
public class TriviaMcQuestionDto {
    private String question;
    private List<String> allOptions;

    public TriviaMcQuestionDto(String question, List<String> allOptions){
        this.question = question;
        this.allOptions = allOptions;
    }
}
