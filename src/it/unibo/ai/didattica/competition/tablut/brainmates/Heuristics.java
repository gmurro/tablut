package it.unibo.ai.didattica.competition.tablut.brainmates;

import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.ArrayList;
import java.util.List;

public abstract class Heuristics {

    private State state;

    public Heuristics(State state) {
        this.state = state;
    }

    public double evaluateState(){
        return 0;
    }

    //TODO capire come ritornare questo valore
    private int[] kingPosition(State state) {
        //where I saved the int position of the king
        int[] king=new int[2];
        //obtain the board
        State.Pawn[][] board = state.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (state.getPawn(i, j).equalsPawn("K")) {
                    king[0] = i;
                    king[1] = j;
                }
            }
        }
        return king;
    }

    //method that return if the king is in the Throne or not
    private boolean checkKingPosition(State state){
        if(state.getPawn(4,4).equalsPawn("K"))
            return true;
        else
            return false;
    }

    //TODO OPPONENT PASSATO O NO?
    //TODO dove è circondato
    //from the state and the position of the pawn I understand how many pawns are near
    private int checkNearPawns(State state, int[] position, String target){
        int count=0;
        //GET TURN
        State.Pawn[][] board = state.getBoard();
        if(board[position[0]-1][position[1]].equalsPawn(target))
            count++;
        if(board[position[0]+1][position[1]].equalsPawn(target))
            count++;
        if(board[position[0]][position[1]-1].equalsPawn(target))
            count++;
        if(board[position[0]][position[1]+1].equalsPawn(target))
            count++;
        return count;
    }

    //method to understand where it is occupied near you
    private List<int[]> positionNearPawns(State state,int[] position, String target){
        List<int[]> occupiedPosition = new ArrayList<int[]>();
        int[] pos = new int[2];
        //GET TURN
        State.Pawn[][] board = state.getBoard();
        if(board[position[0]-1][position[1]].equalsPawn(target))
            pos[0]=position[0]-1;
            pos[1]=position[1];
            occupiedPosition.add(pos);
        if(board[position[0]+1][position[1]].equalsPawn(target))
            pos[0]=position[0]+1;
            occupiedPosition.add(pos);
        if(board[position[0]][position[1]-1].equalsPawn(target))
            pos[0]=position[0];
            pos[1]=position[1]-1;
            occupiedPosition.add(pos);
        if(board[position[0]][position[1]+1].equalsPawn(target))
            pos[0]=position[0];
            pos[1]=position[1]+1;
            occupiedPosition.add(pos);

        return occupiedPosition;
    }

    //method to understand if i'm gonna be eaten
    private boolean checkDanger(State state,int[] position,String opponent){
        State.Pawn[][] board = state.getBoard();
        //TODO come capisco se sto per essere mangiato?
        return checkNearPawns(state, position, opponent) != 0;
    }

    //Method to understand if I have the king near
    private boolean checkNearKing(State state, int[] position){
        return checkNearPawns(state, position, "K") > 0;
    }



}
