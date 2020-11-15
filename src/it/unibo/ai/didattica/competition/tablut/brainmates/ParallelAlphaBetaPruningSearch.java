package it.unibo.ai.didattica.competition.tablut.brainmates;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import aima.core.search.adversarial.Game;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;


public class ParallelAlphaBetaPruningSearch {


    private Game<State, Action, State.Turn> game;
    private int maxDepth;
    private HashMap<Integer,Integer> nodesExpanded;
    private boolean logEnabled;
    private ParallelAlphaBetaPruningSearch.Timer timer;

    /**
     * Creates a new search object for a given game.
     */
    public static ParallelAlphaBetaPruningSearch createFor(Game<State, Action, State.Turn> game, int maxDepth, int time) {
        return new ParallelAlphaBetaPruningSearch(game, maxDepth, time);
    }

    /**
     * Costurctor for this class.
     *
     * @param game Object that implement Game class of AIMA core
     * @param maxDepth Depth of the tree up to which the search is performed (the first level is 0).
     * @param time Maximum time in seconds after which a value is returned.
     */
    public ParallelAlphaBetaPruningSearch(Game<State, Action, State.Turn> game, int maxDepth, int time) {
        this.game = game;
        this.maxDepth = maxDepth;

        // set the map with one entry for each level of depth and set all values to 0
        this.nodesExpanded = new HashMap<>();
        for (int i=0; i<=maxDepth; i++) {
            nodesExpanded.put(i, 0);
        }

        this.timer = new ParallelAlphaBetaPruningSearch.Timer(time);
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


        // the first level (0 level) on tree have always only one node: the current state
        nodesExpanded.put(0,1);

        // get kind of player that is playing
        State.Turn player = game.getPlayer(state);

        // get all possible action for current player
        List<Action> possibleActions = game.getActions(state);

        System.out.println("Number of THREADS run: "+possibleActions.size());

        Vector<Future<Double>> costs = new Vector<Future<Double>>(possibleActions.size());
        costs.setSize(possibleActions.size());

        ExecutorService exec = Executors.newFixedThreadPool(possibleActions.size());
        try {
            // Recur for each possible action for current player
            for (int i = 0; i < possibleActions.size(); i++) {

                Action action = possibleActions.get(i);

                // build new state from given action
                State nextState = game.getResult(state.clone(), action);

                Future<Double> result = exec.submit(new Callable<Double>() {

                    @Override
                    public Double call() {

                        // find action with max value
                        double value = minValue(nextState, player, maxDepth-1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                        return value;
                    }
                });
                costs.set(i, result);
            }
        } finally {
            exec.shutdown();
        }

        // max
        int maxi = -1;
        double max = Double.NEGATIVE_INFINITY;
        for(int i = 0; i < possibleActions.size(); i++) {
            double cost;
            try {
                System.out.println("Cost of node "+i+": "+costs.get(i).get());
                cost = costs.get(i).get();
            } catch (Exception e) {
                System.out.println("\t"+e.toString());
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e1) {
                }
                continue;
            }
            if(cost >= max) {
                max = cost;
                maxi = i;
            }
        }

        Action bestAction = possibleActions.get(maxi);

        System.out.println("\nBest Action for "+bestAction.getTurn() + " is:  "+bestAction.getFrom()+"-->"+bestAction.getTo()+"  (val: "+max+")");
        System.out.println("Node expanded in tree search in "+timer.getTimer()+" s:");
        System.out.println(printNodesExpanded());
        return bestAction;
    }

    public double maxValue(State state, State.Turn player, int depth, double alpha, double beta) {

        // increment node expanded at depth = maxDepth-depth
        nodesExpanded.put(maxDepth-depth, nodesExpanded.get(maxDepth-depth) + 1 );
        /*
        if (this.logEnabled) {
            System.out.println("STATE "+nodesExpanded.get(maxDepth-depth)+" at level "+(maxDepth-depth));
            System.out.println(state);
        }*/

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
        /*
        if (this.logEnabled) {
            System.out.println("STATE " + nodesExpanded.get(maxDepth - depth) + " at level " + (maxDepth - depth));
            System.out.println(state);
        }*/

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