package it.unibo.ai.didattica.competition.tablut.brainmates;

import it.unibo.ai.didattica.competition.tablut.domain.State;

public interface Heuristics {
    public double evaluateState(State state);
}
