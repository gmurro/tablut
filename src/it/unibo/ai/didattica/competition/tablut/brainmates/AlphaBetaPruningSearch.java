package it.unibo.ai.didattica.competition.tablut.brainmates;

import aima.core.search.adversarial.Game;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of MinMax algorithm with AlphaBeta pruning.
 *
 * @author Giuseppe Murro
 */
public class AlphaBetaPruningSearch{


    private Game<State, Action, State.Turn> game;
    private int maxDepth;
    private HashMap<Integer,Integer> nodesExpanded;
    private boolean logEnabled;
    private AlphaBetaPruningSearch.Timer timer;

    /**
     * Creates a new search object for a given game.
     */
    public static AlphaBetaPruningSearch createFor(Game<State, Action, State.Turn> game, int maxDepth, int time) {
        return new AlphaBetaPruningSearch(game, maxDepth, time);
    }

    /**
     * Costurctor for this class.
     *
     * @param game Object that implement Game class of AIMA core
     * @param maxDepth Depth of the tree up to which the search is performed (the first level is 0).
     * @param time Maximum time in seconds after which a value is returned.
     */
    public AlphaBetaPruningSearch(Game<State, Action, State.Turn> game, int maxDepth, int time) {
        this.game = game;
        this.maxDepth = maxDepth;

        // set the map with one entry for each level of depth and set all values to 0
        this.nodesExpanded = new HashMap<>();
        for (int i=0; i<=maxDepth; i++) {
            nodesExpanded.put(i, 0);
        }

        this.timer = new AlphaBetaPruningSearch.Timer(time);
    }

    /**
     * Method to enable log and to debug algorithm.
     * @param logEnabled
     */
    public void setLogEnabled(boolean logEnabled) {
        this.logEnabled = logEnabled;
    }


    /**
     * Method that perform minmax algorithm.
     *
     * @param state State from whitch the search begins.
     * @return Best action performed.
     */
    public Action makeDecision(State state) {

        // start timer
        this.timer.start();

        Action result = null;
        double resultValue = Double.NEGATIVE_INFINITY;

        // the first level (0 level) on tree have always only one node: the current state
        nodesExpanded.put(0,1);

        // get kind of player that is playing
        State.Turn player = game.getPlayer(state);

        List<Action> list = game.getActions(state);

        // Recur for each possible action for current player
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
        System.out.println("\nBest Action for "+result.getTurn() + " is:  "+result.getFrom()+"-->"+result.getTo()+"  (val: "+resultValue+")");
        System.out.println("Node expanded in tree search in "+timer.getTimer()+" s:");
        System.out.println(printNodesExpanded());
        return result;
    }

    public double maxValue(State state, State.Turn player, int depth, double alpha, double beta) {

        // increment node expanded at depth = maxDepth-depth
        nodesExpanded.put(maxDepth-depth, nodesExpanded.get(maxDepth-depth) + 1 );

        if (this.logEnabled) {
            System.out.println("STATE "+nodesExpanded.get(maxDepth-depth)+" at level "+(maxDepth-depth));
            System.out.println(state);
        }

        // Terminating condition. i.e
        // leaf node is reached
        if (game.isTerminal(state) || depth == 0 || this.timer.timeOutOccurred()) {
            double utility = game.getUtility(state, player);

            if (this.logEnabled) {
                System.out.println("UTILITY of state " + nodesExpanded.get(maxDepth - depth) + " at level " + (maxDepth - depth) + ": " + utility+"\n");
            }

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

        if (this.logEnabled) {
            System.out.println("STATE " + nodesExpanded.get(maxDepth - depth) + " at level " + (maxDepth - depth));
            System.out.println(state);
        }

        // Terminating condition. i.e
        // leaf node is reached
        if (game.isTerminal(state) || depth == 0 || this.timer.timeOutOccurred()) {
            double utility = game.getUtility(state, player);

            if (this.logEnabled) {
                System.out.println("UTILITY of state " + nodesExpanded.get(maxDepth - depth) + " at level " + (maxDepth - depth) + ": " + utility+"\n");
            }

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



    private static class Timer {
        private long duration;
        private long startTime;

        public Timer(int maxSeconds) {
            this.duration = (long)(1000 * maxSeconds);
        }

        public void start() {
            this.startTime = System.currentTimeMillis();
        }

        public double getTimer() {
            return (double)(System.currentTimeMillis() - this.startTime)/1000;
        }

        public boolean timeOutOccurred() {
            boolean overTime = System.currentTimeMillis() > this.startTime + this.duration;
            return overTime;
        }
    }
}