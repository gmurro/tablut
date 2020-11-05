package it.unibo.ai.didattica.competition.tablut.brainmates;

import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.*;

public class BlackHeuristics extends Heuristics {

    private Map<String,Double> weights;

    public BlackHeuristics(State state) {

        super(state);
        weights = new HashMap<String, Double>();
        weights.put("Black", 0.5);
        weights.put("White",0.5);
        weights.put("NearKing",0.7);
        weights.put("Rhombus", 0.7);
        weights.put("NextWhiteWins",0.7);

    }

    @Override
    public double evaluateState() {

        if (function to control row and column 1 and 8){

            return Double.NEGATIVE_INFINITY;

        } else{

            double utilityValue = 0.0;

            //Atomic functions to combine to get utility value
            int numberOfBlack = state.getNumberOf(State.Pawn.BLACK);
            int numberOfWhite = state.getNumberOf(State.Pawn.WHITE);
            int pawnsNearKing = checkNearPawns(state, KING_POSITION,State.Turn.BLACK.toString());
            int numberOfPawnsOnRhombus = getNumberOnRhombus();
            int nextMoveWhiteWins = functionToKnowIfWhiteWins();


            //Weighted sum of functions to get final utility value
            String[] mapKeys = (String[]) weights.keySet().toArray();
            List<Integer> atomicUtilities = new ArrayList<Integer>();
            atomicUtilities.add(numberOfBlack);
            atomicUtilities.add(numberOfWhite);
            atomicUtilities.add(pawnsNearKing);
            atomicUtilities.add(numberOfPawnsOnRhombus);
            atomicUtilities.add(nextMoveWhiteWins);
            Integer[] values = (Integer[]) atomicUtilities.toArray();

            for (int i = 0; i <= weights.size(); i++){
                utilityValue += weights.get(mapKeys[i]) * values[i];
            }

            return utilityValue;



        }
    }
}
