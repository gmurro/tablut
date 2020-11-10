package it.unibo.ai.didattica.competition.tablut.brainmates;

import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.ArrayList;
import java.util.List;

public abstract class Heuristics {

    //TODO why?
    protected final int[] KING_POSITION = {4,4};
    protected State state;

    public Heuristics(State state) {
        this.state = state;
    }

    public double evaluateState(){
        return 0;
    }

    /**
     *
     * @return the position of the king
     */
    public int[] kingPosition(State state) {
        //where I saved the int position of the king
        int[] king= new int[2];
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

    /**
     *
     * @return true if king is on throne, false otherwise
     */
    public boolean checkKingPosition(State state){
        if(state.getPawn(4,4).equalsPawn("K"))
            return true;
        else
            return false;
    }

    //TODO OPPONENT PASSATO O NO?
    //TODO dove è circondato
    /**
     *
     * @return the number of near pawn that are target(BLACK or WHITE)
     */
    protected int checkNearPawns(State state, int[] position, String target){
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

    /**
     *
     * @return the positions occupied near the pawn
     */
    protected List<int[]> positionNearPawns(State state,int[] position, String target){
        List<int[]> occupiedPosition = new ArrayList<int[]>();
        int[] pos = new int[2];
        //GET TURN
        State.Pawn[][] board = state.getBoard();
        if(board[position[0]-1][position[1]].equalsPawn(target)) {
            pos[0] = position[0] - 1;
            pos[1] = position[1];
            occupiedPosition.add(pos);
        }
        if(board[position[0]+1][position[1]].equalsPawn(target)) {
            pos[0] = position[0] + 1;
            occupiedPosition.add(pos);
        }
        if(board[position[0]][position[1]-1].equalsPawn(target)) {
            pos[0] = position[0];
            pos[1] = position[1] - 1;
            occupiedPosition.add(pos);
        }
        if(board[position[0]][position[1]+1].equalsPawn(target)) {
            pos[0] = position[0];
            pos[1] = position[1] + 1;
            occupiedPosition.add(pos);
        }

        return occupiedPosition;
    }

    //method to understand if i'm gonna be eaten
    protected boolean checkDanger(State state,int[] position,String opponent){
        State.Pawn[][] board = state.getBoard();
        //TODO come capisco se sto per essere mangiato?
        return checkNearPawns(state, position, opponent) != 0;
    }

    /**
     *
     * @return true if king is near, false otherwise
     */
    protected boolean checkNearKing(State state, int[] position){
        return checkNearPawns(state, position, "K") > 0;
    }

    /**
     *
     * @return how many pawns are in the "strategic" block positions
     */
    protected int getNumberOfBlockedEscape(){
        int count = 0;
        int[][] blockedEscapes = {{1,1},{1,2},{1,6},{1,7},{2,1},{2,7},{6,1},{6,7},{7,1},{7,2},{7,6},{7,7}};
        for (int[] position: blockedEscapes){
            if (state.getPawn(position[0],position[1]).equalsPawn(State.Pawn.BLACK.toString())){
                count++;
            }
        }
        return count;

    }

    /**
     *
     * @return true if king is on an escape tile, false otherwise
     */
    public boolean hasWhiteWon(){
        int[] posKing = kingPosition(state);
        boolean result;
        result = posKing[0] == 0 || posKing[0] == 8 || posKing[1] == 0 || posKing[1] == 8;
        return result;
    }

    public boolean safePositionKing(State state,int[] kingPosition){
        if(kingPosition[0] > 2 && kingPosition[0] < 6) {
            if (kingPosition[1] > 2 && kingPosition[1] < 6) {
                return true;
            }
        }
        return false;
    }

    public boolean kingGoesForWin(State state){
        int[] kingPosition=this.kingPosition(state);
        boolean col = false;
        boolean row = false;
        if(!safePositionKing(state,kingPosition)){
            if((!(kingPosition[1] > 2 && kingPosition[1] < 6)) && (!(kingPosition[0] > 2 && kingPosition[0] < 6))){
                //not safe row not safe col
                col = checkFreeColumn(state, kingPosition);
                row = checkFreeRow(state,kingPosition);
                System.out.println(col);
            }
            if((kingPosition[1] > 2 && kingPosition[1] < 6)){
                // safe row not safe col
                row = checkFreeRow(state, kingPosition);
            }
            if((kingPosition[0] > 2 && kingPosition[0] < 6)) {
                // safe col not safe row
                col = checkFreeColumn(state, kingPosition);
            }
            return (col || row);
        }
        System.out.println("NOT ENTERED");
        return (col || row);
    }

    //TODO maybe better return a way to understand were
    public boolean checkFreeRow(State state,int[] position){
        int row=position[0];
        int column=position[1];
        int[] currentPosition = new int[2];
        int countRight=0;
        int countLeft=0;
        //going right
        for(int i = column+1; i<=8; i++) {
            currentPosition[0]=row;
            currentPosition[1]=i;
            if (checkOccupiedPosition(state,currentPosition)) {
                countRight++;
            }
        }
        //going left
        for(int i=column-1;i>=0;i--) {
            currentPosition[0]=row;
            currentPosition[1]=i;
            if (checkOccupiedPosition(state,currentPosition)){
                countLeft++;
            }
        }
        return (countRight==0 || countLeft==0);
    }

    //TODO maybe better return a way to understand were
    public boolean checkFreeColumn(State state,int[] position){
        //lock column
        int row=position[0];
        int column=position[1];
        int[] currentPosition = new int[2];
        int countUp=0;
        int countDown=0;
        //going down
        for(int i=row+1;i<=8;i++) {
            currentPosition[0]=i;
            currentPosition[1]=column;
            if (checkOccupiedPosition(state,currentPosition)) {
                countDown++;
            }
        }
        //going up
        for(int i=row-1;i>=0;i--) {
            currentPosition[0]=i;
            currentPosition[1]=column;
            if (checkOccupiedPosition(state,currentPosition)) {
                System.out.println("CONTATO");
                countUp++;
            }
        }
        return (countDown==0 || countUp==0);
    }

    public boolean checkOccupiedPosition(State state,int[] position){
        if(state.getPawn(position[0],position[1]).equals(State.Pawn.EMPTY))
            return false;
        else
            return true;
        }
}
