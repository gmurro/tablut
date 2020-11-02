package it.unibo.ai.didattica.competition.tablut.brainmates;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import java.util.Random;

public class WhiteHeuristics extends Heuristics {

    public WhiteHeuristics(State state) {
        super(state);
    }

    @Override
    public double evaluateState() {
        return new Random().nextDouble();
    }
}
