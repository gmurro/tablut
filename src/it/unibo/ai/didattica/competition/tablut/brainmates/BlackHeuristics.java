package it.unibo.ai.didattica.competition.tablut.brainmates;

import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.Random;

public class BlackHeuristics extends Heuristics {

    public BlackHeuristics(State state) {
        super(state);
    }

    @Override
    public double evaluateState() {
        return new Random().nextDouble();
    }
}
