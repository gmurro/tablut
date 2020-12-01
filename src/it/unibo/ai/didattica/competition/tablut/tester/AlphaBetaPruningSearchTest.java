package it.unibo.ai.didattica.competition.tablut.tester;

import it.unibo.ai.didattica.competition.tablut.brainmates.minmax.AlphaBetaPruningSearch;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;
import it.unibo.ai.didattica.competition.tablut.gui.Gui;

public class AlphaBetaPruningSearchTest {

    public static void main(String[] args) {


        //test makeDecision(State state)
        testMakeDecision();

    }

    public static void testMakeDecision() {
        //Arrange
        GameAshtonTablut tablutGame = new GameAshtonTablut(99, 2, "garbage", "white_ai", "black_ai");
        State state = buildState();
        AlphaBetaPruningSearch search = new AlphaBetaPruningSearch(tablutGame, 3,10000);
        search.setLogEnabled(true);

        //Act
        Action action = search.makeDecision(state);

    }

    public static void showGui(State state) {
        // GUI
        Gui theGui = theGui = new Gui(4);
        theGui.update(state);

    }


    public static State buildState() {
        /*
         * Set TRUE to show GUI state
         */
        boolean enableGui = true;


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

        board[3][2] = State.Pawn.KING;

        board[1][0] = State.Pawn.WHITE;
        board[2][5] = State.Pawn.WHITE;
        board[3][1] = State.Pawn.WHITE;
        board[4][3] = State.Pawn.WHITE;
        board[5][4] = State.Pawn.WHITE;
        board[5][6] = State.Pawn.WHITE;
        board[6][4] = State.Pawn.WHITE;
        /*
        board[6][4] = State.Pawn.WHITE;
        board[4][2] = State.Pawn.WHITE;
        board[4][3] = State.Pawn.WHITE;
        board[4][5] = State.Pawn.WHITE;
        board[4][6] = State.Pawn.WHITE;
        */
        board[0][4] = State.Pawn.BLACK;
        board[0][6] = State.Pawn.BLACK;
        board[2][7] = State.Pawn.BLACK;
        board[3][8] = State.Pawn.BLACK;
        board[4][8] = State.Pawn.BLACK;
        board[5][2] = State.Pawn.BLACK;
        board[5][8] = State.Pawn.BLACK;
        board[6][1] = State.Pawn.BLACK;
        board[7][6] = State.Pawn.BLACK;
        board[8][8] = State.Pawn.BLACK;






        State state = new StateTablut();

        // set turn
        state.setTurn(State.Turn.BLACK);

        // set board
        state.setBoard(board);

        // show state
        System.out.println(state.toString());
        if(enableGui) {
            showGui(state);
        }

        return state;
    }
}
