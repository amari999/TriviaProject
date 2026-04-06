package org.example.services.interfaces;

import org.example.models.TriviaCard;

import java.util.List;

public interface TriviaService {
    List<TriviaCard> getCards(int amount, String difficulty);
}
