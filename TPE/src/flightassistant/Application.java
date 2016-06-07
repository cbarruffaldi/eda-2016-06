package flightassistant;

import utils.FileManager;

import java.io.IOException;

public class Application {
    private static FlightAssistant flightAssistant;

    public static void main (String[] args) {

        //TODO Hay que hardcodear un flightAssistant vacio asi se carga siempre el mismo

        //Por ahora lo saco porque no anda
        //TODO flightAssistant = load();	//carga el flightAssistant
        flightAssistant = new FlightAssistant();

        //    	if (calledWithArgument(args)) {
        //            Parser.parseArguments(args, flightAssistant);
        //        } else {
        //            flightAssistant = new FlightAssistant();
        //            Scanner sc = new Scanner(System.in);
        //
        //            while (!Parser.parseShell(sc, flightAssistant));
        //            // el parser devuelve si se usó el comando para terminar el programa o no
        //            sc.close();
        //        }
        exit();    //en ambos casos guarda y termina.
    }

    private static void exit () {
        try {
            FileManager.save(flightAssistant);
        } catch (IOException e) {
            e.printStackTrace();

            //     System.out.println("Error");
            //TODO hacer con el outputmanager
        }
    }

    private static FlightAssistant load () {
        FlightAssistant fa;
        try {
            return FileManager.load();
            // TODO que carajo imprimir en caso de error
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // No se cargo el flightAssistant
    }

    private static boolean calledWithArgument (String[] args) {
        return args.length != 0;
    }
}
