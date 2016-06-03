package flightassistant;

import utils.Parser;

import java.util.Scanner;

public class Application {
    private static FlightAssistant flightAssistant;
    private static Scanner inputSc = new Scanner(System.in);

    public static void main(String[] args) {
        flightAssistant = new FlightAssistant();
        if (calledWithArgument(args)) {
            // Parser para argumentos
        } else {
            Parser.parseShell(inputSc, flightAssistant);
        }
    }

    private static boolean calledWithArgument(String[] args) {
        return args.length != 0;
    }
}
