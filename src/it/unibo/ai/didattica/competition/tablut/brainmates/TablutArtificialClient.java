package it.unibo.ai.didattica.competition.tablut.brainmates;


import it.unibo.ai.didattica.competition.tablut.brainmates.minmax.CustomIterativeDeepeningAlphaBetaSearch;
import it.unibo.ai.didattica.competition.tablut.brainmates.minmax.MyIterativeDeepeningAlphaBetaSearch;
import it.unibo.ai.didattica.competition.tablut.client.TablutClient;
import it.unibo.ai.didattica.competition.tablut.domain.*;

import java.io.IOException;
import java.net.UnknownHostException;

public class TablutArtificialClient extends TablutClient {

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
            role = (args[0]);
        }
        if (args.length == 2) {
            timeout = Integer.parseInt(args[1]);
        }
        if (args.length == 3) {
            ipAddress = args[2];
        }

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
        GameAshtonTablut tablutGame = new GameAshtonTablut(0, -1, "garbage", "white_ai", "black_ai");;


        System.out.println("\n"+
               "+-------------------------  Ashton Tablut game challenge 2020/21  ------------------------+");
        System.out.println(
               "|    ██████╗ ██████╗  █████╗ ██╗███╗   ██╗███╗   ███╗ █████╗ ████████╗███████╗███████╗    |\n" +
               "|    ██╔══██╗██╔══██╗██╔══██╗██║████╗  ██║████╗ ████║██╔══██╗╚══██╔══╝██╔════╝██╔════╝    |\n" +
               "|    ██████╔╝██████╔╝███████║██║██╔██╗ ██║██╔████╔██║███████║   ██║   █████╗  ███████╗    |\n" +
               "|    ██╔══██╗██╔══██╗██╔══██║██║██║╚██╗██║██║╚██╔╝██║██╔══██║   ██║   ██╔══╝  ╚════██║    |\n" +
               "|    ██████╔╝██║  ██║██║  ██║██║██║ ╚████║██║ ╚═╝ ██║██║  ██║   ██║   ███████╗███████║    |\n" +
               "|    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝     ╚═╝╚═╝  ╚═╝   ╚═╝   ╚══════╝╚══════╝    |");

        System.out.println(
               "+-------------  Made by Giuseppe Murro, Giuseppe Boezio, Salvatore Pisciotta  ------------+\n");


        // attribute player depends to first parameter passed to main
        System.out.println("You are player " + this.getPlayer().toString() + "!");

        // still alive until you are playing
        while (true) {

            // update the current state from the server
            try {
                this.read();
            } catch (ClassNotFoundException | IOException e1) {
                e1.printStackTrace();
                System.exit(1);
            }

            // print current state
            System.out.println("Current state:");
            state = this.getCurrentState();
            System.out.println(state.toString());



            // if i'm WHITE
            if (this.getPlayer().equals(State.Turn.WHITE)) {

                // if is my turn (WHITE)
                if (state.getTurn().equals(StateTablut.Turn.WHITE)) {

                    System.out.println("\nSearching a suitable move... ");

                    // search the best move in search tree
                    Action a = findBestMove(tablutGame, state);

                    System.out.println("\nAction selected: " + a.toString());
                    try {
                        this.write(a);
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }

                }

                // if is turn of oppenent (BLACK)
                else if (state.getTurn().equals(StateTablut.Turn.BLACK)) {
                    System.out.println("Waiting for your opponent move...\n");
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

                    System.out.println("\nSearching a suitable move... ");

                    // search the best move in search tree
                    Action a = findBestMove(tablutGame, state);

                    System.out.println("\nAction selected: " + a.toString());
                    try {
                        this.write(a);
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }

                }

                // if is turn of oppenent (WHITE)
                else if (state.getTurn().equals(StateTablut.Turn.WHITE)) {
                    System.out.println("Waiting for your opponent move...\n");
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


    /**
     * Method that find a suitable moves searching in game tree
     * @param tablutGame Current game
     * @param state Current state
     * @return Action that is been evaluated as best
     */
    private Action findBestMove(GameAshtonTablut tablutGame, State state) {

        MyIterativeDeepeningAlphaBetaSearch search = new MyIterativeDeepeningAlphaBetaSearch(tablutGame, Double.MIN_VALUE, Double.MAX_VALUE, this.timeout - 2 );
        search.setLogEnabled(true);
        return (Action) search.makeDecision(state);
    }
}
