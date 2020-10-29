package it.unibo.ai.didattica.competition.tablut.client;

import it.unibo.ai.didattica.competition.tablut.domain.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TablutArtificialClient extends TablutClient{

    private int game;

    public TablutArtificialClient(String player, String name, int timeout, String ipAddress, int game) throws UnknownHostException, IOException {
        super(player, name, timeout, ipAddress);
        this.game = game;
    }

    public static void main(String[] args) throws IOException {
        int gameType = 4;
        String role = "";
        String name = "brAInmates";
        String ipAddress = "localhost";
        int timeout = 60;

        if (args.length < 1) {
            System.out.println("You must specify which player you are (WHITE or BLACK)");
            System.exit(-1);
        } else {
            System.out.println(args[0]);
            role = (args[0]);
        }
        if (args.length == 2) {
            System.out.println(args[1]);
            timeout = Integer.parseInt(args[1]);
        }
        if (args.length == 3) {
            ipAddress = args[2];
        }
        System.out.println("Selected client: " + args[0]);

        TablutArtificialClient client = new TablutArtificialClient(role, name, timeout, ipAddress, gameType);
        client.run();
    }

    @Override
    public void run() {

        // send name of your group to the server saved in variable "name"
        try {
            this.declareName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // set type of state and WHITE must do the first player
        State state = new StateTablut();
        state.setTurn(State.Turn.WHITE);

        // set type of game
        Game rules = new GameAshtonTablut(99, 2, "garbage", "white_ai", "black_ai");;
        System.out.println("Ashton Tablut game");

        /* list of position of pawns that color == current player
         * for WHITE player: white pawns && king
         * for BALCK player: balck pawns
        */
        List<int[]> pawns = new ArrayList<int[]>();

        /* list of position of empty cells
         */
        List<int[]> empty = new ArrayList<int[]>();

        // attribute player depends to first parameter passed to main
        System.out.println("You are player " + this.getPlayer().toString() + "!");

        // still alive until you are playing
        while (true) {

            // update the current state from the server
            try {
                this.read();
            } catch (ClassNotFoundException | IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                System.exit(1);
            }

            // print current state
            System.out.println("Current state:");
            state = this.getCurrentState();
            System.out.println(state.toString());

            // TODO CONTROLLA SE QUESTO THREAD E' NECESSARIO
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }

            // if i'm WHITE
            if (this.getPlayer().equals(State.Turn.WHITE)) {

                // if is my turn (WHITE)
                if (state.getTurn().equals(StateTablut.Turn.WHITE)) {
                    //TODO WHITE actions

                    int[] buf;
                    for (int i = 0; i < state.getBoard().length; i++) {
                        for (int j = 0; j < state.getBoard().length; j++) {
                            if (state.getPawn(i, j).equalsPawn(State.Pawn.WHITE.toString())
                                    || state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {
                                buf = new int[2];
                                buf[0] = i;
                                buf[1] = j;
                                pawns.add(buf);
                            } else if (state.getPawn(i, j).equalsPawn(State.Pawn.EMPTY.toString())) {
                                buf = new int[2];
                                buf[0] = i;
                                buf[1] = j;
                                empty.add(buf);
                            }
                        }
                    }

                    // position of pawn selectd to move
                    int[] selected = null;

                    // if movement is right according to the rules of game
                    boolean found = false;

                    // movement
                    Action a = null;

                    // allow movement only horizontal of pawn
                    int negative = -1;

                    while (!found) {

                        // select last WHITE
                        selected = pawns.get(8);

                        String from = this.getCurrentState().getBox(selected[0], selected[1]);

                        String to = this.getCurrentState().getBox(selected[0], selected[1] + negative);

                        try {
                            a = new Action(from, to, State.Turn.WHITE);
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }

                        try {
                            rules.checkMove(state, a);
                            found = true;
                        } catch (Exception e) {
                            negative *= negative;
                        }

                    }

                    System.out.println("Mossa scelta: " + a.toString());
                    try {
                        this.write(a);
                    } catch (ClassNotFoundException | IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    pawns.clear();
                    empty.clear();

                }

                // if is turn of oppenent (BLACK)
                else if (state.getTurn().equals(StateTablut.Turn.BLACK)) {
                    System.out.println("Waiting for your opponent move... ");
                }
                // if I WIN
                else if (state.getTurn().equals(StateTablut.Turn.WHITEWIN)) {
                    System.out.println("YOU WIN!");
                    System.exit(0);
                }
                // if I LOSE
                else if (state.getTurn().equals(StateTablut.Turn.BLACKWIN)) {
                    System.out.println("YOU LOSE!");
                    System.exit(0);
                }
                // if DRAW
                else if (state.getTurn().equals(StateTablut.Turn.DRAW)) {
                    System.out.println("DRAW!");
                    System.exit(0);
                }

            }
            // if i'm BLACK
            else {

                // if is my turn (BLACK)
                if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACK)) {
                    //TODO BLACK actions

                    int[] buf;
                    for (int i = 0; i < state.getBoard().length; i++) {
                        for (int j = 0; j < state.getBoard().length; j++) {
                            if (state.getPawn(i, j).equalsPawn(State.Pawn.BLACK.toString())) {
                                buf = new int[2];
                                buf[0] = i;
                                buf[1] = j;
                                pawns.add(buf);
                            } else if (state.getPawn(i, j).equalsPawn(State.Pawn.EMPTY.toString())) {
                                buf = new int[2];
                                buf[0] = i;
                                buf[1] = j;
                                empty.add(buf);
                            }
                        }
                    }

                    // position of pawn selectd to move
                    int[] selected = null;

                    // if movement is right according to the rules of game
                    boolean found = false;

                    // movement
                    Action a = null;

                    // allow movement only horizontal of pawn
                    int negative = -1;

                    while (!found) {
                        // select E2 BLACK
                        selected = pawns.get(3);

                        String from = this.getCurrentState().getBox(selected[0], selected[1]);

                        String to = this.getCurrentState().getBox(selected[0], selected[1] + negative);

                        try {
                            a = new Action(from, to, State.Turn.WHITE);
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }

                        try {
                            rules.checkMove(state, a);
                            found = true;
                        } catch (Exception e) {
                            negative *= negative;
                        }

                    }

                    System.out.println("Mossa scelta: " + a.toString());
                    try {
                        this.write(a);
                    } catch (ClassNotFoundException | IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    pawns.clear();
                    empty.clear();

                }

                // if is turn of oppenent (WHITE)
                else if (state.getTurn().equals(StateTablut.Turn.WHITE)) {
                    System.out.println("Waiting for your opponent move... ");
                }

                // if I LOSE
                else if (state.getTurn().equals(StateTablut.Turn.WHITEWIN)) {
                    System.out.println("YOU LOSE!");
                    System.exit(0);
                }

                // if I WIN
                else if (state.getTurn().equals(StateTablut.Turn.BLACKWIN)) {
                    System.out.println("YOU WIN!");
                    System.exit(0);
                }

                // if DRAW
                else if (state.getTurn().equals(StateTablut.Turn.DRAW)) {
                    System.out.println("DRAW!");
                    System.exit(0);
                }
            }
        }
    }
}
