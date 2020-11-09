package it.unibo.ai.didattica.competition.tablut.brainmates;

import aima.core.search.adversarial.Game;
import aima.core.search.framework.Metrics;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.HashMap;
import java.util.Map;

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

    private Game<State, Action, State.Turn> game;
    private int maxDepth;
    private HashMap<Integer,Integer> nodesExpanded;

    /**
     * Creates a new search object for a given game.
     */
    public static AlphaBetaPruningSearch createFor(Game<State, Action, State.Turn> game, int maxDepth) {
        return new AlphaBetaPruningSearch(game, maxDepth);
    }

    public AlphaBetaPruningSearch(Game<State, Action, State.Turn> game, int maxDepth) {
        this.game = game;
        this.maxDepth = maxDepth;

        // set the map with one entry for each level of depth and set all values to 0
        this.nodesExpanded = new HashMap<>();
        for (int i=0; i<=maxDepth; i++) {
            nodesExpanded.put(i, 0);
        }
    }


    public Action makeDecision(State state) {
        Action result = null;
        double resultValue = Double.NEGATIVE_INFINITY;
        State.Turn player = game.getPlayer(state);

        nodesExpanded.put(0,1);


        // Recur for each children
        for (Action action : game.getActions(state)) {


            // build new state from given action
            State nextState = game.getResult(state.clone(), action);

            // find action with max value
            double value = minValue(nextState, player, maxDepth-1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            if (value >= resultValue) {
                result = action;
                resultValue = value;
            }
        }
        System.out.println("Best Action for "+result.getTurn() + " is:  "+result.getFrom()+"-->"+result.getTo()+"  (val: "+resultValue+")");
        System.out.println("Node expanded in tree search:");
        System.out.println(printNodesExpanded());
        return result;
    }

    public double maxValue(State state, State.Turn player, int depth, double alpha, double beta) {

        // increment node expanded at depth = maxDepth-depth
        nodesExpanded.put(maxDepth-depth, nodesExpanded.get(maxDepth-depth) + 1 );

        /*System.out.println("STATE "+nodesExpanded.get(maxDepth-depth)+" at level "+(maxDepth-depth));
        System.out.println(state);*/

        // Terminating condition. i.e
        // leaf node is reached
        if (game.isTerminal(state) || depth == 0) {
            double utility = game.getUtility(state, player);
            /*System.out.println(game.isTerminal(state));
            System.out.println("UTILITY of state "+nodesExpanded.get(maxDepth-depth)+" at level "+(maxDepth-depth)+": "+utility);*/
            return utility;
        }

        double maxEval = Double.NEGATIVE_INFINITY;

        // Recur for each children
        for (Action action : game.getActions(state)) {

            // build new state from given action
            State nextState = game.getResult(state.clone(), action);

            // evaluate new state
            double eval = minValue( nextState, player, depth-1, alpha, beta);
            maxEval = Math.max(maxEval, eval);

            // update alpha
            alpha = Math.max(alpha, eval);

            // Alpha Beta Pruning
            if (beta <= alpha)
                break;
        }

        return maxEval;
    }

    public double minValue(State state, State.Turn player, int depth, double alpha, double beta) {

        // increment node expanded at depth = maxDepth-depth
        nodesExpanded.put(maxDepth-depth, nodesExpanded.get(maxDepth-depth) + 1 );

        /*System.out.println("STATE "+nodesExpanded.get(maxDepth-depth)+" at level "+(maxDepth-depth));
        System.out.println(state);*/

        // Terminating condition. i.e
        // leaf node is reached
        if (game.isTerminal(state) || depth == 0) {
            double utility = game.getUtility(state, player);
            /*System.out.println(game.isTerminal(state));
            System.out.println("UTILITY of state "+nodesExpanded.get(maxDepth-depth)+" at level "+(maxDepth-depth)+": "+utility);*/
            return utility;
        }

        double minEval = Double.POSITIVE_INFINITY;

        // Recur for each children
        for (Action action : game.getActions(state)) {

            // build new state from given action
            State nextState = game.getResult(state.clone(), action);

            // evaluate new state
            double eval = maxValue( nextState, player, depth-1, alpha, beta);
            minEval = Math.min(minEval, eval);

            // update beta
            beta = Math.min(beta, eval);

            // Alpha Beta Pruning
            if (beta <= alpha)
                break;
        }
        return minEval;
    }




    public HashMap<Integer, Integer> getNodesExpanded() {
        return nodesExpanded;
    }

    public String printNodesExpanded(){
        StringBuilder message = new StringBuilder();
        for (Map.Entry me : nodesExpanded.entrySet()) {
            message.append("Depth "+me.getKey() + ": " + me.getValue()+" nodes\n");
        }
        return  message.toString();
    }
}