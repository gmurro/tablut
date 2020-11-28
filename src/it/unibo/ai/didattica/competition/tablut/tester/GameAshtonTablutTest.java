package it.unibo.ai.didattica.competition.tablut.tester;


import it.unibo.ai.didattica.competition.tablut.brainmates.BlackHeuristics;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;
import it.unibo.ai.didattica.competition.tablut.gui.Gui;

import java.io.IOException;
import java.util.List;

public class GameAshtonTablutTest {


    public static void main(String[] args) {


        // test getActions(State state)
        testGetActions();


        // test getResult(State state, Action action)
        //testGetResult();

    }

    public static void testGetActions()  {
        //Arrange

        GameAshtonTablut tablutGame = new GameAshtonTablut(99, 2, "garbage", "white_ai", "black_ai");
        State state = buildState();


        //Act
        long startTime = System.currentTimeMillis();
        List<Action> actions = tablutGame.getActions(state);
        System.out.println("Time to find possible actions in ms: "+(System.currentTimeMillis() -startTime));

        //Show
        System.out.println("\nPossible actions: "+actions);
    }

    public static void testGetResult()  {
        //Arrange
        GameAshtonTablut tablutGame = new GameAshtonTablut(99, 2, "garbage", "white_ai", "black_ai");
        State state = buildState();

        Action action = null;
        try {
            action = new Action("d2", "b2", state.getTurn());
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Act
        long startTime1 = System.currentTimeMillis();
        State newState = tablutGame.getResult(state, action);
        System.out.println("Time to get result in ms: "+(System.currentTimeMillis() -startTime1));

        //Show
        System.out.println(newState);
        showGui(newState);
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

        board[5][4] = State.Pawn.KING;

        board[4][3] = State.Pawn.WHITE;
        board[6][3] = State.Pawn.WHITE;
        board[6][5] = State.Pawn.WHITE;
        /*
        board[6][4] = State.Pawn.WHITE;
        board[4][2] = State.Pawn.WHITE;
        board[4][3] = State.Pawn.WHITE;
        board[4][5] = State.Pawn.WHITE;
        board[4][6] = State.Pawn.WHITE;
        */
        board[0][3] = State.Pawn.BLACK;
        board[0][4] = State.Pawn.BLACK;
        board[1][6] = State.Pawn.BLACK;
        board[3][0] = State.Pawn.BLACK;
        board[3][4] = State.Pawn.BLACK;
        board[4][0] = State.Pawn.BLACK;
        board[4][1] = State.Pawn.BLACK;
        board[4][8] = State.Pawn.BLACK;
        board[5][3] = State.Pawn.BLACK;
        board[5][5] = State.Pawn.BLACK;
        board[6][7] = State.Pawn.BLACK;
        board[8][3] = State.Pawn.BLACK;
        board[8][4] = State.Pawn.BLACK;
        board[8][5] = State.Pawn.BLACK;




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
