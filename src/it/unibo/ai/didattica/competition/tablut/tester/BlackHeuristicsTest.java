package it.unibo.ai.didattica.competition.tablut.tester;

import it.unibo.ai.didattica.competition.tablut.brainmates.heuristics.BlackHeuristics;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;
import it.unibo.ai.didattica.competition.tablut.gui.Gui;

public class BlackHeuristicsTest {

    public static void main(String[] args) {


        /*
         * Build STATE
         */
        State state = buildState();


        BlackHeuristics heuristics = new BlackHeuristics(state);
        boolean val = heuristics.checkKingPosition(state);
        boolean val2 = heuristics.kingGoesForWin(state);
        double val3 = heuristics.evaluateState();
        double val4 = heuristics.getNumberOnRhombus();

        System.out.println("Value of the heuristic in the current status: " + val4);


    }


    public static State buildState() {
        /*
         * Set TRUE to show GUI state
         */
        boolean enableGui = true;

        // GUI
        Gui theGui = null;

        if (enableGui) {
            theGui = new Gui(4);
        }




        /*
         * Build BOARD
         */
        State.Pawn board[][] = new State.Pawn[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = State.Pawn.EMPTY;
            }
        }

        board[4][4] = State.Pawn.THRONE;

        board[4][4] = State.Pawn.KING;

        board[2][4] = State.Pawn.WHITE;
        board[3][3] = State.Pawn.WHITE;
        board[5][4] = State.Pawn.WHITE;
        board[6][4] = State.Pawn.WHITE;
        board[4][2] = State.Pawn.WHITE;
        board[4][3] = State.Pawn.WHITE;
        board[4][5] = State.Pawn.WHITE;
        board[4][6] = State.Pawn.WHITE;

        board[2][3] = State.Pawn.BLACK;
        board[0][4] = State.Pawn.BLACK;
        board[0][5] = State.Pawn.BLACK;
        board[1][2] = State.Pawn.BLACK;
        board[8][3] = State.Pawn.BLACK;
        board[8][4] = State.Pawn.BLACK;
        board[8][5] = State.Pawn.BLACK;
        board[7][6] = State.Pawn.BLACK;
        board[3][0] = State.Pawn.BLACK;
        board[4][0] = State.Pawn.BLACK;
        board[5][0] = State.Pawn.BLACK;
        board[2][1] = State.Pawn.BLACK;
        board[3][8] = State.Pawn.BLACK;
        board[4][8] = State.Pawn.BLACK;
        board[5][8] = State.Pawn.BLACK;
        board[6][7] = State.Pawn.BLACK;


        State state = new StateTablut();

        // set turn
        state.setTurn(State.Turn.BLACK);

        // set board
        state.setBoard(board);

        // show state
        System.out.println(state.toString());
        if(enableGui) {
            theGui.update(state);
        }

        return state;
    }
}

