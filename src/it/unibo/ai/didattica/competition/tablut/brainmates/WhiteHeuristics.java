package it.unibo.ai.didattica.competition.tablut.brainmates;

import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WhiteHeuristics extends Heuristics {

    //row and colons limit of the square used in the first stages of the game
    private final static int START_SQUARE = 2;
    private final static int END_SQUARE = 6;

    //matrix of favourite white positions in the initial stages of the game
    private final static int[][] bestPositions = {{2,5},{3,3},{5,3},{6,5}};

    private Map<String,Double> weights;
    private Map<String,Double> values;
    private String[] keys;


    public WhiteHeuristics(State state) {

        super(state);

        //initializing weights
        weights = new HashMap<String,Double>();
        //Square is the the central area delimited by initial configuration of white pawns
        weights.put("blackNotInSquare", 0.8);
        weights.put("whiteInSquare", 0.5 );
        //Positions which are the best moves at the beginning of the game
        weights.put("bestPositions", 0.6);
        weights.put("numberEscapesKing", 0.7);
        weights.put("blackSurroundKing", 0.6);

        //Extraction of keys
        keys = new String[weights.size()];
        keys = weights.keySet().toArray(new String[0]);

    }

    @Override
    public double evaluateState() {

        double utilityValue = 0;

        int blackNotInSquare = GameAshtonTablut.NUM_BLACK - getNumberOnInnerSquare(State.Pawn.BLACK);
        int whiteInSquare = getNumberOnInnerSquare(State.Pawn.WHITE);
        int bestPositions = getNumberOnBestPositions();
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

    /**
     *
     * @param color pawn type we are looking for in the square
     * @return number of 'color' pawn in the square
     */
    private int getNumberOnInnerSquare(State.Pawn color){

        int num = 0;

        for(int i = START_SQUARE; i <= END_SQUARE; i++){
            for(int j = START_SQUARE; j <= END_SQUARE; j++){
                if(state.getPawn(i,j).equalsPawn(color.toString())){
                    num++;
                }
            }
        }

        return num;
    }


    /**
     *
     * @return number of white pawns on best positions
     */
    private int getNumberOnBestPositions(){

        int num = 0;

        for(int[] pos: bestPositions){
            if(state.getPawn(pos[0],pos[1]).equalsPawn(State.Pawn.WHITE.toString())){
                num++;
            }
        }

        return num;
    }
}
