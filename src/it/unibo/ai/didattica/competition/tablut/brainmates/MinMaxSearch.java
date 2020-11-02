package it.unibo.ai.didattica.competition.tablut.brainmates;

import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import aima.core.search.framework.Metrics;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.Iterator;
import java.util.List;

import aima.core.search.adversarial.Game;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;

public class MinMaxSearch extends IterativeDeepeningAlphaBetaSearch<State, Action, State.Turn>{


    public MinMaxSearch(Game<State, Action, State.Turn> game, double utilMin, double utilMax, int time) {
        super(game, utilMin, utilMax, time);
    }

    @Override
    protected double eval(State state, State.Turn turn) {
        super.eval(state, turn);
        return this.game.getUtility(state, turn);
    }

}
