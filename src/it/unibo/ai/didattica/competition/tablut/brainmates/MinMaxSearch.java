package it.unibo.ai.didattica.competition.tablut.brainmates;

import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import aima.core.search.adversarial.Game;


public class MinMaxSearch extends IterativeDeepeningAlphaBetaSearch<State, Action, State.Turn>{


    public MinMaxSearch(Game<State, Action, State.Turn> game, double utilMin, double utilMax, int time) {
        super(game, utilMin, utilMax, time);
    }


    @Override
    protected double eval(State state, State.Turn player) {
        super.eval(state, player);
        return game.getUtility(state, player);
    }

}
