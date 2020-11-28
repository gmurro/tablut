package it.unibo.ai.didattica.competition.tablut.brainmates;

import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WhiteHeuristics extends Heuristics {

    //row and colons limit of the square used in the first stages of the game
    private final static int START_SQUARE = 2;
    private final static int END_SQUARE = 6;
    private final static int THRESHOLD_BEST = 2;
    private final static int NUM_BEST_POSITION = 4;

    //matrix of favourite white positions in the initial stages of the game
    private final static int[][] bestPositions = {
                {2,2},  {2,6},
                {6,2},  {6,6}
    };

    private Map<String,Double> weights;
    private Map<String,Double> values;
    private String[] keys;

    private int numberOfBlackEaten;

    private boolean flag = false;


    public WhiteHeuristics(State state) {

        super(state);

        //initializing weights
        weights = new HashMap<String,Double>();
        //Square is the the central area delimited by initial configuration of white pawns
        //weights.put("blackNotInSquare", 0.8);
        //weights.put("whiteInSquare", 0.5 );
        //Positions which are the best moves at the beginning of the game
        weights.put("bestPositions", 2.5);
        weights.put("numberOfBlackEaten",6.0);
        weights.put("numberOfWhiteAlive",8.0);
        weights.put("numberOfWinEscapesKing", 9.0);
        weights.put("blackSurroundKing", 5.5);

        //Extraction of keys
        keys = new String[weights.size()];
        keys = weights.keySet().toArray(new String[0]);

    }

    @Override
    public double evaluateState() {

        double utilityValue = 0;

        //int blackNotInSquare = GameAshtonTablut.NUM_BLACK - getNumberOnInnerSquare(State.Pawn.BLACK);
        //int whiteInSquare = getNumberOnInnerSquare(State.Pawn.WHITE);
        double bestPositions = (double) getNumberOnBestPositions() / NUM_BEST_POSITION;
        double numberOfWhiteAlive =  (double)(state.getNumberOf(State.Pawn.WHITE)) / GameAshtonTablut.NUM_WHITE;
        double numberOfBlackEaten = (double)(GameAshtonTablut.NUM_BLACK - state.getNumberOf(State.Pawn.BLACK)) / GameAshtonTablut.NUM_BLACK;
        double blackSurroundKing = (double)(getNumEatenPositions(state) - checkNearPawns(state, kingPosition(state),State.Turn.BLACK.toString())) / getNumEatenPositions(state);

        int numberWinWays = countWinWays(state);
        double numberOfWinEscapesKing = numberWinWays>1 ? (double)countWinWays(state)/4 : 0.0;

        if(flag){
            System.out.println("Number of white alive: " + numberOfWhiteAlive);

            System.out.println("Number of white pawns in best positions " + bestPositions);
            System.out.println("Number of escapes: " + numberOfWinEscapesKing);
            System.out.println("Number of black surrounding king: " + blackSurroundKing);
        }

        Map<String, Double> values = new HashMap<String, Double>();
        //values.put("blackNotInSquare", blackNotInSquare);
        //values.put("whiteInSquare", whiteInSquare);
        values.put("bestPositions", bestPositions);
        values.put("numberOfWhiteAlive", numberOfWhiteAlive);
        values.put("numberOfBlackEaten", numberOfBlackEaten);
        values.put("numberOfWinEscapesKing",numberOfWinEscapesKing);
        values.put("blackSurroundKing",blackSurroundKing);

        for (int i=0; i < weights.size(); i++){
            utilityValue += weights.get(keys[i]) * values.get(keys[i]);
            if(flag){
                System.out.println(keys[i] + ":  "+ weights.get(keys[i]) + " * " + values.get(keys[i]) +
                        " = " + weights.get(keys[i]) * values.get(keys[i]));
            }
        }
        //System.out.println("Utility: "+utilityValue+"\n");


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

        if (state.getNumberOf(State.Pawn.WHITE) >= GameAshtonTablut.NUM_WHITE - THRESHOLD_BEST){
            for(int[] pos: bestPositions){
                if(state.getPawn(pos[0],pos[1]).equalsPawn(State.Pawn.WHITE.toString())){
                    num++;
                }
            }
        }

        return num;
    }

    private double protectionKing(){

        double result = 0.0;

        int[] kingPos = kingPosition(state);
        ArrayList<int[]> pawnsPositions = (ArrayList<int[]>) positionNearPawns(state,kingPos,State.Pawn.BLACK.toString());

        //There is a black pawn that threaten the king and 2 pawns are enough to eat the king
        if (pawnsPositions.size() == 1 && getNumEatenPositions(state) == 2){
            int[] enemyPos = pawnsPositions.get(0);
            //Used to store other position from where king could be eaten
            int[] targetPosition = new int[2];
            //Enemy right to the king
            if(enemyPos[0] == kingPos[0] && enemyPos[1] == kingPos[1] + 1){
                //Left to the king there is a white pawn and king is protected
                targetPosition[0] = kingPos[0];
                targetPosition[1] = kingPos[1] - 1;
                if (state.getPawn(targetPosition[0],targetPosition[1]).equalsPawn(State.Pawn.WHITE.toString())){
                    result += 0.6;
                }
            //Enemy left to the king
            }else if(enemyPos[0] == kingPos[0] && enemyPos[1] == kingPos[1] -1){
                //Right to the king there is a white pawn and king is protected
                targetPosition[0] = kingPos[0];
                targetPosition[1] = kingPos[1] + 1;
                if(state.getPawn(targetPosition[0],targetPosition[1]).equalsPawn(State.Pawn.WHITE.toString())){
                    result += 0.6;
                }
            //Enemy up to the king
            }else if(enemyPos[1] == kingPos[1] && enemyPos[0] == kingPos[0] - 1){
                //Down to the king there is a white pawn and king is protected
                targetPosition[0] = kingPos[0] + 1;
                targetPosition[1] = kingPos[1];
                if(state.getPawn(targetPosition[0], targetPosition[1]).equalsPawn(State.Pawn.WHITE.toString())){
                    result += 0.6;
                }
            //Enemy down to the king
            }else{
                //Up there is a white pawn and king is protected
                targetPosition[0] = kingPos[0] - 1;
                targetPosition[1] = kingPos[1];
                if(state.getPawn(targetPosition[0], targetPosition[1]).equalsPawn(State.Pawn.WHITE.toString())){
                    result += 0.6;
                }
            }

            //Considering white to use as barriers for the target pawn
            result += 0.1 * checkNearPawns(state,targetPosition,State.Pawn.WHITE.toString());

        }
        return result;
    }
}
