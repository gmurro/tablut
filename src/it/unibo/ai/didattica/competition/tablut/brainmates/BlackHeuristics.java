package it.unibo.ai.didattica.competition.tablut.brainmates;

import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.Random;

public class BlackHeuristics extends Heuristics {

    public BlackHeuristics(State state) {
        super(state);
    }

    @Override
    public double evaluateState() {

        //Number of own pawns
        int numberOfBlack = state.getNumberOf(State.Pawn.BLACK);
        int numberOfWhite = state.getNumberOf(State.Pawn.WHITE);
        int pawnsNearKing = checkNearPawns(state, KING_POSITION,State.Turn.BLACK.toString());
        int numberOfBlockedEscapes = 0;


        //King has not moved yet
        if (checkKingPosition(state)){




        }else{

            numberOfBlockedEscapes = getNumberOfBlockedEscape();

        }

        return 0;
    }
}
