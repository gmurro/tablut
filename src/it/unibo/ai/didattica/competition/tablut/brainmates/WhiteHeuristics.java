package it.unibo.ai.didattica.competition.tablut.brainmates;

import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WhiteHeuristics extends Heuristics {

    private Map<String,Double> weights;
    private Map<String,Double> values;
    private String[] keys;


    public WhiteHeuristics(State state) {

        super(state);

        //initializing weights
        weights = new HashMap<String,Double>();
        //Square is the the central area delimited by initial configuration of white pawns
        weights.put("blackNotInSquare", 0.5);
        weights.put("whiteInSquare", 0.5 );
        //Positions which are the best moves at the beginning of the game
        weights.put("bestPositions", 0.7);
        weights.put("numberEscapesKing", 0.7);
        weights.put("blackSurroundKing", 0.6);

        //Extraction of keys
        keys = new String[weights.size()];
        keys = weights.keySet().toArray(new String[0]);

    }

    @Override
    public double evaluateState() {

        double utilityValue = 0;

        int blackNotInSquare = fun();
        int whiteInSquare = fun();
        int bestPositions = fun();
        int numEscapes = fun();
        int blackSurroundKing = checkNearPawns(state, kingPosition(state),State.Turn.BLACK.toString());

        Map<String, Integer> values = new HashMap<String, Integer>();
        values.put("blackNotInSquare", blackNotInSquare);
        values.put("whiteInSquare", whiteInSquare);
        values.put("bestPositions", bestPositions);
        values.put("numberEscapesKing",numEscapes);
        values.put("blackSurroundingKing",blackSurroundKing);

        for (int i=0; i < weights.size(); i++){
            utilityValue += weights.get(keys[i]) * values.get(keys[i]);
        }


        return utilityValue;
    }
}
