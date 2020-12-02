package it.unibo.ai.didattica.competition.tablut.brainmates.minmax;

import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import aima.core.search.adversarial.Game;
import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;

import java.util.ArrayList;
import java.util.List;


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


    /**
     * Method that estimates the value for (not necessarily
     * terminal) states. This implementation returns the utility value for
     * terminal states and heuristic value for non-terminal
     * states.
     */
    @Override
    protected double eval(State state, State.Turn player) {
        // needed to make heuristicEvaluationUsed = true, if the state evaluated isn't terminal
        super.eval(state, player);

        // return heuristic value for given state
        return game.getUtility(state, player);
    }


    /**
     * Template method controlling the search. It is based on iterative
     * deepening and tries to make to a good decision in limited time. Credit
     * goes to Behi Monsio who had the idea of ordering actions by utility in
     * subsequent depth-limited search runs.
     */
    @Override
    public Action makeDecision(State state) {
        Action a = super.makeDecision(state);
        System.out.println("Explored a total of " + getMetrics().get(METRICS_NODES_EXPANDED) + " nodes, reaching a depth limit of " + getMetrics().get(METRICS_MAX_DEPTH));
        return  a;
    }


}
