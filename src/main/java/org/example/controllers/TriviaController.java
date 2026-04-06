package org.example.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.models.TriviaCard;
import org.example.models.TriviaMcQuestionDto;
import org.example.services.interfaces.TriviaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/trivia")
public class TriviaController {

    private final TriviaService service;

    public TriviaController(TriviaService service){
        this.service = service;
    }

    @GetMapping
    public String getQuestion(Model model, HttpSession session){
        Integer score = (Integer) session.getAttribute("score");
        Integer currentIndex = (Integer) session.getAttribute("currentIndex");
        List<TriviaCard> cards = (List<TriviaCard>) session.getAttribute("cards");
        String difficulty = (String) session.getAttribute("difficulty");
        if (score == null) {
            score = 0;
            session.setAttribute("score", score);
        }
        if(difficulty == null){
            difficulty = "easy";
            session.setAttribute("difficulty", difficulty);
        }
        if (cards == null || cards.isEmpty()) {
            Integer amount = (Integer) session.getAttribute("amount");
            if(amount == null){
                amount = 10;
                session.setAttribute("amount", amount);
            }
            cards = service.getCards(amount, difficulty);
            session.setAttribute("cards", cards);
        }
        if(currentIndex == null){
            currentIndex = 0;
            session.setAttribute("currentIndex", currentIndex);
        }

        if(currentIndex >= cards.size()){
            model.addAttribute("score", score);
            model.addAttribute("total", cards.size());
            model.addAttribute("difficulty", difficulty);
            return "final";
        }

        TriviaCard currentCard = cards.get(currentIndex);

        session.setAttribute("currentCard", currentCard);

        TriviaMcQuestionDto questionDto = new TriviaMcQuestionDto(
                currentCard.getQuestion(),
                currentCard.getAllOptions());
        model.addAttribute("questionDto", questionDto);
        model.addAttribute("score", score);
        model.addAttribute("total", cards.size());
        model.addAttribute("currentIndex", currentIndex);
        model.addAttribute("difficulty", difficulty);

        return "question";
    }

    @PostMapping("/new")
    public String startNewGame(@RequestParam int amount, @RequestParam String difficulty, HttpSession session)
    {
        session.removeAttribute("cards");
        session.removeAttribute("currentIndex");
        session.removeAttribute("score");
        session.removeAttribute("currentCard");

        if (amount != 5 && amount != 10 && amount != 20 && amount != 30) {
            amount = 10;
        }

        if (!difficulty.equals("easy") &&
                !difficulty.equals("medium") &&
                !difficulty.equals("hard")) {
            difficulty = "easy";
        }

        session.setAttribute("amount", amount);
        session.setAttribute("difficulty", difficulty);
        return "redirect:/trivia";
    }

    @PostMapping("/answer")
    public String checkAnswer(@RequestParam String chosenAnswer, Model model, HttpSession session){

        TriviaCard currentCard = (TriviaCard) session.getAttribute("currentCard");
        if(currentCard == null){
            model.addAttribute("error", "no triviacard found");
            return "result";
        }



        boolean isCorrect = currentCard.getCorrectAnswer().equals(chosenAnswer);

        Integer score = (Integer) session.getAttribute("score");
        if(score == null){score = 0;}
        if(isCorrect){score++;}

        session.setAttribute("score", score);

        Integer currentIndex = (Integer) session.getAttribute("currentIndex");
        if(currentIndex == null){
            currentIndex = 0;
        }

        currentIndex++;
        session.setAttribute("currentIndex", currentIndex);

        List<TriviaCard> cards = (List<TriviaCard>) session.getAttribute("cards");
        int total = (cards == null) ? 0 : cards.size();

        model.addAttribute("isCorrect", isCorrect);
        model.addAttribute("question", currentCard.getQuestion());
        model.addAttribute("chosenAnswer", chosenAnswer);
        model.addAttribute("correctAnswer", currentCard.getCorrectAnswer());
        model.addAttribute("score", score);
        model.addAttribute("total", total);
        model.addAttribute("currentIndex", currentIndex);

        return "result";
    }
}
