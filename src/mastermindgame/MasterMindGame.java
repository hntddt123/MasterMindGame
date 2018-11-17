package mastermindgame;

import java.util.List;
import java.util.Map;
import java.util.Random;
import static java.util.stream.Collectors.toList;

public class MasterMindGame {

    public enum Result { INPOSITION, MATCH, NOMATCH }
    public enum Status { INPROGRESS, WON, LOST }

    protected int turnsLeft = 20;
    private List<Integer> selectedColorIndices;
    private Status status = Status.INPROGRESS;

    public void setColorCombinationIndices(List<Integer> colorIndices) {
        selectedColorIndices = colorIndices;
    }

    public Map<Result, Integer> guess(List<Integer> guessIndices) {
        int inPosition = 0;
        int match = 0;

        for (int i = 0; i < selectedColorIndices.size(); i++) {
            if (selectedColorIndices.get(i) == guessIndices.get(i)) {
                inPosition++;
                continue;
            }

            if (guessIndices.contains(selectedColorIndices.get(i))) {
                match++;
            }
        }
            updateGameStatus(inPosition);

            return Map.of(
                    Result.INPOSITION, inPosition,
                    Result.MATCH, match,
                    Result.NOMATCH, selectedColorIndices.size() - inPosition - match);
    }

    private void updateGameStatus(int inPosition) {
        if (status != Status.INPROGRESS)
            return;

        turnsLeft--;

        if(turnsLeft == 0) {
          status = Status.LOST;
        }

        if(inPosition == 6) {
          status = Status.WON;
        }
    }

    public Status gameStatus() {
        return status;
    }

    public List<Integer> selectRandomDistinctColorIndices(int poolSize, int size, int randomSeed){
        return new Random(randomSeed)
          .ints(0, poolSize)
          .map(value -> value % poolSize)
          .distinct()
          .boxed()
          .limit(size)
          .collect(toList());
    }
}