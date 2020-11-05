package it.unibo.ai.didattica.competition.tablut.brainmates;

import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import aima.core.search.framework.Metrics;
import it.unibo.ai.didattica.competition.tablut.domain.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IterativeDepthMinMaxSearch {
    public static final String METRICS_NODES_EXPANDED = "nodesExpanded";
    public static final String METRICS_MAX_DEPTH = "maxDepth";
    protected Game<State, Action, State.Turn> game;
    protected double utilMax;
    protected double utilMin;
    protected int currDepthLimit;
    private boolean heuristicEvaluationUsed;
    private IterativeDepthMinMaxSearch.Timer timer;
    private boolean logEnabled;
    private Metrics metrics = new Metrics();

    public static  IterativeDepthMinMaxSearch createFor(Game<State, Action, State.Turn> game, double utilMin, double utilMax, int time) {
        return new IterativeDepthMinMaxSearch(game, utilMin, utilMax, time);
    }


    public IterativeDepthMinMaxSearch(Game<State, Action, State.Turn> game, double utilMin, double utilMax, int time) {
        this.game = game;
        this.utilMin = utilMin;
        this.utilMax = utilMax;
        this.timer = new IterativeDepthMinMaxSearch.Timer(time);
    }

    public void setLogEnabled(boolean b) {
        this.logEnabled = b;
    }

    public Action makeDecision(State state) {
        this.metrics = new Metrics();
        StringBuffer logText = null;
        State.Turn player = this.game.getPlayer(state);
        List<Action> results = this.orderActions(state, this.game.getActions(state), player, 0);
        this.timer.start();
        this.currDepthLimit = 0;

        do {
            System.out.println("\n\nCURRENT DEPTH LIMIT: "+this.currDepthLimit);
            this.incrementDepthLimit();
            if (this.logEnabled) {
                logText = new StringBuffer("depth " + this.currDepthLimit + ": ");
            }

            this.heuristicEvaluationUsed = false;
            IterativeDepthMinMaxSearch.ActionStore<Action> newResults = new IterativeDepthMinMaxSearch.ActionStore();
            System.out.println("\n\nLIST OF ACTIONS:"+results);
            Iterator var6 = results.iterator();

            while(var6.hasNext()) {
                Action action = (Action) var6.next();
                double value = this.minValue(this.game.getResult(state, action), player, -1.0D / 0.0, 1.0D / 0.0, 1);
                if (this.timer.timeOutOccurred()) {
                    break;
                }

                newResults.add(action, value);
                if (this.logEnabled) {
                    logText.append(action).append("->").append(value).append(" ");
                }
            }

            if (this.logEnabled) {
                System.out.println(logText);
            }

            if (newResults.size() > 0) {
                results = newResults.actions;
                if (!this.timer.timeOutOccurred() && (this.hasSafeWinner((Double)newResults.utilValues.get(0)) || newResults.size() > 1 && this.isSignificantlyBetter((Double)newResults.utilValues.get(0), (Double)newResults.utilValues.get(1)))) {
                    break;
                }
            }
        } while(!this.timer.timeOutOccurred() && this.heuristicEvaluationUsed);

        return results.get(0);
    }

    public double maxValue(State state, State.Turn player, double alpha, double beta, int depth) {
        this.updateMetrics(depth);
        if (!this.game.isTerminal(state) && depth < this.currDepthLimit && !this.timer.timeOutOccurred()) {
            double value = -1.0D / 0.0;

            for(Iterator var10 = this.orderActions(state, this.game.getActions(state), player, depth).iterator(); var10.hasNext(); alpha = Math.max(alpha, value)) {
                Action action = (Action) var10.next();
                value = Math.max(value, this.minValue(this.game.getResult(state, action), player, alpha, beta, depth + 1));
                if (value >= beta) {
                    return value;
                }
            }

            return value;
        } else {
            return this.eval(state, player);
        }
    }

    public double minValue(State state, State.Turn player, double alpha, double beta, int depth) {
        this.updateMetrics(depth);
        if (!this.game.isTerminal(state) && depth < this.currDepthLimit && !this.timer.timeOutOccurred()) {
            double value = 1.0D / 0.0;

            for(Iterator var10 = this.orderActions(state, this.game.getActions(state), player, depth).iterator(); var10.hasNext(); beta = Math.min(beta, value)) {
                Action action = (Action) var10.next();
                value = Math.min(value, this.maxValue(this.game.getResult(state, action), player, alpha, beta, depth + 1));
                if (value <= alpha) {
                    return value;
                }
            }

            return value;
        } else {
            return this.eval(state, player);
        }
    }

    private void updateMetrics(int depth) {
        this.metrics.incrementInt("nodesExpanded");
        this.metrics.set("maxDepth", Math.max(this.metrics.getInt("maxDepth"), depth));
    }

    public Metrics getMetrics() {
        return this.metrics;
    }

    protected void incrementDepthLimit() {
        ++this.currDepthLimit;
    }

    protected boolean isSignificantlyBetter(double newUtility, double utility) {
        return false;
    }

    protected boolean hasSafeWinner(double resultUtility) {
        return resultUtility <= this.utilMin || resultUtility >= this.utilMax;
    }

    protected double eval(State state, State.Turn player) {
        return this.game.getUtility(state, player);
    }

    public List<Action> orderActions(State state, List<Action> actions, State.Turn player, int depth) {
        return actions;
    }

    private static class ActionStore<A> {
        private List<A> actions;
        private List<Double> utilValues;

        private ActionStore() {
            this.actions = new ArrayList();
            this.utilValues = new ArrayList();
        }

        void add(A action, double utilValue) {
            int idx;
            for(idx = 0; idx < this.actions.size() && utilValue <= (Double)this.utilValues.get(idx); ++idx) {
            }

            this.actions.add(idx, action);
            this.utilValues.add(idx, utilValue);
        }

        int size() {
            return this.actions.size();
        }
    }

    private static class Timer {
        private long duration;
        private long startTime;

        Timer(int maxSeconds) {
            this.duration = (long)(1000 * maxSeconds);
        }

        void start() {
            this.startTime = System.currentTimeMillis();
        }

        boolean timeOutOccurred() {
            return System.currentTimeMillis() > this.startTime + this.duration;
        }
    }
}