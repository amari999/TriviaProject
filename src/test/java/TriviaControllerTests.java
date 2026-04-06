import org.example.controllers.TriviaController;
import org.example.models.TriviaCard;
import org.example.services.interfaces.TriviaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TriviaControllerTests {
    private TriviaController controller;
    private TriviaService service;

    @BeforeEach
    void setup() {
        service = mock(TriviaService.class);
        controller = new TriviaController(service);
    }
    
    @Test
    void getQuestion_setsDefaultSessionAttributes() {
        MockHttpSession session = new MockHttpSession();
        Model model = new ExtendedModelMap();

        TriviaCard card = mock(TriviaCard.class);
        when(card.getQuestion()).thenReturn("Ques");
        when(card.getAllOptions()).thenReturn(List.of("AnsA", "AnsB"));

        when(service.getCards(10, "easy")).thenReturn(List.of(card));

        controller.getQuestion(model, session);

        assertEquals(0, session.getAttribute("score"));
        assertEquals(0, session.getAttribute("currentIndex"));
        assertEquals("easy", session.getAttribute("difficulty"));
        assertEquals(10, session.getAttribute("amount"));
        assertNotNull(session.getAttribute("cards"));
        assertNotNull(session.getAttribute("currentCard"));
    }
    
    @Test
    void startNewGame_removesOldSessionAttributes() {
        MockHttpSession session = new MockHttpSession();

        session.setAttribute("cards", "something");
        session.setAttribute("currentIndex", 5);
        session.setAttribute("score", 3);
        session.setAttribute("currentCard", "card");

        controller.startNewGame(10, "easy", session);

        assertNull(session.getAttribute("cards"));
        assertNull(session.getAttribute("currentIndex"));
        assertNull(session.getAttribute("score"));
        assertNull(session.getAttribute("currentCard"));
    }
    
    @Test
    void startNewGame_setsValidatedValues() {
        MockHttpSession session = new MockHttpSession();

        controller.startNewGame(999, "easypeasy", session);

        assertEquals(10, session.getAttribute("amount"));
        assertEquals("easy", session.getAttribute("difficulty"));
    }
    
    @Test
    void checkAnswer_updatesScoreAndIndex() {
        MockHttpSession session = new MockHttpSession();
        Model model = new ExtendedModelMap();

        TriviaCard card = mock(TriviaCard.class);
        when(card.getCorrectAnswer()).thenReturn("Answer");
        when(card.getQuestion()).thenReturn("Question");

        session.setAttribute("currentCard", card);
        session.setAttribute("score", 0);
        session.setAttribute("currentIndex", 0);
        session.setAttribute("cards", List.of(card));

        controller.checkAnswer("Answer", model, session);

        assertEquals(1, session.getAttribute("score"));
        assertEquals(1, session.getAttribute("currentIndex"));
    }

    @Test
    void checkAnswer_setsErrorWhenNoCard() {
        MockHttpSession session = new MockHttpSession();
        Model model = new ExtendedModelMap();

        controller.checkAnswer("AnsA", model, session);

        assertEquals("no triviacard found", model.getAttribute("error"));
    }
}
