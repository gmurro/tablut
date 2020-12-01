package it.unibo.ai.didattica.competition.tablut.brainmates.minmax;

import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import aima.core.search.adversarial.Game;
import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;


/**
 * Custom implementation of AIMA iterative deepening Minimax search with alpha-beta pruning
 * with action ordering. Maximal computation time is specified in seconds.
 * This configuration redefines the method eval() using utility in {@link GameAshtonTablut}.
 *
 * @see aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch
 * @author Giuseppe Murro
 */

public class MyIterativeDeepeningAlphaBetaSearch extends IterativeDeepeningAlphaBetaSearch<State, Action, State.Turn>{


    public MyIterativeDeepeningAlphaBetaSearch(Game<State, Action, State.Turn> game, double utilMin, double utilMax, int time) {
        super(game, utilMin, utilMax, time);
    }


    @Override
    protected double eval(State state, State.Turn player) {
        super.eval(state, player);
        return game.getUtility(state, player);
    }

}
