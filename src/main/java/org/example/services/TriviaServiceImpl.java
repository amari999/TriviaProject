package org.example.services;

import org.example.client.TriviaApiClient;
import org.example.models.TriviaApiModel;
import org.example.models.TriviaCard;
import org.example.services.interfaces.TriviaService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TriviaServiceImpl implements TriviaService {

    private final TriviaApiClient client;

    public TriviaServiceImpl(TriviaApiClient client){
        this.client = client;
    }

    @Override
    public List<TriviaCard> getCards(int amount, String difficulty) {
        return client.fetchQuestions(amount, difficulty).stream().map(this::convertCard).toList();
    }

    private TriviaCard convertCard(TriviaApiModel apiModel){
        List<String> allOptions = new ArrayList<>(apiModel.getIncorrect_answers());
        allOptions.add(apiModel.getCorrect_answer());
        Collections.shuffle(allOptions);

        return new TriviaCard(
                URLDecoder.decode(apiModel.getQuestion(), StandardCharsets.UTF_8),
                URLDecoder.decode(apiModel.getCorrect_answer(), StandardCharsets.UTF_8),
                allOptions.stream().map(option -> URLDecoder.decode(option, StandardCharsets.UTF_8)).toList()
        );
    }
}
