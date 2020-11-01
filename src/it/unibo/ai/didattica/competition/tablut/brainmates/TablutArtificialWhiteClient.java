package it.unibo.ai.didattica.competition.tablut.brainmates;

import java.io.IOException;
import java.net.UnknownHostException;

public class TablutArtificialWhiteClient {

    public static void main(String[] args) throws UnknownHostException, ClassNotFoundException, IOException {
        String[] array = new String[]{"WHITE"};
        if (args.length>0){
            array = new String[]{"WHITE", args[0]};
        }
        TablutArtificialClient.main(array);
    }
}
