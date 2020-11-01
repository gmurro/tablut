package it.unibo.ai.didattica.competition.tablut.brainmates;

import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.List;

import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.AlphaBetaSearch;

public class MinMaxSearch extends AlphaBetaSearch{

    private int depthMax;
    private State.Turn currentPlayer;
    private Heuristics heuristics;

    public MinMaxSearch(int depthMax, State.Turn currentPlayer) {
        super();
        this.depthMax = depthMax;
        this.currentPlayer = currentPlayer;
    }

    public List<State> getNextStates(State state, List<int[]> pawns) {
        return null;
    }




}
