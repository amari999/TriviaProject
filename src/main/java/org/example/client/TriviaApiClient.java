package org.example.client;
import org.example.models.TriviaApiModel;
import org.example.models.TriviaApiResponseModel;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class TriviaApiClient {
    private final RestClient client;

    public TriviaApiClient(){
        this.client = RestClient.builder()
                .baseUrl("https://opentdb.com")
                .build();
    }

    public List<TriviaApiModel> fetchQuestions(int amount, String difficulty){
        TriviaApiResponseModel response = client.get()
                .uri(builder -> builder
                        .path("/api.php")
                        .queryParam("amount", amount)
                        .queryParam("difficulty", difficulty)
                        .queryParam("type", "multiple")
                        .queryParam("encode", "url3986")
                        .build())
                .retrieve()
                .body(TriviaApiResponseModel.class);

        if(response == null || response.getResponse_code() != 0 || response.getResults() == null || response.getResults().isEmpty()) throw new IllegalStateException("no api results returned");

        return response.getResults();
    }
}
