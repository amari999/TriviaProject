package org.example.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TriviaApiResponseModel {
    private int response_code;
    private List<TriviaApiModel> results;
}
