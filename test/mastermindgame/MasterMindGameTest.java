package mastermindgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

import static mastermindgame.MasterMindGame.Result.*;
import static mastermindgame.MasterMindGame.Status.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MasterMindGameTest {
    private MasterMindGame masterMindGame;

    @BeforeEach
    void setup() {
        masterMindGame = new MasterMindGame();
        masterMindGame.setColorCombinationIndices(List.of(1, 2, 3, 4, 5, 6));
    }

    @Test
    void canary() {
        assert(true);
    }

    @Test
    void userMakesIncorrectGuess() {
        assertEquals(Map.of(INPOSITION, 0,
                            MATCH, 0,
                            NOMATCH, 6),
                            masterMindGame.guess(List.of(0, 0, 0, 0, 0, 0)));
    }

    @Test
    void userMakesCorrectGuess() {
        assertEquals(Map.of(INPOSITION, 6,
                            MATCH, 0,
                            NOMATCH, 0),
                            masterMindGame.guess(List.of(1, 2, 3, 4, 5, 6)));
    }

    @Test
    void oneColorMatchNonPositional() {
        assertEquals(Map.of(INPOSITION, 0,
                            MATCH, 1,
                            NOMATCH, 5),
                            masterMindGame.guess(List.of(2, 0, 0, 0, 0, 0)));
    }

    @Test
    void twoColorMatchNonPositional() {
        assertEquals(Map.of(INPOSITION, 0,
                            MATCH, 2,
                            NOMATCH, 4),
                            masterMindGame.guess(List.of(2, 1, 0, 0, 0, 0)));
    }

    @Test
    void oneColorMatchPositionalAndOneColorNonPositional() {
        assertEquals(Map.of(INPOSITION, 1,
                            MATCH, 1,
                            NOMATCH, 4),
                            masterMindGame.guess(List.of(0, 2, 1, 0, 0, 0)));
    }

    @Test
    void playerSendsTwoSameColor() {
        assertEquals(Map.of(INPOSITION, 1,
                            MATCH, 0,
                            NOMATCH, 5),
                            masterMindGame.guess(List.of(0, 2, 2, 0, 0, 0)));
    }

    @Test
    void threeSameColorNoneInPosition() {
        assertEquals(Map.of(INPOSITION, 0,
                            MATCH, 1,
                            NOMATCH, 5),
                            masterMindGame.guess(List.of(0, 1, 1, 1, 0, 0)));
    }

    @Test
    void twoSameColorFirstWrongPositionSecondCorrect() {
        assertEquals(Map.of(INPOSITION, 1,
                            MATCH, 0,
                            NOMATCH, 5),
                            masterMindGame.guess(List.of(2, 2, 0, 0, 0, 0)));
    }

    @Test
    void gameOnStart() {
        assertEquals(INPROGRESS, masterMindGame.gameStatus());
    }

    @Test
    void oneCorrectGuess() {
        masterMindGame.guess(List.of(1, 2, 3, 4, 5, 6));
        assertEquals(WON, masterMindGame.gameStatus());
    }

    @Test
    void oneWrongGuess() {
        masterMindGame.guess(List.of(0, 2, 3, 4, 5, 6));
        assertEquals(INPROGRESS, masterMindGame.gameStatus());
    }

    @Test
    void runOutOfTurns() {
         masterMindGame.turnsLeft = 1;
         masterMindGame.guess(List.of(0, 2, 3, 4, 5, 6));
         assertEquals(LOST, masterMindGame.gameStatus());
    }

    @Test
    void afterWinStayWin() {
        masterMindGame.guess(List.of(1, 2, 3, 4, 5, 6));
        masterMindGame.guess(List.of(0, 0, 0, 0, 0, 0));
        assertEquals(WON, masterMindGame.gameStatus());
    }

    @Test
    void afterLoseStayLose() {
        masterMindGame.turnsLeft = 1;
        masterMindGame.guess(List.of(0, 2, 3, 4, 5, 6));
        masterMindGame.guess(List.of(1, 2, 3, 4, 5, 6));
        assertEquals(LOST, masterMindGame.gameStatus());
    }

    @Test
    void selectedIndicesHasSixValues() {
        assertEquals(6, masterMindGame.selectRandomDistinctColorIndices(10, 6, 2).size());
    }

    @Test
    void selectedIndicesAreDistinct() {
        List<Integer> selectedIndices = masterMindGame.selectRandomDistinctColorIndices(10, 6, 1);
        assertEquals(6, new HashSet<Integer>(selectedIndices).size());
    }

    @Test
    void selectedColorsAreSameForSameRandomizerSeed() {
        assertEquals(
                masterMindGame.selectRandomDistinctColorIndices(10, 6, 1),
                masterMindGame.selectRandomDistinctColorIndices(10, 6, 1)
        );
    }

    @Test
    void selectedColorsAreDifferentForDifferentRandomizerSeed() {
        assertNotEquals(
                masterMindGame.selectRandomDistinctColorIndices(10, 6, 1),
                masterMindGame.selectRandomDistinctColorIndices(10, 6, 2)
        );
    }

    @Test
    void selectRandomDistinctColorGivesNonNegativeInteger() {
        List<Integer> selectedIndices = masterMindGame.selectRandomDistinctColorIndices(10, 6, 1);

        assertEquals(0, selectedIndices.stream()
          .filter(value -> value < 0)
          .count());
    }
    
    @Test
    void selectRandomDistinctColorIndicesForLargePool() {
      assertEquals(15, masterMindGame.selectRandomDistinctColorIndices(20, 15, 1).size());
    }

}