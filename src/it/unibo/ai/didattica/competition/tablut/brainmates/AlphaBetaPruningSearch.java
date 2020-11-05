package it.unibo.ai.didattica.competition.tablut.brainmates;

import aima.core.search.adversarial.Game;
import aima.core.search.framework.Metrics;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;

/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Page 173.<br>
 * <p>
 * <pre>
 * <code>
 * function ALPHA-BETA-SEARCH(state) returns an action
 *   v = MAX-VALUE(state, -infinity, +infinity)
 *   return the action in ACTIONS(state) with value v
 *
 * function MAX-VALUE(state, alpha, beta) returns a utility value
 *   if TERMINAL-TEST(state) then return UTILITY(state)
 *   v = -infinity
 *   for each a in ACTIONS(state) do
 *     v = MAX(v, MIN-VALUE(RESULT(s, a), alpha, beta))
 *     if v >= beta then return v
 *     alpha = MAX(alpha, v)
 *   return v
 *
 * function MIN-VALUE(state, alpha, beta) returns a utility value
 *   if TERMINAL-TEST(state) then return UTILITY(state)
 *   v = infinity
 *   for each a in ACTIONS(state) do
 *     v = MIN(v, MAX-VALUE(RESULT(s,a), alpha, beta))
 *     if v <= alpha then return v
 *     beta = MIN(beta, v)
 *   return v
 * </code>
 * </pre>
 * <p>
 * Figure 5.7 The alpha-beta search algorithm. Notice that these routines are
 * the same as the MINIMAX functions in Figure 5.3, except for the two lines in
 * each of MIN-VALUE and MAX-VALUE that maintain alpha and beta (and the
 * bookkeeping to pass these parameters along).
 *
 * @author Ruediger Lunde
 */
public class AlphaBetaPruningSearch{

    public final static String METRICS_NODES_EXPANDED = "nodesExpanded";

    Game<State, Action, State.Turn> game;
    private Metrics metrics = new Metrics();

    /**
     * Creates a new search object for a given game.
     */
    public static AlphaBetaPruningSearch createFor(Game<State, Action, State.Turn> game) {
        return new AlphaBetaPruningSearch(game);
    }

    public AlphaBetaPruningSearch(Game<State, Action, State.Turn> game) {
        this.game = game;
    }


    public Action makeDecision(State state) {
        metrics = new Metrics();
        Action result = null;
        double resultValue = Double.NEGATIVE_INFINITY;
        State.Turn player = game.getPlayer(state);
        for (Action action : game.getActions(state)) {
            double value = minValue(game.getResult(state, action), player,
                    Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            if (value > resultValue) {
                result = action;
                resultValue = value;
            }
        }
        return result;
    }

    public double maxValue(State state, State.Turn player, double alpha, double beta) {
        metrics.incrementInt(METRICS_NODES_EXPANDED);
        if (game.isTerminal(state))
            return game.getUtility(state, player);
        double value = Double.NEGATIVE_INFINITY;
        for (Action action : game.getActions(state)) {
            value = Math.max(value, minValue( //
                    game.getResult(state, action), player, alpha, beta));
            if (value >= beta)
                return value;
            alpha = Math.max(alpha, value);
        }
        return value;
    }

    public double minValue(State state, State.Turn player, double alpha, double beta) {
        metrics.incrementInt(METRICS_NODES_EXPANDED);
        if (game.isTerminal(state))
            return game.getUtility(state, player);
        double value = Double.POSITIVE_INFINITY;
        for (Action action : game.getActions(state)) {
            value = Math.min(value, maxValue( //
                    game.getResult(state, action), player, alpha, beta));
            if (value <= alpha)
                return value;
            beta = Math.min(beta, value);
        }
        return value;
    }


    public Metrics getMetrics() {
        return metrics;
    }
}