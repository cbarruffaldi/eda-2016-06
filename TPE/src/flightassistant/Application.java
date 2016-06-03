package flightassistant;

import utils.Parser;

import java.util.Scanner;

public class Application {
    private static FlightAssistant flightAssistant;

    public static void main(String[] args) {
        flightAssistant = new FlightAssistant();
        if (calledWithArgument(args)) {
            // Parser para argumentos
        } else {
            Scanner sc = new Scanner(System.in);
            boolean ended;
            do {
                ended = Parser.parseShell(sc, flightAssistant);
            } while (!ended);
            sc.close();
        }
    }

    private static boolean calledWithArgument(String[] args) {
        return args.length != 0;
    }
}
