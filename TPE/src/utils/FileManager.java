package utils;

import flightassistant.FlightAssistant;

import java.io.*;

//Por ahora solo se maneja guardando y leyendo FlightAssistant, no se si despues necesitaremos algo mas
public class FileManager {

    public static void save (FlightAssistant flightAssistant) throws IOException {
        File f = new File("flightAssistant.obj");
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(flightAssistant);
        oos.close();
    }

    public static FlightAssistant load () throws ClassNotFoundException, IOException {
        File f = new File("flightAssistant.obj");
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);
        FlightAssistant flightAssistant = (FlightAssistant) ois.readObject();
        ois.close();
        fis.close();
        return flightAssistant;

    }

}
