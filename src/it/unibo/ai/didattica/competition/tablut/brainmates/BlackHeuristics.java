package it.unibo.ai.didattica.competition.tablut.brainmates;

import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.*;

public class BlackHeuristics extends Heuristics {

    //Number of admissible loss of pawns before changing strategy
    private final int THRESHOLD = 2;
    //Number of tiles on rhombus
    private final int NUM_TILES_ON_RHOMBUS = 16;

    private final Map<String,Double> weights;
    private final List<int[]> rhombus;
    private int numberOfBlack;
    private int numberOfWhite;

    public BlackHeuristics(State state) {

        super(state);

        //Loading weights
        weights = new HashMap<String, Double>();
        weights.put("Black", 0.5);
        weights.put("White",0.5);
        weights.put("NearKing",0.7);
        weights.put("Rhombus", 0.7);
        weights.put("NextWhiteWins",0.7);

        //Loading rhombus tiles positions
        int[] buf = new int[2];
        rhombus = new ArrayList<int[]>();
        int[] rows = {1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8};
        int[] columns = {3,4,2,5,1,6,0,7,0,7,1,6,2,5,3,4};
        for (int i = 0; i < NUM_TILES_ON_RHOMBUS; i++){
            buf[0] = rows[i];
            buf[1] = columns[i];
            rhombus.add(buf);
        }

    }

    @Override
    public double evaluateState() {

        if (hasWhiteWon()){

            return Double.NEGATIVE_INFINITY;

        } else{

            double utilityValue = 0.0;

            //Atomic functions to combine to get utility value
            numberOfBlack = state.getNumberOf(State.Pawn.BLACK);
            numberOfWhite = state.getNumberOf(State.Pawn.WHITE);
            int pawnsNearKing = checkNearPawns(state, KING_POSITION,State.Turn.BLACK.toString());
            int numberOfPawnsOnRhombus = getNumberOnRhombus();
            int nextMoveWhiteWins = nextMoveWhiteWon();


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

    /**
     * get number of black pawns on rhombus tiles if particular conditions are satisfied
     * @return number of black pawns on tiles if premise is true, 0 otherwise
     */
    private int getNumberOnRhombus(){
        if (checkKingPosition(state) && numberOfBlack >= THRESHOLD) {
            return getValuesOnRhombus();
        }else{
            return 0;
        }
    }

    /**
     *
     * @return number of black pawns on rhombus configuration
     */
    private int getValuesOnRhombus(){

        int count = 0;
        for (int[] position : rhombus) {
            if (state.getPawn(position[0], position[1]).equalsPawn(State.Pawn.BLACK.toString())) {
                count++;
            }
        }
        return count;

    }

    private int nextMoveWhiteWon(){

        int[] posKing = kingPosition(state);





        return 0;
    }
}
